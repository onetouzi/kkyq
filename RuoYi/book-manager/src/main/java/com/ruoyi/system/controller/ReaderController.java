package com.ruoyi.system.controller;

import java.util.*;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.Book;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Reader;
import com.ruoyi.system.service.IReaderService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户管理Controller
 *
 * @author axz
 * @date 2025-12-30
 */
@Controller
@RequestMapping("/work/reader")
public class ReaderController extends BaseController
{
    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);
    // 手机号正则校验规则
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    private String prefix = "work/reader";

    @Autowired
    private IReaderService readerService;

    @RequiresPermissions("work:reader:view")
    @GetMapping()
    public String reader()
    {
        return prefix + "/reader";
    }

    /**
     * 查询用户管理列表
     */
    @RequiresPermissions("work:reader:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Reader reader)
    {
        startPage();
        List<Reader> list = readerService.selectReaderList(reader);
        return getDataTable(list);
    }

    /**
     * 导出用户管理列表
     */
    @RequiresPermissions("work:reader:export")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Reader reader)
    {
        List<Reader> list = readerService.selectReaderList(reader);
        ExcelUtil<Reader> util = new ExcelUtil<Reader>(Reader.class);
        return util.exportExcel(list, "用户管理数据");
    }

    /**
     * 新增用户管理
     */
    @RequiresPermissions("work:reader:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存用户管理 ✅ 导入功能直接调用此方法
     */
    @RequiresPermissions("work:reader:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Reader reader)
    {
        try {
            // 调用Service执行新增（内含雪花生成+重复校验）
            return toAjax(readerService.insertReader(reader));
        } catch (RuntimeException e) {
            // 捕获编号重复异常，返回错误提示
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改用户管理
     */
    @RequiresPermissions("work:reader:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Reader reader = readerService.selectReaderById(id);
        mmap.put("reader", reader);
        return prefix + "/edit";
    }

    /**
     * 修改保存用户管理 ✅ 导入功能直接调用此方法
     */
    @RequiresPermissions("work:reader:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Reader reader)
    {
        return toAjax(readerService.updateReader(reader));
    }

    /**
     * 删除用户管理
     */
    @RequiresPermissions("work:reader:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(readerService.deleteReaderByIds(ids));
    }

    /**
     * 导入读者数据（核心方法）✅ 完全复用addSave/editSave、原有逻辑无变更
     */
    @RequiresPermissions("work:reader:import")
    @Log(title = "读者管理", businessType = BusinessType.IMPORT)
    @PostMapping("/import")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importData(MultipartFile file, Boolean updateSupport) throws Exception {
        // 强制开启更新模式（与原图书导入保持一致，可根据业务注释此行）
        updateSupport = true;

        // 1. 校验上传文件合法性
        if (file == null || file.isEmpty()) {
            return AjaxResult.error("导入失败：请选择要导入的Excel文件！");
        }

        try {
            // 2. 读取Excel中Reader数据
            ExcelUtil<Reader> util = new ExcelUtil<>(Reader.class);
            List<Reader> readerList = util.importExcel(file.getInputStream());

            // 3. 初始化统计参数
            int successCount = 0;      // 成功总数
            int failCount = 0;         // 失败总数
            int updateCount = 0;       // 更新数量
            int insertCount = 0;       // 新增数量
            StringBuilder failMsg = new StringBuilder(); // 失败详情

            // 4. 批量查询已存在的读者编号，提升效率（避免逐行查询数据库）
            Set<String> existReaderNos = new HashSet<>();
            if (readerList != null && !readerList.isEmpty()) {
                List<String> readerNos = new ArrayList<>();
                // 遍历收集非空读者编号
                for (Reader reader : readerList) {
                    if (reader != null && reader.getReaderNo() != null) {
                        readerNos.add(reader.getReaderNo());
                    }
                }
                // 批量查询数据库中已存在的读者编号
                if (!readerNos.isEmpty()) {
                    existReaderNos = readerService.selectExistReaderNos(readerNos);
                }
            }

            // 5. 逐行处理导入数据 → 核心逻辑完全不变
            if (readerList != null && !readerList.isEmpty()) {
                for (int i = 0; i < readerList.size(); i++) {
                    Reader reader = readerList.get(i);
                    int rowNum = i + 2; // Excel行号（表头占第1行，数据从第2行开始）

                    try {
                        // ========== 基础数据非空校验 ==========
                        if (reader == null) {
                            failCount++;
                            failMsg.append(String.format("第%s行：数据为空，无法导入<br/>", rowNum));
                            continue;
                        }
                        // 读者编号 必填校验（主键级唯一标识）
                        if (reader.getReaderNo() == null) {
                            failCount++;
                            failMsg.append(String.format("第%s行：读者编号不能为空<br/>", rowNum));
                            continue;
                        }
                        // 读者姓名 必填校验
                        if (StringUtils.isBlank(reader.getReaderName())) {
                            failCount++;
                            failMsg.append(String.format("第%s行：读者姓名不能为空<br/>", rowNum));
                            continue;
                        }

                        // ========== 手机号格式校验（非必填，填则校验） ==========
                        String phone = reader.getPhone();
                        if (StringUtils.isNotBlank(phone) && !phone.matches(PHONE_REGEX)) {
                            failCount++;
                            failMsg.append(String.format("第%s行：手机号格式错误，正确格式为11位手机号（如13800138000）<br/>", rowNum));
                            continue;
                        }

                        // ========== 初始化公共字段（符合若依BaseEntity规范） ==========
                        // 创建时间：Excel中有则用Excel值，无则赋值当前时间
                        if (reader.getCreateTime() == null) {
                            reader.setCreateTime(new Date());
                        }

                        // ========== 判断【新增】还是【更新】→ 直接调用已有保存方法 ==========
                        boolean isExist = existReaderNos.contains(reader.getReaderNo());
                        if (isExist && updateSupport) {
                            // ✅ 复用已存在的【修改保存】方法 editSave()
                            Reader existReader = readerService.selectReaderByReaderNo(reader.getReaderNo());
                            if (existReader != null) {
                                reader.setId(existReader.getId()); // 赋值主键ID，用于更新
                                AjaxResult editResult = editSave(reader); // 直接调用本类修改方法
                                if (editResult.isSuccess()) {
                                    updateCount++;
                                    successCount++;
                                } else {
                                    failCount++;
                                    failMsg.append(String.format("第%s行：更新失败，系统异常<br/>", rowNum));
                                }
                            }
                        } else if (!isExist) {
                            // ✅ 复用已存在的【新增保存】方法 addSave()
                            AjaxResult addResult = addSave(reader); // 直接调用本类新增方法
                            if (addResult.isSuccess()) {
                                insertCount++;
                                successCount++;
                                existReaderNos.add(reader.getReaderNo()); // 加入已存在集合，防止同批次重复新增
                            } else {
                                failCount++;
                                failMsg.append(String.format("第%s行：新增失败，系统异常<br/>", rowNum));
                            }
                        } else {
                            // ❌ 逻辑：读者编号已存在 + 未开启更新 → 导入失败
                            failCount++;
                            failMsg.append(String.format("第%s行：读者编号【%s】已存在，且未开启更新模式<br/>", rowNum, reader.getReaderNo()));
                        }
                    } catch (Exception e) {
                        // 捕获单行数据处理异常，不影响其他行
                        failCount++;
                        failMsg.append(String.format("第%s行：处理失败 - %s<br/>", rowNum, e.getMessage()));
                        logger.error("导入读者数据第{}行失败", rowNum, e);
                    }
                }
            }

            // ========== 组装最终返回结果 → 原有逻辑完全不变 ==========
            String resultMsg = String.format(
                    "导入完成！共处理%s条数据，成功%s条（新增%s条，更新%s条），失败%s条。",
                    (readerList == null ? 0 : readerList.size()), successCount, insertCount, updateCount, failCount);

            if (failCount > 0) {
                // 有失败数据 → 警告提示，返回失败详情
                resultMsg += "<br/><br/>失败详情：<br/>" + failMsg.toString();
                return AjaxResult.warn(resultMsg);
            } else {
                // 全部成功 → 成功提示
                return AjaxResult.success(resultMsg);
            }
        } catch (Exception e) {
            // 捕获全局异常，事务自动回滚
            logger.error("批量导入读者数据失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }

}