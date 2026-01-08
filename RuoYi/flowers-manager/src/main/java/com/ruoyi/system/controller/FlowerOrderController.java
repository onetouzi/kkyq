package com.ruoyi.system.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.FlowerIdGenerator;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.DTO.FlowerOrderDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.FlowerOrder;
import com.ruoyi.system.service.IFlowerOrderService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 花卉订单Controller
 * 
 * @author b3q
 * @date 2026-01-07
 */
@Controller
@RequestMapping("/work/order")
public class FlowerOrderController extends BaseController
{
    private String prefix = "work/order";

    @Autowired
    private IFlowerOrderService flowerOrderService;

    @RequiresPermissions("work:order:view")
    @GetMapping()
    public String order()
    {
        return prefix + "/order";
    }

    /**
     * 查询花卉订单列表
     */
    @RequiresPermissions("work:order:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FlowerOrder flowerOrder)
    {
        startPage();
        List<FlowerOrder> list = flowerOrderService.selectFlowerOrderList(flowerOrder);
        return getDataTable(list);
    }

    /**
     * 导出花卉订单列表
     */
    @RequiresPermissions("work:order:export")
    @Log(title = "花卉订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FlowerOrder flowerOrder)
    {
        List<FlowerOrder> list = flowerOrderService.selectFlowerOrderList(flowerOrder);
        ExcelUtil<FlowerOrder> util = new ExcelUtil<FlowerOrder>(FlowerOrder.class);
        return util.exportExcel(list, "花卉订单数据");
    }

    /**
     * 新增花卉订单
     */
    @RequiresPermissions("work:order:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@RequestBody FlowerOrderDTO orderDTO)
    {
        // 参数校验：如果订单项为空，直接返回错误
        if (orderDTO.getItems() == null || orderDTO.getItems().isEmpty()) {
            return AjaxResult.error("订单商品不能为空");
        }

        Long userId = orderDTO.getUserId();
        String userName = orderDTO.getUserName();
        List<FlowerOrder> orders = new ArrayList<>();
        String status = orderDTO.getStatus();

        // 生成这一批次唯一的订单号
        String id = FlowerIdGenerator.generateFlowerNo();

        for (FlowerOrderDTO.OrderItemDTO item : orderDTO.getItems()) {
            FlowerOrder order = new FlowerOrder();
            order.setStatus(status);
            order.setOrderId(id); // 所有人共用这个订单号
            order.setUserId(userId);
            order.setUserName(userName);
            order.setFlowerId(item.getFlowerId());
            order.setFlowerName(item.getFlowerName());
            order.setQuantity(item.getQuantity());

            if (item.getPrice() != null && item.getQuantity() != null) {
                BigDecimal price = item.getPrice();
                BigDecimal quantity = new BigDecimal(item.getQuantity());
                order.setTotalPrice(price.multiply(quantity));
            }

            order.setCreateBy(ShiroUtils.getLoginName());
            order.setCreateTime(new Date());

            orders.add(order);
        }

        // 执行批量插入和库存扣减（确保 Service 层有 @Transactional 事务注解）
        try {
            int result = flowerOrderService.batchPlaceOrders(orders);
            if (result > 0) {
                // 成功：返回带有订单号的数据
                return AjaxResult.success("下单成功", id);
            } else {
                return AjaxResult.error("下单失败，库存不足或系统异常");
            }
        } catch (Exception e) {
            logger.error("订单下单异常", e);
            return AjaxResult.error("系统异常：" + e.getMessage());
        }
    }

    /**
     * 修改花卉订单
     */
    @GetMapping("/edit/{orderId}")
    public String edit(@PathVariable("orderId") String orderId,
                       @RequestParam(value = "userId", required = false) String userId,
                       @RequestParam(value = "flowerId", required = false) String flowerId,
                       ModelMap mmap)
    {
        FlowerOrder flowerOrder = flowerOrderService.selectFlowerOrderOne(orderId,userId,flowerId);
        // 安全校验：如果状态是已支付(0)，跳转到警告页面或返回错误提示
        if (flowerOrder != null && "0".equals(flowerOrder.getStatus())) {
            mmap.put("errorMessage", "该订单已支付，无法进行修改操作。");
            return "error/unauth"; // 或者返回一个专门的提示页
        }
        mmap.put("flowerOrder", flowerOrder);
        return prefix + "/edit";
    }

    /**
     * 修改保存花卉订单
     */
    @RequiresPermissions("work:order:edit")
    @Log(title = "花卉订单", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FlowerOrder flowerOrder)
    {
        return toAjax(flowerOrderService.updateFlowerOrder(flowerOrder));
    }

    /**
     * 删除特定的花卉订单项
     */
    @RequiresPermissions("work:order:remove")
    @Log(title = "花卉订单", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String orderId, String userId, String flowerId)
    {
        return toAjax(flowerOrderService.deleteFlowerOrderOne(orderId, userId, flowerId));
    }

    /**
     * 取消订单
     */
    @RequiresPermissions("work:order:edit") // 或者自定义权限
    @Log(title = "花卉订单", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel")
    @ResponseBody
    public AjaxResult cancel(String orderId, String userId, String flowerId) {
        // 调用 Service 层处理逻辑
        return toAjax(flowerOrderService.cancelOrder(orderId, userId, flowerId));
    }

    @PostMapping("/removeBatch")
    @ResponseBody
    public AjaxResult removeBatch(@RequestBody List<FlowerOrder> orders) {
        int count = 0;
        try {
            count = flowerOrderService.deleteFlowerOrderBatch(orders);
        } catch (Exception e) {
            return error(e.getMessage());
        }
        return toAjax(count);
    }
}
