package cn.edu.xmu.oomall.freight.dao.logistics.exception;

/**
 * @author:dzj
 * @date:2023/12/18 18:42
 * @description:2023-dgn3-001-dzj
 */
public class MethodNoSupportException extends RuntimeException {
    public MethodNoSupportException() {
        super(("方法不支持"));
    }
}
