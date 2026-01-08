package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.Buyers;

/**
 * 买家Mapper接口
 * 
 * @author b3q
 * @date 2026-01-06
 */
public interface BuyersMapper 
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
     * 删除买家
     * 
     * @param userId 买家主键
     * @return 结果
     */
    public int deleteBuyersByUserId(Long userId);

    /**
     * 批量删除买家
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBuyersByUserIds(String[] userIds);

    Buyers selectBuyersByPhone(String userPhone);

    Buyers selectBuyersByEmail(String userEmail);
}
