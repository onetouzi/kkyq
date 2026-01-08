package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import javax.xml.crypto.Data;

/**
 * 花卉订单对象 flower_order
 * 
 * @author b3q
 * @date 2026-01-07
 */
public class FlowerOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    @Excel(name = "订单号")
    private String orderId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 买家姓名 */
    @Excel(name = "买家姓名")
    private String userName;

    /** 花卉ID */
    @Excel(name = "花卉ID")
    private Long flowerId;

    /** 花卉名称 */
    @Excel(name = "花卉名称")
    private String flowerName;

    /** 购买数量 */
    @Excel(name = "购买数量")
    private Long quantity;

    /** 总金额 */
    @Excel(name = "总金额")
    private BigDecimal totalPrice;

    @Excel(name = "状态",readConverterExp = "0=已支付,1=未支付,2=已取消")
    private String status;

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderId()
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

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
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
            .append("quantity", getQuantity())
            .append("totalPrice", getTotalPrice())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
