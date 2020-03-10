[TOC]
# Imageloader Release Notes
## 0.5.0-alpha
##### Imageloader
* Added ability to disable hardware bitmaps config and immediate GIF playback after loading
* ANDDEP-781 Added headers to ImageLoader when specifying URL
* Cross fading with placeholders and transparent images fixed, the ability to hide background image is added to crossfade() method, 
more about this problem -  https://clck.ru/FVpbQ
* Added ability to work with Animatable drawable (GIF) in callbacks of into method
## 0.4.0
##### Imageloader
* ANDDEP-317 Optimization [`ImageLoader`](lib-imageloader/src/main/java/ru/surfstudio/android/imageloader/ImageLoader.kt):
* Refactoring transformations, removing calls to reflection
* Avoiding work based on `SimpleTarget`, eliminating memory leaks, adding the ability to clear memory
* Acceleration of work, adding checks for the optional use of variables
* Adding extension functions for optional work with RequestBuilder [`ImageLoaderUtils.kt`](lib-imageloader/src/main/java/ru/surfstudio/android/imageloader/util/ImageLoaderUtils.kt)
* Adding an extended list of caching strategies [`CacheStrategy`](lib-imageloader/src/main/java/ru/surfstudio/android/imageloader/data/CacheStrategy.kt)
* ANDDEP-442 Functional extension [`ImageLoader`](lib-imageloader/src/main/java/ru/surfstudio/android/imageloader/ImageLoader.kt):
* Added support for setting Tile to image (bridging horizontally and vertically)
* The `ImageLoader.mask` method has been changed, now it accepts the` PorterDuff.Mode` parameter to set the desired fill type
* Adding a list of image download sources [`ImageSource`](lib-imageloader/src/main/java/ru/surfstudio/android/imageloader/data/ImageSource.kt)
* Adding a listener with an image download source: `ImageLoader.listenerWithSource`
* Fix non-obvious behavior of the function `ImageLoader.into`, adding overload with lambda-listeners
* ANDDEP-306 Added SVG support for ImageLoader
## 0.2.1
##### Imageloader
* Update Glide to 3.7.1
* Optimization
* Fixed loading in SimpleTarget
* Added downSampling
* Added the ability to force image updates using the force() method
* ANDDEP-212 Added crossFade method for smooth rendering of images