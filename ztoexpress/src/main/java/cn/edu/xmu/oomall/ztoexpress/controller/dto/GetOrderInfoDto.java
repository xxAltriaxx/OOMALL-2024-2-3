package cn.edu.xmu.oomall.ztoexpress.controller.dto;
import cn.edu.xmu.oomall.ztoexpress.dao.bo.OrderBo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOrderInfoDto{
    private OrderBo orderBo;

}
