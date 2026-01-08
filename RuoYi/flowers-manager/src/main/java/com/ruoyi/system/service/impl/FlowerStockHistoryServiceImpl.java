package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.domain.FlowerOrder;
import com.ruoyi.system.mapper.FlowerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FlowerStockHistoryMapper;
import com.ruoyi.system.domain.FlowerStockHistory;
import com.ruoyi.system.service.IFlowerStockHistoryService;
import com.ruoyi.common.core.text.Convert;

/**
 * 花卉库存变动及交易流水Service业务层处理
 * 
 * @author b3q
 * @date 2026-01-08
 */
@Service
public class FlowerStockHistoryServiceImpl implements IFlowerStockHistoryService 
{
    @Autowired
    private FlowerStockHistoryMapper flowerStockHistoryMapper;

    @Autowired
    private FlowerServiceImpl flowerService;

    @Autowired
    private FlowerMapper flowerMapper;

    /**
     * 场景 A：订单触发的记录（销售、取消、修改）
     */
    @Override
    public void recordLog(FlowerOrder order, String businessType, String remark) {
        // 1. 获取花卉实体，拿到最新的进价 (PurchasePrice)
        Flower flower = flowerMapper.selectFlowerByFlowerId(order.getFlowerId());

        // 2. 准备变动数量：销售为负(1)，取消为正(2)
        long changeQty = "1".equals(businessType) ? -order.getQuantity() : order.getQuantity();

        // 3. 计算订单成交单价 = 总价 / 数量
        BigDecimal orderUnitPrice = order.getTotalPrice().divide(
                new BigDecimal(order.getQuantity()), 2, BigDecimal.ROUND_HALF_UP);

        // 4. 调用底层通用方法，并传入订单特有信息
        this.recordFlowerLogInternal(flower, businessType, changeQty, orderUnitPrice,
                order.getOrderId(), order.getUserId(), order.getUserName(), remark);
    }

    /**
     * 场景 B：管理端手动触发的记录（进货、损耗）
     */
    @Override
    public void recordFlowerLog(Flower flower, String businessType, Long changeQty, String remark) {
        // 进货或损耗时，成交价就是当前花卉的成本价
        BigDecimal costPrice = BigDecimal.valueOf(flower.getPurchasePrice());

        // 进货/损耗没有买家信息，传入 null
        this.recordFlowerLogInternal(flower, businessType, changeQty, costPrice,
                null, null, null, remark);
    }

    /**
     * 底层私有方法：统一计算利润并入库
     */
    private void recordFlowerLogInternal(Flower flower, String businessType, Long changeQty,
                                         BigDecimal tradePrice, String orderId, Long userId,
                                         String userName, String remark) {
        FlowerStockHistory history = new FlowerStockHistory();

        // 基础信息
        history.setFlowerId(flower.getFlowerId());
        history.setFlowerName(flower.getFlowerName());
        history.setBusinessType(businessType);
        history.setQuantity(changeQty);

        // 记录买家/订单信息（如果有）
        history.setOrderId(orderId);
        history.setUserId(userId);
        history.setUserName(userName);

        // 核心财务计算
        BigDecimal purchasePrice = BigDecimal.valueOf(flower.getPurchasePrice()); // 进价
        history.setCostPrice(purchasePrice);
        history.setUnitPrice(tradePrice); // 实际成交单价

        // 总金额 = 成交单价 * 变动数量绝对值
        history.setTotalPrice(tradePrice.multiply(new BigDecimal(Math.abs(changeQty))));

        BigDecimal profit = BigDecimal.ZERO;
        if ("1".equals(businessType)) {
            // 销售：利润 = (售价 - 进价) * 数量
            profit = tradePrice.subtract(purchasePrice).multiply(new BigDecimal(Math.abs(changeQty)));
        } else if ("2".equals(businessType)) {
            // 取消订单：利润冲回（变负数）
            profit = tradePrice.subtract(purchasePrice).multiply(new BigDecimal(Math.abs(changeQty))).negate();
        } else if ("3".equals(businessType)) {
            // 损耗：利润 = -进价 * 损耗数量
            profit = purchasePrice.multiply(new BigDecimal(Math.abs(changeQty))).negate();
        } else {
            // 进货：利润 0
            profit = BigDecimal.ZERO;
        }

        history.setProfit(profit);
        history.setCreateBy(ShiroUtils.getLoginName());
        history.setRemark(remark);

        flowerStockHistoryMapper.insertFlowerStockHistory(history);
    }
    /**
     * 查询花卉库存变动及交易流水
     * 
     * @param historyId 花卉库存变动及交易流水主键
     * @return 花卉库存变动及交易流水
     */
    @Override
    public FlowerStockHistory selectFlowerStockHistoryByHistoryId(Long historyId)
    {
        return flowerStockHistoryMapper.selectFlowerStockHistoryByHistoryId(historyId);
    }

    /**
     * 查询花卉库存变动及交易流水列表
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 花卉库存变动及交易流水
     */
    @Override
    public List<FlowerStockHistory> selectFlowerStockHistoryList(FlowerStockHistory flowerStockHistory)
    {
        return flowerStockHistoryMapper.selectFlowerStockHistoryList(flowerStockHistory);
    }

    /**
     * 新增花卉库存变动及交易流水
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 结果
     */
    @Override
    public int insertFlowerStockHistory(FlowerStockHistory flowerStockHistory)
    {
        flowerStockHistory.setCreateTime(DateUtils.getNowDate());
        return flowerStockHistoryMapper.insertFlowerStockHistory(flowerStockHistory);
    }

    /**
     * 修改花卉库存变动及交易流水
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 结果
     */
    @Override
    public int updateFlowerStockHistory(FlowerStockHistory flowerStockHistory)
    {
        return flowerStockHistoryMapper.updateFlowerStockHistory(flowerStockHistory);
    }

    /**
     * 批量删除花卉库存变动及交易流水
     * 
     * @param historyIds 需要删除的花卉库存变动及交易流水主键
     * @return 结果
     */
    @Override
    public int deleteFlowerStockHistoryByHistoryIds(String historyIds)
    {
        return flowerStockHistoryMapper.deleteFlowerStockHistoryByHistoryIds(Convert.toStrArray(historyIds));
    }

    /**
     * 删除花卉库存变动及交易流水信息
     * 
     * @param historyId 花卉库存变动及交易流水主键
     * @return 结果
     */
    @Override
    public int deleteFlowerStockHistoryByHistoryId(Long historyId)
    {
        return flowerStockHistoryMapper.deleteFlowerStockHistoryByHistoryId(historyId);
    }
}
