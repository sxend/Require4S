package require4s;

import com.google.inject.*;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * Created by sxend on 14/05/03.
 */
public class ModuleBinder {

    public static <M> void bind(Binder binder, Class<M> from, Class<? extends M> to){
        binder.bind(from).to(to);
    }
}
