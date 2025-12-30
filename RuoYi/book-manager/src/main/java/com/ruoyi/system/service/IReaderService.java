package com.ruoyi.system.service;

import java.util.List;
import java.util.Set;

import com.ruoyi.system.domain.Reader;

/**
 * 用户管理Service接口
 * 
 * @author axz
 * @date 2025-12-30
 */
public interface IReaderService 
{
    /**
     * 查询用户管理
     * 
     * @param id 用户管理主键
     * @return 用户管理
     */
    public Reader selectReaderById(Long id);

    /**
     * 查询用户管理列表
     * 
     * @param reader 用户管理
     * @return 用户管理集合
     */
    public List<Reader> selectReaderList(Reader reader);

    /**
     * 新增用户管理
     * 
     * @param reader 用户管理
     * @return 结果
     */
    public int insertReader(Reader reader);

    /**
     * 修改用户管理
     * 
     * @param reader 用户管理
     * @return 结果
     */
    public int updateReader(Reader reader);

    /**
     * 批量删除用户管理
     * 
     * @param ids 需要删除的用户管理主键集合
     * @return 结果
     */
    public int deleteReaderByIds(String ids);

    /**
     * 删除用户管理信息
     * 
     * @param id 用户管理主键
     * @return 结果
     */
    public int deleteReaderById(Long id);

    Set<Long> selectExistReaderNos(List<Long> readerNos);

    Reader selectReaderByReaderNo(Long readerNo);
}
