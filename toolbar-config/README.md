# ToolbarConfig
Билдер для гибкой и удобной настройки Toolbar'а

# Использование

Пример: 

        ToolbarConfig.builder(this)
                .setHomeAsUpIndicatorId(R.drawable.ic_close_small)
                .setDisplayHomeAsUpEnabled(true)
                .setHomeAsUpIndicatorColor(R.color.colorAccent)
                .setOnMenuItemClickListener { presenter.openInfoScreen() }
                .setTitleText(getString(R.string.withdraw_toolbar_title))
                .apply()
                  
[Пример использования](../core-mvp-sample)

# Подключение
Gradle:
```
    implementation "ru.surfstudio.android:toolbar-config:X.X.X"
```