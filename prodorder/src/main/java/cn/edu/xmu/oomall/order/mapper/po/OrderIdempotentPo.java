//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.mapper.po;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_idempotent")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderIdempotentPo {

    @Id
    @Column(length = 64)
    private String idempotentKey;

    /**
     * 0=进行中，1=已成功，2=已失败
     */
    private Byte status;

    /**
     * 本次请求应创建的店铺订单数
     */
    private Integer expectedShopCount;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
