package com.ruoyi.pill.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.pill.mapper.FlowerMapper;
import com.ruoyi.pill.domain.Flower;
import com.ruoyi.pill.service.IFlowerService;
import com.ruoyi.common.core.text.Convert;

/**
 * 花卉信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FlowerServiceImpl implements IFlowerService 
{
    @Autowired
    private FlowerMapper flowerMapper;

    /**
     * 查询花卉信息
     * 
     * @param flowerId 花卉信息主键
     * @return 花卉信息
     */
    @Override
    public Flower selectFlowerByFlowerId(Long flowerId)
    {
        return flowerMapper.selectFlowerByFlowerId(flowerId);
    }

    /**
     * 查询花卉信息列表
     * 
     * @param flower 花卉信息
     * @return 花卉信息
     */
    @Override
    public List<Flower> selectFlowerList(Flower flower)
    {
        return flowerMapper.selectFlowerList(flower);
    }

    /**
     * 新增花卉信息
     * 
     * @param flower 花卉信息
     * @return 结果
     */
    @Override
    public int insertFlower(Flower flower)
    {
        flower.setCreateTime(DateUtils.getNowDate());
        return flowerMapper.insertFlower(flower);
    }

    /**
     * 修改花卉信息
     * 
     * @param flower 花卉信息
     * @return 结果
     */
    @Override
    public int updateFlower(Flower flower)
    {
        flower.setUpdateTime(DateUtils.getNowDate());
        return flowerMapper.updateFlower(flower);
    }

    /**
     * 批量删除花卉信息
     * 
     * @param flowerIds 需要删除的花卉信息主键
     * @return 结果
     */
    @Override
    public int deleteFlowerByFlowerIds(String flowerIds)
    {
        return flowerMapper.deleteFlowerByFlowerIds(Convert.toLongArray(flowerIds));
    }

    /**
     * 删除花卉信息信息
     * 
     * @param flowerId 花卉信息主键
     * @return 结果
     */
    @Override
    public int deleteFlowerByFlowerId(Long flowerId)
    {
        return flowerMapper.deleteFlowerByFlowerId(flowerId);
    }

    /**
     * 根据编码查询花卉
     * 
     * @param flowerCode 花卉编码
     * @return 花卉信息
     */
    @Override
    public Flower selectFlowerByFlowerCode(String flowerCode)
    {
        return flowerMapper.selectFlowerByFlowerCode(flowerCode);
    }

    /**
     * 更新库存
     * 
     * @param flowerId 花卉ID
     * @param quantity 数量（正数增加，负数减少）
     * @return 结果
     */
    @Override
    public int updateFlowerNum(Long flowerId, Integer quantity)
    {
        return flowerMapper.updateFlowerNum(flowerId, quantity);
    }

    /**
     * 查询低库存花卉（库存低于预警值）
     * 
     * @return 花卉信息集合
     */
    @Override
    public List<Flower> selectLowInventoryFlowers()
    {
        return flowerMapper.selectLowInventoryFlowers();
    }
}

