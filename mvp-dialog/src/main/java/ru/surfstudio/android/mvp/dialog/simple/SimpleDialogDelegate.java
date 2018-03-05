package ru.surfstudio.android.mvp.dialog.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import ru.surfstudio.android.core.mvp.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.mvp.configurator.ViewConfigurator;
import ru.surfstudio.android.core.mvp.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.mvp.scope.ActivityViewPersistentScope;
import ru.surfstudio.android.core.mvp.scope.FragmentViewPersistentScope;
import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.scope.PersistentScope;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.scope.PersistentScopeStorageContainer;
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.RemoteLogger;
import ru.surfstudio.android.mvp.widget.scope.WidgetViewPersistentScope;
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface;

/**
 * delegate для {@link CoreSimpleDialogInterface}
 */

public class SimpleDialogDelegate {
    private static final String EXTRA_PARENT_TYPE = "EXTRA_PARENT_TYPE";
    private static final String EXTRA_PARENT_NAME = "EXTRA_PARENT_NAME";

    private ScreenType parentType;
    private String parentName;

    private CoreSimpleDialogInterface simpleDialog;
    private DialogFragment dialogFragment;

    public <D extends DialogFragment & CoreSimpleDialogInterface> SimpleDialogDelegate(D simpleDialog) {
        this.simpleDialog = simpleDialog;
        this.dialogFragment = simpleDialog;
    }

    public <A extends ActivityViewPersistentScope> void show(A parentActivityViewPersistentScope) {
        show(parentActivityViewPersistentScope.getScreenState().getActivity().getSupportFragmentManager(),
                ScreenType.ACTIVITY,
                parentActivityViewPersistentScope.getScopeId());
    }

    public <F extends FragmentViewPersistentScope> void show(F parentFragmentViewPersistentScope) {
        show(parentFragmentViewPersistentScope.getScreenState().getFragment().getFragmentManager(),
                ScreenType.FRAGMENT,
                parentFragmentViewPersistentScope.getScopeId());
    }

    public <W extends WidgetViewPersistentScope> void show(W parentWidgetViewPersistentScope) {
        show(((FragmentActivity) parentWidgetViewPersistentScope.getScreenState().getWidget().getContext())
                        .getSupportFragmentManager(),
                ScreenType.WIDGET,
                parentWidgetViewPersistentScope.getScopeId());
    }

    protected void show(FragmentManager fragmentManager, ScreenType parentType, String parentName) {
        this.parentName = parentName;
        this.parentType = parentType;
        dialogFragment.show(fragmentManager, simpleDialog.getName());
    }

    public <T> T getScreenComponent(Class<T> componentClass) {
        PersistentScopeStorage scopeStorage = PersistentScopeStorageContainer.getPersistentScopeStorage();
        ScreenPersistentScope persistentScope = (ScreenPersistentScope) scopeStorage.get(parentName);
        ViewConfigurator viewConfigurator = (ViewConfigurator) persistentScope.getConfigurator();
        return componentClass.cast(viewConfigurator.getScreenComponent());
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (parentType == null && savedInstanceState != null) {
            parentType = (ScreenType) savedInstanceState.getSerializable(EXTRA_PARENT_TYPE);
            parentName = savedInstanceState.getString(EXTRA_PARENT_TYPE);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(EXTRA_PARENT_TYPE, parentType);
        outState.putString(EXTRA_PARENT_NAME, parentName);
    }

    public void onResume() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_RESUME_FORMAT, simpleDialog.getName()));
    }

    public void onPause() {
        RemoteLogger.logMessage(String.format(LogConstants.LOG_DIALOG_PAUSE_FORMAT, simpleDialog.getName()));
    }
}
