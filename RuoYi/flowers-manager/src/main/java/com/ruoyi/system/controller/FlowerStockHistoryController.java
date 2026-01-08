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
import com.ruoyi.system.domain.FlowerStockHistory;
import com.ruoyi.system.service.IFlowerStockHistoryService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 花卉库存变动及交易流水Controller
 * 
 * @author b3q
 * @date 2026-01-08
 */
@Controller
@RequestMapping("/work/history")
public class FlowerStockHistoryController extends BaseController
{
    private String prefix = "work/history";

    @Autowired
    private IFlowerStockHistoryService flowerStockHistoryService;

    @RequiresPermissions("work:history:view")
    @GetMapping("/stats")
    public String stats()
    {
        return prefix + "/stats";
    }

    @RequiresPermissions("work:history:view")
    @GetMapping()
    public String history()
    {
        return prefix + "/history";
    }

    /**
     * 查询花卉库存变动及交易流水列表
     */
    @RequiresPermissions("work:history:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(FlowerStockHistory flowerStockHistory)
    {
        startPage();
        List<FlowerStockHistory> list = flowerStockHistoryService.selectFlowerStockHistoryList(flowerStockHistory);
        return getDataTable(list);
    }

    /**
     * 导出花卉库存变动及交易流水列表
     */
    @RequiresPermissions("work:history:export")
    @Log(title = "花卉库存变动及交易流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(FlowerStockHistory flowerStockHistory)
    {
        List<FlowerStockHistory> list = flowerStockHistoryService.selectFlowerStockHistoryList(flowerStockHistory);
        ExcelUtil<FlowerStockHistory> util = new ExcelUtil<FlowerStockHistory>(FlowerStockHistory.class);
        return util.exportExcel(list, "花卉库存变动及交易流水数据");
    }

    /**
     * 新增花卉库存变动及交易流水
     */
    @RequiresPermissions("work:history:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存花卉库存变动及交易流水
     */
    @RequiresPermissions("work:history:add")
    @Log(title = "花卉库存变动及交易流水", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(FlowerStockHistory flowerStockHistory)
    {
        return toAjax(flowerStockHistoryService.insertFlowerStockHistory(flowerStockHistory));
    }

    /**
     * 修改花卉库存变动及交易流水
     */
    @RequiresPermissions("work:history:edit")
    @GetMapping("/edit/{historyId}")
    public String edit(@PathVariable("historyId") Long historyId, ModelMap mmap)
    {
        FlowerStockHistory flowerStockHistory = flowerStockHistoryService.selectFlowerStockHistoryByHistoryId(historyId);
        mmap.put("flowerStockHistory", flowerStockHistory);
        return prefix + "/edit";
    }

    /**
     * 修改保存花卉库存变动及交易流水
     */
    @RequiresPermissions("work:history:edit")
    @Log(title = "花卉库存变动及交易流水", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(FlowerStockHistory flowerStockHistory)
    {
        return toAjax(flowerStockHistoryService.updateFlowerStockHistory(flowerStockHistory));
    }

    /**
     * 删除花卉库存变动及交易流水
     */
    @RequiresPermissions("work:history:remove")
    @Log(title = "花卉库存变动及交易流水", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(flowerStockHistoryService.deleteFlowerStockHistoryByHistoryIds(ids));
    }
}
