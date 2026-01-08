package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 花卉信息对象 flower
 * * @author ruoyi
 * @date 2026-01-08
 */
public class Flower extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 花卉ID */
    private Long flowerId;

    /** 花卉名称 */
    @Excel(name = "花卉名称")
    private String flowerName;

    /** 花卉编码 */
    @Excel(name = "花卉编码")
    private String flowerCode;

    /** 花卉类型：0=观花类,1=观叶类,2=观果类,3=多肉类 */
    @Excel(name = "花卉类型", readConverterExp = "0=观花类,1=观叶类,2=观果类,3=多肉类")
    private String flowerType;

    /** 售价 */
    @Excel(name = "售价")
    private Long price;

    /** 进价 */
    @Excel(name = "进价")
    private Long purchasePrice;

    /** 库存量 */
    @Excel(name = "库存量")
    private Long num;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 图片路径 */
    @Excel(name = "图片路径")
    private String image;

    /** 状态：0=正常,1=停用 */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

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

    public void setFlowerType(String flowerType)
    {
        this.flowerType = flowerType;
    }

    public String getFlowerType()
    {
        return flowerType;
    }

    public void setPrice(Long price)
    {
        this.price = price;
    }

    public Long getPrice()
    {
        return price;
    }

    public void setPurchasePrice(Long purchasePrice)
    {
        this.purchasePrice = purchasePrice;
    }

    public Long getPurchasePrice()
    {
        return purchasePrice;
    }

    public void setNum(Long num)
    {
        this.num = num;
    }

    public Long getNum()
    {
        return num;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getImage()
    {
        return image;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("flowerId", getFlowerId())
                .append("flowerName", getFlowerName())
                .append("flowerCode", getFlowerCode())
                .append("flowerType", getFlowerType())
                .append("price", getPrice())
                .append("purchasePrice", getPurchasePrice())
                .append("num", getNum())
                .append("unit", getUnit())
                .append("image", getImage())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}