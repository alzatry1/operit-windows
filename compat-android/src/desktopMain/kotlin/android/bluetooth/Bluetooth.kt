package android.bluetooth

import android.content.Context
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * android.bluetooth 轻 stub（P3-B2 新增）。
 * 桌面无蓝牙栈：isEnabled=false、connectGatt 返回空 BluetoothGatt、回调全部不触发。
 */

/** android.bluetooth.BluetoothAdapter。 */
class BluetoothAdapter private constructor() {

    val isEnabled: Boolean get() = false
    val name: String? get() = null
    val address: String get() = "02:00:00:00:00:00"
    val isDiscovering: Boolean get() = false
    val bondedDevices: Set<BluetoothDevice> get() = emptySet()
    val state: Int get() = STATE_OFF
    val bluetoothLeScanner: android.bluetooth.le.BluetoothLeScanner? get() = null
    val isLe2MPhySupported: Boolean get() = false
    val isMultipleAdvertisementSupported: Boolean get() = false

    fun enable(): Boolean = false
    fun disable(): Boolean = false
    fun getRemoteDevice(address: String): BluetoothDevice = BluetoothDevice(address)
    fun getRemoteDevice(address: ByteArray): BluetoothDevice = BluetoothDevice(address.joinToString(":") { "%02X".format(it) })
    fun startDiscovery(): Boolean = false
    fun cancelDiscovery(): Boolean = false
    fun listenUsingRfcommWithServiceRecord(name: String?, uuid: UUID?): BluetoothServerSocket = BluetoothServerSocket()
    fun listenUsingInsecureRfcommWithServiceRecord(name: String?, uuid: UUID?): BluetoothServerSocket = BluetoothServerSocket()
    fun getProfileProxy(context: Context?, listener: BluetoothProfile.ServiceListener?, profile: Int): Boolean = false
    fun closeProfileProxy(profile: Int, proxy: Any?) {}
    fun setName(name: String?): Boolean = false
    fun getScanMode(): Int = SCAN_MODE_NONE

    companion object {
        const val ERROR = Int.MIN_VALUE
        const val STATE_OFF = 10
        const val STATE_TURNING_ON = 11
        const val STATE_ON = 12
        const val STATE_TURNING_OFF = 13

        const val SCAN_MODE_NONE = 20
        const val SCAN_MODE_CONNECTABLE = 21
        const val SCAN_MODE_CONNECTABLE_DISCOVERABLE = 23

        const val ACTION_STATE_CHANGED = "android.bluetooth.adapter.action.STATE_CHANGED"
        const val ACTION_DISCOVERY_STARTED = "android.bluetooth.adapter.action.DISCOVERY_STARTED"
        const val ACTION_DISCOVERY_FINISHED = "android.bluetooth.adapter.action.DISCOVERY_FINISHED"
        const val ACTION_REQUEST_ENABLE = "android.bluetooth.adapter.action.REQUEST_ENABLE"
        const val ACTION_REQUEST_DISCOVERABLE = "android.bluetooth.adapter.action.REQUEST_DISCOVERABLE"
        const val ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.adapter.action.CONNECTION_STATE_CHANGED"
        const val EXTRA_STATE = "android.bluetooth.adapter.extra.STATE"
        const val EXTRA_PREVIOUS_STATE = "android.bluetooth.adapter.extra.PREVIOUS_STATE"
        const val EXTRA_CONNECTION_STATE = "android.bluetooth.adapter.extra.CONNECTION_STATE"
        const val EXTRA_DISCOVERABLE_DURATION = "android.bluetooth.adapter.extra.DISCOVERABLE_DURATION"

        private val default by lazy { BluetoothAdapter() }

        @Deprecated("Use BluetoothManager")
        @JvmStatic
        fun getDefaultAdapter(): BluetoothAdapter = default

        @JvmStatic
        fun checkBluetoothAddress(address: String?): Boolean =
            address != null && Regex("^([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}$").matches(address)
    }
}

/** android.bluetooth.BluetoothDevice。 */
class BluetoothDevice(val address: String = "00:00:00:00:00:00") {

    val name: String? get() = null
    val bondState: Int get() = BOND_NONE
    val type: Int get() = DEVICE_TYPE_UNKNOWN
    val alias: String? get() = null

    fun createBond(): Boolean = false

    fun connectGatt(context: Context?, autoConnect: Boolean, callback: BluetoothGattCallback?): BluetoothGatt =
        BluetoothGatt(callback)

    fun connectGatt(context: Context?, autoConnect: Boolean, callback: BluetoothGattCallback?, transport: Int): BluetoothGatt =
        BluetoothGatt(callback)

    fun connectGatt(context: Context?, autoConnect: Boolean, callback: BluetoothGattCallback?, transport: Int, phy: Int): BluetoothGatt =
        BluetoothGatt(callback)

    fun fetchUuidsWithSdp(): Boolean = false
    fun setPin(pin: ByteArray?): Boolean = false

    override fun toString(): String = address

    companion object {
        const val BOND_NONE = 10
        const val BOND_BONDING = 11
        const val BOND_BONDED = 12

        const val DEVICE_TYPE_UNKNOWN = 0
        const val DEVICE_TYPE_CLASSIC = 1
        const val DEVICE_TYPE_LE = 2
        const val DEVICE_TYPE_DUAL = 3

        const val TRANSPORT_AUTO = 0
        const val TRANSPORT_BREDR = 1
        const val TRANSPORT_LE = 2

        const val ACTION_FOUND = "android.bluetooth.device.action.FOUND"
        const val ACTION_BOND_STATE_CHANGED = "android.bluetooth.device.action.BOND_STATE_CHANGED"
        const val ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST"
        const val EXTRA_DEVICE = "android.bluetooth.device.extra.DEVICE"
        const val EXTRA_NAME = "android.bluetooth.device.extra.NAME"
        const val EXTRA_BOND_STATE = "android.bluetooth.device.extra.BOND_STATE"
        const val EXTRA_RSSI = "android.bluetooth.device.extra.RSSI"
    }
}

/** android.bluetooth.BluetoothManager。 */
class BluetoothManager {
    val adapter: BluetoothAdapter get() = BluetoothAdapter.getDefaultAdapter()
    fun getConnectedDevices(profile: Int): List<BluetoothDevice> = emptyList()
    fun getDevicesMatchingConnectionStates(profile: Int, states: IntArray?): List<BluetoothDevice> = emptyList()
}

/** android.bluetooth.BluetoothProfile。 */
interface BluetoothProfile {
    interface ServiceListener {
        fun onServiceConnected(profile: Int, proxy: Any?)
        fun onServiceDisconnected(profile: Int)
    }

    companion object {
        const val HEADSET = 1
        const val A2DP = 2
        const val HEALTH = 3
        const val HID_HOST = 4
        const val PAN = 5
        const val PBAP = 6
        const val GATT = 7
        const val GATT_SERVER = 8
        const val MAP = 9
        const val SAP = 10
        const val A2DP_SINK = 11
        const val AVRCP_CONTROLLER = 12

        const val STATE_DISCONNECTED = 0
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = 2
        const val STATE_DISCONNECTING = 3

        const val EXTRA_PREVIOUS_STATE = "android.bluetooth.profile.extra.PREVIOUS_STATE"
        const val EXTRA_STATE = "android.bluetooth.profile.extra.STATE"
    }
}

/** android.bluetooth.BluetoothSocket。 */
open class BluetoothSocket {
    open val isConnected: Boolean = false
    open val remoteDevice: BluetoothDevice? = null
    open val inputStream: InputStream = ByteArrayInputStream(ByteArray(0))
    open val outputStream: OutputStream = ByteArrayOutputStream()

    open fun connect() {}
    open fun close() {}
}

/** android.bluetooth.BluetoothServerSocket。 */
open class BluetoothServerSocket {
    open fun accept(): BluetoothSocket = BluetoothSocket()
    open fun accept(timeout: Int): BluetoothSocket = BluetoothSocket()
    open fun close() {}
}

/** android.bluetooth.BluetoothGatt。 */
open class BluetoothGatt internal constructor(private val callback: BluetoothGattCallback?) {

    open fun connect(): Boolean = false
    open fun disconnect() {}
    open fun close() {}
    open fun discoverServices(): Boolean = false
    open val services: List<BluetoothGattService> = emptyList()

    open fun getService(uuid: UUID?): BluetoothGattService? = null
    open fun readCharacteristic(characteristic: BluetoothGattCharacteristic?): Boolean = false
    open fun writeCharacteristic(characteristic: BluetoothGattCharacteristic?): Boolean = false
    open fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic?, enable: Boolean): Boolean = false
    open fun readDescriptor(descriptor: BluetoothGattDescriptor?): Boolean = false
    open fun writeDescriptor(descriptor: BluetoothGattDescriptor?): Boolean = false
    open fun readRemoteRssi(): Boolean = false
    open fun requestMtu(mtu: Int): Boolean = false
    open fun requestConnectionPriority(connectionPriority: Int): Boolean = false
    open val device: BluetoothDevice? = null

    companion object {
        const val GATT_SUCCESS = 0
        const val GATT_FAILURE = 257
        const val GATT_READ_NOT_PERMITTED = 2
        const val GATT_WRITE_NOT_PERMITTED = 3
        const val GATT_INSUFFICIENT_AUTHENTICATION = 5
        const val GATT_REQUEST_NOT_SUPPORTED = 6
        const val GATT_INVALID_OFFSET = 7
        const val GATT_INSUFFICIENT_AUTHORIZATION = 8
        const val GATT_INVALID_ATTRIBUTE_LENGTH = 13
        const val GATT_INSUFFICIENT_ENCRYPTION = 15
        const val GATT_CONNECTION_CONGESTED = 143

        const val CONNECTION_PRIORITY_BALANCED = 0
        const val CONNECTION_PRIORITY_HIGH = 1
        const val CONNECTION_PRIORITY_LOW_POWER = 2
    }
}

/** android.bluetooth.BluetoothGattCharacteristic。 */
open class BluetoothGattCharacteristic(
    val uuid: UUID? = null,
    val properties: Int = 0,
    val permissions: Int = 0,
) {
    open var value: ByteArray? = null
    open var writeType: Int = WRITE_TYPE_DEFAULT
    open val instanceId: Int = 0
    open val service: BluetoothGattService? = null
    open val descriptors: List<BluetoothGattDescriptor> = emptyList()

    open fun getDescriptor(uuid: UUID?): BluetoothGattDescriptor? = null
    open fun addDescriptor(descriptor: BluetoothGattDescriptor): Boolean = false
    open fun getIntValue(formatType: Int, offset: Int): Int? = null
    open fun getFloatValue(formatType: Int, offset: Int): Float? = null
    open fun getStringValue(offset: Int): String? = null

    companion object {
        const val PROPERTY_BROADCAST = 0x01
        const val PROPERTY_READ = 0x02
        const val PROPERTY_WRITE_NO_RESPONSE = 0x04
        const val PROPERTY_WRITE = 0x08
        const val PROPERTY_NOTIFY = 0x10
        const val PROPERTY_INDICATE = 0x20
        const val PROPERTY_SIGNED_WRITE = 0x40
        const val PROPERTY_EXTENDED_PROPS = 0x80

        const val PERMISSION_READ = 0x01
        const val PERMISSION_READ_ENCRYPTED = 0x02
        const val PERMISSION_READ_ENCRYPTED_MITM = 0x04
        const val PERMISSION_WRITE = 0x10
        const val PERMISSION_WRITE_ENCRYPTED = 0x20
        const val PERMISSION_WRITE_ENCRYPTED_MITM = 0x40
        const val PERMISSION_WRITE_SIGNED = 0x80
        const val PERMISSION_WRITE_SIGNED_MITM = 0x100

        const val WRITE_TYPE_DEFAULT = 0x02
        const val WRITE_TYPE_NO_RESPONSE = 0x01
        const val WRITE_TYPE_SIGNED = 0x04

        const val FORMAT_UINT8 = 0x11
        const val FORMAT_UINT16 = 0x12
        const val FORMAT_UINT32 = 0x14
        const val FORMAT_SINT8 = 0x21
        const val FORMAT_SINT16 = 0x22
        const val FORMAT_SINT32 = 0x24
        const val FORMAT_SFLOAT = 0x32
        const val FORMAT_FLOAT = 0x34
    }
}

/** android.bluetooth.BluetoothGattService。 */
open class BluetoothGattService(
    val uuid: UUID? = null,
    val serviceType: Int = SERVICE_TYPE_PRIMARY,
) {
    open val type: Int get() = serviceType
    open val instanceId: Int = 0
    open val characteristics: List<BluetoothGattCharacteristic> = emptyList()
    open val includedServices: List<BluetoothGattService> = emptyList()

    open fun getCharacteristic(uuid: UUID?): BluetoothGattCharacteristic? = null
    open fun addCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean = false
    open fun addService(service: BluetoothGattService): Boolean = false

    companion object {
        const val SERVICE_TYPE_PRIMARY = 0
        const val SERVICE_TYPE_SECONDARY = 1
    }
}

/** android.bluetooth.BluetoothGattDescriptor。 */
open class BluetoothGattDescriptor(
    val uuid: UUID? = null,
    val permissions: Int = 0,
) {
    open var value: ByteArray? = null
    open val characteristic: BluetoothGattCharacteristic? = null

    companion object {
        const val PERMISSION_READ = 0x01
        const val PERMISSION_WRITE = 0x10
        const val PERMISSION_READ_ENCRYPTED = 0x02
        const val PERMISSION_WRITE_ENCRYPTED = 0x20

        @JvmField val ENABLE_NOTIFICATION_VALUE: ByteArray = byteArrayOf(0x01, 0x00)
        @JvmField val ENABLE_INDICATION_VALUE: ByteArray = byteArrayOf(0x02, 0x00)
        @JvmField val DISABLE_NOTIFICATION_VALUE: ByteArray = byteArrayOf(0x00, 0x00)
    }
}

/** android.bluetooth.BluetoothGattCallback 已迁移至 Java 版（java/android/bluetooth/BluetoothGattCallback.java）。——Nova 注 */
