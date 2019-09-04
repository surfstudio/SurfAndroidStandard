[TOC]
# Filestorage Release Notes
## 0.5.0-alpha.2
##### Filestorage
* TODO
## 0.3.0
##### Filestorage
* Added utilities to get different folders [`AppDirectoriesProvider`] (filestorage / src / main / java / ru / surfstudio / android / filestorage / utils / AppDirectoriesProvider.kt)
* Change `CacheConstant`. Now subdivided into:
     * `INTERNAL_CACHE_DIR_DAGGER_NAME == NO_BACKUP_STORAGE_DIR_NAME, BACKUP_STORAGE_DIR_NAME`
     * `EXTERNAL_CACHE_DIR_DAGGER_NAME == CACHE_DIR_NAME`