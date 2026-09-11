package android.content;

import android.os.IBinder;

/**
 * android.content.ServiceConnection 的 Java 版（平台类型）。
 * app 里 onServiceConnected/onServiceDisconnected 的可空与非空 override 混用，
 * Kotlin 接口无法满足两种，Java 平台类型可以。——Nova 注
 */
public interface ServiceConnection {
    void onServiceConnected(ComponentName name, IBinder service);
    void onServiceDisconnected(ComponentName name);
    default void onBindingDied(ComponentName name) {}
    default void onNullBinding(ComponentName name) {}
}
