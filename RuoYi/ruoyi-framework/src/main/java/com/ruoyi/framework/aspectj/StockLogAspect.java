package com.ruoyi.framework.aspectj;

import java.util.Objects;
import com.ruoyi.common.annotation.StockLog;
import com.ruoyi.common.utils.ShiroUtils;
import com.ruoyi.system.domain.Flower;
import com.ruoyi.system.service.IFlowerService;
import com.ruoyi.system.service.IFlowerStockHistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class StockLogAspect {

    @Autowired
    private IFlowerStockHistoryService stockHistoryService;

    @Autowired
    private IFlowerService flowerService;

    // 线程本地变量，用于在修改前记录旧库存
    private static final ThreadLocal<Flower> BEFORE_FLOWER = new ThreadLocal<>();

    /**
     * 配置切入点：带有 @StockLog 注解的方法
     */
    @Pointcut("@annotation(com.ruoyi.common.annotation.StockLog)")
    public void stockLogPointCut() {}

    /**
     * 在修改前置处理：如果是修改操作，先查询数据库中的旧数据
     */
    @Before("stockLogPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof Flower) {
            Flower flower = (Flower) args[0];
            if (flower.getFlowerId() != null) {
                // 修改操作，记录原始数据
                BEFORE_FLOWER.set(flowerService.selectFlowerByFlowerId(flower.getFlowerId()));
            }
        }
    }

    /**
     * 返回通知：方法成功执行后记录流水
     */
    @AfterReturning(pointcut = "stockLogPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e) {
        try {
            Object[] args = joinPoint.getArgs();
            if (args == null || args.length == 0 || !(args[0] instanceof Flower)) {
                return;
            }

            Flower flower = (Flower) args[0];
            Flower oldFlower = BEFORE_FLOWER.get();
            BEFORE_FLOWER.remove(); // 记得清理

            long changeQty = 0;
            String remark = "";
            String bizType = "0"; // 默认进货

            if (oldFlower == null) {
                // 场景1：新增
                changeQty = flower.getNum();
                remark = "手动新增入库";
            } else {
                // 场景2：修改
                changeQty = flower.getNum() - oldFlower.getNum();
                if (changeQty == 0) return; // 库存没变，不记录流水

                bizType = changeQty > 0 ? "0" : "3"; // 正数为进货，负数为损耗/调整
                remark = changeQty > 0 ? "补货入库" : "库存校准/损耗";
            }

            // 调用 Service 记录流水
            stockHistoryService.recordFlowerLog(flower, bizType, changeQty, remark);

        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}