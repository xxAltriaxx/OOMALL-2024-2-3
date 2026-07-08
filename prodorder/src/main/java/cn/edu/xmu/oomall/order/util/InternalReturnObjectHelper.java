//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.util;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;

public final class InternalReturnObjectHelper {

    private static final int OK = ReturnNo.OK.getErrNo();

    private InternalReturnObjectHelper() {
    }

    public static <T> T requireData(InternalReturnObject<T> ret, ReturnNo notExistErrno, String resourceName, Object resourceId) {
        if (ret == null) {
            throw new BusinessException(ReturnNo.INTERNAL_SERVER_ERR, "远程服务无响应");
        }
        Integer errno = ret.getErrno();
        if (errno == null || errno != OK) {
            ReturnNo mapped = errno == null ? ReturnNo.INTERNAL_SERVER_ERR : ReturnNo.getReturnNoByCode(errno);
            if (mapped == null) {
                mapped = ReturnNo.INTERNAL_SERVER_ERR;
            }
            String message = ret.getErrmsg() != null && !ret.getErrmsg().isBlank()
                    ? ret.getErrmsg()
                    : mapped.getMessage();
            throw new BusinessException(mapped, message);
        }
        if (ret.getData() == null) {
            throw new BusinessException(notExistErrno,
                    String.format(notExistErrno.getMessage(), resourceName, resourceId));
        }
        return ret.getData();
    }
}
