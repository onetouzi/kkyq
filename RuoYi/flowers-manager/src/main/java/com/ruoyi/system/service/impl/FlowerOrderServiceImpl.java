package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.mapper.FlowerMapper;
import com.ruoyi.system.service.IFlowerStockHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FlowerOrderMapper;
import com.ruoyi.system.domain.FlowerOrder;
import com.ruoyi.system.service.IFlowerOrderService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 花卉订单Service业务层处理
 * * @author b3q
 * @date 2026-01-08
 */
@Service
public class FlowerOrderServiceImpl implements IFlowerOrderService
{
    @Autowired
    private FlowerOrderMapper flowerOrderMapper;

    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private IFlowerStockHistoryService stockHistoryService;

    /**
     * 查询花卉订单
     */
    @Override
    public FlowerOrder selectFlowerOrderByOrderId(String orderId)
    {
        return flowerOrderMapper.selectFlowerOrderByOrderId(orderId);
    }

    /**
     * 查询花卉订单列表
     */
    @Override
    public List<FlowerOrder> selectFlowerOrderList(FlowerOrder flowerOrder)
    {
        return flowerOrderMapper.selectFlowerOrderList(flowerOrder);
    }

    /**
     * 批量下单并扣减库存（集成流水与利润记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchPlaceOrders(List<FlowerOrder> orders) {
        if (orders == null || orders.isEmpty()) {
            return 0;
        }
        // 1. 批量插入订单
        flowerOrderMapper.batchInsertFlowerOrder(orders);

        // 2. 批量扣减库存
        int rows = flowerOrderMapper.batchUpdateFlowerStock(orders);

        // 3. 记录销售流水（业务类型1: 售货）
        for (FlowerOrder order : orders) {
            stockHistoryService.recordLog(order, "1", "销售下单：系统自动记录利润");
        }
        return rows;
    }

    @Override
    public FlowerOrder selectFlowerOrderOne(String orderId, String userId, String flowerId) {
        return flowerOrderMapper.selectFlowerOrderOne(orderId, userId, flowerId);
    }

    /**
     * 取消订单（返还库存并冲回利润）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelOrder(String orderId, String userId, String flowerId) {
        FlowerOrder order = flowerOrderMapper.selectFlowerOrderOne(orderId, userId, flowerId);

        if (order != null && "1".equals(order.getStatus())) {
            order.setStatus("2"); // 2: 已取消
            int row = flowerOrderMapper.updateFlowerOrder(order);

            // 关键：返还库存
            flowerMapper.updateFlowerStock(order.getFlowerId(), order.getQuantity());

            // 关键：记录取消流水（业务类型2: 订单取消，利润冲回）
            stockHistoryService.recordLog(order, "2", "订单取消：返还库存并冲回利润");

            return row;
        } else {
            throw new RuntimeException("订单状态已变更，无法取消");
        }
    }

    /**
     * 删除单条订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFlowerOrderOne(String orderId, String userId, String flowerId) {
        FlowerOrder order = flowerOrderMapper.selectFlowerOrderOne(orderId, userId, flowerId);
        if (order != null) {
            // 如果删除的是未支付订单，需要退还库存
            if ("1".equals(order.getStatus())) {
                flowerMapper.updateFlowerStock(order.getFlowerId(), order.getQuantity());
                // 记录流水
                stockHistoryService.recordLog(order, "2", "订单删除：库存退回流水");
            }
        }
        return flowerOrderMapper.deleteFlowerOrderOne(orderId, userId, flowerId);
    }

    /**
     * 批量删除订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFlowerOrderBatch(List<FlowerOrder> orders) {
        int total = 0;
        for (FlowerOrder item : orders) {
            total += deleteFlowerOrderOne(item.getOrderId(),
                    item.getUserId().toString(),
                    item.getFlowerId().toString());
        }
        return total;
    }

    /**
     * 修改保存花卉订单（核心：处理库存差异流水）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFlowerOrder(FlowerOrder flowerOrder) {
        // 1. 获取原数据
        FlowerOrder oldOrder = flowerOrderMapper.selectFlowerOrderOne(
                flowerOrder.getOrderId(), flowerOrder.getUserId().toString(), flowerOrder.getFlowerId().toString());

        if (oldOrder == null) {
            throw new RuntimeException("原订单记录不存在");
        }
        if ("0".equals(oldOrder.getStatus())) {
            throw new RuntimeException("已支付订单无法修改");
        }

        // 2. 获取花卉最新价格
        Flower flower = flowerMapper.selectFlowerByFlowerId(flowerOrder.getFlowerId());
        if (flower == null) {
            throw new RuntimeException("对应花卉信息已失效");
        }

        // 3. 计算并同步最新单价总额
        long currentPrice = flower.getPrice();
        BigDecimal expectedTotalPrice = BigDecimal.valueOf(currentPrice).multiply(new BigDecimal(flowerOrder.getQuantity()));
        flowerOrder.setTotalPrice(expectedTotalPrice);

        // 4. 库存检查与差额流水处理
        long oldQty = oldOrder.getQuantity();
        long newQty = flowerOrder.getQuantity();
        long diff = newQty - oldQty;

        if (diff != 0) {
            if (diff > 0) {
                // 追加购买
                if (flower.getNum() < diff) {
                    throw new RuntimeException("库存不足，剩余：" + flower.getNum());
                }
                flowerMapper.updateFlowerStock(flowerOrder.getFlowerId(), -diff);

                // 记录追加部分的销售流水
                FlowerOrder addLog = copyOrder(flowerOrder, diff, currentPrice);
                stockHistoryService.recordLog(addLog, "1", "订单修改：追加购买数量");
            } else {
                // 减少购买
                flowerMapper.updateFlowerStock(flowerOrder.getFlowerId(), Math.abs(diff));

                // 记录减少部分的退货流水
                FlowerOrder subLog = copyOrder(flowerOrder, Math.abs(diff), currentPrice);
                stockHistoryService.recordLog(subLog, "2", "订单修改：减少购买数量");
            }
        }

        flowerOrder.setUpdateBy(ShiroUtils.getLoginName());
        return flowerOrderMapper.updateFlowerOrder(flowerOrder);
    }

    /**
     * 辅助工具：复制订单对象用于记录差异流水
     */
    private FlowerOrder copyOrder(FlowerOrder source, long qty, long price) {
        FlowerOrder target = new FlowerOrder();
        target.setOrderId(source.getOrderId());
        target.setFlowerId(source.getFlowerId());
        target.setFlowerName(source.getFlowerName());
        target.setUserId(source.getUserId());
        target.setUserName(source.getUserName());
        target.setQuantity(qty);
        target.setTotalPrice(BigDecimal.valueOf(price).multiply(new BigDecimal(qty)));
        return target;
    }

    // --- 以下为生成的常规方法 ---

    @Override
    public int insertFlowerOrder(FlowerOrder flowerOrder) {
        flowerOrder.setCreateTime(DateUtils.getNowDate());
        return flowerOrderMapper.insertFlowerOrder(flowerOrder);
    }

    @Override
    public int deleteFlowerOrderByOrderIds(String orderIds) {
        return flowerOrderMapper.deleteFlowerOrderByOrderIds(Convert.toStrArray(orderIds));
    }

    @Override
    public int deleteFlowerOrderByOrderId(Long orderId) {
        return flowerOrderMapper.deleteFlowerOrderByOrderId(orderId);
    }

    public int insertOrders(List<FlowerOrder> orders) {
        if (orders != null && !orders.isEmpty()) {
            return flowerOrderMapper.batchInsertFlowerOrder(orders);
        }
        return 0;
    }
}