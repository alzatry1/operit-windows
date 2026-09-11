package androidx.security.crypto

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * androidx.security.crypto.MasterKey 桌面 stub。
 * 桌面无 AndroidKeyStore；仅作为 keyAlias 容器。
 */
class MasterKey private constructor(
    val keyAlias: String,
) {
    enum class KeyScheme { AES256_GCM }

    class Builder(private val context: Context) {
        private var keyAlias: String = DEFAULT_MASTER_KEY_ALIAS
        private var keyScheme: KeyScheme = KeyScheme.AES256_GCM

        fun setKeyAlias(keyAlias: String): Builder = apply { this.keyAlias = keyAlias }
        fun setKeyScheme(keyScheme: KeyScheme): Builder = apply { this.keyScheme = keyScheme }
        fun build(): MasterKey = MasterKey(keyAlias)
    }

    companion object {
        const val DEFAULT_MASTER_KEY_ALIAS = "_androidx_security_master_key_"

        @JvmStatic
        fun getOrCreate(context: Context): MasterKey = Builder(context).build()

        @JvmStatic
        fun getOrCreate(context: Context, keyAlias: String): MasterKey =
            Builder(context).setKeyAlias(keyAlias).build()
    }
}

/**
 * androidx.security.crypto.EncryptedSharedPreferences 桌面版。
 * 桌面无 AndroidKeyStore，直接返回 Properties 持久化实现并打 warn。
 */
object EncryptedSharedPreferences {

    enum class PrefKeyEncryptionScheme { AES256_SIV }
    enum class PrefValueEncryptionScheme { AES256_GCM }

    @JvmStatic
    fun create(
        context: Context,
        fileName: String,
        masterKey: MasterKey,
        keyEncryptionScheme: PrefKeyEncryptionScheme,
        valueEncryptionScheme: PrefValueEncryptionScheme,
    ): SharedPreferences {
        Log.w("EncryptedSharedPreferences", "桌面版未加密：$fileName 直接落盘为 Properties")
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE)
    }

    @JvmStatic
    fun create(
        fileName: String,
        masterKeyAlias: String,
        context: Context,
        keyEncryptionScheme: PrefKeyEncryptionScheme,
        valueEncryptionScheme: PrefValueEncryptionScheme,
    ): SharedPreferences = create(
        context, fileName, MasterKey.Builder(context).setKeyAlias(masterKeyAlias).build(),
        keyEncryptionScheme, valueEncryptionScheme,
    )
}
