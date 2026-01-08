package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FlowerStockHistory;

/**
 * 花卉库存变动及交易流水Mapper接口
 * 
 * @author b3q
 * @date 2026-01-08
 */
public interface FlowerStockHistoryMapper 
{
    /**
     * 查询花卉库存变动及交易流水
     * 
     * @param historyId 花卉库存变动及交易流水主键
     * @return 花卉库存变动及交易流水
     */
    public FlowerStockHistory selectFlowerStockHistoryByHistoryId(Long historyId);

    /**
     * 查询花卉库存变动及交易流水列表
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 花卉库存变动及交易流水集合
     */
    public List<FlowerStockHistory> selectFlowerStockHistoryList(FlowerStockHistory flowerStockHistory);

    /**
     * 新增花卉库存变动及交易流水
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 结果
     */
    public int insertFlowerStockHistory(FlowerStockHistory flowerStockHistory);

    /**
     * 修改花卉库存变动及交易流水
     * 
     * @param flowerStockHistory 花卉库存变动及交易流水
     * @return 结果
     */
    public int updateFlowerStockHistory(FlowerStockHistory flowerStockHistory);

    /**
     * 删除花卉库存变动及交易流水
     * 
     * @param historyId 花卉库存变动及交易流水主键
     * @return 结果
     */
    public int deleteFlowerStockHistoryByHistoryId(Long historyId);

    /**
     * 批量删除花卉库存变动及交易流水
     * 
     * @param historyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFlowerStockHistoryByHistoryIds(String[] historyIds);
}
