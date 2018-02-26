package ru.surfstudio.android.core.ui.navigation.fragment.tabfragment;

import android.support.v4.app.Fragment;

/**
 * Слуштель для создания корневых фрагментова
 */
@PerMainScreen
public class RootFragmentListener implements FragNavController.RootFragmentListener {

    /**
     * В ia не создаем табы
     */
    @Override
    public Fragment getRootFragment(int i) {
        return new Fragment();
    }
}
