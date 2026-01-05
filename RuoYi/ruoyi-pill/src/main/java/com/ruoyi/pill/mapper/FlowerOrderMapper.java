package com.ruoyi.pill.mapper;

import java.util.List;
import com.ruoyi.pill.domain.FlowerOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 花卉订单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Mapper
public interface FlowerOrderMapper 
{
    /**
     * 查询花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 花卉订单
     */
    public FlowerOrder selectFlowerOrderByOrderId(Long orderId);

    /**
     * 查询花卉订单列表
     * 
     * @param flowerOrder 花卉订单
     * @return 花卉订单集合
     */
    public List<FlowerOrder> selectFlowerOrderList(FlowerOrder flowerOrder);

    /**
     * 新增花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    public int insertFlowerOrder(FlowerOrder flowerOrder);

    /**
     * 修改花卉订单
     * 
     * @param flowerOrder 花卉订单
     * @return 结果
     */
    public int updateFlowerOrder(FlowerOrder flowerOrder);

    /**
     * 删除花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 结果
     */
    public int deleteFlowerOrderByOrderId(Long orderId);

    /**
     * 批量删除花卉订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFlowerOrderByOrderIds(Long[] orderIds);
}

