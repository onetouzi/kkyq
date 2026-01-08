package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BuyersMapper;
import com.ruoyi.system.domain.Buyers;
import com.ruoyi.system.service.IBuyersService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 买家Service业务层处理
 * 
 * @author b3q
 * @date 2026-01-06
 */
@Service
public class BuyersServiceImpl implements IBuyersService 
{
    @Autowired
    private BuyersMapper buyersMapper;

    /**
     * 查询买家
     * 
     * @param userId 买家主键
     * @return 买家
     */
    @Override
    public Buyers selectBuyersByUserId(Long userId)
    {
        return buyersMapper.selectBuyersByUserId(userId);
    }

    /**
     * 查询买家列表
     * 
     * @param buyers 买家
     * @return 买家
     */
    @Override
    public List<Buyers> selectBuyersList(Buyers buyers)
    {
        return buyersMapper.selectBuyersList(buyers);
    }

    /**
     * 新增买家
     * 
     * @param buyers 买家
     * @return 结果
     */
    @Override
    public int insertBuyers(Buyers buyers)
    {
        return buyersMapper.insertBuyers(buyers);
    }

    /**
     * 修改买家
     * 
     * @param buyers 买家
     * @return 结果
     */
    @Override
    public int updateBuyers(Buyers buyers)
    {
        return buyersMapper.updateBuyers(buyers);
    }

    /**
     * 批量删除买家
     * 
     * @param userIds 需要删除的买家主键
     * @return 结果
     */
    @Override
    public int deleteBuyersByUserIds(String userIds)
    {
        return buyersMapper.deleteBuyersByUserIds(Convert.toStrArray(userIds));
    }

    /**
     * 删除买家信息
     * 
     * @param userId 买家主键
     * @return 结果
     */
    @Override
    public int deleteBuyersByUserId(Long userId)
    {
        return buyersMapper.deleteBuyersByUserId(userId);
    }

    /**
     * 导入买家数据
     */
    @Override
    @Transactional
    public String importBuyers(List<Buyers> buyersList, Boolean updateSupport, String operName)
    {
        if (StringUtils.isNull(buyersList) || buyersList.size() == 0)
        {
            throw new ServiceException("导入买家数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (Buyers buyer : buyersList)
        {
            try
            {
                // 1. 验证是否存在（此处根据手机号查重，如果没有该方法需在Mapper中定义）
                Buyers b = buyersMapper.selectBuyersByPhone(buyer.getUserPhone());

                if (StringUtils.isNull(b))
                {
                    buyer.setCreateBy(operName);
                    buyersMapper.insertBuyers(buyer);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、买家 " + buyer.getUserName() + " 导入成功");
                }
                else if (updateSupport)
                {
                    buyer.setUserId(b.getUserId());
                    buyer.setUpdateBy(operName);
                    buyersMapper.updateBuyers(buyer);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、买家 " + buyer.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、买家手机号 " + buyer.getUserPhone() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、买家 " + buyer.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 校验手机号码是否唯一
     */
    @Override
    public String checkPhoneUnique(Buyers buyers)
    {
        Long userId = StringUtils.isNull(buyers.getUserId()) ? -1L : buyers.getUserId();
        // 此处调用之前定义的根据手机号查询的方法
        Buyers info = buyersMapper.selectBuyersByPhone(buyers.getUserPhone());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return "1"; // 返回 1 在若依封装的校验中代表“不唯一”（匹配前端 $.validate.unique）
        }
        return "0"; // 返回 0 代表“唯一”
    }

    /**
     * 校验邮箱是否唯一
     */
    @Override
    public String checkEmailUnique(Buyers buyers)
    {
        Long userId = StringUtils.isNull(buyers.getUserId()) ? -1L : buyers.getUserId();
        // 需要在 Mapper 中增加 selectBuyersByEmail 方法
        Buyers info = buyersMapper.selectBuyersByEmail(buyers.getUserEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return "1";
        }
        return "0";
    }

}
