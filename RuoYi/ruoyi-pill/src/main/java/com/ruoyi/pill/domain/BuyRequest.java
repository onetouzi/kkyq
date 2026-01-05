package com.ruoyi.pill.domain;

import java.io.Serializable;

/**
 * 购买请求对象
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public class BuyRequest implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 花卉ID */
    private Long flowerId;

    /** 花卉名称 */
    private String flowerName;

    /** 购买数量 */
    private Integer quantity;

    /** 价格 */
    private Integer price;

    public Long getFlowerId() 
    {
        return flowerId;
    }

    public void setFlowerId(Long flowerId) 
    {
        this.flowerId = flowerId;
    }

    public String getFlowerName() 
    {
        return flowerName;
    }

    public void setFlowerName(String flowerName) 
    {
        this.flowerName = flowerName;
    }

    public Integer getQuantity() 
    {
        return quantity;
    }

    public void setQuantity(Integer quantity) 
    {
        this.quantity = quantity;
    }

    public Integer getPrice() 
    {
        return price;
    }

    public void setPrice(Integer price) 
    {
        this.price = price;
    }
}

