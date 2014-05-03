package require4s;

import com.google.inject.Binder;

/**
 * Created by sxend on 14/05/03.
 */
public class ModuleBinder {

    public static <M> void bind(Binder binder, Class<M> from, Class<? extends M> to){
        binder.bind(from).to(to);
    }
}
