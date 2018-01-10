package ru.surfstudio.standard.ui.common.contentcontainer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.BaseHandleableErrorFragmentView;
import ru.surfstudio.android.core.ui.base.screen.presenter.CorePresenter;
import ru.surfstudio.android.core.ui.base.screen.view.ContentContainerView;
import ru.surfstudio.standard.R;


/**
 * Вью рутового экрана для дочерних фрагментов
 */
public class ContentContainerFragmentView extends BaseHandleableErrorFragmentView
        implements ContentContainerView {
    @Inject
    ContentContainerPresenter presenter;

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    @Override
    public ScreenConfigurator createScreenConfigurator(Activity activity, Bundle args) {
        return new ContentContainerScreenConfigurator(activity, args);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_content_container, container, false);
    }

    @Override
    public int getContentContainerViewId() {
        return R.id.content_container_view_container;
    }
}
