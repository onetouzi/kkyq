package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Flower;
import org.apache.ibatis.annotations.Param;

/**
 * 花卉信息Mapper接口
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
public interface FlowerMapper 
{
    /**
     * 查询花卉信息
     * 
     * @param flowerId 花卉信息主键
     * @return 花卉信息
     */
    public Flower selectFlowerByFlowerId(Long flowerId);

    /**
     * 查询花卉信息列表
     * 
     * @param flower 花卉信息
     * @return 花卉信息集合
     */
    public List<Flower> selectFlowerList(Flower flower);

    /**
     * 新增花卉信息
     * 
     * @param flower 花卉信息
     * @return 结果
     */
    public int insertFlower(Flower flower);

    /**
     * 修改花卉信息
     * 
     * @param flower 花卉信息
     * @return 结果
     */
    public int updateFlower(Flower flower);

    /**
     * 删除花卉信息
     * 
     * @param flowerId 花卉信息主键
     * @return 结果
     */
    public int deleteFlowerByFlowerId(Long flowerId);

    /**
     * 批量删除花卉信息
     * 
     * @param flowerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFlowerByFlowerIds(String[] flowerIds);

    /**
     * 查询库存低于阈值的花卉名称列表
     * * @param threshold 阈值
     * @return 花卉名称列表
     */
    public List<String> selectLowStockFlowerNames(Long threshold);

    public void updateFlowerStock(Long flowerId, long l);

    public int deleteFlowerOrderOne(String orderId, String userId, String flowerId);

    /**
     * 更新花卉库存量
     * @param flowerId 花卉ID
     * @param number 增减数量（正数加，负数减）
     */
    public int updateFlowerStock(@Param("flowerId") Long flowerId, @Param("number") int number);

    Flower selectFlowerByCode(String flowerCode);
}
