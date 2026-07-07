//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.service.exception;

public class OrderCreationInProgressException extends RuntimeException {

    public OrderCreationInProgressException(String idempotentKey) {
        super(String.format("订单创建进行中，idempotentKey=%s", idempotentKey));
    }
}
