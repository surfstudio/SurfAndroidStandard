# Sample Dagger
Module which is used to create samples for another modules, which
require Dagger configuration.

This module contains base Dagger configuration.

# Usage
## Default configuration
If sample's Dagger configuration doesn't require custom dependencies,
then just inherit the screen configurators from the base configurator
which is supplied by this module.

## Sample
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

## Custom configuration
If sample's Dagger configuration is different from this module's
configuration, it's required to create classes `CustomAppComponent` and
`CustomActivityComponent` which have to be inherited from
`DefaultAppComponent` and `DefaultActivityComponent`, and then add
necessary dependencies which will be supplied by custom modules.

If custom components are created, it's required to create custom class
for application (`CustomApp`) and custom configurators for Activity and
Fragment.

## Sample
```
@PerApplication
@Component(modules = [
    DefaultAppModule::class,        // required module for all components
    DefaultSharedPrefModule::class, // if needed
    CustomModule::class])           // list of sample's custom modules
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

# Usage
With usage of `sample-dagger` you don't need to add dependency for
`sample-common`.

Example of dependencies for `build.gradle` of sample which is used this
module:

```
dependencies {
    //module-name - module-name for sample creation
    implementation project(':module-name')
    implementation project(':sample-dagger')

    kapt "com.google.dagger:dagger-compiler:$daggerVersion"

    //other dependencies
}
```