package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment;


import android.support.annotation.AnimRes;
import android.support.annotation.StyleRes;
import android.support.v4.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FragNavTransactionOptions {
    List<Pair<View, String>> sharedElements;
    int transition;
    @AnimRes
    int enterAnimation;
    @AnimRes
    int exitAnimation;
    @AnimRes
    int popEnterAnimation;
    @AnimRes
    int popExitAnimation;
    @StyleRes
    int transitionStyle;
    String breadCrumbTitle;
    String breadCrumbShortTitle;
    boolean allowStateLoss;

    private FragNavTransactionOptions(FragNavTransactionOptions.Builder builder) {
        this.transition = 0;
        this.enterAnimation = 0;
        this.exitAnimation = 0;
        this.popEnterAnimation = 0;
        this.popExitAnimation = 0;
        this.transitionStyle = 0;
        this.sharedElements = builder.sharedElements;
        this.transition = builder.transition;
        this.enterAnimation = builder.enterAnimation;
        this.exitAnimation = builder.exitAnimation;
        this.transitionStyle = builder.transitionStyle;
        this.popEnterAnimation = builder.popEnterAnimation;
        this.popExitAnimation = builder.popExitAnimation;
        this.breadCrumbTitle = builder.breadCrumbTitle;
        this.breadCrumbShortTitle = builder.breadCrumbShortTitle;
        this.allowStateLoss = builder.allowStateLoss;
    }

    public static FragNavTransactionOptions.Builder newBuilder() {
        return new FragNavTransactionOptions.Builder();
    }

    public static final class Builder {
        private List<Pair<View, String>> sharedElements;
        private int transition;
        private int enterAnimation;
        private int exitAnimation;
        private int transitionStyle;
        private int popEnterAnimation;
        private int popExitAnimation;
        private String breadCrumbTitle;
        private String breadCrumbShortTitle;
        private boolean allowStateLoss;

        private Builder() {
            this.allowStateLoss = false;
        }

        public FragNavTransactionOptions.Builder addSharedElement(Pair<View, String> val) {
            if(this.sharedElements == null) {
                this.sharedElements = new ArrayList(3);
            }

            this.sharedElements.add(val);
            return this;
        }

        public FragNavTransactionOptions.Builder sharedElements(List<Pair<View, String>> val) {
            this.sharedElements = val;
            return this;
        }

        public FragNavTransactionOptions.Builder transition(int val) {
            this.transition = val;
            return this;
        }

        public FragNavTransactionOptions.Builder customAnimations(@AnimRes int enterAnimation, @AnimRes int exitAnimation) {
            this.enterAnimation = enterAnimation;
            this.exitAnimation = exitAnimation;
            return this;
        }

        public FragNavTransactionOptions.Builder customAnimations(@AnimRes int enterAnimation, @AnimRes int exitAnimation, @AnimRes int popEnterAnimation, @AnimRes int popExitAnimation) {
            this.popEnterAnimation = popEnterAnimation;
            this.popExitAnimation = popExitAnimation;
            return this.customAnimations(enterAnimation, exitAnimation);
        }

        public FragNavTransactionOptions.Builder transitionStyle(@StyleRes int val) {
            this.transitionStyle = val;
            return this;
        }

        public FragNavTransactionOptions.Builder breadCrumbTitle(String val) {
            this.breadCrumbTitle = val;
            return this;
        }

        public FragNavTransactionOptions.Builder breadCrumbShortTitle(String val) {
            this.breadCrumbShortTitle = val;
            return this;
        }

        public FragNavTransactionOptions.Builder allowStateLoss(boolean allow) {
            this.allowStateLoss = allow;
            return this;
        }

        public FragNavTransactionOptions build() {
            return new FragNavTransactionOptions(this);
        }
    }
}
