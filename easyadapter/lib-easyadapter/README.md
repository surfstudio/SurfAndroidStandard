# EasyAdapter
This module evolved from project
[EasyAdapter](https://github.com/MaksTuev/EasyAdapter).

This is adapter which simplifies the process of adding a complex content
to RecyclerView.

# Usage
Gradle:
```
    implementation "ru.surfstudio.android:easyadapter:X.X.X"
```

# Sample code
```
adapter.setItems(
    ItemList.create()
        .addAll(sm.firstDataList, firstDataItemController)
        .addAll(sm.secondDataList, secondDataItemController)
        .add(emptyItemController)
        .add(sm.firstData, sm.secondData, twoDataItemController)
)
                
```

# Features
* Encapsulation for logic of rendering a single element type
* Simple and declarative RecyclerView filling
* No need to call `notify...` methods
* Async `DiffUtil` support
* Async inflate support
* [Pagination support](../lib-easyadapter-pagination/README.md)
* Endless scroll support

# Detailed description
The main idea is using unique `ItemController` which is responsible for
render and behaviour of a single element type.

It can be used for static and dynamically populated content.

## Multitype list
1. Create instance of `EasyAdapter` using default constructor and pass
   to `RecyclerView`
2. For each list element create its controller which should be inherited
   from one of base
   [controllers](src/main/java/ru/surfstudio/android/easyadapter/controller)
3. Create `ItemList` and add data using pairs of data and controller
4. Pass filled `ItemList` to `EasyAdapter` using `setItems()`

[Sample for multitype list](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/multitype/MultitypeListActivityView.kt)

The library has base `BaseItemController` class for every controller in
order to create your own implementations.

For example:
* [Sticky header and footer controllers](https://github.com/surfstudio/SurfAndroidStandard/tree/dev/G-0.5.0/recycler-extension/lib-recycler-extension/src/main/java/ru/surfstudio/android/recycler/extension/sticky/controller)
* [Sliding controllers](https://github.com/surfstudio/SurfAndroidStandard/tree/dev/G-0.5.0/recycler-extension/lib-recycler-extension/src/main/java/ru/surfstudio/android/recycler/extension/slide)

Also there are 3 kinds of controllers for common usage:
1. For a single data you should inherit `BindableItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/FirstDataItemController.kt)
2. For two kinds of data you should inherit
   `DoubleBindableItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/TwoDataItemController.kt).
   You can also use `BindableItemController` which could be initialized
   with object which contains all data sources.
3. For a static controller which contains no changeable data you should
   inherit `NoDataItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/EmptyItemController.kt)

## Async inflate

A library also has async inflate support which could be uses for each
controller. The implementation based on `AsyncViewHolder`.

[Sample async inflate controller](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/async/AsyncInflateItemController.kt)

## Render based on diff of elements

The adapter does not require to call `notify...` methods because it uses
`DiffUtil`.

`DiffUtil` is used to determine which elements were changed and when a
new render is required for them. Each element has two fields for such
determination, see `ItemInfo`.
* `id` must be unique and constant for each element in order to be
  different.
* `hash` is calculated internally and is based on content, see
  `BaseItemController.getItemHash`.

Type of `hash` and `id` was changed to `Object` in order to avoid
possible collisions and achieve more flexibility.

If list contains a big number of elements and there is a big probability
of collisions, then it's recommended to override controller's method
`getItemHash` and return object itself in order to avoid collisions:

```
override fun getItemHash(data: SampleData): Object {
    return data
}
```

For usual cases there is no need to override `getItemHash`.

For previous versions see [docs](docs/deprecated.md)

## Async diff

`DiffResult` of elements is also could be calculated in a worker thread,
see `BaseAsyncDiffer`. There are two async diff strategies which are
supported, see `AsyncDiffStrategy`, and it is possible to invoke some
action after diff dispatching, see
`EasyAdapter.setDiffResultDispatchListener`. These options could be set
up during EasyAdapter instance initialization.

[Sample for async diff usage](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/async_diff/AsyncDiffActivityView.kt)
