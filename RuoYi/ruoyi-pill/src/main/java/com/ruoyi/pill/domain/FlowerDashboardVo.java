package com.ruoyi.pill.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 数据统计视图对象
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public class FlowerDashboardVo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 花卉总数 */
    private Long totalFlowers;

    /** 今日订单数 */
    private Long todayOrders;

    /** 低库存数量 */
    private Long lowInventory;

    /** 总销售额 */
    private BigDecimal totalSales;

    /** 总库存量 */
    private Long totalInventory;

    public Long getTotalFlowers() 
    {
        return totalFlowers;
    }

    public void setTotalFlowers(Long totalFlowers) 
    {
        this.totalFlowers = totalFlowers;
    }

    public Long getTodayOrders() 
    {
        return todayOrders;
    }

    public void setTodayOrders(Long todayOrders) 
    {
        this.todayOrders = todayOrders;
    }

    public Long getLowInventory() 
    {
        return lowInventory;
    }

    public void setLowInventory(Long lowInventory) 
    {
        this.lowInventory = lowInventory;
    }

    public BigDecimal getTotalSales() 
    {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) 
    {
        this.totalSales = totalSales;
    }

    public Long getTotalInventory() 
    {
        return totalInventory;
    }

    public void setTotalInventory(Long totalInventory) 
    {
        this.totalInventory = totalInventory;
    }
}

