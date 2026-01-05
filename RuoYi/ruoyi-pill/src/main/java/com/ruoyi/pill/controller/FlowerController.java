package com.ruoyi.pill.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pill.domain.Flower;
import com.ruoyi.pill.service.IFlowerService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 花卉信息Controller
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/pill/flower")
public class FlowerController extends BaseController
{
    @Autowired
    private IFlowerService flowerService;

    /**
     * 查询花卉信息列表
     */
    @RequiresPermissions("pill:flower:list")
    @GetMapping("/list")
    public TableDataInfo list(Flower flower)
    {
        startPage();
        List<Flower> list = flowerService.selectFlowerList(flower);
        return getDataTable(list);
    }

    /**
     * 导出花卉信息列表
     */
    @RequiresPermissions("pill:flower:export")
    @Log(title = "花卉信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Flower flower)
    {
        List<Flower> list = flowerService.selectFlowerList(flower);
        ExcelUtil<Flower> util = new ExcelUtil<Flower>(Flower.class);
        util.exportExcel(response, list, "花卉信息数据");
    }

    /**
     * 获取花卉信息详细信息
     */
    @RequiresPermissions("pill:flower:query")
    @GetMapping(value = "/{flowerId}")
    public AjaxResult getInfo(@PathVariable("flowerId") Long flowerId)
    {
        return success(flowerService.selectFlowerByFlowerId(flowerId));
    }

    /**
     * 新增花卉信息
     */
    @RequiresPermissions("pill:flower:add")
    @Log(title = "花卉信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Flower flower)
    {
        // 校验花卉编码唯一性
        Flower existFlower = flowerService.selectFlowerByFlowerCode(flower.getFlowerCode());
        if (existFlower != null)
        {
            return error("花卉编码已存在");
        }
        return toAjax(flowerService.insertFlower(flower));
    }

    /**
     * 修改花卉信息
     */
    @RequiresPermissions("pill:flower:edit")
    @Log(title = "花卉信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Flower flower)
    {
        // 校验花卉编码唯一性（排除自己）
        Flower existFlower = flowerService.selectFlowerByFlowerCode(flower.getFlowerCode());
        if (existFlower != null && !existFlower.getFlowerId().equals(flower.getFlowerId()))
        {
            return error("花卉编码已存在");
        }
        return toAjax(flowerService.updateFlower(flower));
    }

    /**
     * 删除花卉信息
     */
    @RequiresPermissions("pill:flower:remove")
    @Log(title = "花卉信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{flowerIds}")
    public AjaxResult remove(@PathVariable Long[] flowerIds)
    {
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < flowerIds.length; i++)
        {
            if (i > 0)
            {
                ids.append(",");
            }
            ids.append(flowerIds[i]);
        }
        return toAjax(flowerService.deleteFlowerByFlowerIds(ids.toString()));
    }

    /**
     * 查询低库存花卉（库存低于预警值）
     */
    @RequiresPermissions("pill:flower:list")
    @GetMapping("/lowInventory")
    public AjaxResult getLowInventoryFlowers()
    {
        List<Flower> list = flowerService.selectLowInventoryFlowers();
        return success(list);
    }
}

