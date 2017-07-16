package annotation;

import java.lang.annotation.*;

/**
 * Created by Oliver on 7/7/2017.
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
    String title() default "PastaBox";
}
