package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FlowerOrder;
import org.apache.ibatis.annotations.Param;

/**
 * 花卉订单Mapper接口
 * 
 * @author b3q
 * @date 2026-01-07
 */
public interface FlowerOrderMapper 
{
    /**
     * 查询花卉订单
     * 
     * @param orderId 花卉订单主键
     * @return 花卉订单
     */
    public FlowerOrder selectFlowerOrderByOrderId(String orderId);

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
    public int deleteFlowerOrderByOrderIds(String[] orderIds);

    /**
     * 批量新增花卉订单
     * * @param flowerOrderList 花卉订单列表
     * @return 结果
     */
    public int batchInsertFlowerOrder(List<FlowerOrder> flowerOrderList);

    /**
     * 批量扣减花卉库存
     */
    public int batchUpdateFlowerStock(List<FlowerOrder> flowerOrderList);

    public FlowerOrder selectFlowerOrderOne(@Param("orderId") String orderId,
                                            @Param("userId") String userId,
                                            @Param("flowerId") String flowerId);

    /**
     * 精确删除某订单下的某个花卉记录
     */
    public int deleteFlowerOrderOne(@Param("orderId") String orderId,
                                    @Param("userId") String userId,
                                    @Param("flowerId") String flowerId);


}
