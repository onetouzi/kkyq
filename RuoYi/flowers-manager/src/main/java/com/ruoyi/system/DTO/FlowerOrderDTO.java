package com.ruoyi.system.DTO;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 花卉订单对象 flower_order
 *
 * @author b3q
 * @date 2026-01-07
 */

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 花卉下单传输对象
 * 用于接收前端选择的买家和多个花卉信息
 */
public class FlowerOrderDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 买家用户ID */
    private Long userId;

    private String userName;

    private String status;


    /** 订单总金额 (由前端计算或后端校验) */
    private BigDecimal totalOrderPrice;

    /** 选中的花卉列表 */
    private List<OrderItemDTO> items;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * 内部类：订单项数据
     */
    public static class OrderItemDTO {
        private Long flowerId;
        private String flowerName;
        private Long quantity;
        private BigDecimal price; // 单价
        private BigDecimal subtotal; // 小计

        // Getter & Setter
        public Long getFlowerId() { return flowerId; }
        public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }
        public String getFlowerName() { return flowerName; }
        public void setFlowerName(String flowerName) { this.flowerName = flowerName; }
        public Long getQuantity() { return quantity; }
        public void setQuantity(Long quantity) { this.quantity = quantity; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }


    }

    // 主类 Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public BigDecimal getTotalOrderPrice() { return totalOrderPrice; }
    public void setTotalOrderPrice(BigDecimal totalOrderPrice) { this.totalOrderPrice = totalOrderPrice; }
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("totalOrderPrice", getTotalOrderPrice())
                .append("items", getItems())
                .append("status",getStatus())
                .toString();
    }
}