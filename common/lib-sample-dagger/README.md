# Sample Dagger
Модуль, используемый для создания примеров к другим модулям,
использующим конфигурацию Dagger.

Содержит базовую конфигурацию Dagger.

# Использование
## Конфигурация, используемая по умолчанию
Если конфигурация Dagger для примера не отличается от конфигурации данного модуля
(если не нужно добавлять кастомные зависимости), то достаточно наследовать
конфигураторы экранов от базового конфигуратора, поставляемым данным модулем.

## Пример
```
internal class MainScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class])
    internal interface MainScreenComponent
        : ScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute)
        : DefaultCustomScreenModule<MainActivityRoute>(route)

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}
```

## Кастомная конфигурация модуля
Если конфигурация Dagger для примера отличается от конфигурации данного модуля,
необходимо добавить создать классы ```CustomAppComponent```
и ```CustomActivityComponent```, отнаследовав их от классов
```DefaultAppComponent``` и ```DefaultActivityComponent```, и добавить
необходимые зависимости, которые должны предоставлять кастомные модули.

Создав кастомные компоненты, необходимо также создать кастомные классы
для приложения (```CustomApp```) и кастомные конфигураторы для Activity и Fragment.

## Пример
```
@PerApplication
@Component(modules = [
    DefaultAppModule::class,        // обязательный модуль для всех компонентов
    DefaultSharedPrefModule::class, // по мере необходимости
    CustomModule::class])           // список кастомных модулей конкретного примера
interface CustomAppComponent : DefaultAppComponent {
    fun customStorage(): CustomStorage
}
```
```
@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun customStorage(): CustomStorage
}
```
```
public class CustomActivityConfigurator 
        extends BaseActivityConfigurator<CustomActivityComponent, CustomAppComponent> {
        
    @Override
    protected CustomActivityComponent createActivityComponent(CustomAppComponent parentComponent) {
        return DaggerCustomActivityComponent.builder()
                .customAppComponent(parentComponent)
                .defaultActivityModule(new DefaultActivityModule(getPersistentScope()))
                .build();
    }

    @Override
    protected CustomAppComponent getParentComponent() {
        return ((CustomApp) getTargetActivity().getApplicationContext()).getCustomAppComponent();
    }
}
```

# Подключение
При использовании ```sample-dagger``` модуль ```sample-common``` 
отдельно подключать не нужно.

Пример зависимостей для build.gradle примера, использующего данный модуль:

```
dependencies {
    //module-name - имя модуля, для которого создается пример
    implementation project(':module-name')
    implementation project(':sample-dagger')

    kapt "com.google.dagger:dagger-compiler:$daggerVersion"

    //other dependencies
}
```