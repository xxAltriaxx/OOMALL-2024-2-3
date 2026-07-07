//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.dao;

public enum IdempotencyAcquireResult {
    ACQUIRED,
    ALREADY_COMMITTED,
    IN_PROGRESS,
    FAILED
}
