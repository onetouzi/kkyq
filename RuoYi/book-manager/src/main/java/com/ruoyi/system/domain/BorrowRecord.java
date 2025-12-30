package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 读者书籍借阅关系对象 borrow_record
 * 
 * @author axz
 * @date 2025-12-30
 */
public class BorrowRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 借阅主键ID，自增唯一 */
    private Long borrowId;

    /** 读者编号 */
    @Excel(name = "读者编号")
    private String readerNo;

    /** 读者姓名，冗余存储提升查询效率 */
    @Excel(name = "读者姓名，冗余存储提升查询效率")
    private String readerName;

    /** 书籍编号 */
    @Excel(name = "书籍编号")
    private String bookNo;

    /** 书籍名称，冗余存储提升查询效率 */
    @Excel(name = "书籍名称，冗余存储提升查询效率")
    private String bookName;

    /** 借阅时间，默认当前时间 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Excel(name = "借阅时间，默认当前时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date borrowTime;

    /** 归还时间（NULL表示未归还） */
    @Excel(name = "归还时间", readConverterExp = "N=ULL表示未归还")
    private Date returnTime;

    /** 借阅状态：1-借阅中 2-已归还 3-逾期未还 */
    @Excel(name = "借阅状态：1-借阅中 2-已归还 3-逾期未还")
    private Long borrowStatus;

    /** 逾期天数，无逾期为0，非负数 */
    @Excel(name = "逾期天数，无逾期为0，非负数")
    private String overdueDays;

    public void setBorrowId(Long borrowId) 
    {
        this.borrowId = borrowId;
    }

    public Long getBorrowId() 
    {
        return borrowId;
    }

    public void setReaderNo(String readerNo) 
    {
        this.readerNo = readerNo;
    }

    public String getReaderNo() 
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

    public void setBorrowTime(Date borrowTime) 
    {
        this.borrowTime = borrowTime;
    }

    public Date getBorrowTime() 
    {
        return borrowTime;
    }

    public void setReturnTime(Date returnTime) 
    {
        this.returnTime = returnTime;
    }

    public Date getReturnTime() 
    {
        return returnTime;
    }

    public void setBorrowStatus(Long borrowStatus) 
    {
        this.borrowStatus = borrowStatus;
    }

    public Long getBorrowStatus() 
    {
        return borrowStatus;
    }

    public void setOverdueDays(String overdueDays) 
    {
        this.overdueDays = overdueDays;
    }

    public String getOverdueDays() 
    {
        return overdueDays;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("borrowId", getBorrowId())
            .append("readerNo", getReaderNo())
            .append("readerName", getReaderName())
            .append("bookNo", getBookNo())
            .append("bookName", getBookName())
            .append("borrowTime", getBorrowTime())
            .append("returnTime", getReturnTime())
            .append("borrowStatus", getBorrowStatus())
            .append("overdueDays", getOverdueDays())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
