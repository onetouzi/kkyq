package com.ruoyi.system.controller;

import java.util.Date;
import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BorrowRecord;
import com.ruoyi.system.service.IBorrowRecordService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 读者书籍借阅关系Controller
 * 
 * @author axz
 * @date 2025-12-30
 */
@Controller
@RequestMapping("/work/record")
public class BorrowRecordController extends BaseController
{
    private String prefix = "work/record";

    @Autowired
    private IBorrowRecordService borrowRecordService;

    @RequiresPermissions("work:record:view")
    @GetMapping()
    public String record()
    {
        return prefix + "/record";
    }

    /**
     * 查询读者书籍借阅关系列表
     */
    @RequiresPermissions("work:record:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BorrowRecord borrowRecord)
    {
        startPage();
        List<BorrowRecord> list = borrowRecordService.selectBorrowRecordList(borrowRecord);
        return getDataTable(list);
    }

    /**
     * 导出读者书籍借阅关系列表
     */
    @RequiresPermissions("work:record:export")
    @Log(title = "读者书籍借阅关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BorrowRecord borrowRecord)
    {
        List<BorrowRecord> list = borrowRecordService.selectBorrowRecordList(borrowRecord);
        ExcelUtil<BorrowRecord> util = new ExcelUtil<BorrowRecord>(BorrowRecord.class);
        return util.exportExcel(list, "读者书籍借阅关系数据");
    }

    /**
     * 新增读者书籍借阅关系
     */
    @RequiresPermissions("work:record:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存读者书籍借阅关系
     */
    @RequiresPermissions("work:record:add")
    @Log(title = "读者书籍借阅关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BorrowRecord borrowRecord) {
        // ========== 1. 核心校验逻辑 ==========
        // 获取传入的借阅时间、归还时间
        Date borrowTime = borrowRecord.getBorrowTime();
        Date returnTime = borrowRecord.getReturnTime();
        // 获取系统本地当前时间（精确到【天】，消除时分秒差异导致的校验失败）
        Date currentLocalDate = new Date();

        // 校验1：借阅时间、归还时间不能为空
        if (borrowTime == null || returnTime == null) {
            return AjaxResult.error("借阅时间和归还时间不能为空！");
        }

        // 校验2：借阅时间必须等于系统本地当前时间（按天比对，忽略时分秒）
        boolean isBorrowTimeEqualsNow = isSameDay(borrowTime, currentLocalDate);
        if (!isBorrowTimeEqualsNow) {
            return AjaxResult.error("借阅时间必须为系统本地当前时间，不允许手动修改！");
        }

        // 校验3：归还时间与借阅时间的差值，必须在7~30天范围内（含7、30天）
        long timeDiff = returnTime.getTime() - borrowTime.getTime();
        // 1天的毫秒数：24h * 60min * 60s * 1000ms = 86400000
        long oneDayMills = 86400000L;
        // 转换为天数（向下取整，保证差值精准）
        long diffDays = timeDiff / oneDayMills;
        if ( diffDays > 30) {
            return AjaxResult.error("借阅期限不合法！归还时间与借阅时间的差值必须在7-30天范围内");
        }

        // ========== 2. 自动赋值（业务优化） ==========
        // 自动设置借阅状态：1-借阅中（新增借阅默认状态）
        borrowRecord.setBorrowStatus(1L);
        // 自动设置逾期天数：0（新增借阅无逾期）
        borrowRecord.setOverdueDays("0");

        // ========== 3. 执行入库 ==========
        return toAjax(borrowRecordService.insertBorrowRecord(borrowRecord));
    }

    // ========== 工具方法：判断两个Date是否为【同一天】（忽略时分秒） ==========
    private boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        // 比对【年、月、日】是否一致
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR)
                && cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH)
                && cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }

    /**
     * 修改读者书籍借阅关系
     */
    @RequiresPermissions("work:record:edit")
    @GetMapping("/edit/{borrowId}")
    public String edit(@PathVariable("borrowId") Long borrowId, ModelMap mmap)
    {
        BorrowRecord borrowRecord = borrowRecordService.selectBorrowRecordByBorrowId(borrowId);
        mmap.put("borrowRecord", borrowRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存读者书籍借阅关系
     */
    @RequiresPermissions("work:record:edit")
    @Log(title = "读者书籍借阅关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BorrowRecord borrowRecord)
    {
        return toAjax(borrowRecordService.updateBorrowRecord(borrowRecord));
    }

    /**
     * 删除读者书籍借阅关系
     */
    @RequiresPermissions("work:record:remove")
    @Log(title = "读者书籍借阅关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(borrowRecordService.deleteBorrowRecordByBorrowIds(ids));
    }
}
