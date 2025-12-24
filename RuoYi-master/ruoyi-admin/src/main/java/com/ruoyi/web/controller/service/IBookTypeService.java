package com.ruoyi.web.controller.service;

import java.util.List;
import com.ruoyi.web.controller.domain.BookType;

/**
 * 图书类型Service接口
 * 
 * @author azzzz
 * @date 2025-12-23
 */
public interface IBookTypeService 
{
    /**
     * 查询图书类型
     * 
     * @param id 图书类型主键
     * @return 图书类型
     */
    public BookType selectBookTypeById(Long id);

    /**
     * 查询图书类型列表
     * 
     * @param bookType 图书类型
     * @return 图书类型集合
     */
    public List<BookType> selectBookTypeList(BookType bookType);

    /**
     * 新增图书类型
     * 
     * @param bookType 图书类型
     * @return 结果
     */
    public int insertBookType(BookType bookType);

    /**
     * 修改图书类型
     * 
     * @param bookType 图书类型
     * @return 结果
     */
    public int updateBookType(BookType bookType);

    /**
     * 批量删除图书类型
     * 
     * @param ids 需要删除的图书类型主键集合
     * @return 结果
     */
    public int deleteBookTypeByIds(String ids);

    /**
     * 删除图书类型信息
     * 
     * @param id 图书类型主键
     * @return 结果
     */
    public int deleteBookTypeById(Long id);
}
