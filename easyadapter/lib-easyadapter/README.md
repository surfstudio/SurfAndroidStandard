# EasyAdapter
This module evolved from project
[EasyAdapter](https://github.com/MaksTuev/EasyAdapter).

This is an implementation of `RecyclerView.Adapter`, which simplifies
the process of adding a complex content to RecyclerView.

# Usage
Gradle:
```
    implementation "ru.surfstudio.android:easyadapter:X.X.X"
```

# Sample code
```
adapter.setItems(
    ItemList.create()
        .addAll(dataList, firstDataItemController)
        .addAllIf(condition, anotherDataList, secondDataItemController)
        .add(emptyItemController)
        .add(firstData, secondData, twoDataItemController)
        .addIf(condition, data, controller)
        .addHeader(header, headerController)
        .addFooter(footer, footerController)
)
                
```

# Features
* Rendering logic is separated by element types
* Simple and declarative RecyclerView filling
* No need to call `notify...` methods
* Async `DiffUtil` support
* Async inflate support
* [Pagination support](../lib-easyadapter-pagination/README.md)
* Endless scroll support

# Detailed description
The main idea is to separate element types and to use different
`ItemController` for each type.

`ItemController` is responsible for element's identification, rendering
and behavior.

It can be used for static and dynamically populated content.

## Multitype list
1. Create instance of `EasyAdapter` using default constructor and pass
   it to `RecyclerView`
2. For each element type in the list create `ItemController`, which
   should be inherited from one of base
   [controllers](src/main/java/ru/surfstudio/android/easyadapter/controller)
3. Create `ItemList` and add data using pairs of data and controller
4. Pass filled `ItemList` to `EasyAdapter` using `setItems()`

`ItemList` is used for data population and contains several methods to
add data (see Sample code above).

It's possible to add or insert by index
single or multiple data which is associated to particular
`ItemController` implementation.

[Sample for multitype list](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/multitype/MultitypeListActivityView.kt)

The library has base `BaseItemController` class for every controller in
order to create your own implementations.

For example:
* [Sticky header and footer controllers](https://github.com/surfstudio/SurfAndroidStandard/tree/dev/G-0.5.0/recycler-extension/lib-recycler-extension/src/main/java/ru/surfstudio/android/recycler/extension/sticky/controller)
* [Sliding controllers](https://github.com/surfstudio/SurfAndroidStandard/tree/dev/G-0.5.0/recycler-extension/lib-recycler-extension/src/main/java/ru/surfstudio/android/recycler/extension/slide)

Also there are 3 kinds of controllers for common usage:
1. For a controller which is bound to a single element of data, you
   should inherit `BindableItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/FirstDataItemController.kt)
2. For a controller which is bound to two elements of data, you should
   inherit `DoubleBindableItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/TwoDataItemController.kt).
3. For a static controller which contains no changeable data you should
   inherit `NoDataItemController`,
   [sample](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/common/controllers/EmptyItemController.kt)

## Async inflate

A library also has async inflate support which could be used for each
controller. The implementation based on `AsyncViewHolder`.

[Sample async inflate controller](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/async/AsyncInflateItemController.kt)

## Render based on diff of elements

The adapter does not require to call `notify...` methods because it uses
`DiffUtil`.

`DiffUtil` is used to determine which elements were changed and when we
should re-render them. Each element has two fields for such
determination, see `ItemInfo`.

`ItemInfo` is used internally and its content is initialized using
`getItemId` and `getItemHash` methods of `BaseItemController`:
* `id` must be unique and constant for each element in order to be
  different.
* `hash` is calculated internally and is based on content, see
  `BaseItemController.getItemHash`.

In most cases you only need to implement `getItemId` for your
`BaseItemController` implementation.

`hash` and `id` have `Object` type to avoid possible collisions and
achieve more flexibility.

If list contains a big number of elements and there is a big probability
of collisions, then it's recommended to override controller's method
`getItemHash` and return object itself in order to avoid collisions:

```
override fun getItemHash(data: SampleData): Object {
    return data
}
```

For usual cases there is no need to override `getItemHash`.

## Async diff

`DiffResult` of elements is also could be calculated in a worker thread,
see `BaseAsyncDiffer`. There are two async diff strategies which are
supported, see `AsyncDiffStrategy`, and it is possible to invoke some
action after diff dispatching, see
`EasyAdapter.setDiffResultDispatchListener`. These options could be set
up during EasyAdapter instance initialization.

[Sample for async diff usage](../sample/src/main/java/ru/surfstudio/android/easyadapter/sample/ui/screen/async_diff/AsyncDiffActivityView.kt)
