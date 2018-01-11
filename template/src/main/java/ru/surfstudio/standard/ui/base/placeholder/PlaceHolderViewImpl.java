package ru.surfstudio.standard.ui.base.placeholder;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import ru.surfstudio.android.core.ui.base.placeholder.PlaceHolderView;
import ru.surfstudio.android.core.ui.base.screen.model.state.LoadState;
import ru.surfstudio.standard.R;
import ru.surfstudio.standard.ui.util.ViewUtils;


/**
 * плейсхолдер с состояниями: PlaceHolderView.State#LOADING, LOADING_TRANSPARENT, EMPTY, ERROR, NOT_FOUND, NONE.
 * Используется на всех экранах, где для отображения контента необходимо сначала загрузить данные.
 */
public class PlaceHolderViewImpl extends FrameLayout implements PlaceHolderView {
    private static final long VISIBILITY_TOGGLE_ANIMATION_DURATION = 120;
    private static final long VISIBILITY_TOGGLE_DELAY_MS = 150;

    private static final int MSG_SHOW = 1;
    private static final int MSG_HIDE = 0;
    Map<LoadState, ViewData> viewDataMap = new HashMap<>();
    private ImageView iconIv;
    private TextView titleTv;
    private TextView subtitleTv;
    private Button submitBtn;
    private ProgressBar loadingPb;
    private ViewGroup contentContainer;

    private OnActionClickListener listener;
    private ViewData defaultViewData;
    private int opaqueBackgroundColor = Color.TRANSPARENT;
    private int transparentBackgroundColor = Color.TRANSPARENT;
    private LoadState state = LoadState.UNSPECIFIED;
    private long visibleUptime;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SHOW || msg.what == MSG_HIDE) {
                visibleUptime = msg.what == MSG_HIDE ? 0 : SystemClock.uptimeMillis();
                if ((int) msg.obj == PlaceHolderViewImpl.this.getVisibility()) {
                    return;
                }

                final int msgWhat = msg.what;
                final int visibility = (int) msg.obj;
                ViewCompat.animate(PlaceHolderViewImpl.this)
                        .alpha(msg.what == MSG_SHOW ? 1.0f : 0f)
                        .setDuration(VISIBILITY_TOGGLE_ANIMATION_DURATION)
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(View view) {
                                view.setAlpha(msgWhat == MSG_SHOW ? 0f : 1.0f);
                                PlaceHolderViewImpl.super.setVisibility(VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(View view) {
                                view.setAlpha(1.0f);
                                //noinspection WrongConstant
                                PlaceHolderViewImpl.super.setVisibility(visibility);
                            }

                            @Override
                            public void onAnimationCancel(View view) {
                                //noinspection WrongConstant
                                PlaceHolderViewImpl.super.setVisibility(visibility);
                            }
                        });
            }
        }
    };

    public PlaceHolderViewImpl(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.placeHolderStyle);
    }

    public PlaceHolderViewImpl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private static void setContentTextOrHide(TextView textView, String text) {
        final int visibility = textView.getVisibility();
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            if (visibility != VISIBLE) {
                textView.setVisibility(VISIBLE);
            }
        } else if (visibility != GONE) {
            textView.setVisibility(GONE);
        }
    }

    private static int obtainColorAttribute(TypedArray ta, int idx, int fallback) {
        int color = fallback;
        try {
            color = ta.getColor(idx, fallback);
        } catch (UnsupportedOperationException e) {
            TypedValue tv = new TypedValue();
            ta.getValue(idx, tv);
            Log.w("PlaceHolderView", "неопознанный тип цвета. " +
                    "Забылу установить в стилях?\n" + tv.coerceToString(), e);
        }

        return color;
    }

    private static Drawable obtainDrawableAttribute(TypedArray ta, int idx, Context context) {
        int resId = ta.getResourceId(idx, 0);
        if (resId == 0) {
            return null;
        }

        return ContextCompat.getDrawable(context, resId);
    }

    /**
     * переводит плейсхолдер в нужное состояние
     */
    public void render(LoadState state) {
        this.state = state;
        if (state == LoadState.UNSPECIFIED ||
                state == LoadState.NONE) {
            setVisibility(INVISIBLE);
            return;
        }

        int backgroundColor = opaqueBackgroundColor;

        if (state == LoadState.MAIN_LOADING ||
                state == LoadState.TRANSPARENT_LOADING) {
            if (state == LoadState.TRANSPARENT_LOADING) {
                backgroundColor = transparentBackgroundColor;
            }

            contentContainer.setVisibility(INVISIBLE);
            loadingPb.setVisibility(VISIBLE);
        } else {
            contentContainer.setVisibility(VISIBLE);
            loadingPb.setVisibility(INVISIBLE);
            updateContentState();
        }

        setBackgroundColor(backgroundColor);
        setVisibility(VISIBLE);
    }

    public void setOnActionClickListener(OnActionClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void setVisibility(int visibility) {
        handler.removeMessages(MSG_HIDE);
        if (visibility != VISIBLE) {
            handler.removeMessages(MSG_SHOW);
            final long uptime = SystemClock.uptimeMillis();
            handler.sendMessageAtTime(handler.obtainMessage(MSG_HIDE, visibility),
                    uptime + visibleUptime + VISIBILITY_TOGGLE_DELAY_MS - uptime);
        } else {
            visibleUptime = SystemClock.uptimeMillis() + VISIBILITY_TOGGLE_DELAY_MS;
            if (!handler.hasMessages(MSG_SHOW)) {
                handler.sendMessageDelayed(handler.obtainMessage(MSG_SHOW, visibility),
                        VISIBILITY_TOGGLE_DELAY_MS);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        handler.removeMessages(MSG_SHOW);
        handler.removeMessages(MSG_HIDE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        handler.removeMessages(MSG_SHOW);
        handler.removeMessages(MSG_HIDE);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(context, R.layout.placeholder_view_layout, this);

        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);

        findViews();
        initChildren(context, attrs, defStyle);
        applyAttributes(context, attrs, defStyle);
        render(state);
    }

    private void findViews() {
        iconIv = findViewById(R.id.placeholder_icon_iv);
        loadingPb = findViewById(R.id.placeholder_loading_pb);
        contentContainer = findViewById(R.id.placeholder_content_view_container);
    }

    private void initChildren(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.App, defStyle, R.style.Widget_PlaceHolder);

        int titleStyle = ta.getResourceId(R.styleable.App_placeHolderTitleStyle, 0);
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLayoutParams.bottomMargin = ViewUtils.convertDpToPx(context, 12);
        titleTv = new AppCompatTextView(new ContextThemeWrapper(context, titleStyle), null, 0);
        titleTv.setLayoutParams(titleLayoutParams);

        int subtitleStyle = ta.getResourceId(R.styleable.App_placeHolderSubtitleStyle, 0);
        subtitleTv = new AppCompatTextView(new ContextThemeWrapper(context, subtitleStyle), null, 0);
        subtitleTv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        int buttonStyle = ta.getResourceId(R.styleable.App_placeHolderButtonStyle, 0);
        LinearLayout.LayoutParams submitLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        submitLayoutParams.topMargin = ViewUtils.convertDpToPx(context, 24);
        submitBtn = new AppCompatButton(new ContextThemeWrapper(context, buttonStyle), null, 0);
        submitBtn.setLayoutParams(submitLayoutParams);

        ta.recycle();

        contentContainer.addView(titleTv);
        contentContainer.addView(subtitleTv);
        contentContainer.addView(submitBtn);

        submitBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onActionClick(state);
            }
        });
    }

    private void applyAttributes(Context context, AttributeSet attrs, int defStyle) {
        TypedArray viewTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PlaceHolderViewImpl, defStyle, R.style.Widget_PlaceHolder);
        TypedArray appTypedArray = context.obtainStyledAttributes(attrs, R.styleable.App, defStyle, R.style.Widget_PlaceHolder);

        opaqueBackgroundColor = obtainColorAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_opaqueBackgroundColor,
                opaqueBackgroundColor);
        transparentBackgroundColor = obtainColorAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_transparentBackgroundColor,
                transparentBackgroundColor);

        String emptyTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptyTitle);
        String emptySubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptySubtitle);
        String emptyButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_emptyButtonText);
        Drawable emptyIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_emptyIcon, context);
        viewDataMap.put(LoadState.EMPTY, new ViewData(emptyTitle, emptySubtitle, emptyButtonText, emptyIcon));

        String notFoundTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundTitle);
        String notFoundSubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundSubtitle);
        String notFoundButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_notFoundButtonText);
        Drawable notFoundIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_notFoundIcon, context);
        viewDataMap.put(LoadState.NOT_FOUND, new ViewData(notFoundTitle, notFoundSubtitle, notFoundButtonText, notFoundIcon));

        String errorTitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorTitle);
        String errorSubtitle = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorSubtitle);
        String errorButtonText = viewTypedArray.getString(R.styleable.PlaceHolderViewImpl_errorButtonText);
        Drawable errorIcon = obtainDrawableAttribute(viewTypedArray, R.styleable.PlaceHolderViewImpl_errorIcon, context);
        viewDataMap.put(LoadState.ERROR, new ViewData(errorTitle, errorSubtitle, errorButtonText, errorIcon));

        String defaultTitle = appTypedArray.getString(R.styleable.App_placeHolderDefaultTitle);
        String defaultSubtitle = appTypedArray.getString(R.styleable.App_placeHolderDefaultSubtitle);
        String defaultButtonText = appTypedArray.getString(R.styleable.App_placeHolderDefaultButtonText);
        Drawable defaultIcon = obtainDrawableAttribute(appTypedArray, R.styleable.App_placeHolderDefaultIcon, context);
        defaultViewData = new ViewData(defaultTitle, defaultSubtitle,
                defaultButtonText, defaultIcon);

        viewTypedArray.recycle();
        appTypedArray.recycle();
    }

    private void updateContentState() {
        ViewData viewData = viewDataMap.get(state);
        if (viewData == null || viewData.isEmpty()) {
            viewData = defaultViewData;
        }

        if (viewData.icon != null) {
            iconIv.setImageDrawable(viewData.icon);
            if (iconIv.getVisibility() != VISIBLE) {
                iconIv.setVisibility(VISIBLE);
            }
        } else if (iconIv.getVisibility() != GONE) {
            iconIv.setVisibility(GONE);
        }

        setContentTextOrHide(titleTv, viewData.title);
        setContentTextOrHide(subtitleTv, viewData.subtitle);
        setContentTextOrHide(submitBtn, viewData.buttonText);
    }

    public interface OnActionClickListener {
        void onActionClick(LoadState state);
    }

    @AllArgsConstructor
    private static class ViewData {
        final String title;
        final String subtitle;
        final String buttonText;
        final Drawable icon;

        boolean isEmpty() {
            return TextUtils.isEmpty(title) &&
                    TextUtils.isEmpty(subtitle) &&
                    TextUtils.isEmpty(buttonText) &&
                    icon == null;
        }
    }
}
