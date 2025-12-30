package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BookType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书类别Mapper接口
 * 
 * @author b3q
 * @date 2025-12-29
 */
@Mapper

public interface BookTypeMapper 
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
     * 删除图书类别
     * 
     * @param id 图书类别主键
     * @return 结果
     */
    public int deleteBookTypeById(Long id);

    /**
     * 批量删除图书类别
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookTypeByIds(String[] ids);

    List<Long> selectBookTypeIdsByNames(List<String> typeNames);
}
