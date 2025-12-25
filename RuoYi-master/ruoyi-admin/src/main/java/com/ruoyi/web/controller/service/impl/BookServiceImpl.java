package com.ruoyi.web.controller.service.impl;

import java.util.List;

import com.ruoyi.web.controller.domain.Book;
import com.ruoyi.web.controller.mapper.BookMapper;
import com.ruoyi.web.controller.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.core.text.Convert;

/**
 * 图书管理Service业务层处理
 * 
 * @author azzzz
 * @date 2025-12-24
 */
@Service
public class BookServiceImpl implements IBookService
{
    @Autowired
    private BookMapper bookMapper;

    /**
     * 查询图书管理
     * 
     * @param id 图书管理主键
     * @return 图书管理
     */
    @Override
    public Book selectBookById(Long id)
    {
        return bookMapper.selectBookById(id);
    }

    /**
     * 查询图书管理列表
     * 
     * @param book 图书管理
     * @return 图书管理
     */
    @Override
    public List<Book> selectBookList(Book book)
    {
        return bookMapper.selectBookList(book);
    }

    /**
     * 新增图书管理
     * 
     * @param book 图书管理
     * @return 结果
     */
    @Override
    public int insertBook(Book book)
    {
        return bookMapper.insertBook(book);
    }

    /**
     * 修改图书管理
     * 
     * @param book 图书管理
     * @return 结果
     */
    @Override
    public int updateBook(Book book)
    {
        return bookMapper.updateBook(book);
    }

    /**
     * 批量删除图书管理
     * 
     * @param ids 需要删除的图书管理主键
     * @return 结果
     */
    @Override
    public int deleteBookByIds(String ids)
    {
        return bookMapper.deleteBookByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图书管理信息
     * 
     * @param id 图书管理主键
     * @return 结果
     */
    @Override
    public int deleteBookById(Long id)
    {
        return bookMapper.deleteBookById(id);
    }
}
