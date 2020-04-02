/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.toolbar.config;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

/**
 * Builder class for flexible toolbar configuring.
 */
public class ToolbarConfig {

    public interface OnClickMenuItemListener {
        void onClickMenuItem(MenuItem item);
    }

    private AppCompatActivity activity;

    private
    @IdRes
    int toolbarId = R.id.toolbar;

    private boolean showHomeAsUp;

    private String titleText;

    private String subtitleText;

    private int toolbarBackGroundColor;

    private @DrawableRes
    int homeAsUpIndicatorId;

    private @ColorRes
    int homeAsUpIndicatorColor;

    private OnClickMenuItemListener onClickMenuItemListener;

    private View.OnClickListener onClickNavigationListener;

    private ToolbarConfig(AppCompatActivity activity) {
        this.activity = activity;
    }

    public static ToolbarConfig builder(AppCompatActivity activity) {
        return new ToolbarConfig(activity);
    }

    /**
     * Provide ID of the toolbar to find it on the screen layout
     *
     * @param toolbarId Resource ID of the toolbar from the current screen layout
     */
    public ToolbarConfig fromId(int toolbarId) {
        this.toolbarId = toolbarId;
        return this;
    }

    /**
     * Set the title of this toolbar.
     * <p>
     * <p>A title should be used as the anchor for a section of content. It should
     * describe or name the content being viewed.</p>
     *
     * @param titleText string to set as title
     */
    public ToolbarConfig setTitleText(String titleText) {
        this.titleText = titleText;
        return this;
    }

    /**
     * Set the subtitle of this toolbar.
     *
     * @param subtitleText subtitle string
     */
    public ToolbarConfig setSubtitleText(String subtitleText) {
        this.subtitleText = subtitleText;
        return this;
    }

    /**
     * Sets the background color for this view.
     *
     * @param color the color of the background
     */
    public ToolbarConfig setToolbarBackgroundColor(int color) {
        this.toolbarBackGroundColor = color;
        return this;
    }

    /**
     * Sets the transparent background color for this view.
     */
    public ToolbarConfig setToolbarTransparent() {
        this.toolbarBackGroundColor = ContextCompat.getColor(activity, android.R.color.transparent);
        return this;
    }

    /**
     * Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     *
     * @param showHomeAsUp True to show the user that selecting home will return one
     *                     level up rather than to the top level of the app
     */
    public ToolbarConfig setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        this.showHomeAsUp = showHomeAsUp;
        return this;
    }

    /**
     * Set an alternate drawable to display next to the icon/logo/title
     * when DISPLAY_HOME_AS_UP is enabled. This can be useful if you are using
     * this mode to display an alternate selection for up navigation, such as a sliding drawer.
     *
     * @param resId Resource ID of a drawable to use for the up indicator, or null
     *              to use the theme's default
     */
    public ToolbarConfig setHomeAsUpIndicatorId(@DrawableRes int resId) {
        this.homeAsUpIndicatorId = resId;
        return this;
    }

    /**
     * @param color the color of the toolbar icon
     */
    public ToolbarConfig setHomeAsUpIndicatorColor(int color) {
        this.homeAsUpIndicatorColor = color;
        return this;
    }

    /**
     * Set a listener to respond to menu item click events.
     * <p>
     * <p>This listener will be invoked whenever a user selects a menu item from
     * the action buttons presented at the end of the toolbar or the associated overflow.</p>
     *
     * @param listener Listener to set
     */
    public ToolbarConfig setOnMenuItemClickListener(OnClickMenuItemListener listener) {
        this.onClickMenuItemListener = listener;
        return this;
    }

    /**
     * Set a listener to respond to navigation events.
     * <p>
     * <p>This listener will be called whenever the user clicks the navigation button
     * at the start of the toolbar. An icon must be set for the navigation button to appear.</p>
     *
     * @param listener Listener to set
     */
    public ToolbarConfig setOnNavigationClickListener(View.OnClickListener listener) {
        this.onClickNavigationListener = listener;
        return this;
    }

    /**
     * Apply all changes and render toolbar.
     */
    public void apply() {
        if (activity != null) {
            Toolbar toolbar = activity.findViewById(toolbarId);
            if (toolbar != null) {
                configToolbar(toolbar);
            }
        }
    }

    private void configToolbar(Toolbar toolbar) {
        activity.setSupportActionBar(toolbar);
        configTitle(activity.getSupportActionBar());
        configSubtitle(activity.getSupportActionBar());
        configDisplayHomeAsUpEnabled(activity.getSupportActionBar());
        configHomeAsUpIndicator(activity.getSupportActionBar());
        configOnMenuItemClickListener(toolbar);
        configOnNavigationClickListener(toolbar);
        configToolbarBackground(toolbar);
    }

    private void configTitle(ActionBar actionBar) {
        String title = "";
        if (!TextUtils.isEmpty(titleText)) {
            title = titleText;
        }
        if (title != null) {
            actionBar.setTitle(title);
        }
    }

    private void configSubtitle(ActionBar actionBar) {
        String subtitle = "";
        if (!TextUtils.isEmpty(subtitleText)) {
            subtitle = subtitleText;
        }
        if (subtitle != null
                ) {
            actionBar.setSubtitle(subtitle);
        }
    }

    private void configDisplayHomeAsUpEnabled(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(this.showHomeAsUp);
        }
    }

    private void configHomeAsUpIndicator(ActionBar actionBar) {
        if (actionBar != null && this.homeAsUpIndicatorId != 0) {
            final Drawable homeIcon = ContextCompat.getDrawable(activity, this.homeAsUpIndicatorId);
            if (homeIcon != null) {
                if (homeAsUpIndicatorColor != 0) {
                    homeIcon.setColorFilter(ContextCompat.getColor(activity, this.homeAsUpIndicatorColor), PorterDuff.Mode.SRC_ATOP);
                }
            }
            actionBar.setHomeAsUpIndicator(homeIcon);
        }
    }

    private void configOnMenuItemClickListener(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(item -> {
                if (ToolbarConfig.this.onClickMenuItemListener != null) {
                    ToolbarConfig.this.onClickMenuItemListener.onClickMenuItem(item);
                }
                return true;
            });
        }
    }

    private void configOnNavigationClickListener(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(view -> {
                if (ToolbarConfig.this.onClickNavigationListener != null) {
                    ToolbarConfig.this.onClickNavigationListener.onClick(view);
                } else {
                    activity.finish();
                }
            });
        }
    }

    private void configToolbarBackground(Toolbar toolbar) {
        if (toolbar != null && toolbarBackGroundColor != 0) {
            toolbar.setBackgroundColor(toolbarBackGroundColor);
        }
    }
}