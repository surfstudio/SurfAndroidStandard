package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.view.View;

import org.json.JSONArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class FragNavController {
    public static final int NO_TAB = -1;
    public static final int TAB1 = 0;
    public static final int TAB2 = 1;
    public static final int TAB3 = 2;
    public static final int TAB4 = 3;
    public static final int TAB5 = 4;
    public static final int TAB6 = 5;
    public static final int TAB7 = 6;
    public static final int TAB8 = 7;
    public static final int TAB9 = 8;
    public static final int TAB10 = 9;
    public static final int TAB11 = 10;
    public static final int TAB12 = 11;
    public static final int TAB13 = 12;
    public static final int TAB14 = 13;
    public static final int TAB15 = 14;
    public static final int TAB16 = 15;
    public static final int TAB17 = 16;
    public static final int TAB18 = 17;
    public static final int TAB19 = 18;
    public static final int TAB20 = 19;
    private static final int MAX_NUM_TABS = 20;
    private static final String EXTRA_TAG_COUNT = FragNavController.class.getName() + ":EXTRA_TAG_COUNT";
    private static final String EXTRA_SELECTED_TAB_INDEX = FragNavController.class.getName() + ":EXTRA_SELECTED_TAB_INDEX";
    private static final String EXTRA_CURRENT_FRAGMENT = FragNavController.class.getName() + ":EXTRA_CURRENT_FRAGMENT";
    private static final String EXTRA_FRAGMENT_STACK = FragNavController.class.getName() + ":EXTRA_FRAGMENT_STACK";
    @IdRes
    private final int mContainerId;
    @NonNull
    private final List<Stack<Fragment>> mFragmentStacks;
    @NonNull
    private final FragmentManager mFragmentManager;
    private final FragNavTransactionOptions mDefaultTransactionOptions;
    private int mSelectedTabIndex;
    private int mTagCount;
    @Nullable
    private Fragment mCurrentFrag;
    @Nullable
    private DialogFragment mCurrentDialogFrag;
    @Nullable
    private FragNavController.RootFragmentListener mRootFragmentListener;
    @Nullable
    private FragNavController.TransactionListener mTransactionListener;
    private boolean mExecutingTransaction;

    private FragNavController(FragNavController.Builder builder, @Nullable Bundle savedInstanceState) {
        this.mFragmentManager = builder.mFragmentManager;
        this.mContainerId = builder.mContainerId;
        this.mFragmentStacks = new ArrayList(builder.mNumberOfTabs);
        this.mRootFragmentListener = builder.mRootFragmentListener;
        this.mTransactionListener = builder.mTransactionListener;
        this.mDefaultTransactionOptions = builder.mDefaultTransactionOptions;
        this.mSelectedTabIndex = builder.mSelectedTabIndex;
        if(!this.restoreFromBundle(savedInstanceState, builder.mRootFragments)) {
            for(int i = 0; i < builder.mNumberOfTabs; ++i) {
                Stack<Fragment> stack = new Stack();
                if(builder.mRootFragments != null) {
                    stack.add(builder.mRootFragments.get(i));
                }

                this.mFragmentStacks.add(stack);
            }

            this.initialize(builder.mSelectedTabIndex);
        }

    }

    public static FragNavController.Builder newBuilder(@Nullable Bundle savedInstanceState, FragmentManager fragmentManager, int containerId) {
        return new FragNavController.Builder(savedInstanceState, fragmentManager, containerId);
    }

    /**
     * Модифицированный метод библиотеки FragNav. При переключении по табам фрагменты не пересоздаются,
     * а скрываются и отображаются заново
     * @param isRestoreFromBundle true если вызывается после пересоздания активити
     */
    public void switchTab(int index, @Nullable FragNavTransactionOptions transactionOptions, boolean isRestoreFromBundle) throws IndexOutOfBoundsException {
        if(index >= this.mFragmentStacks.size()) {
            throw new IndexOutOfBoundsException("Can't switch to a tab that hasn't been initialized, Index : " + index + ", current stack size : " + this.mFragmentStacks.size() + ". Make sure to create all of the tabs you need in the Constructor or provide a way for them to be created via RootFragmentListener.");
        } else {
            if(this.mSelectedTabIndex != index) {
                this.mSelectedTabIndex = index;
                FragmentTransaction ft = this.createTransactionWithOptions(transactionOptions, false);
                if (isRestoreFromBundle) {
                     this.detachCurrentFragment(ft);
                } else {
                    this.hideCurrentFragment(ft);
                }
                Fragment fragment = null;
                if(index == -1) {
                    this.commitTransaction(ft, transactionOptions);
                } else {
                    fragment = isRestoreFromBundle ? reattachPreviousFragment(ft) : this.reshowPreviousFragment(ft);
                    if(fragment != null) {
                        this.commitTransaction(ft, transactionOptions);
                    } else {
                        fragment = this.getRootFragment(this.mSelectedTabIndex);
                        ft.add(this.mContainerId, fragment, this.generateTag(fragment));
                        this.commitTransaction(ft, transactionOptions);
                    }
                }

                this.mCurrentFrag = fragment;
                if(this.mTransactionListener != null) {
                    this.mTransactionListener.onTabTransaction(this.mCurrentFrag, this.mSelectedTabIndex);
                }
            }

        }
    }

    public void switchTab(int index) throws IndexOutOfBoundsException {
        this.switchTab(index, (FragNavTransactionOptions)null, false);
    }

    public void pushFragment(@Nullable Fragment fragment, @Nullable FragNavTransactionOptions transactionOptions) {
        if(fragment != null && this.mSelectedTabIndex != -1) {
            FragmentTransaction ft = this.createTransactionWithOptions(transactionOptions, false);
            this.detachCurrentFragment(ft);
            ft.add(this.mContainerId, fragment, this.generateTag(fragment));
            this.commitTransaction(ft, transactionOptions);
            ((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).push(fragment);
            this.mCurrentFrag = fragment;
            if(this.mTransactionListener != null) {
                this.mTransactionListener.onFragmentTransaction(this.mCurrentFrag, FragNavController.TransactionType.PUSH);
            }
        }

    }

    public void pushFragment(@Nullable Fragment fragment) {
        this.pushFragment(fragment, (FragNavTransactionOptions)null);
    }

    public void popFragment(@Nullable FragNavTransactionOptions transactionOptions) throws UnsupportedOperationException {
        this.popFragments(1, transactionOptions);
    }

    public void popFragment() throws UnsupportedOperationException {
        this.popFragment((FragNavTransactionOptions)null);
    }

    public void popFragments(int popDepth, @Nullable FragNavTransactionOptions transactionOptions) throws UnsupportedOperationException {
        if(this.isRootFragment()) {
            throw new UnsupportedOperationException("You can not popFragment the rootFragment. If you need to change this fragment, use replaceFragment(fragment)");
        } else if(popDepth < 1) {
            throw new UnsupportedOperationException("popFragments parameter needs to be greater than 0");
        } else if(this.mSelectedTabIndex == -1) {
            throw new UnsupportedOperationException("You can not pop fragments when no tab is selected");
        } else if(popDepth >= ((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).size() - 1) {
            this.clearStack(transactionOptions);
        } else {
            FragmentTransaction ft = this.createTransactionWithOptions(transactionOptions, true);

            Fragment fragment;
            for(int i = 0; i < popDepth; ++i) {
                fragment = this.mFragmentManager.findFragmentByTag(((Fragment)((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).pop()).getTag());
                if(fragment != null) {
                    ft.remove(fragment);
                }
            }

            fragment = this.reattachPreviousFragment(ft);
            boolean bShouldPush = false;
            if(fragment != null) {
                this.commitTransaction(ft, transactionOptions);
            } else if(!((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).isEmpty()) {
                fragment = (Fragment)((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).peek();
                ft.add(this.mContainerId, fragment, fragment.getTag());
                this.commitTransaction(ft, transactionOptions);
            } else {
                fragment = this.getRootFragment(this.mSelectedTabIndex);
                ft.add(this.mContainerId, fragment, this.generateTag(fragment));
                this.commitTransaction(ft, transactionOptions);
                bShouldPush = true;
            }

            if(bShouldPush) {
                ((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).push(fragment);
            }

            this.mCurrentFrag = fragment;
            if(this.mTransactionListener != null) {
                this.mTransactionListener.onFragmentTransaction(this.mCurrentFrag, FragNavController.TransactionType.POP);
            }

        }
    }

    public void popFragments(int popDepth) throws UnsupportedOperationException {
        this.popFragments(popDepth, (FragNavTransactionOptions)null);
    }

    public void clearStack(@Nullable FragNavTransactionOptions transactionOptions) {
        if(this.mSelectedTabIndex != -1) {
            Stack<Fragment> fragmentStack = (Stack)this.mFragmentStacks.get(this.mSelectedTabIndex);
            if(fragmentStack.size() > 1) {
                FragmentTransaction ft = this.createTransactionWithOptions(transactionOptions, true);

                Fragment fragment;
                while(fragmentStack.size() > 1) {
                    fragment = this.mFragmentManager.findFragmentByTag(((Fragment)fragmentStack.pop()).getTag());
                    if(fragment != null) {
                        ft.remove(fragment);
                    }
                }

                fragment = this.reattachPreviousFragment(ft);
                boolean bShouldPush = false;
                if(fragment != null) {
                    this.commitTransaction(ft, transactionOptions);
                } else if(!fragmentStack.isEmpty()) {
                    fragment = (Fragment)fragmentStack.peek();
                    ft.add(this.mContainerId, fragment, fragment.getTag());
                    this.commitTransaction(ft, transactionOptions);
                } else {
                    fragment = this.getRootFragment(this.mSelectedTabIndex);
                    ft.add(this.mContainerId, fragment, this.generateTag(fragment));
                    this.commitTransaction(ft, transactionOptions);
                    bShouldPush = true;
                }

                if(bShouldPush) {
                    ((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).push(fragment);
                }

                this.mFragmentStacks.set(this.mSelectedTabIndex, fragmentStack);
                this.mCurrentFrag = fragment;
                if(this.mTransactionListener != null) {
                    this.mTransactionListener.onFragmentTransaction(this.mCurrentFrag, FragNavController.TransactionType.POP);
                }
            }

        }
    }

    public void clearStack() {
        this.clearStack((FragNavTransactionOptions)null);
    }

    public void replaceFragment(@NonNull Fragment fragment, @Nullable FragNavTransactionOptions transactionOptions) {
        Fragment poppingFrag = this.getCurrentFrag();
        if(poppingFrag != null) {
            FragmentTransaction ft = this.createTransactionWithOptions(transactionOptions, false);
            Stack<Fragment> fragmentStack = (Stack)this.mFragmentStacks.get(this.mSelectedTabIndex);
            if(!fragmentStack.isEmpty()) {
                fragmentStack.pop();
            }

            String tag = this.generateTag(fragment);
            ft.replace(this.mContainerId, fragment, tag);
            this.commitTransaction(ft, transactionOptions);
            fragmentStack.push(fragment);
            this.mCurrentFrag = fragment;
            if(this.mTransactionListener != null) {
                this.mTransactionListener.onFragmentTransaction(this.mCurrentFrag, FragNavController.TransactionType.REPLACE);
            }
        }

    }

    public void replaceFragment(@NonNull Fragment fragment) {
        this.replaceFragment(fragment, (FragNavTransactionOptions)null);
    }

    @Nullable
    @CheckResult
    public DialogFragment getCurrentDialogFrag() {
        if(this.mCurrentDialogFrag != null) {
            return this.mCurrentDialogFrag;
        } else {
            FragmentManager fragmentManager;
            if(this.mCurrentFrag != null) {
                fragmentManager = this.mCurrentFrag.getChildFragmentManager();
            } else {
                fragmentManager = this.mFragmentManager;
            }

            if(fragmentManager.getFragments() != null) {
                Iterator var2 = fragmentManager.getFragments().iterator();

                while(var2.hasNext()) {
                    Fragment fragment = (Fragment)var2.next();
                    if(fragment instanceof DialogFragment) {
                        this.mCurrentDialogFrag = (DialogFragment)fragment;
                        break;
                    }
                }
            }

            return this.mCurrentDialogFrag;
        }
    }

    public void clearDialogFragment() {
        if(this.mCurrentDialogFrag != null) {
            this.mCurrentDialogFrag.dismiss();
            this.mCurrentDialogFrag = null;
        } else {
            FragmentManager fragmentManager;
            if(this.mCurrentFrag != null) {
                fragmentManager = this.mCurrentFrag.getChildFragmentManager();
            } else {
                fragmentManager = this.mFragmentManager;
            }

            if(fragmentManager.getFragments() != null) {
                Iterator var2 = fragmentManager.getFragments().iterator();

                while(var2.hasNext()) {
                    Fragment fragment = (Fragment)var2.next();
                    if(fragment instanceof DialogFragment) {
                        ((DialogFragment)fragment).dismiss();
                    }
                }
            }
        }

    }

    public void showDialogFragment(@Nullable DialogFragment dialogFragment) {
        if(dialogFragment != null) {
            FragmentManager fragmentManager;
            if(this.mCurrentFrag != null) {
                fragmentManager = this.mCurrentFrag.getChildFragmentManager();
            } else {
                fragmentManager = this.mFragmentManager;
            }

            if(fragmentManager.getFragments() != null) {
                Iterator var3 = fragmentManager.getFragments().iterator();

                while(var3.hasNext()) {
                    Fragment fragment = (Fragment)var3.next();
                    if(fragment instanceof DialogFragment) {
                        ((DialogFragment)fragment).dismiss();
                        this.mCurrentDialogFrag = null;
                    }
                }
            }

            this.mCurrentDialogFrag = dialogFragment;

            try {
                dialogFragment.show(fragmentManager, dialogFragment.getClass().getName());
            } catch (IllegalStateException var5) {
                ;
            }
        }

    }

    private void initialize(int index) {
        this.mSelectedTabIndex = index;
        if(this.mSelectedTabIndex > this.mFragmentStacks.size()) {
            throw new IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks");
        } else {
            this.mSelectedTabIndex = index;
            this.clearFragmentManager();
            this.clearDialogFragment();
            if(index != -1) {
                FragmentTransaction ft = this.createTransactionWithOptions((FragNavTransactionOptions)null, false);
                Fragment fragment = this.getRootFragment(index);
                ft.add(this.mContainerId, fragment, this.generateTag(fragment));
                this.commitTransaction(ft, (FragNavTransactionOptions)null);
                this.mCurrentFrag = fragment;
                if(this.mTransactionListener != null) {
                    this.mTransactionListener.onTabTransaction(this.mCurrentFrag, this.mSelectedTabIndex);
                }

            }
        }
    }

    @NonNull
    @CheckResult
    private Fragment getRootFragment(int index) throws IllegalStateException {
        Fragment fragment = null;
        if(!((Stack)this.mFragmentStacks.get(index)).isEmpty()) {
            fragment = (Fragment)((Stack)this.mFragmentStacks.get(index)).peek();
        } else if(this.mRootFragmentListener != null) {
            fragment = this.mRootFragmentListener.getRootFragment(index);
            if(this.mSelectedTabIndex != -1) {
                ((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).push(fragment);
            }
        }

        if(fragment == null) {
            throw new IllegalStateException("Either you haven't past in a fragment at this index in your constructor, or you haven't provided a way to create it while via your RootFragmentListener.getRootFragment(index)");
        } else {
            return fragment;
        }
    }

    @Nullable
    private Fragment reattachPreviousFragment(@NonNull FragmentTransaction ft) {
        Stack<Fragment> fragmentStack = (Stack)this.mFragmentStacks.get(this.mSelectedTabIndex);
        Fragment fragment = null;
        if(!fragmentStack.isEmpty()) {
            fragment = this.mFragmentManager.findFragmentByTag(((Fragment)fragmentStack.peek()).getTag());
            if(fragment != null) {
                ft.attach(fragment);
            }
        }

        return fragment;
    }

    @Nullable
    private Fragment reshowPreviousFragment(FragmentTransaction ft) {
        Stack<Fragment> fragmentStack = (Stack)this.mFragmentStacks.get(this.mSelectedTabIndex);
        Fragment fragment = null;
        if(!fragmentStack.isEmpty()) {
            fragment = this.mFragmentManager.findFragmentByTag(((Fragment)fragmentStack.peek()).getTag());
            if (fragment != null && fragment.isHidden()) {
                ft.show(fragment);
            }
        }

        return fragment;
    }

    private void detachCurrentFragment(@NonNull FragmentTransaction ft) {
        Fragment oldFrag = this.getCurrentFrag();
        if(oldFrag != null) {
            ft.detach(oldFrag);
        }

    }

    private void hideCurrentFragment(FragmentTransaction ft) {
        Fragment oldFrag = getCurrentFrag();
        if (oldFrag != null) {
            ft.hide(oldFrag);
        }
    }

    @Nullable
    @CheckResult
    public Fragment getCurrentFrag() {
        if(this.mCurrentFrag != null) {
            return this.mCurrentFrag;
        } else if(this.mSelectedTabIndex == -1) {
            return null;
        } else {
            Stack<Fragment> fragmentStack = (Stack)this.mFragmentStacks.get(this.mSelectedTabIndex);
            if(!fragmentStack.isEmpty()) {
                this.mCurrentFrag = this.mFragmentManager.findFragmentByTag(((Fragment)((Stack)this.mFragmentStacks.get(this.mSelectedTabIndex)).peek()).getTag());
            }

            return this.mCurrentFrag;
        }
    }

    @NonNull
    @CheckResult
    private String generateTag(@NonNull Fragment fragment) {
        return fragment.getClass().getName() + ++this.mTagCount;
    }

    private void executePendingTransactions() {
        if(!this.mExecutingTransaction) {
            this.mExecutingTransaction = true;
            this.mFragmentManager.executePendingTransactions();
            this.mExecutingTransaction = false;
        }

    }

    private void clearFragmentManager() {
        if(this.mFragmentManager.getFragments() != null) {
            FragmentTransaction ft = this.createTransactionWithOptions((FragNavTransactionOptions)null, false);
            Iterator var2 = this.mFragmentManager.getFragments().iterator();

            while(var2.hasNext()) {
                Fragment fragment = (Fragment)var2.next();
                if(fragment != null) {
                    ft.remove(fragment);
                }
            }

            this.commitTransaction(ft, (FragNavTransactionOptions)null);
        }

    }

    @CheckResult
    private FragmentTransaction createTransactionWithOptions(@Nullable FragNavTransactionOptions transactionOptions, boolean isPopping) {
        FragmentTransaction ft = this.mFragmentManager.beginTransaction();
        if(transactionOptions == null) {
            transactionOptions = this.mDefaultTransactionOptions;
        }

        if(transactionOptions != null) {
            if(isPopping) {
                ft.setCustomAnimations(transactionOptions.popEnterAnimation, transactionOptions.popExitAnimation);
            } else {
                ft.setCustomAnimations(transactionOptions.enterAnimation, transactionOptions.exitAnimation);
            }

            ft.setTransitionStyle(transactionOptions.transitionStyle);
            ft.setTransition(transactionOptions.transition);
            if(transactionOptions.sharedElements != null) {
                Iterator var4 = transactionOptions.sharedElements.iterator();

                while(var4.hasNext()) {
                    Pair<View, String> sharedElement = (Pair)var4.next();
                    ft.addSharedElement((View)sharedElement.first, (String)sharedElement.second);
                }
            }

            if(transactionOptions.breadCrumbTitle != null) {
                ft.setBreadCrumbTitle(transactionOptions.breadCrumbTitle);
            }

            if(transactionOptions.breadCrumbShortTitle != null) {
                ft.setBreadCrumbShortTitle(transactionOptions.breadCrumbShortTitle);
            }
        }

        return ft;
    }

    private void commitTransaction(FragmentTransaction fragmentTransaction, @Nullable FragNavTransactionOptions transactionOptions) {
        if(transactionOptions != null && transactionOptions.allowStateLoss) {
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            fragmentTransaction.commit();
        }

        this.executePendingTransactions();
    }

    @CheckResult
    public int getSize() {
        return this.mFragmentStacks.size();
    }

    @CheckResult
    @Nullable
    public Stack<Fragment> getStack(int index) {
        if(index == -1) {
            return null;
        } else if(index >= this.mFragmentStacks.size()) {
            throw new IndexOutOfBoundsException("Can't get an index that's larger than we've setup");
        } else {
            return (Stack)((Stack)this.mFragmentStacks.get(index)).clone();
        }
    }

    @CheckResult
    @Nullable
    public Stack<Fragment> getCurrentStack() {
        return this.getStack(this.mSelectedTabIndex);
    }

    @CheckResult
    public int getCurrentStackIndex() {
        return this.mSelectedTabIndex;
    }

    @CheckResult
    public boolean isRootFragment() {
        Stack<Fragment> stack = this.getCurrentStack();
        return stack == null || stack.size() == 1;
    }

    public boolean isStateSaved() {
        return this.mFragmentManager.isStateSaved();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(EXTRA_TAG_COUNT, this.mTagCount);
        outState.putInt(EXTRA_SELECTED_TAB_INDEX, this.mSelectedTabIndex);
        if(this.mCurrentFrag != null) {
            outState.putString(EXTRA_CURRENT_FRAGMENT, this.mCurrentFrag.getTag());
        }

        try {
            JSONArray stackArrays = new JSONArray();
            Iterator var3 = this.mFragmentStacks.iterator();

            while(var3.hasNext()) {
                Stack<Fragment> stack = (Stack)var3.next();
                JSONArray stackArray = new JSONArray();
                Iterator var6 = stack.iterator();

                while(var6.hasNext()) {
                    Fragment fragment = (Fragment)var6.next();
                    stackArray.put(fragment.getTag());
                }

                stackArrays.put(stackArray);
            }

            outState.putString(EXTRA_FRAGMENT_STACK, stackArrays.toString());
        } catch (Throwable var8) {
            ;
        }

    }

    private boolean restoreFromBundle(@Nullable Bundle savedInstanceState, @Nullable List<Fragment> rootFragments) {
        if(savedInstanceState == null) {
            return false;
        } else {
            this.mTagCount = savedInstanceState.getInt(EXTRA_TAG_COUNT, 0);
            this.mCurrentFrag = this.mFragmentManager.findFragmentByTag(savedInstanceState.getString(EXTRA_CURRENT_FRAGMENT));

            try {
                JSONArray stackArrays = new JSONArray(savedInstanceState.getString(EXTRA_FRAGMENT_STACK));

                int x;
                for(x = 0; x < stackArrays.length(); ++x) {
                    JSONArray stackArray = stackArrays.getJSONArray(x);
                    Stack<Fragment> stack = new Stack();
                    if(stackArray.length() != 1) {
                        for(int y = 0; y < stackArray.length(); ++y) {
                            String tag = stackArray.getString(y);
                            if(tag != null && !"null".equalsIgnoreCase(tag)) {
                                Fragment fragment = this.mFragmentManager.findFragmentByTag(tag);
                                if(fragment != null) {
                                    stack.add(fragment);
                                }
                            }
                        }
                    } else {
                        String tag = stackArray.getString(0);
                        Fragment fragment;
                        if(tag != null && !"null".equalsIgnoreCase(tag)) {
                            fragment = this.mFragmentManager.findFragmentByTag(tag);
                        } else if(rootFragments != null) {
                            fragment = (Fragment)rootFragments.get(x);
                        } else {
                            fragment = this.getRootFragment(x);
                        }

                        if(fragment != null) {
                            stack.add(fragment);
                        }
                    }

                    this.mFragmentStacks.add(stack);
                }

                x = savedInstanceState.getInt(EXTRA_SELECTED_TAB_INDEX);
                if(x >= 0 && x < 20) {
                    this.switchTab(x, FragNavTransactionOptions.newBuilder().build(), true);
                }

                return true;
            } catch (Throwable var10) {
                return false;
            }
        }
    }

    public static final class Builder {
        private final int mContainerId;
        private FragmentManager mFragmentManager;
        private FragNavController.RootFragmentListener mRootFragmentListener;
        private int mSelectedTabIndex = 0;
        private FragNavController.TransactionListener mTransactionListener;
        private FragNavTransactionOptions mDefaultTransactionOptions;
        private int mNumberOfTabs = 0;
        private List<Fragment> mRootFragments;
        private Bundle mSavedInstanceState;

        public Builder(@Nullable Bundle savedInstanceState, FragmentManager mFragmentManager, int mContainerId) {
            this.mSavedInstanceState = savedInstanceState;
            this.mFragmentManager = mFragmentManager;
            this.mContainerId = mContainerId;
        }

        public FragNavController.Builder selectedTabIndex(int selectedTabIndex) {
            this.mSelectedTabIndex = selectedTabIndex;
            if(this.mRootFragments != null && this.mSelectedTabIndex > this.mNumberOfTabs) {
                throw new IndexOutOfBoundsException("Starting index cannot be larger than the number of stacks");
            } else {
                return this;
            }
        }

        public FragNavController.Builder rootFragment(Fragment rootFragment) {
            this.mRootFragments = new ArrayList(1);
            this.mRootFragments.add(rootFragment);
            this.mNumberOfTabs = 1;
            return this.rootFragments(this.mRootFragments);
        }

        public FragNavController.Builder rootFragments(@NonNull List<Fragment> rootFragments) {
            this.mRootFragments = rootFragments;
            this.mNumberOfTabs = rootFragments.size();
            if(this.mNumberOfTabs > 20) {
                throw new IllegalArgumentException("Number of root fragments cannot be greater than 20");
            } else {
                return this;
            }
        }

        public FragNavController.Builder defaultTransactionOptions(@NonNull FragNavTransactionOptions transactionOptions) {
            this.mDefaultTransactionOptions = transactionOptions;
            return this;
        }

        public FragNavController.Builder rootFragmentListener(FragNavController.RootFragmentListener rootFragmentListener, int numberOfTabs) {
            this.mRootFragmentListener = rootFragmentListener;
            this.mNumberOfTabs = numberOfTabs;
            if(this.mNumberOfTabs > 20) {
                throw new IllegalArgumentException("Number of tabs cannot be greater than 20");
            } else {
                return this;
            }
        }

        public FragNavController.Builder transactionListener(FragNavController.TransactionListener val) {
            this.mTransactionListener = val;
            return this;
        }

        public FragNavController build() {
            if(this.mRootFragmentListener == null && this.mRootFragments == null) {
                throw new IndexOutOfBoundsException("Either a root fragment(s) needs to be set, or a fragment listener");
            } else {
                return new FragNavController(this, this.mSavedInstanceState);
            }
        }
    }

    public interface TransactionListener {
        void onTabTransaction(Fragment var1, int var2);

        void onFragmentTransaction(Fragment var1, FragNavController.TransactionType var2);
    }

    public interface RootFragmentListener {
        Fragment getRootFragment(int var1);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TabIndex {
    }

    public static enum TransactionType {
        PUSH,
        POP,
        REPLACE;

        private TransactionType() {
        }
    }
}

