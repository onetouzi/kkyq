package com.ruoyi.system.controller;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.annotation.StockLog;
import com.ruoyi.common.config.ImagesModelConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.FlowerIdGenerator;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.service.IFlowerService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 花卉信息Controller
 *
 * @author ruoyi
 * @date 2026-01-07
 */
@Controller
@RequestMapping("/work/flower")
public class FlowerController extends BaseController
{

    @Autowired
    private ImagesModelConfig imagesModelConfig;

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private RestTemplate restTemplate;

    private String prefix = "work/flower";

    @Autowired
    private IFlowerService flowerService;

    @RequiresPermissions("work:flower:view")
    @GetMapping()
    public String flower()
    {
        return prefix + "/flower";
    }

    @RequiresPermissions("work:flower:view")
    @GetMapping("/select")
    public String selectFlowers()
    {
        return prefix + "/select";
    }


    /**
     * 查询花卉信息列表
     */
    @RequiresPermissions("work:flower:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Flower flower)
    {
        startPage();
        List<Flower> list = flowerService.selectFlowerList(flower);
        return getDataTable(list);
    }

    /**
     * 导出花卉信息列表
     */
    @RequiresPermissions("work:flower:export")
    @Log(title = "花卉信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Flower flower)
    {
        List<Flower> list = flowerService.selectFlowerList(flower);
        ExcelUtil<Flower> util = new ExcelUtil<Flower>(Flower.class);
        return util.exportExcel(list, "花卉信息数据");
    }

    /**
     * 新增花卉信息
     */
    @RequiresPermissions("work:flower:view")
    @GetMapping("/classifier")
    public String classifier()
    {
        return prefix + "/classifier";
    }

    /**
     * 新增花卉信息
     */
    @RequiresPermissions("work:flower:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存花卉信息
     */
    @RequiresPermissions("work:flower:add")
    @StockLog(businessType = "0") // 触发 AOP 记录
    @Log(title = "花卉信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Flower flower) {
        flower.setFlowerCode(FlowerIdGenerator.generateFlowerNo());
        return toAjax(flowerService.insertFlower(flower));
    }

    /**
     * 修改花卉信息
     */
    @RequiresPermissions("work:flower:edit")
    @GetMapping("/edit/{flowerId}")
    public String edit(@PathVariable("flowerId") Long flowerId, ModelMap mmap)
    {
        Flower flower = flowerService.selectFlowerByFlowerId(flowerId);
        mmap.put("flower", flower);
        return prefix + "/edit";
    }

    /**
     * 修改保存花卉信息
     */
    @RequiresPermissions("work:flower:edit")
    @StockLog(businessType = "0") // 触发 AOP 自动计算差额
    @Log(title = "花卉信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Flower flower)
    {
        return toAjax(flowerService.updateFlower(flower));
    }

    /**
     * 删除花卉信息
     */
    @RequiresPermissions("work:flower:remove")
    @Log(title = "花卉信息", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(flowerService.deleteFlowerByFlowerIds(ids));
    }

    /**
     * 自定义获取文件扩展名的方法（兼容若依框架）
     * @param fileName 文件名
     * @return 小写的文件扩展名（无点号），如：jpg、png
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // 无扩展名
        }
        return fileName.substring(lastDotIndex + 1).toLowerCase();
    }

    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {
            // ========== 1. 基础文件上传逻辑（原有） ==========
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            // ========== 2. 读取imagesModel配置，构建预测接口URL ==========
            ImagesModelConfig.Upload uploadConfig = imagesModelConfig.getUpload();
            // 构建本地8000端口的predict接口URL：http://localhost:8000/predict
            String predictUrl = String.format("http://localhost:%d%s",
                    uploadConfig.getPort(),
                    uploadConfig.getPath());

            // ========== 3. 验证文件扩展名（修复：替换为若依兼容的方式） ==========
            String originalFilename = file.getOriginalFilename();
            // 修复点：若依FileUtils无getExtension，改用字符串截取方式获取扩展名
            String fileExt = getFileExtension(originalFilename);
            // 验证扩展名（转小写后匹配）
            String allowExts = uploadConfig.getAllowExts().toLowerCase();
            if (!allowExts.contains(fileExt)) {
                return AjaxResult.error("文件格式不允许，仅支持：" + uploadConfig.getAllowExts());
            }

            // ========== 4. 构建POST请求调用8000端口的predict接口 ==========
            // 设置请求头（表单格式，因为要上传文件）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 构建请求体（包含上传的文件）
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource()); // 注意参数名要和predict接口一致

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // 发送POST请求，获取响应（解析为Map）
            ResponseEntity<Map> response = restTemplate.postForEntity(predictUrl, requestEntity, Map.class);

            // ========== 5. 解析predict接口的响应 ==========
            Map<String, Object> predictResult = null;
            if (response.getStatusCode() == HttpStatus.OK) {
                predictResult = response.getBody();
            } else {
                return AjaxResult.error("调用预测接口失败，状态码：" + response.getStatusCodeValue());
            }

            // ========== 6. 整合结果返回 ==========
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", originalFilename);
            // 新增：预测接口返回的完整结果
            ajax.put("predictResult", predictResult);
            // 新增：提取预测结果中的关键字段（按需）
            if (predictResult != null && predictResult.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) predictResult.get("data");
                ajax.put("is_book", data.get("is_book"));
                ajax.put("confidence", data.get("confidence"));
                ajax.put("threshold", data.get("threshold"));
                ajax.put("model_info", data.get("model_info"));
            }
            return ajax;

        } catch (Exception e) {
            // 捕获调用预测接口的异常，友好提示
            String errorMsg = "文件上传/预测失败：" + e.getMessage();
            // 若预测接口返回500等错误，解析具体信息
            if (e.getMessage().contains("500")) {
                errorMsg = "图片处理失败：请检查图片格式是否正常或预测服务是否启动";
            }
            return AjaxResult.error(errorMsg);
        }
    }

    @GetMapping("/detail/{flowerId}")
    @ResponseBody
    public Flower selectFlowerById(@PathVariable("flowerId") Long flowerId)
    {
        return flowerService.selectFlowerByFlowerId(flowerId);
    }

    /**
     * 下载导入模板
     */
    @RequiresPermissions("work:flower:import")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<Flower> util = new ExcelUtil<Flower>(Flower.class);
        return util.importTemplateExcel("花卉数据模板");
    }

    /**
     * 导入花卉数据
     * * @param file 导入文件
     * @param updateSupport 是否更新现有数据
     */
    @Log(title = "花卉信息", businessType = BusinessType.IMPORT)
    @RequiresPermissions("work:flower:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<Flower> util = new ExcelUtil<Flower>(Flower.class);
        List<Flower> flowerList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getLoginName();
        // 调用 Service 层处理业务逻辑
        String message = flowerService.importFlower(flowerList, updateSupport, operName);
        return AjaxResult.success(message);
    }

}
