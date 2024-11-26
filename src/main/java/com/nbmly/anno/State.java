package com.nbmly.anno;

import com.nbmly.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented//元注解
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StateValidation.class})
public @interface State {
    //校验失败后的提示信息
    String message() default "{state的值只能是已经发布或者草稿}";
    //指定分组
    Class<?>[] groups() default {};
    //负载 获取state注解的附加信息
    Class<? extends Payload>[] payload() default {};

}
