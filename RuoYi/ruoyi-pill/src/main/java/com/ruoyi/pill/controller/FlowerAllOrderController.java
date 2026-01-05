package com.ruoyi.pill.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pill.domain.FlowerAllOrder;
import com.ruoyi.pill.service.IFlowerAllOrderService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 全部订单Controller
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/pill/flowerAllOrder")
public class FlowerAllOrderController extends BaseController
{
    @Autowired
    private IFlowerAllOrderService flowerAllOrderService;

    /**
     * 查询全部订单列表
     */
    @RequiresPermissions("pill:flowerAllOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(FlowerAllOrder flowerAllOrder)
    {
        startPage();
        List<FlowerAllOrder> list = flowerAllOrderService.selectFlowerAllOrderList(flowerAllOrder);
        return getDataTable(list);
    }
}

