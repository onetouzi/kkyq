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
import com.ruoyi.system.domain.BookType;
import com.ruoyi.system.service.IBookTypeService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 图书类别Controller
 * 
 * @author b3q
 * @date 2025-12-29
 */
@Controller
@RequestMapping("/work/type")
public class BookTypeController extends BaseController
{
    private String prefix = "work/type";

    @Autowired
    private IBookTypeService bookTypeService;

    @RequiresPermissions("work:type:view")
    @GetMapping()
    public String type()
    {
        return prefix + "/type";
    }

    /**
     * 查询图书类别列表
     */
    @RequiresPermissions("work:type:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(BookType bookType)
    {
        startPage();
        List<BookType> list = bookTypeService.selectBookTypeList(bookType);
        return getDataTable(list);
    }

    /**
     * 导出图书类别列表
     */
    @RequiresPermissions("work:type:export")
    @Log(title = "图书类别", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(BookType bookType)
    {
        List<BookType> list = bookTypeService.selectBookTypeList(bookType);
        ExcelUtil<BookType> util = new ExcelUtil<BookType>(BookType.class);
        return util.exportExcel(list, "图书类别数据");
    }

    /**
     * 新增图书类别
     */
    @RequiresPermissions("work:type:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存图书类别
     */
    @RequiresPermissions("work:type:add")
    @Log(title = "图书类别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(BookType bookType)
    {
        return toAjax(bookTypeService.insertBookType(bookType));
    }

    /**
     * 修改图书类别
     */
    @RequiresPermissions("work:type:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        BookType bookType = bookTypeService.selectBookTypeById(id);
        mmap.put("bookType", bookType);
        return prefix + "/edit";
    }

    /**
     * 修改保存图书类别
     */
    @RequiresPermissions("work:type:edit")
    @Log(title = "图书类别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(BookType bookType)
    {
        return toAjax(bookTypeService.updateBookType(bookType));
    }

    /**
     * 删除图书类别
     */
    @RequiresPermissions("work:type:remove")
    @Log(title = "图书类别", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookTypeService.deleteBookTypeByIds(ids));
    }
}
