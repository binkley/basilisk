package hm.binkley.basilisk;

import javax.validation.constraints.NotNull;

public interface Codeable<T extends Codeable<T>>
        extends Comparable<T> {
    @NotNull String getCode();

    @Override
    default int compareTo(final T that) {
        return getCode().compareTo(that.getCode());
    }
}
