package ru.surfstudio.android.core.util;

import android.app.Activity;
import android.support.annotation.Nullable;

/**
 * Содержит активную (отображаемуй) активти
 */
public class ActiveActivityHolder {

    private Activity activity;

    public ActiveActivityHolder() {
        //do nothing
    }

    public void setActivity(Activity activeActivity){
        this.activity = activeActivity;
    }

    public void clearActivity(){
        this.activity = null;
    }

    @Nullable
    public Activity getActivity() {
        return activity;
    }

    public boolean isExist(){
        return activity != null;
    }
}