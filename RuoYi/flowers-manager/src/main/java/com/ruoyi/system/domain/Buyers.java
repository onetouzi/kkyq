package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 买家对象 buyers
 * 
 * @author b3q
 * @date 2026-01-06
 */
public class Buyers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 买家ID（用户ID） */
    private Long userId;

    /** 买家姓名 */
    @Excel(name = "买家姓名")
    private String userName;

    /** 买家手机号 */
    @Excel(name = "买家手机号")
    private String userPhone;

    /** 买家邮箱 */
    @Excel(name = "买家邮箱")
    private String userEmail;

    /** 性别：0=女,1=男,2=未知 */
    @Excel(name = "性别：0=女,1=男,2=未知")
    private String userGender;

    /** 常用收货地址 */
    @Excel(name = "常用收货地址")
    private String userAddress;

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setUserName(String userName) 
    {
        this.userName = userName;
    }

    public String getUserName() 
    {
        return userName;
    }

    public void setUserPhone(String userPhone) 
    {
        this.userPhone = userPhone;
    }

    public String getUserPhone() 
    {
        return userPhone;
    }

    public void setUserEmail(String userEmail) 
    {
        this.userEmail = userEmail;
    }

    public String getUserEmail() 
    {
        return userEmail;
    }

    public void setUserGender(String userGender) 
    {
        this.userGender = userGender;
    }

    public String getUserGender() 
    {
        return userGender;
    }

    public void setUserAddress(String userAddress) 
    {
        this.userAddress = userAddress;
    }

    public String getUserAddress() 
    {
        return userAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("userName", getUserName())
            .append("userPhone", getUserPhone())
            .append("userEmail", getUserEmail())
            .append("userGender", getUserGender())
            .append("userAddress", getUserAddress())
            .append("remark", getRemark())
            .toString();
    }
}
