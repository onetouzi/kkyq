package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 用户管理对象 reader
 * 
 * @author axz
 * @date 2025-12-30
 */
public class Reader extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /**  */
    @Excel(name = "读者编号")
    private Long readerNo;

    /**  */
    @Excel(name = "读者姓名")
    private String readerName;

    /**  */
    @Excel(name = "读者电话")
    private String phone;

    @Excel(name = "创建时间",dateFormat = "yyyy-MM-dd")
    private Date createTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setReaderNo(Long readerNo) 
    {
        this.readerNo = readerNo;
    }

    public Long getReaderNo() 
    {
        return readerNo;
    }

    public void setReaderName(String readerName) 
    {
        this.readerName = readerName;
    }

    public String getReaderName() 
    {
        return readerName;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("readerNo", getReaderNo())
            .append("readerName", getReaderName())
            .append("phone", getPhone())
            .append("createTime", getCreateTime())
            .toString();
    }
}
