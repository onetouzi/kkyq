package com.ruoyi.pill.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 全部订单对象 flower_order（管理员查看所有订单）
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public class FlowerAllOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 用户名 */
    @Excel(name = "用户名")
    private String userName;

    /** 花卉ID */
    @Excel(name = "花卉ID")
    private Long flowerId;

    /** 花卉名称 */
    @Excel(name = "花卉名称")
    private String flowerName;

    /** 花卉编码 */
    @Excel(name = "花卉编码")
    private String flowerCode;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private Integer quantity;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalPrice;

    /** 订单状态：PAID=已支付,COMPLETED=已完成,CANCELLED=已取消 */
    @Excel(name = "订单状态", readConverterExp = "PAID=已支付,COMPLETED=已完成,CANCELLED=已取消")
    private String status;

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setFlowerId(Long flowerId) 
    {
        this.flowerId = flowerId;
    }

    public Long getFlowerId() 
    {
        return flowerId;
    }

    public void setFlowerName(String flowerName) 
    {
        this.flowerName = flowerName;
    }

    public String getFlowerName() 
    {
        return flowerName;
    }

    public void setFlowerCode(String flowerCode) 
    {
        this.flowerCode = flowerCode;
    }

    public String getFlowerCode() 
    {
        return flowerCode;
    }

    public void setQuantity(Integer quantity) 
    {
        this.quantity = quantity;
    }

    public Integer getQuantity() 
    {
        return quantity;
    }

    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("flowerId", getFlowerId())
            .append("flowerName", getFlowerName())
            .append("flowerCode", getFlowerCode())
            .append("quantity", getQuantity())
            .append("totalPrice", getTotalPrice())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}

