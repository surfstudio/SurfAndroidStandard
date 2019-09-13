package ru.surfstudio.android.app.migration.sample.app.initialization.migration

import ru.surfstudio.android.app.migration.AppMigration
import ru.surfstudio.android.app.migration.AppMigrationStorage
import ru.surfstudio.android.logger.Logger

class AppMigrationStorageImpl : AppMigrationStorage {

    override fun getMigrations(): Array<AppMigration> = MIGRATIONS

    companion object {

        private const val MIGRATION_FOR_EACH = -1

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