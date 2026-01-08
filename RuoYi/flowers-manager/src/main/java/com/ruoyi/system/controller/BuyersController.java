package com.ruoyi.system.controller;

import java.util.List;

import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.StringUtils;
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
import com.ruoyi.system.domain.Buyers;
import com.ruoyi.system.service.IBuyersService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 买家Controller
 * 
 * @author b3q
 * @date 2026-01-06
 */
@Controller
@RequestMapping("/work/buyers")
public class BuyersController extends BaseController
{
    private String prefix = "work/buyers";

    @Autowired
    private IBuyersService buyersService;

    @RequiresPermissions("work:buyers:view")
    @GetMapping()
    public String buyers()
    {
        return prefix + "/buyers";
    }

    /**
     * 查询买家列表
     */
    @RequiresPermissions("work:buyers:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Buyers buyers)
    {
        startPage();
        List<Buyers> list = buyersService.selectBuyersList(buyers);
        return getDataTable(list);
    }

    /**
     * 导出买家列表
     */
    @RequiresPermissions("work:buyers:export")
    @Log(title = "买家", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Buyers buyers)
    {
        List<Buyers> list = buyersService.selectBuyersList(buyers);
        ExcelUtil<Buyers> util = new ExcelUtil<Buyers>(Buyers.class);
        return util.exportExcel(list, "买家数据");
    }

    /**
     * 新增买家
     */
    @RequiresPermissions("work:buyers:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存买家
     */
    @RequiresPermissions("work:buyers:add")
    @Log(title = "买家", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Buyers buyers)
    {
        return toAjax(buyersService.insertBuyers(buyers));
    }

    /**
     * 修改买家
     */
    @RequiresPermissions("work:buyers:edit")
    @GetMapping("/edit/{userId}")
    public String edit(@PathVariable("userId") Long userId, ModelMap mmap)
    {
        Buyers buyers = buyersService.selectBuyersByUserId(userId);
        mmap.put("buyers", buyers);
        return prefix + "/edit";
    }

    /**
     * 修改保存买家
     */
    @RequiresPermissions("work:buyers:edit")
    @Log(title = "买家", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Buyers buyers)
    {
        return toAjax(buyersService.updateBuyers(buyers));
    }

    /**
     * 删除买家
     */
    @RequiresPermissions("work:buyers:remove")
    @Log(title = "买家", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(buyersService.deleteBuyersByUserIds(ids));
    }

    /**
     * 下载导入模板
     */
    @RequiresPermissions("work:buyers:import")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate()
    {
        ExcelUtil<Buyers> util = new ExcelUtil<Buyers>(Buyers.class);
        return util.importTemplateExcel("买家数据模板");
    }

    /**
     * 导入买家数据
     */
    @Log(title = "买家管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("work:buyers:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Buyers> util = new ExcelUtil<Buyers>(Buyers.class);
        List<Buyers> buyersList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getLoginName();
        String message = buyersService.importBuyers(buyersList, updateSupport, operName);
        return AjaxResult.success(message);
    }
    /**
     * 校验手机号码是否唯一（增加格式验证）
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(Buyers buyers)
    {
        // 1. 后端格式二次校验：必须是11位数字且符合手机号段
        String phonePattern = "^1[3-9]\\d{9}$";
        if (StringUtils.isNotEmpty(buyers.getUserPhone()) && !buyers.getUserPhone().matches(phonePattern))
        {
            return "1"; // 格式不正确，视为校验不通过
        }

        // 2. 调用 Service 校验数据库唯一性
        return buyersService.checkPhoneUnique(buyers);
    }

    /**
     * 校验邮箱是否唯一（增加格式验证）
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(Buyers buyers)
    {
        // 1. 后端格式二次校验：基本的邮箱正则
        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
        if (StringUtils.isNotEmpty(buyers.getUserEmail()) && !buyers.getUserEmail().matches(emailPattern))
        {
            return "1"; // 格式不正确，视为校验不通过
        }

        // 2. 调用 Service 校验数据库唯一性
        return buyersService.checkEmailUnique(buyers);
    }

}
