package cn.edu.xmu.oomall.ztoexpress.mapper.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "zto_order")
@EntityListeners(AuditListener.class)
public class OrderPo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "partnerType")
    private String partnerType;
    @Column(name = "orderType")
    private String orderType;
    @Column(name = "partnerOrderCode")
    private String partnerOrderCode;
    @Column(name = "orderCode")
    private String orderCode;
    @Column(name = "senderId")
    private Long senderId;
    @Column(name = "receiverId")
    private Long receiverId;
    @Column(name = "parcelOrderSum")
    private BigDecimal parcelOrderSum;
    @Column(name = "orderRemark")
    private String orderRemark;
    @Column(name = "orderStatus")
    private Integer orderStatus;//0:已取消 1：进行中 2.已完成
    @Column(name = "freight")
    private BigDecimal freight;
    @Column(name = "premium")
    private BigDecimal premium;
    public void toBeDefault(){
        this.orderStatus = 1;
        this.orderRemark= "";
        this.freight = new BigDecimal(0);
        this.premium = new BigDecimal(0);
        this.parcelOrderSum = new BigDecimal(0);
    }

}