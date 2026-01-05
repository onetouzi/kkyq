package com.ruoyi.pill.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pill.domain.FlowerOrder;
import com.ruoyi.pill.service.IFlowerOrderService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 花卉订单Controller
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/pill/flowerOrder")
public class FlowerOrderController extends BaseController
{
    @Autowired
    private IFlowerOrderService flowerOrderService;

    /**
     * 查询我的订单列表
     */
    @RequiresPermissions("pill:flowerOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(FlowerOrder flowerOrder)
    {
        // 只查询当前用户的订单
        flowerOrder.setUserId(getUserId());
        startPage();
        List<FlowerOrder> list = flowerOrderService.selectFlowerOrderList(flowerOrder);
        return getDataTable(list);
    }

    /**
     * 获取订单详细信息
     */
    @RequiresPermissions("pill:flowerOrder:query")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        FlowerOrder order = flowerOrderService.selectFlowerOrderByOrderId(orderId);
        // 验证订单是否属于当前用户
        if (order != null && !order.getUserId().equals(getUserId()))
        {
            return error("无权访问该订单");
        }
        return success(order);
    }

    /**
     * 取消订单
     */
    @RequiresPermissions("pill:flowerOrder:edit")
    @Log(title = "取消订单", businessType = BusinessType.UPDATE)
    @PutMapping("/cancel/{orderId}")
    public AjaxResult cancel(@PathVariable("orderId") Long orderId)
    {
        FlowerOrder order = flowerOrderService.selectFlowerOrderByOrderId(orderId);
        if (order == null)
        {
            return error("订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(getUserId()))
        {
            return error("无权操作该订单");
        }
        if ("CANCELLED".equals(order.getStatus()))
        {
            return error("订单已取消");
        }
        if ("COMPLETED".equals(order.getStatus()))
        {
            return error("订单已完成，无法取消");
        }
        return toAjax(flowerOrderService.cancelOrder(orderId));
    }

    /**
     * 完成订单
     */
    @RequiresPermissions("pill:flowerOrder:edit")
    @Log(title = "完成订单", businessType = BusinessType.UPDATE)
    @PutMapping("/complete/{orderId}")
    public AjaxResult complete(@PathVariable("orderId") Long orderId)
    {
        FlowerOrder order = flowerOrderService.selectFlowerOrderByOrderId(orderId);
        if (order == null)
        {
            return error("订单不存在");
        }
        // 验证订单是否属于当前用户
        if (!order.getUserId().equals(getUserId()))
        {
            return error("无权操作该订单");
        }
        if ("COMPLETED".equals(order.getStatus()))
        {
            return error("订单已完成");
        }
        if ("CANCELLED".equals(order.getStatus()))
        {
            return error("订单已取消，无法完成");
        }
        return toAjax(flowerOrderService.completeOrder(orderId));
    }
}

