package ru.surfstudio.standard.ui.common.contentcontainer;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.io.Serializable;

import lombok.Data;
import lombok.NonNull;
import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentRoute;
import ru.surfstudio.android.core.ui.base.navigation.fragment.route.FragmentWithParamsRoute;

/**
 * Маршрут рутового экрана для дочерних фрагментов
 */
@Data
public class ContentContainerFragmentRoute extends FragmentWithParamsRoute {
    private final FragmentRoute initialRoute;

    /**
     * initialRoute должен быть Serializable
     */
    public <R extends FragmentRoute & Serializable> ContentContainerFragmentRoute(@NonNull R initialRoute) {
        this.initialRoute = initialRoute;
    }

    ContentContainerFragmentRoute(Bundle args) {
        initialRoute = (FragmentRoute) args.getSerializable(EXTRA_FIRST);
    }

    @Override
    public String getTag() {
        return super.getTag() + ":" + initialRoute.getTag();
    }

    @Override
    protected Bundle prepareBundle() {
        Bundle args = new Bundle(1);
        args.putSerializable(EXTRA_FIRST, (Serializable) initialRoute);
        args.putString(ContentContainerScreenConfigurator.EXTRA_INITIAL_ROUTE_KEY, initialRoute.getTag());
        return args;
    }

    @Override
    protected Class<? extends Fragment> getFragmentClass() {
        return ContentContainerFragmentView.class;
    }
}
