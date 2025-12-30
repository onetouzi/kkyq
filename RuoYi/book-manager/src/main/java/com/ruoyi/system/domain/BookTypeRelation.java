package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书类别关系对象 book_type_relation
 * 
 * @author ruoyi
 * @date 2025-12-29
 */
public class BookTypeRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图书id */
    @Excel(name = "图书id")
    private Long bookId;

    /** 图书类比id */
    @Excel(name = "图书类比id")
    private Long bookTypeId;

    /** 图书名称（加长长度，适配实际业务） */
    @Excel(name = "图书名称", readConverterExp = "加=长长度，适配实际业务")
    private String bookName;

    /** 类型名称 */
    @Excel(name = "类型名称")
    private String typeName;

    public void setBookId(Long bookId) 
    {
        this.bookId = bookId;
    }

    public Long getBookId() 
    {
        return bookId;
    }

    public void setBookTypedId(Long bookTypedId) 
    {
        this.bookTypeId = bookTypedId;
    }

    public Long getBookTypedId() 
    {
        return bookTypeId;
    }

    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }

    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bookId", getBookId())
            .append("bookTypedId", getBookTypedId())
            .append("bookName", getBookName())
            .append("typeName", getTypeName())
            .toString();
    }
}
