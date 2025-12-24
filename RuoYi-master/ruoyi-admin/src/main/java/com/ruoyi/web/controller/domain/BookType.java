package com.ruoyi.web.controller.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书类型对象 book_type
 * 
 * @author azzzz
 * @date 2025-12-23
 */
public class BookType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 父类型 */
    @Excel(name = "父类型")
    private Long parentId;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String tname;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setTname(String tname) 
    {
        this.tname = tname;
    }

    public String getTname() 
    {
        return tname;
    }

    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("tname", getTname())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .toString();
    }
}
