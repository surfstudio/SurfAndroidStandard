<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <#if generateToolbar>
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    </#if>

    <#if generateRecyclerView && (screenType=='activity' && typeViewActivity!='3' || screenType=='fragment' && typeViewFragment!='3')>
    <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    <#elseif (screenType=='activity' && typeViewActivity=='3') || (screenType=='fragment' && typeViewFragment=='3')>
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

    <android.support.v4.widget.SwipeRefreshLayout
            android:id="@+id/swipe_refresh"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        <#if generateRecyclerView>
        <android.support.v7.widget.RecyclerView
                android:id="@+id/recycler"
                android:layout_width="match_parent"
                android:layout_height="match_parent" />
        </#if>
    </android.support.v4.widget.SwipeRefreshLayout>
    </FrameLayout>
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
    <${applicationPackage}.ui.base.placeholder.PlaceHolderViewImpl
        android:id="@+id/placeholder"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    </#if>
</LinearLayout>