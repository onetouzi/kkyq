package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.BookTypeRelation;
import com.ruoyi.system.service.IBookTypeRelationService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书类别关系Controller
 * 
 * @author ruoyi
 * @date 2025-12-29
 */
@Controller
@RequestMapping("/work/relation")
public class BookTypeRelationController extends BaseController
{
    private String prefix = "work/relation";

    @Autowired
    private IBookTypeRelationService bookTypeRelationService;

    @RequiresPermissions("work:relation:view")
    @GetMapping()
    public String relation()
    {
        return prefix + "/relation";
    }

    /**
     * 查询图书类别关系列表
     */
    @RequiresPermissions("work:relation:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookTypeRelation bookTypeRelation)
    {
        startPage();
        List<BookTypeRelation> list = bookTypeRelationService.selectBookTypeRelationList(bookTypeRelation);
        return getDataTable(list);
    }

    /**
     * 导出图书类别关系列表
     */
    @RequiresPermissions("work:relation:export")
    @Log(title = "图书类别关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BookTypeRelation bookTypeRelation)
    {
        List<BookTypeRelation> list = bookTypeRelationService.selectBookTypeRelationList(bookTypeRelation);
        ExcelUtil<BookTypeRelation> util = new ExcelUtil<BookTypeRelation>(BookTypeRelation.class);
        return util.exportExcel(list, "图书类别关系数据");
    }

    /**
     * 新增图书类别关系
     */
    @RequiresPermissions("work:relation:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存图书类别关系
     */
    @RequiresPermissions("work:relation:add")
    @Log(title = "图书类别关系", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BookTypeRelation bookTypeRelation)
    {
        return toAjax(bookTypeRelationService.insertBookTypeRelation(bookTypeRelation));
    }

    /**
     * 修改图书类别关系
     */
    @RequiresPermissions("work:relation:edit")
    @GetMapping("/edit/{bookId}")
    public String edit(@PathVariable("bookId") Long bookId, ModelMap mmap)
    {
        BookTypeRelation bookTypeRelation = bookTypeRelationService.selectBookTypeRelationByBookId(bookId);
        mmap.put("bookTypeRelation", bookTypeRelation);
        return prefix + "/edit";
    }

    /**
     * 修改保存图书类别关系
     */
    @RequiresPermissions("work:relation:edit")
    @Log(title = "图书类别关系", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BookTypeRelation bookTypeRelation)
    {
        return toAjax(bookTypeRelationService.updateBookTypeRelation(bookTypeRelation));
    }

    /**
     * 删除图书类别关系
     */
    @RequiresPermissions("work:relation:remove")
    @Log(title = "图书类别关系", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookTypeRelationService.deleteBookTypeRelationByBookIds(ids));
    }
}
