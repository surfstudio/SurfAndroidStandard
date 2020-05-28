# EasyAdapter Pagination
This module contains a class for adapter of list with pagination which
is based on `EasyAdapter`.

# Usage
Gradle:
```
    implementation "ru.surfstudio.android:easyadapter-pagination:X.X.X"
```

The main idea is based on `PaginationState` which describes all possible
states during pagination.

All you need is to create your implementation of
`BasePaginationFooterController` and pass its instance to
`EasyPaginationAdapter` constructor. In the same way you can pass a
listener for loading next page.

[Sample BasePaginationFooterController implementation](../../common/lib-sample-common/src/main/java/ru/surfstudio/android/sample/common/ui/base/easyadapter/PaginationFooterItemController.kt)

Also you can change logic for checking if it's needed to show more
elements, see `EasyPaginationAdapter.shouldShowMoreElements`

The rest usage is just the same as usage of `EasyAdapter`.

[Sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/pagination/PaginationListActivityView.kt)
