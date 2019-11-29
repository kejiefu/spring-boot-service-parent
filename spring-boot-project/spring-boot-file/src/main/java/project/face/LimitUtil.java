package project.face;

import java.lang.annotation.*;

/**
 * @Auther kejiefu
 * @Date 2019/5/31 0031
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface LimitUtil {

    String methodName() ;

}
