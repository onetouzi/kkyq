package com.ruoyi.framework.aspectj;

import com.ruoyi.system.domain.FlowerOrder;
import com.ruoyi.system.service.IFlowerStockHistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.domain.AjaxResult;
import java.util.List;

@Aspect
@Component
public class OrderStockLogAspect {

    @Autowired
    private IFlowerStockHistoryService stockHistoryService;

    /**
     * 拦截下单、取消、修改、删除
     */
    @Pointcut("execution(* com.ruoyi.system.controller.FlowerOrderController.addSave(..)) || " +
            "execution(* com.ruoyi.system.controller.FlowerOrderController.cancel(..)) || " +
            "execution(* com.ruoyi.system.controller.FlowerOrderController.remove(..))")
    public void orderLogPointCut() {}

    @AfterReturning(pointcut = "orderLogPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, AjaxResult jsonResult) {
        // 只有在操作成功（AjaxResult.success）时才记录流水
        if (jsonResult != null && jsonResult.get(AjaxResult.CODE_TAG).equals(200)) {
            handleOrderLog(joinPoint);
        }
    }

    private void handleOrderLog(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        try {
            if ("addSave".equals(methodName)) {
                // 下单逻辑在 Service 层 batchPlaceOrders 中已经通过循环记录了流水
                // 这里如果需要重复记录或额外记录，可以解析 DTO，但建议保持在 Service 层以保证事务一致性
            }
            else if ("cancel".equals(methodName) || "remove".equals(methodName)) {
                // 取消或删除操作，参数通常是 orderId, userId, flowerId
                String orderId = (String) args[0];
                String userId = (String) args[1];
                String flowerId = (String) args[2];

                // 由于 Controller 此时只有 ID，建议在 Service 层 recordLog 逻辑中通过 ID 查库记录
                // 或者在 Service 实现类中已经调用的情况下，AOP 仅作为日志补充
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}