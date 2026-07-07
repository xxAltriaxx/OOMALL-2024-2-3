//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao.bo;

public final class OrderIdempotentStatus {

    public static final byte PENDING = 0;

    public static final byte COMMITTED = 1;

    public static final byte FAILED = 2;

    private OrderIdempotentStatus() {
    }
}
