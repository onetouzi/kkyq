package com.ruoyi.system.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.ruoyi.common.config.ImagesModelConfig;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.config.ServerConfig;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.system.domain.BookTypeRelation;
import com.ruoyi.system.service.IBookTypeRelationService;
import com.ruoyi.system.service.IBookTypeService;
import nl.basjes.parse.useragent.yauaa.shaded.org.yaml.snakeyaml.constructor.DuplicateKeyException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Book;
import com.ruoyi.system.service.IBookService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 图书Controller
 *
 * @author b3q
 * @date 2025-12-29
 */
@Controller
@RequestMapping("/work/book")
public class BookController extends BaseController
{
    private static final String DEFAULT_TYPE_NAME = "未分类";
    private static final int MAX_TYPE_COUNT = 10;
    private String prefix = "work/book";

    @Autowired
    private IBookService bookService;

    @Autowired
    private IBookTypeService bookTypeService;

    @Autowired
    private IBookTypeRelationService bookTypeRelationService;

    @Autowired
    private ImagesModelConfig imagesModelConfig;

    @Resource
    private ServerConfig serverConfig;

    @Resource
    private RestTemplate restTemplate;

    @RequiresPermissions("work:book:view")
    @GetMapping()
    public String book()
    {
        return prefix + "/book";
    }

    /**
     * 查询图书列表
     */
    @RequiresPermissions("work:book:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Book book)
    {
        startPage();
        List<Book> list = bookService.selectBookList(book);
        return getDataTable(list);
    }

    /**
     * 导出图书列表
     */
    @RequiresPermissions("work:book:export")
    @Log(title = "图书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Book book)
    {
        List<Book> list = bookService.selectBookList(book);
        ExcelUtil<Book> util = new ExcelUtil<Book>(Book.class);
        return util.exportExcel(list, "图书数据");
    }

    /**
     * 下载导入模板
     */
    @RequiresPermissions("work:book:import")
    @GetMapping("/downloadTemplate")
    @ResponseBody
    public AjaxResult downloadTemplate()
    {
        try {
            // 创建Excel模板
            ExcelUtil<Book> util = new ExcelUtil<Book>(Book.class);
            // 设置模板表头说明
            Map<String, String> headerComments = new HashMap<>();
            headerComments.put("bookNo", "图书编号（必填）");
            headerComments.put("bookName", "图书名称（必填）");
            headerComments.put("img", "封面图片URL（选填）");
            headerComments.put("author", "作者（选填）");
            headerComments.put("press", "出版社（选填）");
            headerComments.put("publicTime", "出版时间（选填，格式：yyyy-MM-dd）");
            headerComments.put("stock", "库存（选填，数字）");
            headerComments.put("status", "状态（选填，正常/下架）");
            headerComments.put("typeName", "图书类别（选填，多个用逗号分隔）");

            // 生成模板文件
            return util.importTemplateExcel("图书导入模板");
        } catch (Exception e) {
            logger.error("下载图书导入模板失败", e);
            return AjaxResult.error("下载模板失败：" + e.getMessage());
        }
    }

    /**
     * 新增图书
     */
    @RequiresPermissions("work:book:add")
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存图书
     */
    @RequiresPermissions("work:book:add")
    @Log(title = "图书", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Book book) {
        // ========== 1. 基础参数合法性校验 ==========
        if (book == null) {
            String errorMsg = "新增图书失败：请求参数为空！";
            return AjaxResult.error(errorMsg);
        }
        if (StringUtils.isBlank(book.getBookName())) {
            String errorMsg = "新增图书失败：图书名称不能为空！";
            return AjaxResult.error(errorMsg);
        }

        if(bookService.selectBookByBookNo(book.getBookNo())!=null){
            String errorMsg = "图书编号重复";
            return AjaxResult.error(errorMsg);
        }

        // ========== 2. 图书类型处理（空值/去重/去空格/默认值） ==========
        List<String> bookTypeNames = new ArrayList<>();
        String rawTypeName = book.getTypeName();

        // 空值/纯空格 → 设为默认类型"未分类"
        if (StringUtils.isBlank(rawTypeName)) {
            bookTypeNames.add(DEFAULT_TYPE_NAME);
        } else {
            // 分割+去空格+过滤空值+去重
            String[] tmpArray = rawTypeName.split(",");
            Set<String> typeNameSet = new HashSet<>(); // 去重
            for (String tmp : tmpArray) {
                String typeName = StringUtils.trim(tmp); // 去除首尾空格
                if (StringUtils.isNotBlank(typeName)) {
                    typeNameSet.add(typeName);
                }
            }
            // 转换为List，保证顺序
            bookTypeNames = new ArrayList<>(typeNameSet);

            // 分割后无有效类型 → 设为默认类型
            if (StringUtils.isEmpty(bookTypeNames)) {
                bookTypeNames.add(DEFAULT_TYPE_NAME);
            }
        }

        // ========== 3. 类型数量限制校验 ==========
        if (bookTypeNames.size() > MAX_TYPE_COUNT) {
            String errorMsg = String.format("新增图书失败：图书类型最多只能添加%s个，当前传入%s个！",
                    MAX_TYPE_COUNT, bookTypeNames.size());
            return AjaxResult.error(errorMsg);
        }

        try {
            // ========== 4. 批量获取/创建类型ID ==========
            List<Long> bookTypeIds = new ArrayList<>();
            for (String typeName : bookTypeNames) {
                try {
                    Long typeId = bookTypeService.getOrCreateBookTypeId(typeName);
                    if (typeId == null || typeId <= 0) {
                        String errorMsg = String.format("新增图书失败：类型【%s】ID获取/创建失败！", typeName);
                        return AjaxResult.error(errorMsg);
                    }
                    bookTypeIds.add(typeId);
                } catch (Exception e) {
                    String errorMsg = String.format("新增图书失败：处理类型【%s】时异常！", typeName);
                    return AjaxResult.error(errorMsg + " 异常信息：" + e.getMessage());
                }
            }

            // ========== 5. 保存图书主表 ==========
            int insertBookResult = bookService.insertBook(book);
            if (insertBookResult <= 0) {
                String errorMsg = "新增图书失败：图书主表插入失败！";
                return AjaxResult.error(errorMsg);
            }
            Long bookId = book.getId();
            if (bookId == null || bookId <= 0) {
                String errorMsg = "新增图书失败：图书ID生成失败！";
                return AjaxResult.error(errorMsg);
            }

            // ========== 6. 批量保存图书-类型关联表（使用INSERT IGNORE避免重复） ==========
            List<BookTypeRelation> relationList = new ArrayList<>();
            for (int i = 0; i < bookTypeNames.size(); i++) {
                BookTypeRelation relation = new BookTypeRelation();
                relation.setBookId(bookId);
                relation.setBookTypedId(bookTypeIds.get(i)); // 注意字段名：若实际是typeId需修正
                relation.setBookName(book.getBookName()); // 冗余存储图书名
                relation.setTypeName(bookTypeNames.get(i)); // 冗余存储类型名
                relationList.add(relation);
            }

            // 批量插入关联表（推荐批量，性能更高）
            try {
                int insertRelationResult = bookTypeRelationService.batchInsertIgnoreBookTypeRelation(relationList);

            } catch (Exception e) {
                String errorMsg = String.format("图书主表插入成功（ID：%s），但关联表插入失败！", bookId);
                return AjaxResult.warn(errorMsg + " 异常信息：" + e.getMessage());
            }

            // ========== 7. 接口返回 ==========
            return AjaxResult.success("新增图书成功！", bookId);

        } catch (Exception e) {
            String errorMsg = "新增图书失败：系统异常！";
            return AjaxResult.error(errorMsg + " 异常信息：" + e.getMessage());
        }
    }

    /**
     * 修改图书
     */
    @RequiresPermissions("work:book:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        Book book = bookService.selectBookById(id);
        mmap.put("book", book);
        return prefix + "/edit";
    }

    /**
     * 修改保存图书（重点完善类型更新逻辑，兼容Java 1.8）
     */
    @RequiresPermissions("work:book:edit")
    @Log(title = "图书", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class) // 事务保证原子性
    public AjaxResult editSave( Book book) {
        // ========== 1. 基础参数校验 ==========
        if (book == null || book.getId() == null || book.getId() <= 0) {
            return AjaxResult.error("修改图书失败：图书ID不能为空！");
        }
        if (StringUtils.isBlank(book.getBookName())) {
            return AjaxResult.error("修改图书失败：图书名称不能为空！");
        }
        if(bookService.selectBookByBookNo(book.getBookNo())!=null){
            String errorMsg = "图书编号重复";
            return AjaxResult.error(errorMsg);
        }

        try {
            // ========== 2. 查询图书原数据 ==========
            Book oldBook = bookService.selectBookById(book.getId());
            if (oldBook == null) {
                return AjaxResult.error("修改图书失败：图书不存在！");
            }

            // ========== 3. 处理新类型（Java 1.8循环写法，替代Stream） ==========
            List<String> newTypeNames = new ArrayList<>();
            String rawNewTypeName = book.getTypeName();
            // 空值/纯空格 → 设为默认类型
            if (StringUtils.isBlank(rawNewTypeName)) {
                newTypeNames.add(DEFAULT_TYPE_NAME);
            } else {
                // 分割+去空格+过滤空值+去重（循环替代Stream）
                String[] tmpArray = rawNewTypeName.split(",");
                Set<String> newTypeSet = new HashSet<>();
                for (String tmp : tmpArray) {
                    String typeName = StringUtils.trim(tmp);
                    if (StringUtils.isNotBlank(typeName)) {
                        newTypeSet.add(typeName);
                    }
                }
                newTypeNames = new ArrayList<>(newTypeSet);
                // 无有效类型 → 设为默认类型
                if (newTypeNames.isEmpty()) {
                    newTypeNames.add(DEFAULT_TYPE_NAME);
                }
            }

            // 类型数量限制校验
            if (newTypeNames.size() > MAX_TYPE_COUNT) {
                return AjaxResult.error(String.format("修改图书失败：图书类型最多只能添加%s个，当前传入%s个！",
                        MAX_TYPE_COUNT, newTypeNames.size()));
            }

            // ========== 4. 解析原类型（Java 1.8循环写法） ==========
            List<String> oldTypeNames = new ArrayList<>();
            String rawOldTypeName = oldBook.getTypeName();
            if (StringUtils.isNotBlank(rawOldTypeName)) {
                String[] oldTmpArray = rawOldTypeName.split(",");
                for (String tmp : oldTmpArray) {
                    String typeName = StringUtils.trim(tmp);
                    if (StringUtils.isNotBlank(typeName)) {
                        oldTypeNames.add(typeName);
                    }
                }
            }
            // 原类型为空 → 设为默认类型
            if (oldTypeNames.isEmpty()) {
                oldTypeNames.add(DEFAULT_TYPE_NAME);
            }

            // ========== 5. 对比新旧类型，计算新增/删除类型（Java 1.8循环） ==========
            Set<String> oldTypeSet = new HashSet<>(oldTypeNames);
            Set<String> newTypeSet = new HashSet<>(newTypeNames);

            // 新增类型：新有旧无
            List<String> addTypeNames = new ArrayList<>();
            for (String typeName : newTypeNames) {
                if (!oldTypeSet.contains(typeName)) {
                    addTypeNames.add(typeName);
                }
            }

            // 删除类型：旧有新无
            List<String> deleteTypeNames = new ArrayList<>();
            for (String typeName : oldTypeNames) {
                if (!newTypeSet.contains(typeName)) {
                    deleteTypeNames.add(typeName);
                }
            }

            // ========== 6. 处理删除类型：删除关系数据 ==========
            if (!deleteTypeNames.isEmpty()) {
                // 根据类型名查ID（Java 1.8兼容）
                List<Long> deleteTypeIds = bookTypeService.selectBookTypeIdsByNames(deleteTypeNames);
                if (!deleteTypeIds.isEmpty()) {
                    int deleteCount = bookTypeRelationService.deleteBookTypeRelationByBookIdAndTypeIds(
                            book.getId(), deleteTypeIds);
                }
            }

            // ========== 处理新增类型：创建类型+新增关系 ==========
            List<BookTypeRelation> addRelationList = new ArrayList<>();
            if (!addTypeNames.isEmpty()) {
                for (String typeName : addTypeNames) {
                    // 获取/创建类型ID
                    Long typeId = bookTypeService.getOrCreateBookTypeId(typeName);
                    if (typeId == null || typeId <= 0) {
                        throw new RuntimeException(String.format("类型【%s】ID获取/创建失败！", typeName));
                    }
                    // 构建关系数据
                    BookTypeRelation relation = new BookTypeRelation();
                    relation.setBookId(book.getId());
                    relation.setBookTypedId(typeId);
                    relation.setBookName(book.getBookName());
                    relation.setTypeName(typeName);
                    addRelationList.add(relation);
                }
                // 批量插入关系
                if (!addRelationList.isEmpty()) {
                    int addCount = bookTypeRelationService.batchInsertIgnoreBookTypeRelation(addRelationList);

                }
            }

            // ========== 更新图书主表 ==========
            // 格式化typeName（逗号分隔）
            StringBuilder typeNameSb = new StringBuilder();
            for (int i = 0; i < newTypeNames.size(); i++) {
                if (i > 0) {
                    typeNameSb.append(",");
                }
                typeNameSb.append(newTypeNames.get(i));
            }
            book.setTypeName(typeNameSb.toString());
            int updateResult = bookService.updateBook(book);
            if (updateResult <= 0) {
                throw new RuntimeException("图书主表更新失败！");
            }

            // ========== 返回结果 ==========
            return AjaxResult.success("修改图书成功！", book.getId());

        } catch (Exception e) {
            return AjaxResult.error("修改图书失败：" + e.getMessage());
        }
    }

    /**
     * 删除图书
     */
    @RequiresPermissions("work:book:remove")
    @Log(title = "图书", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(bookService.deleteBookByIds(ids));
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

    /**
     * 导入图书数据
     */
    @RequiresPermissions("work:book:import")
    @Log(title = "图书", businessType = BusinessType.IMPORT)
        @PostMapping("/import")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        updateSupport = true;
        // 校验文件
        if (file == null || file.isEmpty()) {
            return AjaxResult.error("导入失败：请选择要导入的Excel文件！");
        }

        try {
            // 获取要导入的数据
            ExcelUtil<Book> util = new ExcelUtil<Book>(Book.class);
            List<Book> bookList = util.importExcel(file.getInputStream());

            // 数据统计
            int successCount = 0;      // 成功数量
            int failCount = 0;         // 失败数量
            int updateCount = 0;       // 更新数量
            int insertCount = 0;       // 新增数量
            StringBuilder failMsg = new StringBuilder(); // 失败原因

            // 批量查询已存在的图书编号，用于判断新增/更新
            Set<String> existBookNos = new HashSet<String>();
            if (bookList != null && !bookList.isEmpty()) {
                List<String> bookNos = new ArrayList<String>();

                // 替换stream：遍历收集图书编号
                for (Book b : bookList) {
                    if (b != null && StringUtils.isNotBlank(b.getBookNo())) {
                        bookNos.add(b.getBookNo());
                    }
                }

                if (!bookNos.isEmpty()) {
                    existBookNos = bookService.selectExistBookNos(bookNos);
                }
            }

            // 遍历处理每条数据
            if (bookList != null && !bookList.isEmpty()) {
                for (int i = 0; i < bookList.size(); i++) {
                    Book book = bookList.get(i);
                    int rowNum = i + 2; // Excel行号（表头占1行）

                    try {
                        // 基础校验
                        if (book == null) {
                            failCount++;
                            failMsg.append(String.format("第%s行：数据为空<br/>", rowNum));
                            continue;
                        }

                        // 图书编号和名称必填校验
                        if (StringUtils.isBlank(book.getBookNo())) {
                            failCount++;
                            failMsg.append(String.format("第%s行：图书编号不能为空<br/>", rowNum));
                            continue;
                        }
                        if (StringUtils.isBlank(book.getBookName())) {
                            failCount++;
                            failMsg.append(String.format("第%s行：图书名称不能为空<br/>", rowNum));
                            continue;
                        }

                        // 处理图书类型
                        List<String> bookTypeNames = new ArrayList<String>();
                        String rawTypeName = book.getTypeName();

                        // 空值/纯空格 → 设为默认类型"未分类"
                        if (StringUtils.isBlank(rawTypeName)) {
                            bookTypeNames.add(DEFAULT_TYPE_NAME);
                        } else {
                            // 分割+去空格+过滤空值+去重（替换stream）
                            String[] tmpArray = rawTypeName.split(",");
                            Set<String> typeNameSet = new HashSet<String>();

                            // 遍历分割后的类型数组
                            for (int j = 0; j < tmpArray.length; j++) {
                                String tmp = tmpArray[j];
                                String typeName = StringUtils.trim(tmp);
                                if (StringUtils.isNotBlank(typeName)) {
                                    typeNameSet.add(typeName);
                                }
                            }

                            // 将Set转为List
                            bookTypeNames = new ArrayList<String>(typeNameSet);

                            // 无有效类型 → 设为默认类型
                            if (bookTypeNames.isEmpty()) {
                                bookTypeNames.add(DEFAULT_TYPE_NAME);
                            }
                        }

                        // 类型数量限制校验
                        if (bookTypeNames.size() > MAX_TYPE_COUNT) {
                            failCount++;
                            failMsg.append(String.format("第%s行：图书类型最多只能添加%s个，当前传入%s个<br/>",
                                    rowNum, MAX_TYPE_COUNT, bookTypeNames.size()));
                            continue;
                        }

                        // 判断是新增还是更新
                        boolean isExist = existBookNos.contains(book.getBookNo());
                        if (isExist && updateSupport) {
                            // 更新操作
                            Book existBook = bookService.selectBookByBookNo(book.getBookNo());
                            if (existBook != null) {
                                book.setId(existBook.getId()); // 设置ID用于更新

                                // 处理类型（复用修改逻辑）
                                // 拼接类型名称（替换String.join）
                                StringBuilder typeNameSb = new StringBuilder();
                                for (int k = 0; k < bookTypeNames.size(); k++) {
                                    if (k > 0) {
                                        typeNameSb.append(",");
                                    }
                                    typeNameSb.append(bookTypeNames.get(k));
                                }
                                book.setTypeName(typeNameSb.toString());

                                editSave(book); // 调用修改方法

                                updateCount++;
                                successCount++;
                            }
                        } else if (!isExist) {
                            // 新增操作
                            // 拼接类型名称（替换String.join）
                            StringBuilder typeNameSb = new StringBuilder();
                            for (int k = 0; k < bookTypeNames.size(); k++) {
                                if (k > 0) {
                                    typeNameSb.append(",");
                                }
                                typeNameSb.append(bookTypeNames.get(k));
                            }
                            book.setTypeName(typeNameSb.toString());

                            addSave(book); // 调用新增方法

                            insertCount++;
                            successCount++;
                            existBookNos.add(book.getBookNo()); // 添加到已存在集合
                        } else {
                            // 已存在但不更新
                            failCount++;
                            failMsg.append(String.format("第%s行：图书编号【%s】已存在，且未开启更新模式<br/>",
                                    rowNum, book.getBookNo()));
                        }
                    } catch (Exception e) {
                        failCount++;
                        failMsg.append(String.format("第%s行：处理失败 - %s<br/>", rowNum, e.getMessage()));
                        logger.error("导入图书第{}行失败", rowNum, e);
                    }
                }
            }

            // 构建返回结果
            String resultMsg = String.format(
                    "导入完成！共处理%s条数据，成功%s条（新增%s条，更新%s条），失败%s条。",
                    (bookList == null ? 0 : bookList.size()), successCount, insertCount, updateCount, failCount);

            if (failCount > 0) {
                resultMsg += "<br/><br/>失败详情：<br/>" + failMsg.toString();
                return AjaxResult.warn(resultMsg);
            } else {
                return AjaxResult.success(resultMsg);
            }
        } catch (Exception e) {
            logger.error("导入图书数据失败", e);
            return AjaxResult.error("导入失败：" + e.getMessage());
        }
    }
}