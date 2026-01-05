package com.ruoyi.pill.service.impl;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.pill.mapper.FlowerMapper;
import com.ruoyi.pill.mapper.FlowerOrderMapper;
import com.ruoyi.pill.domain.BuyRequest;
import com.ruoyi.pill.domain.Flower;
import com.ruoyi.pill.domain.FlowerOrder;
import com.ruoyi.pill.service.IFlowerClientService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ShiroUtils;

/**
 * 花卉购买Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FlowerClientServiceImpl implements IFlowerClientService 
{
    @Autowired
    private FlowerMapper flowerMapper;

    @Autowired
    private FlowerOrderMapper flowerOrderMapper;

    /**
     * 查询可购买花卉列表（仅显示有库存且状态正常的花卉）
     * 
     * @param flower 花卉信息
     * @return 花卉信息集合
     */
    @Override
    public List<Flower> selectFlowerClientList(Flower flower)
    {
        // 只查询状态正常且有库存的花卉
        flower.setStatus("0");
        List<Flower> list = flowerMapper.selectFlowerList(flower);
        // 过滤掉库存为0的花卉
        list.removeIf(f -> f.getNum() == null || f.getNum() <= 0);
        return list;
    }

    /**
     * 购买花卉
     * 
     * @param buyRequests 购买请求列表
     * @param userId 用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int buyFlowers(List<BuyRequest> buyRequests, Long userId)
    {
        if (buyRequests == null || buyRequests.isEmpty())
        {
            throw new RuntimeException("购买请求不能为空");
        }

        for (BuyRequest buyRequest : buyRequests)
        {
            // 查询花卉信息
            Flower flower = flowerMapper.selectFlowerByFlowerId(buyRequest.getFlowerId());
            if (flower == null)
            {
                throw new RuntimeException("花卉不存在，ID：" + buyRequest.getFlowerId());
            }
            if (!"0".equals(flower.getStatus()))
            {
                throw new RuntimeException("花卉已停用：" + flower.getFlowerName());
            }
            if (flower.getNum() == null || flower.getNum() < buyRequest.getQuantity())
            {
                throw new RuntimeException("库存不足：" + flower.getFlowerName() + "，当前库存：" + flower.getNum());
            }

            // 扣减库存
            int updateResult = flowerMapper.updateFlowerNum(buyRequest.getFlowerId(), -buyRequest.getQuantity());
            if (updateResult <= 0)
            {
                throw new RuntimeException("扣减库存失败：" + flower.getFlowerName());
            }

            // 创建订单
            FlowerOrder order = new FlowerOrder();
            order.setUserId(userId);
            order.setFlowerId(buyRequest.getFlowerId());
            order.setQuantity(buyRequest.getQuantity());
            order.setTotalPrice(new BigDecimal(buyRequest.getPrice() * buyRequest.getQuantity()));
            order.setStatus("PAID");
            order.setCreateTime(DateUtils.getNowDate());
            flowerOrderMapper.insertFlowerOrder(order);
        }

        return buyRequests.size();
    }

    /**
     * 购买花卉（使用当前登录用户）
     * 
     * @param buyRequests 购买请求列表
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int buyFlowers(List<BuyRequest> buyRequests)
    {
        Long userId = ShiroUtils.getUserId();
        return buyFlowers(buyRequests, userId);
    }
}

