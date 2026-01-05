package com.ruoyi.pill.mapper;

import java.util.List;
import com.ruoyi.pill.domain.Flower;
import org.apache.ibatis.annotations.Mapper;

/**
 * 花卉信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Mapper
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
    public int deleteFlowerByFlowerIds(Long[] flowerIds);

    /**
     * 根据编码查询花卉
     * 
     * @param flowerCode 花卉编码
     * @return 花卉信息
     */
    public Flower selectFlowerByFlowerCode(String flowerCode);

    /**
     * 更新库存
     * 
     * @param flowerId 花卉ID
     * @param quantity 数量（正数增加，负数减少）
     * @return 结果
     */
    public int updateFlowerNum(Long flowerId, Integer quantity);

    /**
     * 查询低库存花卉（库存低于预警值）
     * 
     * @return 花卉信息集合
     */
    public List<Flower> selectLowInventoryFlowers();
}

