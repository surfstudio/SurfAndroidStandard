package ru.surfstudio.android.core.ui;


import java.util.Arrays;
import java.util.List;

public enum ScreenType {
    ACTIVITY,
    FRAGMENT;

    public static List<ScreenType> allTypes(){
        return Arrays.asList(ScreenType.values());
    }
}
