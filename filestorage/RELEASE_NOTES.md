# Filestorage Release Notes

- [0.5.0-alpha](#050-alpha)
- [0.3.0](#030)

## 0.5.0-alpha
##### Filestorage
* ANDDEP-687 Changed "com.google.code.gson:gson" dependency from "api" to "implementation" type
* Add JsonTypeConverter
## 0.3.0
##### Filestorage
* Added utilities to get different folders [`AppDirectoriesProvider`](lib-filestorage/src/main/java/ru/surfstudio/android/filestorage/utils/AppDirectoriesProvider.kt)
* Change `CacheConstant`. Now subdivided into:
  * `INTERNAL_CACHE_DIR_DAGGER_NAME == NO_BACKUP_STORAGE_DIR_NAME, BACKUP_STORAGE_DIR_NAME`
  * `EXTERNAL_CACHE_DIR_DAGGER_NAME == CACHE_DIR_NAME`