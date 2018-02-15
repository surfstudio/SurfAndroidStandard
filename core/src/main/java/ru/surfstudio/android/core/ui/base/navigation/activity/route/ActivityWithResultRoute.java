package ru.surfstudio.android.core.ui.base.navigation.activity.route;


import android.content.Intent;

import java.io.Serializable;

import ru.surfstudio.android.core.ui.base.screen.event.result.SupportOnActivityResultRoute;

/**
 * см {@link ActivityRoute}
 * @param <T> тип результата
 */
public abstract class ActivityWithResultRoute<T extends Serializable> extends ActivityRoute
        implements SupportOnActivityResultRoute<T> {

    private static final int MAX_REQUEST_CODE = 32768;

    @Override
    public Intent prepareResultIntent(T resultData){
        Intent i = new Intent();
        i.putExtra(EXTRA_RESULT, resultData);
        return i;
    }

    @Override
    public T parseResultIntent(Intent resultIntent){
        return (T)resultIntent.getSerializableExtra(EXTRA_RESULT);
    }

    @Override
    public final int getRequestCode(){
        return Math.abs(this.getClass().getCanonicalName().hashCode() % MAX_REQUEST_CODE);
    }

}
