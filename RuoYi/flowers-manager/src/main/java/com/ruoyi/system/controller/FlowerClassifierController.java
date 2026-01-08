package com.ruoyi.system.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/work/flowerClassifer")
public class FlowerClassifierController {
    private String prefix = "work/flower";

    @GetMapping("/classifier")
    public String add()
    {
        return prefix + "/classifier";
    }
}
