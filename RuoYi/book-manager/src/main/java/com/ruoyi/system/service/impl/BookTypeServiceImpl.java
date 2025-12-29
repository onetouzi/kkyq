package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BookTypeMapper;
import com.ruoyi.system.domain.BookType;
import com.ruoyi.system.service.IBookTypeService;
import com.ruoyi.common.core.text.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 图书类别Service业务层处理
 * 
 * @author b3q
 * @date 2025-12-29
 */
@Service
public class BookTypeServiceImpl implements IBookTypeService 
{
    @Autowired
    private BookTypeMapper bookTypeMapper;

    /**
     * 查询图书类别
     * 
     * @param id 图书类别主键
     * @return 图书类别
     */
    @Override
    public BookType selectBookTypeById(Long id)
    {
        return bookTypeMapper.selectBookTypeById(id);
    }

    /**
     * 查询图书类别列表
     * 
     * @param bookType 图书类别
     * @return 图书类别
     */
    @Override
    public List<BookType> selectBookTypeList(BookType bookType)
    {
        return bookTypeMapper.selectBookTypeList(bookType);
    }

    /**
     * 新增图书类别
     * 
     * @param bookType 图书类别
     * @return 结果
     */
    @Override
    public int insertBookType(BookType bookType)
    {
        return bookTypeMapper.insertBookType(bookType);
    }

    /**
     * 修改图书类别
     * 
     * @param bookType 图书类别
     * @return 结果
     */
    @Override
    public int updateBookType(BookType bookType)
    {
        return bookTypeMapper.updateBookType(bookType);
    }

    /**
     * 批量删除图书类别
     * 
     * @param ids 需要删除的图书类别主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeByIds(String ids)
    {
        return bookTypeMapper.deleteBookTypeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除图书类别信息
     * 
     * @param id 图书类别主键
     * @return 结果
     */
    @Override
    public int deleteBookTypeById(Long id)
    {
        return bookTypeMapper.deleteBookTypeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 事务控制，避免并发问题
    public Long getOrCreateBookTypeId(String typeName) {
        if (StringUtils.isBlank(typeName)) {
            throw new IllegalArgumentException("图书类型名称不能为空！");
        }
        // 去除前后空格（避免" 编程 "和"编程"被判定为不同类型）
        String cleanTypeName = typeName.trim();

        //  构造查询条件：根据类型名称查询
        BookType queryCondition = new BookType();
        queryCondition.setTypeName(cleanTypeName); // 假设BookType有typeName字段存储类型名称
        List<BookType> existTypes = bookTypeMapper.selectBookTypeList(queryCondition);

        // 存在则返回ID
        if (!existTypes.isEmpty()) {
            return existTypes.get(0).getId(); // 取第一个匹配的ID（需保证typeName唯一）
        }

        // 不存在则新建类型
        BookType newBookType = new BookType();
        newBookType.setTypeName(cleanTypeName);
        // 若有其他字段（如创建时间），可在这里赋值（若Mapper已配置自动填充则无需手动加）
        // newBookType.setCreateTime(new Date());

        // 插入数据库并返回新ID
        int insertResult = bookTypeMapper.insertBookType(newBookType);
        if (insertResult <= 0) {
            throw new ServiceException("新增图书类型失败：" + cleanTypeName);
        }
        return newBookType.getId();
    }

    @Override
    public List<Long> selectBookTypeIdsByNames(List<String> typeNames) {
        if (typeNames == null || typeNames.isEmpty()) {
            return new ArrayList<>();
        }
        return bookTypeMapper.selectBookTypeIdsByNames(typeNames);
    }

}
