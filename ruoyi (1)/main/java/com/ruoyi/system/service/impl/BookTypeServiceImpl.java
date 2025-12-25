package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BookTypeMapper;
import com.ruoyi.system.domain.BookType;
import com.ruoyi.system.service.IBookTypeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 图书类型Service业务层处理
 * 
 * @author azzzz
 * @date 2025-12-23
 */
@Service
public class BookTypeServiceImpl implements IBookTypeService 
{
    @Autowired
    private BookTypeMapper bookTypeMapper;

    /**
     * 查询图书类型
     * 
     * @param id 图书类型主键
     * @return 图书类型
     */
    @Override
    public BookType selectBookTypeById(Long id)
    {
        return bookTypeMapper.selectBookTypeById(id);
    }

    /**
     * 查询图书类型列表
     * 
     * @param bookType 图书类型
     * @return 图书类型
     */
    @Override
    public List<BookType> selectBookTypeList(BookType bookType)
    {
        return bookTypeMapper.selectBookTypeList(bookType);
    }

    /**
     * 新增图书类型
     * 
     * @param bookType 图书类型
     * @return 结果
     */
    @Override
    public int insertBookType(BookType bookType)
    {
        return bookTypeMapper.insertBookType(bookType);
    }

    /**
     * 修改图书类型
     * 
     * @param bookType 图书类型
     * @return 结果
     */
    @Override
    public int updateBookType(BookType bookType)
    {
        return bookTypeMapper.updateBookType(bookType);
    }

    /**
     * 批量删除图书类型
     * 
     * @param ids 需要删除的图书类型主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeByIds(String ids)
    {
        return bookTypeMapper.deleteBookTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图书类型信息
     * 
     * @param id 图书类型主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeById(Long id)
    {
        return bookTypeMapper.deleteBookTypeById(id);
    }
}
