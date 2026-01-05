package com.ruoyi.pill.service;

import java.util.List;
import com.ruoyi.pill.domain.BuyRequest;
import com.ruoyi.pill.domain.Flower;

/**
 * 花卉购买Service接口
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
public interface IFlowerClientService 
{
    /**
     * 查询可购买花卉列表（仅显示有库存且状态正常的花卉）
     * 
     * @param flower 花卉信息
     * @return 花卉信息集合
     */
    public List<Flower> selectFlowerClientList(Flower flower);

    /**
     * 购买花卉（使用当前登录用户）
     * 
     * @param buyRequests 购买请求列表
     * @return 结果
     */
    public int buyFlowers(List<BuyRequest> buyRequests);
}

