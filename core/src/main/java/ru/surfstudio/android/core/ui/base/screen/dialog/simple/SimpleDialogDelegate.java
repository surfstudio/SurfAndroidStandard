package ru.surfstudio.android.core.ui.base.screen.dialog.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import ru.surfstudio.android.core.ui.ScreenType;
import ru.surfstudio.android.core.ui.base.screen.activity.CoreActivityViewInterface;
import ru.surfstudio.android.core.ui.base.screen.configurator.ViewConfigurator;
import ru.surfstudio.android.core.ui.base.screen.fragment.CoreFragmentViewInterface;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorage;
import ru.surfstudio.android.core.ui.base.screen.scope.PersistentScopeStorageContainer;
import ru.surfstudio.android.core.ui.base.screen.widjet.CoreWidgetViewInterface;
import ru.surfstudio.android.logger.LogConstants;
import ru.surfstudio.android.logger.RemoteLogger;

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

    public <D extends DialogFragment & CoreSimpleDialogInterface>SimpleDialogDelegate(D simpleDialog) {
        this.simpleDialog = simpleDialog;
        this.dialogFragment = simpleDialog;
    }

    public <A extends FragmentActivity & CoreActivityViewInterface> void show(A parentActivityView) {
        show(parentActivityView.getSupportFragmentManager(),
                ScreenType.ACTIVITY,
                parentActivityView.getName());
    }

    public <F extends Fragment & CoreFragmentViewInterface> void show(F parentFragmentView) {
        show(parentFragmentView.getFragmentManager(),
                ScreenType.FRAGMENT,
                parentFragmentView.getName());
    }

    public <W extends View & CoreWidgetViewInterface> void show(W parentWidgetView) {
        show(((FragmentActivity)parentWidgetView.getContext()).getSupportFragmentManager(),
                ScreenType.WIDGET,
                parentWidgetView.getName());
    }

    protected void show(FragmentManager fragmentManager, ScreenType parentType, String parentName) {
        this.parentName = parentName;
        this.parentType = parentType;
        dialogFragment.show(fragmentManager, simpleDialog.getName());
    }

    public <T> T getScreenComponent(Class<T> componentClass) {
        PersistentScopeStorage scopeStorage = PersistentScopeStorageContainer.getFrom(dialogFragment.getActivity());
        PersistentScope persistentScope = scopeStorage.get(parentName);
        ViewConfigurator viewConfigurator = (ViewConfigurator)persistentScope.getConfigurator();
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
