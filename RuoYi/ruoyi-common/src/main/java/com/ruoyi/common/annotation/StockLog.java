package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * 库存变更流水注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StockLog {
    /** 业务类型：0=进货, 1=售货, 2=订单取消 */
    String businessType() default "0";

    /** 操作说明 */
    String remark() default "";
}