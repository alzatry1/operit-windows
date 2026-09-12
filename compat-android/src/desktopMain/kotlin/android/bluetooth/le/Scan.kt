package android.bluetooth.le

import android.bluetooth.BluetoothDevice

/**
 * android.bluetooth.le 轻 stub（P3-B2 新增）。
 */

/** android.bluetooth.le.ScanResult。 */
open class ScanResult(
    val device: BluetoothDevice? = null,
    val rssi: Int = 0,
    val timestampNanos: Long = 0,
) {
    open val scanRecord: ScanRecord? = null
    open val advertisingSid: Int = 255
    open val txPower: Int = 127
    open val periodicAdvertisingInterval: Int = 0
    open val primaryPhy: Int = 1
    open val secondaryPhy: Int = 0

    override fun toString(): String = "ScanResult(device=$device, rssi=$rssi)"
}

/** android.bluetooth.le.ScanRecord。 */
open class ScanRecord {
    open val deviceName: String? = null
    open val txPowerLevel: Int = Int.MIN_VALUE
    open val advertiseFlags: Int = -1
    open val bytes: ByteArray = ByteArray(0)

    open fun getServiceData(): Map<Any?, ByteArray> = emptyMap()
    open fun getManufacturerSpecificData(): Map<Int, ByteArray> = emptyMap()
    open fun getServiceUuids(): List<Any?>? = null
}

/** android.bluetooth.le.ScanCallback。 */
open class ScanCallback {
    open fun onScanResult(callbackType: Int, result: ScanResult) {}
    open fun onBatchScanResults(results: MutableList<ScanResult>) {}
    open fun onScanFailed(errorCode: Int) {}

    companion object {
        const val SCAN_FAILED_ALREADY_STARTED = 1
        const val SCAN_FAILED_APPLICATION_REGISTRATION_FAILED = 2
        const val SCAN_FAILED_INTERNAL_ERROR = 3
        const val SCAN_FAILED_FEATURE_UNSUPPORTED = 4
        const val SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES = 5
        const val SCAN_FAILED_SCANNING_TOO_FREQUENTLY = 6
    }
}

/** android.bluetooth.le.ScanSettings。 */
class ScanSettings private constructor() {
    class Builder {
        fun setScanMode(scanMode: Int): Builder = this
        fun setCallbackType(callbackType: Int): Builder = this
        fun setReportDelay(reportDelayMillis: Long): Builder = this
        fun setNumOfMatches(numOfMatches: Int): Builder = this
        fun build(): ScanSettings = ScanSettings()
    }

    companion object {
        const val SCAN_MODE_LOW_POWER = 0
        const val SCAN_MODE_BALANCED = 1
        const val SCAN_MODE_LOW_LATENCY = 2
        const val CALLBACK_TYPE_ALL_MATCHES = 1
        const val CALLBACK_TYPE_FIRST_MATCH = 2
        const val CALLBACK_TYPE_MATCH_LOST = 4
    }
}

/** android.bluetooth.le.ScanFilter。 */
class ScanFilter private constructor() {
    class Builder {
        fun setDeviceAddress(deviceAddress: String?): Builder = this
        fun setDeviceName(deviceName: String?): Builder = this
        fun setServiceUuid(serviceUuid: Any?): Builder = this
        fun build(): ScanFilter = ScanFilter()
    }
}

/** android.bluetooth.le.BluetoothLeScanner。 */
open class BluetoothLeScanner {
    open fun startScan(callback: ScanCallback?) {}
    open fun startScan(filters: List<ScanFilter>?, settings: ScanSettings?, callback: ScanCallback?) {}
    open fun stopScan(callback: ScanCallback?) {}
    open fun flushPendingScanResults(callback: ScanCallback?) {}
}
