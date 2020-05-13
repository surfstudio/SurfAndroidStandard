# EasyAdapter
This module is a development of project
[EasyAdapter](https://github.com/MaksTuev/EasyAdapter).

This is adapter which simplifies the process of adding a complex content
to RecyclerView.

The main idea is using unique `ItemController` which is responsible for
render and behaviour of a single element.

It can be used for static and dynamically populated content.

# Initialization
1. Create instance of `EasyAdapter` using default constructor and pass
   to `RecyclerView`
2. For each list element create [controller](src/main/java/ru/surfstudio/android/easyadapter/controller)
3. Create `ItemList` and add data using pairs of data and controller
4. Pass filled `ItemList` to `EasyAdapter` using `setItems()`

[Sample](../sample)

# Usage
Gradle:
```
    implementation "ru.surfstudio.android:easyadapter:X.X.X"
```

# What's new
### Version EasyAdapter 1.1.0-alpha

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

### Version AndroidStandard 0.3.0

Type of `hash` and `id` was changed to `String` in order to avoid
possible collisions.

If list contains a big number of elements and there is a big probability
of collisions, then it's recommended to override controller's method
`getItemHash` and implement a hashing of object without using standard
method `hashCode()` but using library
[guava](https://github.com/google/guava) instead.

Sample hashing implementation:
```
override fun getItemHash(data: SampleData?): String {
    return Hashing.md5().newHasher()
           .putLong(data.longValue)
           .putString(data.StringValue, Charsets.UTF_8)
           .hash()
           .toString()
}
```

For usual cases there is no need to override `getItemHash`.

[Hashing using guava](https://github.com/google/guava/wiki/HashingExplained)
