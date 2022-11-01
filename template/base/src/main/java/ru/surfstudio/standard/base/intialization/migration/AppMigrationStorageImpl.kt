package ru.surfstudio.standard.base.intialization.migration

import ru.surfstudio.android.app.migration.AppMigration
import ru.surfstudio.android.app.migration.AppMigrationStorage
import ru.surfstudio.android.logger.Logger

/**
 * хранилище [AppMigration]
 */
class AppMigrationStorageImpl : AppMigrationStorage {

    override fun getMigrations(): Array<AppMigration> {
        return MIGRATIONS
    }

    companion object {

        private const val MIGRATION_FOR_EACH = -1L
        private val MIGRATIONS = arrayOf(
                //вызывается для каждого обновления
                object : AppMigration(MIGRATION_FOR_EACH) {

                    override fun execute(oldVer: Long, newVer: Long) {
                        Logger.d("Executing app migration for each update")
                        apply() //нет проверки версий
                    }

                    override fun apply() {
                        //здесь должен быть код миграции, который будет вызываться для каждого обновления
                    }
                },

                object : AppMigration(2L) {
                    override fun apply() {
                        //здесь должен быть код миграции на версию 2
                    }
                })
    }
}
