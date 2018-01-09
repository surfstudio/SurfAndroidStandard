<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/coordinator"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <#if isUseToolbar>
    <android.support.design.widget.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:fitsSystemWindows="false"
        android:theme="@style/AppTheme.AppBarOverlay">

        <android.support.v7.widget.Toolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize" />
    </android.support.design.widget.AppBarLayout>
    </#if>

    <#if typeView=='4' || typeView=='5'>
    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipe_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">
         <#if isUseRecyclerView>
            <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
        </#if>
    </android.support.v4.widget.SwipeRefreshLayout>
    </#if>

    <#if typeView=='3' || typeView=='4' || typeView=='5'>
    <ru.avs.express.android.ui.base.placeholder.PlaceHolderView
        android:id="@+id/placeholder"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="bottom"
        app:layout_anchor="@id/toolbar"
        app:layout_anchorGravity="bottom" />
    </#if>

</android.support.design.widget.CoordinatorLayout>
