package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.domain.FlowerOrder;

/**
 * 花卉信息Service接口
 * 
 * @author ruoyi
 * @date 2026-01-07
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

    public List<String> selectLowStockFlowerNames(Long threshold);

    /**
     * 导入花卉数据
     * * @param flowerList 花卉数据列表
     * @param updateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importFlower(List<Flower> flowerList, Boolean updateSupport, String operName);

}
