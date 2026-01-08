package com.ruoyi.quartz.task;

import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.framework.webSocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.IFlowerService;
import com.ruoyi.system.service.ISysNoticeService;
import com.ruoyi.common.utils.ShiroUtils;

/**
 * 花卉库存监控定时任务
 */
@Component("flowerTask")
public class FlowerTask {

    @Autowired
    private IFlowerService flowerService;

    @Autowired
    private ISysNoticeService noticeService;


    public void checkFlowerStock() {
        // 1. 查询所有库存小于 5 的花卉
        List<String> lowStockNames = flowerService.selectLowStockFlowerNames(5L);

        if (lowStockNames != null && !lowStockNames.isEmpty()) {
            int totalSize = lowStockNames.size();

            // 处理花名展示逻辑：取前5个
            String displayName = lowStockNames.stream()
                    .limit(5)
                    .collect(Collectors.joining("、"));

            String message;
            if (totalSize > 5) {
                message = "STOCK_WARN:" + displayName + " 等 " + totalSize + " 种花卉库存不足！";
            } else {
                message = "STOCK_WARN:" + displayName + " 库存不足！";
            }

            // 3. 发送带内容的指令
            WebSocketServer.sendInfo(message);
        }
    }
}