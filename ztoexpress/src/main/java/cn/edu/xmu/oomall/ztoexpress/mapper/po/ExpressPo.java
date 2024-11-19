package cn.edu.xmu.oomall.ztoexpress.mapper.po;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.actuate.audit.listener.AuditListener;

import java.time.LocalDate;
@Data
@Entity
@Table(name = "zto_express")
@EntityListeners(AuditListener.class)
public class ExpressPo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "zto_order_id")
    private Long ztoOrderId;
    @Column(name = "billCode")
    private String billcode;
    @Column(name = "scanDate")
    private LocalDate scandate;
    @Column(name = "scanSiteId")
    private Long scansiteid;
    @Column(name = "parcelWeight")
    private Integer parcelweight;
    @Column(name = "parcelPackingFee")
    private Integer parcelpackingfee;
    @Column(name = "parcelSize")
    private Integer parcelsize;
    @Column(name = "parcelQuantity")
    private Integer parcelquantity;
    @Column(name = "parcelOtherFee")
    private Integer parcelotherfee;
    @Column(name = "expressStatus")
    private Integer expressStatus;//0：已取消 1：进行中 2：已完成

}