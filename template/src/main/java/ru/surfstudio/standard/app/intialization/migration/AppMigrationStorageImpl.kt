package ru.surfstudio.standard.app.intialization.migration


import ru.surfstudio.android.core.app.intialization.migration.AppMigration
import ru.surfstudio.android.core.app.intialization.migration.AppMigrationStorage
import ru.surfstudio.android.core.app.log.Logger

/**
 * хранилище [AppMigration]
 */
class AppMigrationStorageImpl : AppMigrationStorage {

    override fun getMigrations(): Array<AppMigration> {
        return MIGRATIONS
    }

    companion object {

        private val MIGRATION_FOR_EACH = -1
        private val MIGRATIONS = arrayOf(
                //вызывается для каждого обновления
                object : AppMigration(MIGRATION_FOR_EACH) {

                    override fun execute(oldVer: Int, newVer: Int) {
                        Logger.d("Executing app migration for each update")
                        apply() //нет проверки версий
                    }

                    override fun apply() {
                        //здесь должен быть код миграции, который будет вызываться для каждого обновления
                    }
                },

                object : AppMigration(2) {
                    override fun apply() {
                        //здесь должен быть код миграции на версию 2
                    }
                })
    }
}
