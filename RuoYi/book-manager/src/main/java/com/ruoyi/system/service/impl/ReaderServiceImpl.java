package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Set;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ReaderMapper;
import com.ruoyi.system.domain.Reader;
import com.ruoyi.system.service.IReaderService;
import com.ruoyi.common.core.text.Convert;

/**
 * 用户管理Service业务层处理
 * 
 * @author axz
 * @date 2025-12-30
 */
@Service
public class ReaderServiceImpl implements IReaderService 
{
    @Autowired
    private ReaderMapper readerMapper;

    /**
     * 查询用户管理
     * 
     * @param id 用户管理主键
     * @return 用户管理
     */
    @Override
    public Reader selectReaderById(Long id)
    {
        return readerMapper.selectReaderById(id);
    }

    /**
     * 查询用户管理列表
     * 
     * @param reader 用户管理
     * @return 用户管理
     */
    @Override
    public List<Reader> selectReaderList(Reader reader)
    {
        return readerMapper.selectReaderList(reader);
    }

    /**
     * 新增用户管理
     * 
     * @param reader 用户管理
     * @return 结果
     */
    @Override
    public int insertReader(Reader reader)
    {
        reader.setCreateTime(DateUtils.getNowDate());
        if (reader.getReaderNo() == null) {
            // 为空 → 调用雪花算法自动生成19位唯一编号
            reader.setReaderNo(IdUtils.nextId());
        } else {
            // ========== 核心逻辑2：编号非空 → 校验是否重复 ==========
            Reader existReader = this.selectReaderByReaderNo(reader.getReaderNo());
            if (existReader != null) {
                // 编号已存在 → 抛出异常（上层Controller捕获返回错误）
                throw new RuntimeException("读者编号【" + reader.getReaderNo() + "】已存在，请勿重复添加！");
            }
        }
        // 校验通过 → 执行新增入库
        return readerMapper.insertReader(reader);
    }

    /**
     * 修改用户管理
     * 
     * @param reader 用户管理
     * @return 结果
     */
    @Override
    public int updateReader(Reader reader)
    {
        return readerMapper.updateReader(reader);
    }

    /**
     * 批量删除用户管理
     * 
     * @param ids 需要删除的用户管理主键
     * @return 结果
     */
    @Override
    public int deleteReaderByIds(String ids)
    {
        return readerMapper.deleteReaderByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除用户管理信息
     * 
     * @param id 用户管理主键
     * @return 结果
     */
    @Override
    public int deleteReaderById(Long id)
    {
        return readerMapper.deleteReaderById(id);
    }

    @Override
    public Set<String> selectExistReaderNos(List<String> readerNos) {
        return readerMapper.selectExistReaderNos(readerNos);
    }

    @Override
    public Reader selectReaderByReaderNo(String readerNo) {
        return readerMapper.selectReaderByReaderNo(readerNo);
    }
}
