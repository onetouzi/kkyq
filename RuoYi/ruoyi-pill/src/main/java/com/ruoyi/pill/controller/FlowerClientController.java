package com.ruoyi.pill.controller;

import java.util.List;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.pill.domain.BuyRequest;
import com.ruoyi.pill.domain.Flower;
import com.ruoyi.pill.service.IFlowerClientService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 花卉购买Controller
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/pill/flowerClient")
public class FlowerClientController extends BaseController
{
    @Autowired
    private IFlowerClientService flowerClientService;

    /**
     * 查询可购买花卉列表（仅显示有库存且状态正常的花卉）
     */
    @RequiresPermissions("pill:flowerClient:list")
    @GetMapping("/list")
    public TableDataInfo list(Flower flower)
    {
        startPage();
        List<Flower> list = flowerClientService.selectFlowerClientList(flower);
        return getDataTable(list);
    }

    /**
     * 购买花卉
     */
    @RequiresPermissions("pill:flowerClient:buy")
    @Log(title = "花卉购买", businessType = BusinessType.INSERT)
    @PostMapping("/buy")
    public AjaxResult buy(@RequestBody List<BuyRequest> buyRequests)
    {
        try
        {
            int result = flowerClientService.buyFlowers(buyRequests);
            return success("购买成功，共购买" + result + "种花卉");
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
}

