package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Buyers;

/**
 * 买家Service接口
 * 
 * @author b3q
 * @date 2026-01-06
 */
public interface IBuyersService 
{
    /**
     * 查询买家
     * 
     * @param userId 买家主键
     * @return 买家
     */
    public Buyers selectBuyersByUserId(Long userId);

    /**
     * 查询买家列表
     * 
     * @param buyers 买家
     * @return 买家集合
     */
    public List<Buyers> selectBuyersList(Buyers buyers);

    /**
     * 新增买家
     * 
     * @param buyers 买家
     * @return 结果
     */
    public int insertBuyers(Buyers buyers);

    /**
     * 修改买家
     * 
     * @param buyers 买家
     * @return 结果
     */
    public int updateBuyers(Buyers buyers);

    /**
     * 批量删除买家
     * 
     * @param userIds 需要删除的买家主键集合
     * @return 结果
     */
    public int deleteBuyersByUserIds(String userIds);

    /**
     * 删除买家信息
     * 
     * @param userId 买家主键
     * @return 结果
     */
    public int deleteBuyersByUserId(Long userId);

    /**
     * 导入买家数据
     * * @param buyersList 买家数据列表
     * @param updateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    public String importBuyers(List<Buyers> buyersList, Boolean updateSupport, String operName);

    String checkPhoneUnique(Buyers buyers);

    String checkEmailUnique(Buyers buyers);
}
