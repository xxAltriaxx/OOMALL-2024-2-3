package cn.edu.xmu.oomall.aftersale.Mapper.Po;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aftersale")
public class AfterSalesPo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "shop_id")
    Long shopId;

    @Column(name = "user_id")
    Long userId;

    String reason;

    String conclusion;

    Integer quantity;

    String address;

    String contact;

    String mobile;

    Integer in_arbitrated;

    String serialNo;

    Integer state;

    String type;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "afterSalesHistory_id",referencedColumnName = "id")
    AfterSalesHistoryPo afterSalesHistoryPo;
}
