package com.yangteng.library.utils;


import com.yangteng.library.function.StyleUtilFunction;

import java.util.ArrayList;
import java.util.List;


public final class  FXBuildUtils<T> {
    private final List<T> list = new ArrayList<>();

    @SafeVarargs
    public final FXBuildUtils<T> addAll(T... t) {
        list.addAll(List.of(t));
        return this;
    }

    public FXBuildUtils<T> StyleUtil(StyleUtilFunction<T> fun) {
        list.forEach(fun::apply);
        return this;
    }

    public List<T> toList() {
        return list;
    }

}
