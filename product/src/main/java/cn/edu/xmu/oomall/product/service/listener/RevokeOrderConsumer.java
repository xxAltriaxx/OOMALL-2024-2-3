//School of Informatics Xiamen University, GPL-3.0 license

package cn.edu.xmu.oomall.product.service.listener;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.product.dao.bo.Order;
import cn.edu.xmu.oomall.product.service.OnsaleService;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * 取消订单消息
 */
@Service
@RocketMQMessageListener(topic = "Revoke-Order", consumerGroup = "goods-revoke-order", consumeThreadMax = 1)
public class RevokeOrderConsumer implements RocketMQListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(RevokeOrderConsumer.class);

    private OnsaleService onsaleService;

    @Autowired
    public RevokeOrderConsumer(OnsaleService onsaleService) {
        this.onsaleService = onsaleService;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String content = new String(message.getBody(), "UTF-8");
            Order order = JacksonUtil.toObj(content, Order.class);
            if (null == order || null == order.getOrderItems()){
                logger.error("OrderConsumer: wrong format.... content = {}",content);
            }else{
                order.getOrderItems().stream().forEach(item -> {
                    this.onsaleService.incrQuantity(item.getId(), item.getQuantity());
                });
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("OrderConsumer: wrong encoding.... msg = {}",message);
        }

    }
}
