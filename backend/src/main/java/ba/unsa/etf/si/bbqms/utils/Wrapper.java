package ba.unsa.etf.si.bbqms.utils;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.NoSuchElementException;

public interface Wrapper {

    @OverridingMethodsMustInvokeSuper
    default <T> T unwrap(final Class<T> tClass) {
        if (tClass.isAssignableFrom(this.getClass())) {
            return tClass.cast(this);
        }

        throw new NoSuchElementException("Unable to unwrap " + tClass.getName() + " from " + getClass());
    }
}
