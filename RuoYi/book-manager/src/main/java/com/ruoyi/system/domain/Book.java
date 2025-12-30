package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书对象 book
 * 
 * @author b3q
 * @date 2025-12-29
 */
public class Book extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 图书编号 */
    @Excel(name = "图书编号")
    private String bookNo;

    /** 图书名称（加长长度，适配实际业务） */
    @Excel(name = "图书名称")
    private String bookName;

    /** 封面图片（加长存储路径） */
    @Excel(name = "封面图片")
    private String img;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 出版社 */
    @Excel(name = "出版社")
    private String press;

    /** 出版时间（改用DATE类型，支持完整日期） */
    @Excel(name = "出版时间",dateFormat = "yyyy-MM-dd")
    private Date publicTime;

    /** 库存（默认0） */
    @Excel(name = "库存")
    private Long stock;

    /** 状态（0正常 1下架） */
    @Excel(name = "状态", readConverterExp="0=已经借出,1=没有借出")
    private String status;

    /** 图书类别，用逗号分割(科幻,日常) */
    @Excel(name = "图书类别，用逗号分割(科幻,日常)")
    private String typeName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setBookNo(String bookNo) 
    {
        this.bookNo = bookNo;
    }

    public String getBookNo() 
    {
        return bookNo;
    }

    public void setBookName(String bookName) 
    {
        this.bookName = bookName;
    }

    public String getBookName() 
    {
        return bookName;
    }

    public void setImg(String img) 
    {
        this.img = img;
    }

    public String getImg() 
    {
        return img;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setPress(String press) 
    {
        this.press = press;
    }

    public String getPress() 
    {
        return press;
    }

    public void setPublicTime(Date publicTime) 
    {
        this.publicTime = publicTime;
    }

    public Date getPublicTime() 
    {
        return publicTime;
    }

    public void setStock(Long stock) 
    {
        this.stock = stock;
    }

    public Long getStock() 
    {
        return stock;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("id", getId())
            .append("bookNo", getBookNo())
            .append("bookName", getBookName())
            .append("img", getImg())
            .append("author", getAuthor())
            .append("press", getPress())
            .append("publicTime", getPublicTime())
            .append("stock", getStock())
            .append("status", getStatus())
            .append("typeName", getTypeName())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
