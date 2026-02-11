package ee.zxz.helloapi.annotation;
import java.lang.annotation.*;

/**
 * 登录校验注解
 */
@Target({ElementType.METHOD, ElementType.TYPE}) // 作用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface RequiresLogin {
    int mode() default 0;
}
