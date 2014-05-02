package require4s;

import java.lang.annotation.*;

/**
 * Created by A12184 on 2014/05/02.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {
    Class<?> value();
}
