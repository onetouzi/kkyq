package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 花卉库存变动及交易流水对象 flower_stock_history
 * 
 * @author b3q
 * @date 2026-01-08
 */
public class FlowerStockHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 成本价快照 */
    @Excel(name = "成本价快照")
    private BigDecimal costPrice;

    /** 利润快照 */
    @Excel(name = "利润快照")
    private BigDecimal profit;

    // ... 省略其他字段的 Getter/Setter ...

    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public BigDecimal getCostPrice() { return costPrice; }

    public void setProfit(BigDecimal profit) { this.profit = profit; }
    public BigDecimal getProfit() { return profit; }

    /** 主键ID */
    private Long historyId;

    /** 花卉ID */
    @Excel(name = "花卉ID")
    private Long flowerId;

    /** 花卉名称 */
    @Excel(name = "花卉名称")
    private String flowerName;

    /** 买家ID */
    @Excel(name = "买家ID")
    private Long userId;

    /** 买家姓名 */
    @Excel(name = "买家姓名")
    private String userName;

    /** 买家电话 */
    @Excel(name = "买家电话")
    private String userPhone;

    /** 业务类型：0=进货, 1=售货, 2=订单取消 */
    @Excel(name = "业务类型：0=进货, 1=售货, 2=订单取消")
    private String businessType;

    /** 变动数量（进货为正，售货为负） */
    @Excel(name = "变动数量", readConverterExp = "进=货为正，售货为负")
    private Long quantity;

    /** 单价快照 */
    @Excel(name = "单价快照")
    private BigDecimal unitPrice;

    /** 总金额快照 */
    @Excel(name = "总金额快照")
    private BigDecimal totalPrice;

    /** 关联业务订单号 */
    @Excel(name = "关联业务订单号")
    private String orderId;

    public void setHistoryId(Long historyId) 
    {
        this.historyId = historyId;
    }

    public Long getHistoryId() 
    {
        return historyId;
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

    public void setUserPhone(String userPhone) 
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone() 
    {
        return userPhone;
    }

    public void setBusinessType(String businessType) 
    {
        this.businessType = businessType;
    }

    public String getBusinessType() 
    {
        return businessType;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) 
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() 
    {
        return unitPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) 
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPrice() 
    {
        return totalPrice;
    }

    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("historyId", getHistoryId())
            .append("flowerId", getFlowerId())
            .append("flowerName", getFlowerName())
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("userPhone", getUserPhone())
            .append("businessType", getBusinessType())
            .append("quantity", getQuantity())
            .append("unitPrice", getUnitPrice())
            .append("totalPrice", getTotalPrice())
            .append("orderId", getOrderId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("remark", getRemark())
            .toString();
    }
}
