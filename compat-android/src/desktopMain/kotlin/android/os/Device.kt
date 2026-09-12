package android.os

import android.util.Log

/** android.os.PowerManager + WakeLock。 */
class PowerManager {
    companion object {
        const val PARTIAL_WAKE_LOCK = 0x00000001
        const val SCREEN_DIM_WAKE_LOCK = 0x00000006
        const val SCREEN_BRIGHT_WAKE_LOCK = 0x0000000a
        const val FULL_WAKE_LOCK = 0x0000001a
        const val PROXIMITY_SCREEN_OFF_WAKE_LOCK = 0x00000020
        const val DOZE_WAKE_LOCK = 0x00000080
        const val DRAW_WAKE_LOCK = 0x00000040
        const val ACQUIRE_CAUSES_WAKEUP = 0x10000000
        const val ON_AFTER_RELEASE = 0x20000000
        const val RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY = 0x00000001
        const val WAKE_LOCK_LEVEL_MASK = 0x0000ffff
        const val ACTION_POWER_SAVE_MODE_CHANGED = "android.os.action.POWER_SAVE_MODE_CHANGED"
        const val ACTION_DEVICE_IDLE_MODE_CHANGED = "android.os.action.DEVICE_IDLE_MODE_CHANGED"
        const val ACTION_LIGHT_DEVICE_IDLE_MODE_CHANGED = "android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED"
        const val ACTION_LOW_POWER_STANDBY_ENABLED_CHANGED = "android.os.action.LOW_POWER_STANDBY_ENABLED_CHANGED"
        const val LOCATION_MODE_NO_CHANGE = 0
        const val BRIGHTNESS_ON = 255
        const val BRIGHTNESS_OFF = 0
    }

    inner class WakeLock(private val levelAndFlags: Int, private val tag: String?) {
        private var held = false
        private var refCounted = true
        private var count = 0

        @Synchronized
        fun acquire() {
            if (!refCounted || count++ == 0) held = true
        }

        @Synchronized
        fun acquire(timeout: Long) {
            acquire()
            Log.d("PowerManager", "WakeLock($tag) acquire($timeout) — 桌面忽略超时")
        }

        @Synchronized
        fun release() { release(0) }

        @Synchronized
        fun release(flags: Int) {
            if (!refCounted || --count <= 0) { held = false; count = 0.coerceAtLeast(count) }
        }

        /** WakeLock.isHeld（真实 Android 是 is 前缀 Java 方法，映射为属性；app 用 wakeLock?.isHeld）。——Nova 注 */
        val isHeld: Boolean @Synchronized get() = held
        fun setReferenceCounted(value: Boolean) { refCounted = value }
        fun setWorkSource(ws: Any?) {}
        override fun toString(): String = "WakeLock{$tag, held=$held}"
    }

    fun newWakeLock(levelAndFlags: Int, tag: String): WakeLock = WakeLock(levelAndFlags, tag)
    fun isInteractive(): Boolean = true
    @Deprecated("deprecated") fun isScreenOn(): Boolean = true
    fun isPowerSaveMode(): Boolean = false
    fun isDeviceIdleMode(): Boolean = false
    fun isLowPowerStandbyEnabled(): Boolean = false
    fun isIgnoringBatteryOptimizations(packageName: String?): Boolean = true
    fun isWakeLockLevelSupported(level: Int): Boolean = true
    fun isSustainedPerformanceModeSupported(): Boolean = false
    fun isRebootingUserspaceSupported(): Boolean = false
    fun getLocationPowerSaveMode(): Int = LOCATION_MODE_NO_CHANGE
    fun reboot(reason: String?) { Log.w("PowerManager", "reboot($reason) ignored on desktop") }
    fun goToSleep(timeMs: Long) { Log.w("PowerManager", "goToSleep ignored on desktop") }
    fun wakeUp(timeMs: Long) {}
    fun nap(timeMs: Long) {}
    fun userActivity(whenMs: Long, noChangeLights: Boolean) {}
}

/** android.os.VibrationEffect。 */
class VibrationEffect private constructor(
    private val description: String,
) : Parcelable {
    override fun describeContents(): Int = 0
    override fun toString(): String = "VibrationEffect($description)"

    companion object {
        const val DEFAULT_AMPLITUDE = -1
        const val EFFECT_CLICK = 0
        const val EFFECT_DOUBLE_CLICK = 1
        const val EFFECT_TICK = 2
        const val EFFECT_HEAVY_CLICK = 5
        const val EFFECT_TEXTURE_TICK = 21
        const val PARCELABLE_WRITE_RETURN_VALUE = 1

        @JvmStatic fun createOneShot(milliseconds: Long, amplitude: Int): VibrationEffect =
            VibrationEffect("oneshot(${milliseconds}ms,amp=$amplitude)")

        @JvmStatic fun createWaveform(timings: LongArray, repeat: Int): VibrationEffect =
            VibrationEffect("waveform(${timings.size},repeat=$repeat)")

        @JvmStatic fun createWaveform(timings: LongArray, amplitudes: IntArray, repeat: Int): VibrationEffect =
            VibrationEffect("waveform(${timings.size},amps,repeat=$repeat)")

        @JvmStatic fun createPredefined(effectId: Int): VibrationEffect = VibrationEffect("predefined($effectId)")
    }
}

/** android.os.Vibrator：桌面无马达，记录日志。 */
open class Vibrator {
    open fun cancel() {}
    open fun vibrate(milliseconds: Long) { Log.v("Vibrator", "vibrate(${milliseconds}ms)") }
    open fun vibrate(pattern: LongArray, repeat: Int) { Log.v("Vibrator", "vibrate(pattern,repeat=$repeat)") }
    open fun vibrate(effect: VibrationEffect) { Log.v("Vibrator", "vibrate($effect)") }
    open fun vibrate(effect: VibrationEffect, attrs: Any?) { vibrate(effect) }
    open fun hasVibrator(): Boolean = false
    open fun hasAmplitudeControl(): Boolean = false
    open fun areAllPrimitivesSupported(vararg primitiveIds: Int): Boolean = false
    open fun areEffectsSupported(vararg effectIds: Int): IntArray = IntArray(effectIds.size) { 0 }
    open fun areAllEffectsSupported(vararg effectIds: Int): Int = 0
}

/** android.os.BatteryManager：常量 + 固定满电状态。 */
open class BatteryManager {
    companion object {
        const val ACTION_BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED"
        const val ACTION_CHARGING = "android.os.action.CHARGING"
        const val ACTION_DISCHARGING = "android.os.action.DISCHARGING"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_SCALE = "scale"
        const val EXTRA_STATUS = "status"
        const val EXTRA_PLUGGED = "plugged"
        const val EXTRA_VOLTAGE = "voltage"
        const val EXTRA_TEMPERATURE = "temperature"
        const val EXTRA_TECHNOLOGY = "technology"
        const val EXTRA_HEALTH = "health"
        const val EXTRA_ICON_SMALL = "icon-small"
        const val EXTRA_PRESENT = "present"
        const val EXTRA_MAX_CHARGING_CURRENT = "max_charging_current"
        const val EXTRA_MAX_CHARGING_VOLTAGE = "max_charging_voltage"
        const val EXTRA_CHARGE_COUNTER = "charge_counter"
        const val BATTERY_STATUS_UNKNOWN = 1
        const val BATTERY_STATUS_CHARGING = 2
        const val BATTERY_STATUS_DISCHARGING = 3
        const val BATTERY_STATUS_NOT_CHARGING = 4
        const val BATTERY_STATUS_FULL = 5
        const val BATTERY_HEALTH_UNKNOWN = 1
        const val BATTERY_HEALTH_GOOD = 2
        const val BATTERY_HEALTH_OVERHEAT = 3
        const val BATTERY_HEALTH_DEAD = 4
        const val BATTERY_HEALTH_OVER_VOLTAGE = 5
        const val BATTERY_HEALTH_UNSPECIFIED_FAILURE = 6
        const val BATTERY_HEALTH_COLD = 7
        const val BATTERY_PLUGGED_AC = 1
        const val BATTERY_PLUGGED_USB = 2
        const val BATTERY_PLUGGED_WIRELESS = 4
        const val BATTERY_PLUGGED_DOCK = 8
        const val BATTERY_PLUGGED_ANY = 0xF
        const val BATTERY_PROPERTY_CAPACITY = 4
        const val BATTERY_PROPERTY_CHARGE_COUNTER = 1
        const val BATTERY_PROPERTY_CURRENT_AVERAGE = 3
        const val BATTERY_PROPERTY_CURRENT_NOW = 2
        const val BATTERY_PROPERTY_ENERGY_COUNTER = 5
        const val BATTERY_PROPERTY_STATUS = 6
    }

    open fun getIntProperty(id: Int): Int = when (id) {
        BATTERY_PROPERTY_CAPACITY -> 100
        else -> 0
    }

    open fun getLongProperty(id: Int): Long = getIntProperty(id).toLong()
    open fun isCharging(): Boolean = true
    open fun computeChargeTimeRemaining(): Long = -1L
}
