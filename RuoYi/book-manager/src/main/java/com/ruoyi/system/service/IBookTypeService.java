package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BookType;

/**
 * 图书类别Service接口
 * 
 * @author b3q
 * @date 2025-12-29
 */
public interface IBookTypeService 
{
    /**
     * 查询图书类别
     * 
     * @param id 图书类别主键
     * @return 图书类别
     */
    public BookType selectBookTypeById(Long id);

    /**
     * 查询图书类别列表
     * 
     * @param bookType 图书类别
     * @return 图书类别集合
     */
    public List<BookType> selectBookTypeList(BookType bookType);

    /**
     * 新增图书类别
     * 
     * @param bookType 图书类别
     * @return 结果
     */
    public int insertBookType(BookType bookType);

    /**
     * 修改图书类别
     * 
     * @param bookType 图书类别
     * @return 结果
     */
    public int updateBookType(BookType bookType);

    /**
     * 批量删除图书类别
     * 
     * @param ids 需要删除的图书类别主键集合
     * @return 结果
     */
    public int deleteBookTypeByIds(String ids);

    /**
     * 删除图书类别信息
     * 
     * @param id 图书类别主键
     * @return 结果
     */
    public int deleteBookTypeById(Long id);

    /**
     * 根据类型名称查询ID，不存在则新建并返回ID
     * @param typeName 图书类型名称（如"编程"，不能为空/纯空格）
     * @return 对应类型的ID
     * @throws IllegalArgumentException 若typeName为空则抛出异常
     */
    Long getOrCreateBookTypeId(String typeName);

    List<Long> selectBookTypeIdsByNames(List<String> deleteTypeNames);
}
