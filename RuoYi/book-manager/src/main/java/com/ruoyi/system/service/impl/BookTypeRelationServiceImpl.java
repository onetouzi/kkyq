package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BookTypeRelationMapper;
import com.ruoyi.system.domain.BookTypeRelation;
import com.ruoyi.system.service.IBookTypeRelationService;
import com.ruoyi.common.core.text.Convert;

/**
 * 图书类别关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-29
 */
@Service
public class BookTypeRelationServiceImpl implements IBookTypeRelationService 
{
    @Autowired
    private BookTypeRelationMapper bookTypeRelationMapper;

    /**
     * 查询图书类别关系
     * 
     * @param bookId 图书类别关系主键
     * @return 图书类别关系
     */
    @Override
    public BookTypeRelation selectBookTypeRelationByBookId(Long bookId)
    {
        return bookTypeRelationMapper.selectBookTypeRelationByBookId(bookId);
    }

    /**
     * 查询图书类别关系列表
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 图书类别关系
     */
    @Override
    public List<BookTypeRelation> selectBookTypeRelationList(BookTypeRelation bookTypeRelation)
    {
        return bookTypeRelationMapper.selectBookTypeRelationList(bookTypeRelation);
    }

    /**
     * 新增图书类别关系
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 结果
     */
    @Override
    public int insertBookTypeRelation(BookTypeRelation bookTypeRelation)
    {
        return bookTypeRelationMapper.insertBookTypeRelation(bookTypeRelation);
    }

    /**
     * 修改图书类别关系
     * 
     * @param bookTypeRelation 图书类别关系
     * @return 结果
     */
    @Override
    public int updateBookTypeRelation(BookTypeRelation bookTypeRelation)
    {
        return bookTypeRelationMapper.updateBookTypeRelation(bookTypeRelation);
    }

    /**
     * 批量删除图书类别关系
     * 
     * @param bookIds 需要删除的图书类别关系主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeRelationByBookIds(String bookIds)
    {
        return bookTypeRelationMapper.deleteBookTypeRelationByBookIds(Convert.toStrArray(bookIds));
    }

    /**
     * 删除图书类别关系信息
     * 
     * @param bookId 图书类别关系主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeRelationByBookId(Long bookId)
    {
        return bookTypeRelationMapper.deleteBookTypeRelationByBookId(bookId);
    }

    @Override
    public int batchInsertIgnoreBookTypeRelation(List<BookTypeRelation> relationList) {
        if (StringUtils.isEmpty(relationList)) {
            return 0;
        }
        return bookTypeRelationMapper.batchInsertIgnoreBookTypeRelation(relationList);
    }

    @Override
    public int deleteBookTypeRelationByBookIdAndTypeIds(Long bookId, List<Long> typeIds) {
        if (bookId == null || bookId <= 0 || typeIds == null || typeIds.isEmpty()) {
            return 0;
        }
        return bookTypeRelationMapper.deleteBookTypeRelationByBookIdAndTypeIds(bookId, typeIds);
    }
}
