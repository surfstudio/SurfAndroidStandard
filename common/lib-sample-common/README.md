# Sample Common
Module which is used to create samples for another modules, which don't
require Dagger configuration.

This module contains common resources for every sample.

# Usage
Example of dependencies for `build.gradle` of sample which is used this
module:

```
dependencies {
    //module-name - module-name for sample creation
    implementation project(':module-name')
    implementation project(':sample-common')

    //other dependencies
}
```
