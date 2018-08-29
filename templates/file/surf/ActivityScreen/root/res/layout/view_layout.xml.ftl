<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    <#if generateRecyclerView>
    xmlns:app="http://schemas.android.com/apk/res-auto"
    </#if>
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <#if generateToolbar>
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    <android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipe_refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </#if>
    <#if generateRecyclerView>
    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    </#if>

    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    </android.support.v4.widget.SwipeRefreshLayout>
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    <${applicationPackage}.ui.base.placeholder.PlaceHolderViewImpl
        android:id="@+id/placeholder"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    </FrameLayout>
    </#if>
</LinearLayout>