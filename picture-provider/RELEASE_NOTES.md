# Picture-provider Release Notes

- [0.5.1-alpha](#051-alpha)
- [0.5.0](#050)
- [0.3.0](#030)
- [0.2.2](#022)
- [0.2.1](#021)

## 0.5.1-alpha
##### Picture-provider
* ANDDEP-1226 Added `picture-provider-deprecated` module that grants backward compatibility if enabled along with other `-deprecated` modules in project.
## 0.5.0
##### Picture-provider
* ANDDEP-687 Changed "androidx.exifinterface:exifinterface" dependency from "api" to "implementation" type
* ANDDEP-729 Removed camera-view dependency from picture-provider-sample
* ANDDEP-1148 Adding support for android 29 and higher. Added the ability to customize the file location for saving photos from an external camera application
## 0.3.0
##### Picture-provider
* ANDDEP-235 - refactoring and adding functional:
* In addition to going directly to the gallery, it became possible to select a file and file manager
* Added method for pre-saving image
* Added wrapper class over Uri
* Fix obtaining permission to access the camera
* ANDDEP-286 - Fix working with remote images in PictureProvider
* Replaced return types Observable -> Single
## 0.2.2
##### Picture-provider
* Added a method in PictureProvider that returns the Uri (instead of the real path) of the selected file
## 0.2.1
##### Picture-provider
* ANDDEP-199 PictureProvider go directly to the gallery (changed route, removed Chooser)