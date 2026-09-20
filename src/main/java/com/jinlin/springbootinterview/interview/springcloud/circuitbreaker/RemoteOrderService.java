package com.jinlin.springbootinterview.interview.springcloud.circuitbreaker;

/**
 * 模拟远程订单微服务（前 3 次调用模拟下游数据库连接超时故障抛异常，第 4 次及之后自愈）
 */
public class RemoteOrderService {

    private int callCount = 0;

    public String queryOrder(String orderId) {
        callCount++;
        if (callCount <= 3) {
            throw new RuntimeException("503 Service Unavailable: 数据库连接超时");
        }
        return "[200 OK 真实远程数据] 订单【" + orderId + "】查询成功，支付状态已完成。";
    }

    public int getCallCount() {
        return callCount;
    }
}
