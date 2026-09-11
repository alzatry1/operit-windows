package androidx.datastore.preferences

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toOkioPath

/**
 * datastore-preferences Android 工件特有的 Context receiver 版 preferencesDataStore。
 * KMP 版（datastore-preferences-core）不提供此重载，这里按原始签名补齐。
 * 落盘位置与 Android 一致：context.filesDir/datastore/<name>.preferences_pb。
 * 同名 DataStore 全局单例（进程内第二次取同一名称返回同一实例，避免多实例冲突）。
 */
fun preferencesDataStore(
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { emptyList() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): ReadOnlyProperty<Context, DataStore<Preferences>> =
    PreferencesDataStoreContextDelegate(name, corruptionHandler, produceMigrations, scope)

private class PreferencesDataStoreContextDelegate(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) : ReadOnlyProperty<Context, DataStore<Preferences>> {
    override fun getValue(thisRef: Context, property: KProperty<*>): DataStore<Preferences> =
        PreferenceDataStoreSingletons.get(name, thisRef, corruptionHandler, produceMigrations, scope)
}

private object PreferenceDataStoreSingletons {
    private val cache = ConcurrentHashMap<String, DataStore<Preferences>>()

    fun get(
        name: String,
        context: Context,
        corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
        produceMigrations: (Context) -> List<DataMigration<Preferences>>,
        scope: CoroutineScope
    ): DataStore<Preferences> {
        val appContext = context.applicationContext
        return cache.getOrPut(name) {
            PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = corruptionHandler,
                migrations = produceMigrations(appContext),
                scope = scope
            ) {
                File(appContext.filesDir, "datastore/$name.preferences_pb").toOkioPath()
            }
        }
    }
}
