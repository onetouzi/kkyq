package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BookTypeRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * 图书类别关系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-29
 */
@Mapper
public interface BookTypeRelationMapper 
{
    /**
     * 查询图书类别关系
     * 
     * @param bookId 图书类别关系主键
     * @return 图书类别关系
     */
    public BookTypeRelation selectBookTypeRelationByBookId(Long bookId);

    /**
     * 查询图书类别关系列表
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 图书类别关系集合
     */
    public List<BookTypeRelation> selectBookTypeRelationList(BookTypeRelation bookTypeRelation);

    /**
     * 新增图书类别关系
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 结果
     */
    public int insertBookTypeRelation(BookTypeRelation bookTypeRelation);

    /**
     * 修改图书类别关系
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 结果
     */
    public int updateBookTypeRelation(BookTypeRelation bookTypeRelation);

    /**
     * 删除图书类别关系
     * 
     * @param bookId 图书类别关系主键
     * @return 结果
     */
    public int deleteBookTypeRelationByBookId(Long bookId);

    /**
     * 批量删除图书类别关系
     * 
     * @param bookIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBookTypeRelationByBookIds(String[] bookIds);

    public int batchInsertIgnoreBookTypeRelation(List<BookTypeRelation> relationList);

    int deleteBookTypeRelationByBookIdAndTypeIds(Long bookId, List<Long> typeIds);
}
