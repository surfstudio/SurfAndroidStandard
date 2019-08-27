[TOC]
# Picture-provider Release Notes
## 0.4.0-alpha.0
##### Picture-provider
* TODO
## 0.4.0
##### Picture-provider
* TODO
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