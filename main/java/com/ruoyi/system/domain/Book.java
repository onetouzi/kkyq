package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 图书管理对象 book
 * 
 * @author azzzz
 * @date 2025-12-24
 */
public class Book extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 图书编号 */
    @Excel(name = "图书编号")
    private String bookNo;

    /** 图书名称 */
    @Excel(name = "图书名称")
    private String bookName;

    /** 图片 */
    @Excel(name = "图片")
    private String img;

    /** 作者 */
    @Excel(name = "作者")
    private String author;

    /** 出版社 */
    @Excel(name = "出版社")
    private String press;

    /** 出版时间 */
    @Excel(name = "出版时间")
    private String publicTime;

    /** 库存 */
    @Excel(name = "库存")
    private Long stock;

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

    public void setPublicTime(String publicTime) 
    {
        this.publicTime = publicTime;
    }

    public String getPublicTime() 
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
            .toString();
    }
}
