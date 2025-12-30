package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BookMapper;
import com.ruoyi.system.domain.Book;
import com.ruoyi.system.service.IBookService;
import com.ruoyi.common.core.text.Convert;

/**
 * 图书Service业务层处理
 * 
 * @author b3q
 * @date 2025-12-29
 */
@Service
public class BookServiceImpl implements IBookService 
{
    @Autowired
    private BookMapper bookMapper;

    /**
     * 查询图书
     * 
     * @param id 图书主键
     * @return 图书
     */
    @Override
    public Book selectBookById(Long id)
    {
        return bookMapper.selectBookById(id);
    }

    /**
     * 查询图书列表
     * 
     * @param book 图书
     * @return 图书
     */
    @Override
    public List<Book> selectBookList(Book book)
    {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增图书
     * 
     * @param book 图书
     * @return 结果
     */
    @Override
    public int insertBook(Book book)
    {
        book.setCreateTime(DateUtils.getNowDate());
        return bookMapper.insertBook(book);
    }

    /**
     * 修改图书
     * 
     * @param book 图书
     * @return 结果
     */
    @Override
    public int updateBook(Book book)
    {
        book.setUpdateTime(DateUtils.getNowDate());
        return bookMapper.updateBook(book);
    }

    /**
     * 批量删除图书
     * 
     * @param ids 需要删除的图书主键
     * @return 结果
     */
    @Override
    public int deleteBookByIds(String ids)
    {
        return bookMapper.deleteBookByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图书信息
     * 
     * @param id 图书主键
     * @return 结果
     */
    @Override
    public int deleteBookById(Long id)
    {
        return bookMapper.deleteBookById(id);
    }

    @Override
    public Book selectBookByBookNo(String bookNo) {
        return bookMapper.selectBookByBookNo(bookNo);
    }

    @Override
    public Set<String> selectExistBookNos(List<String> bookNos) {
        return bookMapper.selectExistBookNos(bookNos);
    }
}
