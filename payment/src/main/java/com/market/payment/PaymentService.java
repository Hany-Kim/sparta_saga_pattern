package com.market.payment;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${message.queue.err.product}")
    private String productErrorQueue;

    public void createPayment(DeliveryMessage deliveryMessage) {

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID())
                .userId(deliveryMessage.getUserId())
                .payAmount(deliveryMessage.getPayAmount())
                .payStatus("SUCCESS")
                .build();

        Integer payAmount = payment.getPayAmount();

        if(payAmount >= 10000) {
            log.error("Payment amount exceeds limit : {}", payAmount);
            deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEED");
            this.rollbackPayment(deliveryMessage);
        }
    }

    public void rollbackPayment(DeliveryMessage deliveryMessage) {
        log.info("PAYMENT ROLLBACK !!!");
        rabbitTemplate.convertAndSend(productErrorQueue, deliveryMessage);
    }
}
