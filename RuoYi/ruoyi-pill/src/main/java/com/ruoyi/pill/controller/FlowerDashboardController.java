package com.ruoyi.pill.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.pill.domain.FlowerDashboardVo;
import com.ruoyi.pill.service.IFlowerDashboardService;

/**
 * 数据统计Controller
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/pill/flowerDashboard")
public class FlowerDashboardController extends BaseController
{
    @Autowired
    private IFlowerDashboardService flowerDashboardService;

    /**
     * 获取统计数据
     */
    @RequiresPermissions("pill:flowerDashboard:list")
    @GetMapping("/statistics")
    public AjaxResult getStatistics()
    {
        FlowerDashboardVo statistics = flowerDashboardService.selectStatistics();
        return success(statistics);
    }
}

