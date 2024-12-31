package com.market.order;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class Order {
    private UUID orderId;
    private String userId;
    private String orderStatus;
    private String errorType;

    public void cancelOrder(String receiveErrorType) { // 실제로는 에러 Enum으로 해야함
        orderStatus = "CANCELLED";
        errorType = receiveErrorType;
    }
}
