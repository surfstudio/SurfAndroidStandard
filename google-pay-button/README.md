[Main](/docs/main.md)

If you're using this module in production, you should notice that it has many resources, that probably would never used in application.
To avoid packaging of unnecessary resources into your APK, you should use `resConfigs` in your `build.gradle` file to *select* what configurations of resources you need.
For example if you support only 3 languages, then your config should look like:
```txt
android {
    ...
    defaultConfig {
        ...
        resConfigs 'en', 'ru', 'de'
        ...
    }
    ...
}
```

# Modules

- [Google Pay Button](lib-google-pay-button)
- [Sample](sample)