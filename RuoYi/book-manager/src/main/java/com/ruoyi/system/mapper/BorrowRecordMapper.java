package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 读者书籍借阅关系Mapper接口
 * 
 * @author axz
 * @date 2025-12-30
 */
@Mapper
public interface BorrowRecordMapper 
{
    /**
     * 查询读者书籍借阅关系
     * 
     * @param borrowId 读者书籍借阅关系主键
     * @return 读者书籍借阅关系
     */
    public BorrowRecord selectBorrowRecordByBorrowId(Long borrowId);

    /**
     * 查询读者书籍借阅关系列表
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 读者书籍借阅关系集合
     */
    public List<BorrowRecord> selectBorrowRecordList(BorrowRecord borrowRecord);

    /**
     * 新增读者书籍借阅关系
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 结果
     */
    public int insertBorrowRecord(BorrowRecord borrowRecord);

    /**
     * 修改读者书籍借阅关系
     * 
     * @param borrowRecord 读者书籍借阅关系
     * @return 结果
     */
    public int updateBorrowRecord(BorrowRecord borrowRecord);

    /**
     * 删除读者书籍借阅关系
     * 
     * @param borrowId 读者书籍借阅关系主键
     * @return 结果
     */
    public int deleteBorrowRecordByBorrowId(Long borrowId);

    /**
     * 批量删除读者书籍借阅关系
     * 
     * @param borrowIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBorrowRecordByBorrowIds(String[] borrowIds);
}
