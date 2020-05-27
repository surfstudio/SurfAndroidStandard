[Main docs](../README.md)

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
