package android.bluetooth;

/**
 * android.bluetooth.BluetoothGattCallback 的 Java 版（平台类型）。
 * app 的蓝牙回调 override 可空/非空混用，Java 平台类型让两种都成立。——Nova 注
 */
public class BluetoothGattCallback {
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {}
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {}
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {}
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value, int status) {}
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {}
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {}
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] value) {}
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {}
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, byte[] value, int status) {}
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {}
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {}
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {}
    public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {}
    public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {}
    public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {}
    public void onServiceChanged(BluetoothGatt gatt) {}
}
