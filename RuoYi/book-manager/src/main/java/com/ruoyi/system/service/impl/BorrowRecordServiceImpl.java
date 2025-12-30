package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.BorrowRecordMapper;
import com.ruoyi.system.domain.BorrowRecord;
import com.ruoyi.system.service.IBorrowRecordService;
import com.ruoyi.common.core.text.Convert;

/**
 * 读者书籍借阅关系Service业务层处理
 * 
 * @author axz
 * @date 2025-12-30
 */
@Service
public class BorrowRecordServiceImpl implements IBorrowRecordService 
{
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    /**
     * 查询读者书籍借阅关系
     * 
     * @param borrowId 读者书籍借阅关系主键
     * @return 读者书籍借阅关系
     */
    @Override
    public BorrowRecord selectBorrowRecordByBorrowId(Long borrowId)
    {
        return borrowRecordMapper.selectBorrowRecordByBorrowId(borrowId);
    }

    /**
     * 查询读者书籍借阅关系列表
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 读者书籍借阅关系
     */
    @Override
    public List<BorrowRecord> selectBorrowRecordList(BorrowRecord borrowRecord)
    {
        return borrowRecordMapper.selectBorrowRecordList(borrowRecord);
    }

    /**
     * 新增读者书籍借阅关系
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 结果
     */
    @Override
    public int insertBorrowRecord(BorrowRecord borrowRecord)
    {
        borrowRecord.setCreateTime(DateUtils.getNowDate());
        return borrowRecordMapper.insertBorrowRecord(borrowRecord);
    }

    /**
     * 修改读者书籍借阅关系
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 结果
     */
    @Override
    public int updateBorrowRecord(BorrowRecord borrowRecord)
    {
        borrowRecord.setUpdateTime(DateUtils.getNowDate());
        return borrowRecordMapper.updateBorrowRecord(borrowRecord);
    }

    /**
     * 批量删除读者书籍借阅关系
     * 
     * @param borrowIds 需要删除的读者书籍借阅关系主键
     * @return 结果
     */
    @Override
    public int deleteBorrowRecordByBorrowIds(String borrowIds)
    {
        return borrowRecordMapper.deleteBorrowRecordByBorrowIds(Convert.toStrArray(borrowIds));
    }

    /**
     * 删除读者书籍借阅关系信息
     * 
     * @param borrowId 读者书籍借阅关系主键
     * @return 结果
     */
    @Override
    public int deleteBorrowRecordByBorrowId(Long borrowId)
    {
        return borrowRecordMapper.deleteBorrowRecordByBorrowId(borrowId);
    }
}
