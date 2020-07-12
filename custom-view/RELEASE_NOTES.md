# Custom-view Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.4.0](#040)
- [0.2.2](#022)
- [0.2.1](#021)
- [0.2.0](#020)

## 0.5.0-alpha
##### Custom-view
* ANDDEP-998 ShadowLayout was moved to the custom-view module
* ANDDEP-687 Added "com.google.android.material:material" dependency with "implementation" type
* ANDDEP-468 Added functionality for setting title/subtitle drawables programmatically
* ANDDEP-1048 Fixing wrong docs links and docs structure
* Fixed lag when one type of state showed twice
* ANDDEP-1090 Fixed default ellipsize mode on TitleSubtitleView
## 0.4.0
##### Custom-view
* Fixed incorrect behavior of MaterialProgressBar on Android version 5 and below
* Added BottomSheetView
## 0.2.2
##### Custom-view
* Fixed package name for TitleSubtitleView
## 0.2.1
##### Custom-view
* TitleSubtitleView - disabling Clickable when installing an empty listener, minor edits
* ANDDEP-50 fixed state switching delays with in PlaceHolderView
* ANDDEP-138 Improvements to PlaceHolderView
* Added a set of loading indicators
* Added a new state LoadState "No Internet";
* Added attributes `pvProgressBarWidth` and` pvProgressBarHeight` - through them you can set the width and height of the progress indicator, respectively;
* Added attributes `pvOpaqueBackground` and` pvTransparentBackground` - through them you can set drawable-resources as a background;
* There is support for 28 custom progress indicators `pvProgressBarType`;
* The `pvProgressBarColor` attribute now accepts not only links to color resources, but also codes like` # 00FFAA`;
* Edit gravity signatures
* Added attributes `pvTitleLineSpacingExtra` and` pvSubtitleLineSpacingExtra` to change the height of the title bar
* Fix problems with interception of gestures on the placeholder
## 0.2.0
##### Custom-view
* ANDDEP-107 Fix PlaceholderView