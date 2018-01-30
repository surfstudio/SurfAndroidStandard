package ru.surfstudio.android.core.ui.base.message;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import ru.surfstudio.android.core.R;
import ru.surfstudio.android.core.ui.base.dagger.provider.ActivityProvider;
import ru.surfstudio.android.core.ui.base.dagger.provider.FragmentProvider;
import ru.surfstudio.android.dagger.scope.PerScreen;

/**
 * Для нахождения view использует fragment и затем activity провайдеры
 */
@PerScreen
public class DefaultMessageController implements MessageController {

    private static final int ILLEGAL_COLOR = Color.TRANSPARENT;
    private static final int DEFAULT_TOAST_GRAVITY = Gravity.BOTTOM;

    private final ActivityProvider activityProvider;

    @Nullable
    private final FragmentProvider fragmentProvider;

    @Nullable
    private Integer snackBarBackgroundColor;
    private Toast toast;

    public DefaultMessageController(ActivityProvider activityProvider,
                                    @Nullable FragmentProvider fragmentProvider) {
        this.activityProvider = activityProvider;
        this.fragmentProvider = fragmentProvider;

        TypedArray typedArray = activityProvider.get()
                .obtainStyledAttributes(new int[]{R.attr.snackBarBackgroundColor});
        try {
            int color = typedArray.getColor(0, ILLEGAL_COLOR);
            if (color != ILLEGAL_COLOR) {
                snackBarBackgroundColor = color;
            }
        } catch (UnsupportedOperationException ignored) {
            // ignored
        }

        typedArray.recycle();
    }

    public DefaultMessageController(ActivityProvider activityProvider) {
        this(activityProvider, null);
    }

    @Override
    public void show(@StringRes int stringId) {
        show(getView(), stringId);
    }

    public void show(View view, int stringId) {
        show(view, view.getResources().getString(stringId));
    }

    @Override
    public void show(String message) {
        show(getView(), message);
    }

    @Override
    public void showToast(@StringRes int stringId) {
        showToast(getView().getResources().getString(stringId), DEFAULT_TOAST_GRAVITY);
    }

    @Override
    public void showToast(@StringRes int stringId, int gravity) {
        showToast(getView().getResources().getString(stringId), gravity);
    }

    @Override
    public void showToast(String message) {
        showToast(message, DEFAULT_TOAST_GRAVITY);
    }

    @Override
    public void showToast(String message, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getView().getContext(), message, Toast.LENGTH_LONG);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public void show(View view, String message) {
        Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        if (snackBarBackgroundColor != null) {
            sb.getView().setBackgroundColor(snackBarBackgroundColor);
        }

        sb.show();
    }

    public void show(String message, int backgroundColor) {
        show(getView(), message, backgroundColor);
    }

    public void show(View view, String message, int backgroundColor) {
        Snackbar sb = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        sb.getView().setBackgroundColor(backgroundColor);
        sb.show();
    }

    public void show(@StringRes int stringId,
                     @ColorRes int backgroundColor,
                     @StringRes int actionStringId,
                     @ColorRes int buttonColor) {
        View view = getView();
        Snackbar sb = Snackbar.make(view, view.getResources().getString(stringId), Snackbar.LENGTH_INDEFINITE);
        sb.getView().setBackgroundColor(view.getResources().getColor((backgroundColor)));
        sb.setAction(actionStringId, v -> sb.dismiss());
        sb.setActionTextColor(view.getResources().getColor(buttonColor));
        sb.show();
    }

    public void show(@StringRes int stringId,
                     @StringRes int actionStringId,
                     @ColorRes int buttonColor,
                     View.OnClickListener listener) {
        View view = getView();
        Snackbar sb = Snackbar.make(view, view.getResources().getString(stringId), Snackbar.LENGTH_INDEFINITE);
        if (snackBarBackgroundColor != null) {
            sb.getView().setBackgroundColor(snackBarBackgroundColor);
        }
        sb.setAction(actionStringId, listener);
        sb.setActionTextColor(view.getResources().getColor(buttonColor));
        sb.show();
    }

    /**
     * Порядок поиска подходящей корневой вью для SnackBar происходит со следующим приоритетом:
     * R.id.snackbar_container во фрагменте, должен быть FrameLayout
     * R.id.coordinator во фрагменте, должен быть CoordinatorLayout
     * R.id.snackbar_container в активитиб должен быть CoordinatorLayout или FrameLayout
     * R.id.coordinator в активити, должен быть CoordinatorLayout
     * android.R.id.content активити
     * <p>
     * Для того, чтобы срабатывал Behavior на появление Snackbar,
     * нужно чтобы найденая View была {@link CoordinatorLayout}
     */
    protected View getView() {
        View v = getViewFromFragment(fragmentProvider);
        if (v == null) {
            v = getViewFromActivity(activityProvider);
        }

        return v;
    }

    private View getViewFromFragment(FragmentProvider fragmentProvider) {
        if (fragmentProvider == null) {
            return null;
        }
        View fragmentView = fragmentProvider.get().getView();
        if (fragmentView == null) {
            return null;
        }
        View v = fragmentView.findViewById(R.id.snackbar_container);
        if (v == null) {
            v = fragmentView.findViewById(R.id.coordinator);
        }
        return v;
    }

    private View getViewFromActivity(ActivityProvider activityProvider) {
        AppCompatActivity activity = activityProvider.get();
        View v = activity.findViewById(R.id.snackbar_container);
        if (v == null) {
            v = activity.findViewById(R.id.coordinator);
        }
        if (v == null) {
            v = activity.findViewById(android.R.id.content);
        }
        return v;
    }
}
