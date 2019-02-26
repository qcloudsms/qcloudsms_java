package com.github.qcloudsms.async;

import java.util.Objects;


class Pair<T1, T2> {
    public final T1 first;
    public final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Pair other = (Pair)obj;

        return (this.first == other.first && this.second == other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
};
