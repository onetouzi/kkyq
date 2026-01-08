package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.FlowerIdGenerator;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.FlowerStockHistory;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FlowerMapper;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.service.IFlowerService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 花卉信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-01-07
 */
@Service
public class FlowerServiceImpl implements IFlowerService 
{
    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private FlowerStockHistoryServiceImpl stockHistoryService;

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
        return flowerMapper.deleteFlowerByFlowerIds(Convert.toStrArray(flowerIds));
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

    @Override
    public List<String> selectLowStockFlowerNames(Long threshold) {
        return flowerMapper.selectLowStockFlowerNames(threshold);
    }

    @Override
    @Transactional
    public String importFlower(List<Flower> flowerList, Boolean updateSupport, String operName) {
        if (flowerList == null || flowerList.isEmpty()) {
            throw new ServiceException("导入花卉数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (Flower flower : flowerList) {
            try {
                // 1. 验证是否存在（根据编码查重）
                Flower f = flowerMapper.selectFlowerByCode(flower.getFlowerCode());

                if (f == null) {
                    // 执行新增逻辑
                    flower.setCreateBy(operName);
                    flower.setCreateTime(DateUtils.getNowDate());
                    // 如果Excel没填编码，则自动生成
                    if (StringUtils.isEmpty(flower.getFlowerCode())) {
                        flower.setFlowerCode(FlowerIdGenerator.generateFlowerNo());
                    }

                    flowerMapper.insertFlower(flower);

                    // 关键点：调用你已有的 recordFlowerLog 记录进货流水
                    // 业务类型 "0" 代表进货，利润会自动计算为 0
                    if (flower.getNum() != null && flower.getNum() > 0) {
                        stockHistoryService.recordFlowerLog(flower, "0", flower.getNum(), "Excel导入：初始库存入库");
                    }

                    successNum++;
                } else if (updateSupport) {
                    // 执行更新逻辑
                    flower.setFlowerId(f.getFlowerId());
                    flower.setUpdateBy(operName);
                    flower.setUpdateTime(DateUtils.getNowDate());
                    flowerMapper.updateFlower(flower);
                    successNum++;
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、花卉 " + flower.getFlowerName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                failureMsg.append("<br/>" + failureNum + "、花卉 " + flower.getFlowerName() + " 导入失败：" + e.getMessage());
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入部分失败！共 " + failureNum + " 条错误：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "数据已全部导入成功！共 " + successNum + " 条。");
        }
        return successMsg.toString();
    }


}
