package ru.surfstudio.android.core.ui.base.screen.fragment;

import ru.surfstudio.android.core.ui.HasName;
import ru.surfstudio.android.core.ui.base.screen.delegate.BaseFragmentDelegate;

//todo comment
public interface BaseFragmentInterface extends HasName {
    BaseFragmentDelegate createFragmentDelegate();
}
