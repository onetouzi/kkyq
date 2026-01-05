package com.ruoyi.pill.service;

import java.util.List;
import com.ruoyi.pill.domain.Flower;

/**
 * 花卉信息Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface IFlowerService 
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
     * 批量删除花卉信息
     * 
     * @param flowerIds 需要删除的花卉信息主键集合
     * @return 结果
     */
    public int deleteFlowerByFlowerIds(String flowerIds);

    /**
     * 删除花卉信息信息
     * 
     * @param flowerId 花卉信息主键
     * @return 结果
     */
    public int deleteFlowerByFlowerId(Long flowerId);

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

