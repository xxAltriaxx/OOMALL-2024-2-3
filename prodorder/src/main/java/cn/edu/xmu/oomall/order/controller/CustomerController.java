//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.order.controller.vo.OrderItemVo;
import cn.edu.xmu.oomall.order.controller.vo.OrderVo;
import cn.edu.xmu.oomall.order.service.OrderService;
import cn.edu.xmu.oomall.order.service.dto.ConsigneeDto;
import cn.edu.xmu.oomall.order.service.dto.OrderItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CustomerController {

    private final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ReturnObject createOrder(@RequestBody @Validated OrderVo orderVo, @LoginUser UserDto user) {
        orderService.createOrder(toOrderItemDtos(orderVo.getItems()),
                ConsigneeDto.builder()
                        .consignee(orderVo.getConsignee())
                        .address(orderVo.getAddress())
                        .regionId(orderVo.getRegionId())
                        .mobile(orderVo.getMobile())
                        .build(),
                orderVo.getMessage(), user);
        return new ReturnObject(ReturnNo.CREATED);
    }

    private List<OrderItemDto> toOrderItemDtos(List<OrderItemVo> items) {
        return items.stream()
                .map(item -> OrderItemDto.builder()
                        .onsaleId(item.getOnsaleId())
                        .quantity(item.getQuantity())
                        .actId(item.getActId())
                        .couponId(item.getCouponId())
                        .build())
                .collect(Collectors.toList());
    }
}
