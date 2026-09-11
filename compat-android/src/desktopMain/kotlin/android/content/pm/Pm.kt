package android.content.pm

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.ai.assistance.operit.compat.AppGlobals

/** android.content.pm.PackageItemInfo：各 info 的基类。 */
open class PackageItemInfo() {
    var name: String? = null
    var packageName: String? = null
    var labelRes: Int = 0
    var nonLocalizedLabel: CharSequence? = null
    var icon: Int = 0
    var banner: Int = 0
    var logo: Int = 0
    var metaData: android.os.Bundle? = null

    constructor(orig: PackageItemInfo) : this() {
        name = orig.name; packageName = orig.packageName; labelRes = orig.labelRes
        nonLocalizedLabel = orig.nonLocalizedLabel; icon = orig.icon; banner = orig.banner
        logo = orig.logo; metaData = orig.metaData
    }

    open fun loadLabel(pm: PackageManager): CharSequence =
        nonLocalizedLabel ?: name ?: packageName ?: ""

    open fun loadIcon(pm: PackageManager): Drawable? = null
    open fun loadBanner(pm: PackageManager): Drawable? = null
    open fun loadLogo(pm: PackageManager): Drawable? = null
    open fun loadUnbadgedIcon(pm: PackageManager): Drawable? = null
    open fun loadXmlMetaData(pm: PackageManager, name: String): Any? = null
}

/** android.content.pm.ComponentInfo。 */
open class ComponentInfo() : PackageItemInfo() {
    var applicationInfo: ApplicationInfo? = null
    var processName: String? = null
    var splitName: String? = null
    var attributionTags: Array<String>? = null
    var descriptionRes: Int = 0
    var directBootAware: Boolean = false
    var enabled: Boolean = true
    var exported: Boolean = false

    constructor(orig: ComponentInfo) : this() {
        name = orig.name; packageName = orig.packageName; labelRes = orig.labelRes
        nonLocalizedLabel = orig.nonLocalizedLabel; icon = orig.icon; banner = orig.banner
        logo = orig.logo; metaData = orig.metaData
        applicationInfo = orig.applicationInfo; processName = orig.processName
        descriptionRes = orig.descriptionRes; enabled = orig.enabled; exported = orig.exported
    }

    open fun isEnabled(): Boolean = enabled
    open fun getIconResource(): Int = icon
    open fun getLogoResource(): Int = logo
    open fun getBannerResource(): Int = banner
    fun getComponentName(): ComponentName = ComponentName(packageName ?: "", name ?: "")
}

/** android.content.pm.ApplicationInfo。 */
open class ApplicationInfo() : PackageItemInfo(), Parcelable {
    var taskAffinity: String? = null
    var permission: String? = null
    var processName: String? = null
    var className: String? = null
    var descriptionRes: Int = 0
    var theme: Int = 0
    var manageSpaceActivityName: String? = null
    var backupAgentName: String? = null
    var uiOptions: Int = 0
    var flags: Int = 0
    var requiresSmallestWidthDp: Int = 0
    var compatibleWidthLimitDp: Int = 0
    var largestWidthLimitDp: Int = 0
    var publicSourceDir: String? = null
    var sourceDir: String? = null
    var splitSourceDirs: Array<String>? = null
    var splitPublicSourceDirs: Array<String>? = null
    var splitNames: Array<String>? = null
    var sharedLibraryFiles: Array<String>? = null
    var dataDir: String = ""
    var deviceProtectedDataDir: String? = null
    var credentialProtectedDataDir: String? = null
    var nativeLibraryDir: String = ""
    var nativeLibraryRootDir: String? = null
    var nativeLibraryRootRequiresIsa: Boolean = false
    var primaryCpuAbi: String? = "x86_64"
    var secondaryCpuAbi: String? = null
    var resourceDirs: Array<String>? = null
    var overlayPaths: Array<String>? = null
    var seinfo: String? = null
    var targetSdkVersion: Int = 35
    var minSdkVersion: Int = 24
    var enabled: Boolean = true
    var enabledSetting: Int = 0
    var installLocation: Int = 0
    var networkSecurityConfigRes: Int = 0
    var uid: Int = 10000
    var category: Int = CATEGORY_UNDEFINED
    var appComponentFactory: String? = null
    var iconRes: Int = 0
    var roundIconRes: Int = 0
    var staticLocationsDir: String? = null
    var localeConfigRes: Int = 0
    var requestsRawExternalStorageAccess: Boolean = false
    var crossProfile: Boolean = false

    constructor(orig: ApplicationInfo) : this() {
        name = orig.name; packageName = orig.packageName; labelRes = orig.labelRes
        nonLocalizedLabel = orig.nonLocalizedLabel; icon = orig.icon; banner = orig.banner
        logo = orig.logo; metaData = orig.metaData
        taskAffinity = orig.taskAffinity; permission = orig.permission; processName = orig.processName
        className = orig.className; theme = orig.theme; flags = orig.flags
        publicSourceDir = orig.publicSourceDir; sourceDir = orig.sourceDir; dataDir = orig.dataDir
        targetSdkVersion = orig.targetSdkVersion; minSdkVersion = orig.minSdkVersion
        enabled = orig.enabled; uid = orig.uid
    }

    fun isPackageUnavailable(pm: PackageManager): Boolean = false
    fun usesNonSdkApi(): Boolean = false
    fun hasCode(): Boolean = flags and FLAG_HAS_CODE != 0
    fun loadDescription(pm: PackageManager): CharSequence? = null
    fun isExternal(): Boolean = false
    fun isSystemApp(): Boolean = false
    fun isUpdatedSystemApp(): Boolean = false
    fun isPrivilegedApp(): Boolean = false
    fun isInstantApp(): Boolean = false
    fun isProfileableByShell(): Boolean = true
    fun isEmbedded(): Boolean = false
    fun requestsIsolatedSplitLoading(): Boolean = false

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val FLAG_SYSTEM = 1
        const val FLAG_DEBUGGABLE = 2
        const val FLAG_HAS_CODE = 4
        const val FLAG_PERSISTENT = 8
        const val FLAG_FACTORY_TEST = 16
        const val FLAG_ALLOW_TASK_REPARENTING = 32
        const val FLAG_ALLOW_CLEAR_USER_DATA = 64
        const val FLAG_UPDATED_SYSTEM_APP = 128
        const val FLAG_TEST_ONLY = 256
        const val FLAG_SUPPORTS_SMALL_SCREENS = 512
        const val FLAG_SUPPORTS_NORMAL_SCREENS = 1024
        const val FLAG_SUPPORTS_LARGE_SCREENS = 2048
        const val FLAG_RESIZEABLE_FOR_SCREENS = 4096
        const val FLAG_SUPPORTS_SCREEN_DENSITIES = 8192
        const val FLAG_VM_SAFE_MODE = 16384
        const val FLAG_ALLOW_BACKUP = 32768
        const val FLAG_KILL_AFTER_RESTORE = 65536
        const val FLAG_RESTORE_ANY_VERSION = 131072
        const val FLAG_EXTERNAL_STORAGE = 262144
        const val FLAG_SUPPORTS_XLARGE_SCREENS = 524288
        const val FLAG_LARGE_HEAP = 1048576
        const val FLAG_STOPPED = 2097152
        const val FLAG_SUPPORTS_RTL = 4194304
        const val FLAG_INSTALLED = 8388608
        const val FLAG_IS_DATA_ONLY = 16777216
        const val FLAG_IS_GAME = 33554432
        const val FLAG_FULL_BACKUP_ONLY = 67108864
        const val FLAG_HIDDEN = 134217728
        const val FLAG_CANT_SAVE_STATE = 268435456
        const val FLAG_FORWARD_LOCK = 536870912
        const val FLAG_PRIVILEGED = 1073741824
        const val FLAG_MULTIARCH = Int.MIN_VALUE

        const val CATEGORY_UNDEFINED = -1
        const val CATEGORY_GAME = 0
        const val CATEGORY_AUDIO = 1
        const val CATEGORY_VIDEO = 2
        const val CATEGORY_IMAGE = 3
        const val CATEGORY_SOCIAL = 4
        const val CATEGORY_NEWS = 5
        const val CATEGORY_MAPS = 6
        const val CATEGORY_PRODUCTIVITY = 7
        const val CATEGORY_ACCESSIBILITY = 8

        @JvmField val CREATOR: Parcelable.Creator<ApplicationInfo> = object : Parcelable.Creator<ApplicationInfo> {
            override fun createFromParcel(source: Parcel): ApplicationInfo = ApplicationInfo()
            override fun newArray(size: Int): Array<ApplicationInfo?> = arrayOfNulls(size)
        }
    }
}

/** android.content.pm.ActivityInfo。 */
open class ActivityInfo() : ComponentInfo(), Parcelable {
    var theme: Int = 0
    var launchMode: Int = LAUNCH_MULTIPLE
    var documentLaunchMode: Int = 0
    var permission: String? = null
    var taskAffinity: String? = null
    var targetActivity: String? = null
    var parentActivityName: String? = null
    var softInputMode: Int = 0
    var flags: Int = 0
    var screenOrientation: Int = SCREEN_ORIENTATION_UNSPECIFIED
    var configChanges: Int = 0
    var windowSoftInputMode: Int = 0
    var persistableMode: Int = 0
    var maxRecents: Int = 0
    var lockTaskLaunchMode: Int = 0
    var uiOptions: Int = 0
    var colorMode: Int = 0
    var resizeMode: Int = 0
    var maxAspectRatio: Float = 0f
    var minAspectRatio: Float = 0f
    var rotationAnimation: Int = 0
    var windowLayout: Any? = null
    var requiredDisplayCategory: String? = null
    var requestedVrComponent: ComponentName? = null

    override fun getIconResource(): Int = icon
    override fun getLogoResource(): Int = logo

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val LAUNCH_MULTIPLE = 0
        const val LAUNCH_SINGLE_TOP = 1
        const val LAUNCH_SINGLE_TASK = 2
        const val LAUNCH_SINGLE_INSTANCE = 3
        const val LAUNCH_SINGLE_INSTANCE_PER_TASK = 4
        const val LAUNCH_SINGLE_TASK_PER_TASK = 5

        const val SCREEN_ORIENTATION_UNSPECIFIED = -1
        const val SCREEN_ORIENTATION_LANDSCAPE = 0
        const val SCREEN_ORIENTATION_PORTRAIT = 1
        const val SCREEN_ORIENTATION_USER = 2
        const val SCREEN_ORIENTATION_BEHIND = 3
        const val SCREEN_ORIENTATION_SENSOR = 4
        const val SCREEN_ORIENTATION_NOSENSOR = 5
        const val SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6
        const val SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7
        const val SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8
        const val SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9
        const val SCREEN_ORIENTATION_FULL_SENSOR = 10
        const val SCREEN_ORIENTATION_USER_LANDSCAPE = 11
        const val SCREEN_ORIENTATION_USER_PORTRAIT = 12
        const val SCREEN_ORIENTATION_FULL_USER = 13
        const val SCREEN_ORIENTATION_LOCKED = 14

        const val CONFIG_MCC = 0x0001
        const val CONFIG_MNC = 0x0002
        const val CONFIG_LOCALE = 0x0004
        const val CONFIG_TOUCHSCREEN = 0x0008
        const val CONFIG_KEYBOARD = 0x0010
        const val CONFIG_KEYBOARD_HIDDEN = 0x0020
        const val CONFIG_NAVIGATION = 0x0040
        const val CONFIG_ORIENTATION = 0x0080
        const val CONFIG_SCREEN_LAYOUT = 0x0100
        const val CONFIG_UI_MODE = 0x0200
        const val CONFIG_SCREEN_SIZE = 0x0400
        const val CONFIG_SMALLEST_SCREEN_SIZE = 0x0800
        const val CONFIG_DENSITY = 0x1000
        const val CONFIG_LAYOUT_DIRECTION = 0x2000
        const val CONFIG_COLOR_MODE = 0x4000
        const val CONFIG_ASSETS_PATHS = 0x8000000
        const val CONFIG_FONT_SCALE = 0x40000000
        const val CONFIG_WINDOW_CONFIGURATION = 0x20000000

        const val FLAG_ALLOW_TASK_REPARENTING = 0x00000040
        const val FLAG_ALLOW_EMBEDDED = 0x80000000.toInt()
        const val FLAG_AUTO_REMOVE_FROM_RECENTS = 0x00002000
        const val FLAG_CLEAR_TASK_ON_LAUNCH = 0x00000004
        const val FLAG_EXCLUDE_FROM_RECENTS = 0x00000008
        const val FLAG_FINISH_ON_TASK_LAUNCH = 0x00000002
        const val FLAG_IMMERSIVE = 0x00000800
        const val FLAG_MULTIPROCESS = 0x00000001
        const val FLAG_NO_HISTORY = 0x00000080
        const val FLAG_SHOW_ON_LOCK_SCREEN = 0x00000400
        const val FLAG_SINGLE_USER = 0x40000000
        const val FLAG_STATE_NOT_NEEDED = 0x00000010
        const val FLAG_VISIBLE_TO_INSTANT_APPS = 0x00100000

        const val COLOR_MODE_DEFAULT = 0
        const val COLOR_MODE_WIDE_COLOR_GAMUT = 1
        const val COLOR_MODE_HDR = 2

        const val PERSIST_ACROSS_REBOOTS = 2
        const val PERSIST_NEVER = 0
        const val PERSIST_ROOT_ONLY = 1

        @JvmField val CREATOR: Parcelable.Creator<ActivityInfo> = object : Parcelable.Creator<ActivityInfo> {
            override fun createFromParcel(source: Parcel): ActivityInfo = ActivityInfo()
            override fun newArray(size: Int): Array<ActivityInfo?> = arrayOfNulls(size)
        }
    }
}

/** android.content.pm.ServiceInfo。 */
open class ServiceInfo() : ComponentInfo(), Parcelable {
    var permission: String? = null
    var flags: Int = 0
    var foregroundServiceType: Int = FOREGROUND_SERVICE_TYPE_NONE

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val FOREGROUND_SERVICE_TYPE_NONE = 0
        const val FOREGROUND_SERVICE_TYPE_MANIFEST = -1
        const val FOREGROUND_SERVICE_TYPE_DATA_SYNC = 1
        const val FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK = 2
        const val FOREGROUND_SERVICE_TYPE_PHONE_CALL = 4
        const val FOREGROUND_SERVICE_TYPE_LOCATION = 8
        const val FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE = 16
        const val FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION = 32
        const val FOREGROUND_SERVICE_TYPE_CAMERA = 64
        const val FOREGROUND_SERVICE_TYPE_MICROPHONE = 128
        const val FOREGROUND_SERVICE_TYPE_HEALTH = 256
        const val FOREGROUND_SERVICE_TYPE_REMOTE_MESSAGING = 512
        const val FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED = 1024
        const val FOREGROUND_SERVICE_TYPE_SHORT_SERVICE = 2048
        const val FOREGROUND_SERVICE_TYPE_SPECIAL_USE = 1073741824
        const val FLAG_STOP_WITH_TASK = 1
        const val FLAG_ISOLATED_PROCESS = 2
        const val FLAG_EXTERNAL_SERVICE = 4
        const val FLAG_SINGLE_USER = 1073741824
        const val FLAG_VISIBLE_TO_INSTANT_APPS = 1048576
        const val FLAG_USE_APP_ZYGOTE = 8
    }
}

/** android.content.pm.ProviderInfo。 */
open class ProviderInfo() : ComponentInfo(), Parcelable {
    var authority: String? = null
    var readPermission: String? = null
    var writePermission: String? = null
    var grantUriPermissions: Boolean = false
    var multiprocess: Boolean = false
    var initOrder: Int = 0
    var isSyncable: Boolean = false
    var forceUriPermissions: Boolean = false

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val FLAG_SINGLE_USER = 1073741824
        const val FLAG_VISIBLE_TO_INSTANT_APPS = 1048576
    }
}

/** android.content.pm.PermissionInfo。 */
open class PermissionInfo() : PackageItemInfo(), Parcelable {
    var group: String? = null
    var descriptionRes: Int = 0
    var nonLocalizedDescription: CharSequence? = null
    var protectionLevel: Int = PROTECTION_NORMAL
    var flags: Int = 0
    var backgroundPermission: String? = null

    fun loadDescription(pm: PackageManager): CharSequence? = nonLocalizedDescription

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val PROTECTION_NORMAL = 0
        const val PROTECTION_DANGEROUS = 1
        const val PROTECTION_SIGNATURE = 2
        const val PROTECTION_SIGNATURE_OR_SYSTEM = 3
        const val PROTECTION_INTERNAL = 4
        const val PROTECTION_MASK_BASE = 15
        const val PROTECTION_FLAG_PRIVILEGED = 16
        const val PROTECTION_FLAG_DEVELOPMENT = 32
        const val PROTECTION_FLAG_APPOP = 64
        const val PROTECTION_FLAG_PRE23 = 128
        const val PROTECTION_FLAG_INSTALLER = 256
        const val PROTECTION_FLAG_VERIFIER = 512
        const val PROTECTION_FLAG_PREINSTALLED = 1024
        const val PROTECTION_FLAG_SETUP = 2048
        const val PROTECTION_FLAG_INSTANT = 4096
        const val PROTECTION_FLAG_RUNTIME_ONLY = 8192
        const val PROTECTION_FLAG_OEM = 16384
        const val PROTECTION_FLAG_VENDOR_PRIVILEGED = 32768
        const val PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER = 65536
        const val PROTECTION_FLAG_CONFIGURATOR = 131072
        const val PROTECTION_FLAG_INCIDENT_REPORT_APPROVER = 262144
        const val PROTECTION_FLAG_APP_PREDICTOR = 524288
        const val PROTECTION_FLAG_COMPANION = 1048576
        const val PROTECTION_FLAG_RETAIL_DEMO = 8388608
        const val PROTECTION_FLAG_RECENTS = 16777216
        const val PROTECTION_FLAG_ROLE = 33554432
        const val PROTECTION_FLAG_KNOWN_SIGNER = 67108864
        const val FLAG_COSTS_MONEY = 1
        const val FLAG_HARD_RESTRICTED = 4
        const val FLAG_SOFT_RESTRICTED = 8
    }
}

/** android.content.pm.PermissionGroupInfo。 */
open class PermissionGroupInfo() : PackageItemInfo(), Parcelable {
    var descriptionRes: Int = 0
    var nonLocalizedDescription: CharSequence? = null
    var flags: Int = 0
    var priority: Int = 0

    fun loadDescription(pm: PackageManager): CharSequence? = nonLocalizedDescription

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val FLAG_PERSONAL_INFO = 1
    }
}

/** android.content.pm.Signature。 */
class Signature(private val bytes: ByteArray) : Parcelable {
    fun toByteArray(): ByteArray = bytes.copyOf()
    fun toCharsString(): String = bytes.joinToString("") { "%02X".format(it) }
    override fun equals(other: Any?): Boolean = other is Signature && other.bytes.contentEquals(bytes)
    override fun hashCode(): Int = bytes.contentHashCode()
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) { dest.writeByteArray(bytes) }
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Signature> = object : Parcelable.Creator<Signature> {
            override fun createFromParcel(source: Parcel): Signature = Signature(source.createByteArray() ?: byteArrayOf())
            override fun newArray(size: Int): Array<Signature?> = arrayOfNulls(size)
        }
    }
}

/** android.content.pm.SigningInfo 轻 stub。 */
class SigningInfo : Parcelable {
    fun hasMultipleSigners(): Boolean = false
    fun hasPastSigningCertificates(): Boolean = false
    fun getSigningCertificateHistory(): Array<Signature> = emptyArray()
    fun getApkContentsSigners(): Array<Signature> = emptyArray()
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}
}

/** android.content.pm.PackageInfo。 */
open class PackageInfo : Parcelable {
    var packageName: String? = null
    var splitNames: Array<String>? = null
    var versionCode: Int = 1
    var versionName: String? = "1.0"
    var baseRevisionCode: Int = 0
    var splitRevisionCodes: IntArray? = null
    var sharedUserId: String? = null
    var sharedUserLabel: Int = 0
    var applicationInfo: ApplicationInfo? = null
    var firstInstallTime: Long = System.currentTimeMillis()
    var lastUpdateTime: Long = System.currentTimeMillis()
    var gids: IntArray? = null
    var activities: Array<ActivityInfo>? = null
    var receivers: Array<ActivityInfo>? = null
    var services: Array<ServiceInfo>? = null
    var providers: Array<ProviderInfo>? = null
    var instrumentation: Array<InstrumentationInfo>? = null
    var permissions: Array<PermissionInfo>? = null
    var requestedPermissions: Array<String>? = null
    var requestedPermissionsFlags: IntArray? = null
    var signatures: Array<Signature>? = null
    var signingInfo: SigningInfo? = null
    var installLocation: Int = 0
    var overlayTarget: String? = null
    var isApex: Boolean = false
    var isStub: Boolean = false
    var coreApp: Boolean = false
    var requiredForAllUsers: Boolean = false
    var restrictedAccountType: String? = null
    var overlayCategory: String? = null
    var overlayPriority: Int = 0
    var isActive: Boolean = false

    fun getLongVersionCode(): Long = versionCode.toLong()

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<PackageInfo> = object : Parcelable.Creator<PackageInfo> {
            override fun createFromParcel(source: Parcel): PackageInfo = PackageInfo()
            override fun newArray(size: Int): Array<PackageInfo?> = arrayOfNulls(size)
        }
    }
}

/** android.content.pm.InstrumentationInfo。 */
class InstrumentationInfo : PackageItemInfo() {
    var targetPackage: String? = null
    var sourceDir: String? = null
    var publicSourceDir: String? = null
    var dataDir: String? = null
    var nativeLibraryDir: String? = null
    var targetProcesses: String? = null
    var functionalTest: Boolean = false
    var handleProfiling: Boolean = false
}

/** android.content.pm.ResolveInfo。 */
open class ResolveInfo : Parcelable {
    var activityInfo: ActivityInfo? = null
    var serviceInfo: ServiceInfo? = null
    var providerInfo: ProviderInfo? = null
    var resolvePackageName: String? = null
    var labelRes: Int = 0
    var nonLocalizedLabel: CharSequence? = null
    var icon: Int = 0
    var specificIndex: Int = 0
    var priority: Int = 0
    var preferredOrder: Int = 0
    var match: Int = 0
    var filter: IntentFilter? = null
    var system: Boolean = false
    var targetUserId: Int = 0
    var handleAllWebDataURI: Boolean = false
    var instantAppAvailable: Boolean = false

    open fun loadLabel(pm: PackageManager): CharSequence =
        nonLocalizedLabel ?: activityInfo?.name ?: serviceInfo?.name ?: ""

    fun loadIcon(pm: PackageManager): Drawable? = null
    fun getIconResource(): Int = activityInfo?.getIconResource() ?: icon

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<ResolveInfo> = object : Parcelable.Creator<ResolveInfo> {
            override fun createFromParcel(source: Parcel): ResolveInfo = ResolveInfo()
            override fun newArray(size: Int): Array<ResolveInfo?> = arrayOfNulls(size)
        }
    }
}

/** android.content.pm.FeatureInfo。 */
open class FeatureInfo : Parcelable {
    var name: String? = null
    var version: Int = 0
    var reqGlEsVersion: Int = 0
    var flags: Int = 0

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}
    override fun toString(): String = "FeatureInfo{$name ver=$version}"

    companion object {
        const val FLAG_REQUIRED = 1
        const val GL_ES_VERSION_UNDEFINED = 0
    }
}

/** android.content.pm.ConfigurationInfo。 */
open class ConfigurationInfo : Parcelable {
    var reqTouchScreen: Int = 0
    var reqKeyboardType: Int = 0
    var reqNavigation: Int = 0
    var reqInputFeatures: Int = 0
    var reqGlEsVersion: Int = 0
    var reqGlEsVersion2: Int = 0

    fun getGlEsVersion(): String = "3.2"

    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {}

    companion object {
        const val INPUT_FEATURE_FIVE_WAY_NAV = 2
        const val INPUT_FEATURE_HARD_KEYBOARD = 1
    }
}

/** android.content.pm.VersionedPackage。 */
class VersionedPackage(
    private val packageName: String,
    private val versionCode: Long,
) : Parcelable {
    fun getPackageName(): String = packageName
    fun getLongVersionCode(): Long = versionCode
    override fun describeContents(): Int = 0
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(packageName)
        dest.writeLong(versionCode)
    }
}

/** android.content.pm.PackageManager：桌面版，单应用视角。 */
open class PackageManager {

    /** android.content.pm.PackageManager.ComponentInfoFlags。 */
    class ComponentInfoFlags private constructor(val value: Long) {
        companion object {
            @JvmStatic fun of(value: Long): ComponentInfoFlags = ComponentInfoFlags(value)
        }
    }
    open class NameNotFoundException : Exception {
        constructor() : super()
        constructor(name: String?) : super(name)
    }

    open class ApplicationInfoFlags private constructor() {
        companion object {
            @JvmStatic fun of(flags: Long): ApplicationInfoFlags = ApplicationInfoFlags()
        }
    }

    open class PackageInfoFlags private constructor() {
        companion object {
            @JvmStatic fun of(flags: Long): PackageInfoFlags = PackageInfoFlags()
        }
    }

    private fun selfPackageInfo(): PackageInfo = PackageInfo().apply {
        packageName = AppGlobals.PACKAGE_NAME
        versionName = "1.0.0"
        versionCode = 1
        applicationInfo = ApplicationInfo().apply {
            packageName = AppGlobals.PACKAGE_NAME
            dataDir = AppGlobals.appDir.absolutePath
            sourceDir = AppGlobals.appDir.absolutePath
            targetSdkVersion = 35
            minSdkVersion = 24
            flags = ApplicationInfo.FLAG_DEBUGGABLE
        }
        activities = emptyArray()
        services = emptyArray()
        providers = emptyArray()
        receivers = emptyArray()
        requestedPermissions = emptyArray()
    }

    @Throws(NameNotFoundException::class)
    open fun getPackageInfo(packageName: String, flags: Int): PackageInfo {
        if (packageName == AppGlobals.PACKAGE_NAME) return selfPackageInfo()
        throw NameNotFoundException(packageName)
    }

    @Throws(NameNotFoundException::class)
    open fun getPackageInfo(packageName: String, flags: PackageInfoFlags): PackageInfo =
        getPackageInfo(packageName, 0)

    @Throws(NameNotFoundException::class)
    open fun getPackageInfo(versionedPackage: VersionedPackage, flags: Int): PackageInfo =
        getPackageInfo(versionedPackage.getPackageName(), flags)

    open fun getInstalledPackages(flags: Int): List<PackageInfo> = emptyList()
    open fun getInstalledPackages(flags: PackageInfoFlags): List<PackageInfo> = emptyList()

    @Throws(NameNotFoundException::class)
    open fun getApplicationInfo(packageName: String, flags: Int): ApplicationInfo {
        if (packageName == AppGlobals.PACKAGE_NAME) return selfPackageInfo().applicationInfo!!
        throw NameNotFoundException(packageName)
    }

    @Throws(NameNotFoundException::class)
    open fun getApplicationInfo(packageName: String, flags: ApplicationInfoFlags): ApplicationInfo =
        getApplicationInfo(packageName, 0)

    open fun getInstalledApplications(flags: Int): List<ApplicationInfo> = emptyList()

    open fun getApplicationLabel(info: ApplicationInfo): CharSequence =
        info.nonLocalizedLabel ?: info.packageName ?: AppGlobals.PACKAGE_NAME

    open fun getApplicationIcon(info: ApplicationInfo): Drawable? = null
    open fun getApplicationIcon(packageName: String): Drawable? = null
    open fun getDefaultActivityIcon(): Drawable? = null
    open fun getDrawable(packageName: String, resid: Int, appInfo: ApplicationInfo?): Drawable? = null
    open fun getText(packageName: String, resid: Int, appInfo: ApplicationInfo?): CharSequence? = null
    open fun getXml(packageName: String, resid: Int, appInfo: ApplicationInfo?): Any? = null

    open fun checkPermission(permName: String, pkgName: String): Int = PERMISSION_GRANTED
    open fun isPermissionRevokedByPolicy(permName: String, pkgName: String): Boolean = false

    open fun resolveActivity(intent: Intent, flags: Int): ResolveInfo? = null
    open fun queryIntentActivities(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()
    open fun queryIntentActivityOptions(
        caller: ComponentName?, specifics: Array<Intent>?, intent: Intent, flags: Int,
    ): List<ResolveInfo> = emptyList()

    open fun resolveService(intent: Intent, flags: Int): ResolveInfo? = null
    open fun queryIntentServices(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()
    open fun queryIntentContentProviders(intent: Intent, flags: Int): List<ResolveInfo> = emptyList()
    open fun resolveContentProvider(name: String, flags: Int): ProviderInfo? = null
    open fun queryContentProviders(processName: String?, uid: Int, flags: Int): List<ProviderInfo> = emptyList()

    @Throws(NameNotFoundException::class)
    open fun getActivityInfo(component: ComponentName, flags: Int): ActivityInfo =
        ActivityInfo().apply {
            name = component.getClassName()
            packageName = component.getPackageName()
            applicationInfo = selfPackageInfo().applicationInfo
        }

    @Throws(NameNotFoundException::class)
    open fun getActivityInfo(component: ComponentName, flags: ComponentInfoFlags): ActivityInfo =
        getActivityInfo(component, flags.value.toInt())

    @Throws(NameNotFoundException::class)
    open fun getReceiverInfo(component: ComponentName, flags: Int): ActivityInfo =
        getActivityInfo(component, flags)

    @Throws(NameNotFoundException::class)
    open fun getServiceInfo(component: ComponentName, flags: Int): ServiceInfo =
        ServiceInfo().apply {
            name = component.getClassName()
            packageName = component.getPackageName()
            applicationInfo = selfPackageInfo().applicationInfo
        }

    @Throws(NameNotFoundException::class)
    open fun getProviderInfo(component: ComponentName, flags: Int): ProviderInfo =
        ProviderInfo().apply {
            name = component.getClassName()
            packageName = component.getPackageName()
            applicationInfo = selfPackageInfo().applicationInfo
        }

    @Throws(NameNotFoundException::class)
    open fun getPermissionInfo(name: String, flags: Int): PermissionInfo =
        PermissionInfo().apply { this.name = name }

    open fun getAllPermissionGroups(flags: Int): List<PermissionGroupInfo> = emptyList()

    @Throws(NameNotFoundException::class)
    open fun getPermissionGroupInfo(name: String, flags: Int): PermissionGroupInfo =
        PermissionGroupInfo().apply { this.name = name }

    open fun getLaunchIntentForPackage(packageName: String): Intent? {
        if (packageName != AppGlobals.PACKAGE_NAME) return null
        return Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setPackage(packageName)
        }
    }

    open fun getLeanbackLaunchIntentForPackage(packageName: String): Intent? = getLaunchIntentForPackage(packageName)

    open fun getPackagesForUid(uid: Int): Array<String> = arrayOf(AppGlobals.PACKAGE_NAME)
    open fun getNameForUid(uid: Int): String = AppGlobals.PACKAGE_NAME
    open fun getInstallerPackageName(packageName: String): String? = null
    open fun getInstallerSourceInfo(packageName: String): Any? = null
    open fun getInstallSourceInfo(packageName: String): Any? = null

    open fun checkSignatures(pkg1: String, pkg2: String): Int = SIGNATURE_MATCH
    open fun checkSignatures(uid1: Int, uid2: Int): Int = SIGNATURE_MATCH

    open fun hasSystemFeature(name: String?): Boolean = false
    open fun hasSystemFeature(name: String?, version: Int): Boolean = false
    open fun getSystemAvailableFeatures(): Array<FeatureInfo> = emptyArray()
    open fun isSafeMode(): Boolean = false
    open fun isInstantApp(): Boolean = false
    open fun isInstantApp(packageName: String): Boolean = false
    open fun getInstantAppCookieMaxBytes(): Int = 0
    open fun getInstantAppCookie(): ByteArray = byteArrayOf()
    open fun clearInstantAppCookie() {}
    open fun updateInstantAppCookie(cookie: ByteArray?) {}
    open fun setApplicationEnabledSetting(packageName: String?, newState: Int, flags: Int) {}
    open fun getApplicationEnabledSetting(packageName: String?): Int = COMPONENT_ENABLED_STATE_DEFAULT
    open fun setComponentEnabledSetting(componentName: ComponentName?, newState: Int, flags: Int) {}
    open fun getComponentEnabledSetting(componentName: ComponentName?): Int = COMPONENT_ENABLED_STATE_DEFAULT
    open fun addPermission(info: PermissionInfo?): Boolean = false
    open fun removePermission(name: String?) {}
    open fun addPackageToPreferred(pkg: String?) {}
    open fun removePackageFromPreferred(pkg: String?) {}
    open fun getPackageArchiveInfo(archiveFilePath: String, flags: Int): PackageInfo? = null
    open fun getResourcesForApplication(appPackageName: String): android.content.res.Resources = AppGlobals.resources
    open fun getResourcesForApplication(appInfo: ApplicationInfo): android.content.res.Resources = AppGlobals.resources
    open fun getUserBadgeForDensity(user: Any?, density: Int): Drawable? = null
    open fun getSuspendedPackageAppExtras(packageName: String?): android.os.Bundle? = null
    open fun canRequestPackageInstalls(): Boolean = true
    open fun isPackageSuspended(): Boolean = false
    open fun isPackageSuspended(packageName: String): Boolean = false
    open fun getChangedPackages(sequenceNumber: Int): Any? = null
    open fun isDeviceUpgrading(): Boolean = false
    open fun getSharedLibraries(flags: Int): List<Any> = emptyList()

    companion object {
        const val GET_ACTIVITIES = 1
        const val GET_RECEIVERS = 2
        const val GET_SERVICES = 4
        const val GET_PROVIDERS = 8
        const val GET_INSTRUMENTATION = 16
        const val GET_INTENT_FILTERS = 32
        const val GET_SIGNATURES = 64
        const val GET_RESOLVED_FILTER = 64
        const val GET_META_DATA = 128
        const val GET_GIDS = 256
        const val GET_DISABLED_COMPONENTS = 512
        const val GET_SHARED_LIBRARY_FILES = 1024
        const val GET_URI_PERMISSION_PATTERNS = 2048
        const val GET_PERMISSIONS = 4096
        const val MATCH_UNINSTALLED_PACKAGES = 8192
        const val GET_CONFIGURATIONS = 16384
        const val GET_DISABLED_UNTIL_USED_COMPONENTS = 32768
        const val MATCH_DISABLED_COMPONENTS = 2048
        const val MATCH_DISABLED_UNTIL_USED_COMPONENTS = 32768
        const val MATCH_DEFAULT_ONLY = 65536
        const val MATCH_DIRECT_BOOT_AWARE = 131072
        const val MATCH_DIRECT_BOOT_UNAWARE = 262144
        const val MATCH_SYSTEM_ONLY = 1048576
        const val MATCH_DEBUG_TRIAGED_MISSING = 268435456
        const val MATCH_ALL = 536870912
        const val MATCH_UNINSTALLED_PACKAGES_LONG = 8192L
        const val GET_SIGNING_CERTIFICATES = 134217728
        const val MATCH_APEX = 1073741824
        const val MATCH_INSTANT = 8388608
        const val PERMISSION_GRANTED = 0
        const val PERMISSION_DENIED = -1
        const val SIGNATURE_MATCH = 0
        const val SIGNATURE_NEITHER_SIGNED = 1
        const val SIGNATURE_FIRST_NOT_SIGNED = -1
        const val SIGNATURE_NO_MATCH = -3
        const val SIGNATURE_SECOND_NOT_SIGNED = -2
        const val SIGNATURE_UNKNOWN_PACKAGE = -4
        const val COMPONENT_ENABLED_STATE_DEFAULT = 0
        const val COMPONENT_ENABLED_STATE_ENABLED = 1
        const val COMPONENT_ENABLED_STATE_DISABLED = 2
        const val COMPONENT_ENABLED_STATE_DISABLED_USER = 3
        const val COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED = 4
        const val DONT_KILL_APP = 1
        const val SYNC_IMMEDIATE = 2
        const val INSTALL_REASON_UNKNOWN = 0
        const val INSTALL_REASON_POLICY = 1
        const val INSTALL_REASON_DEVICE_RESTORE = 2
        const val INSTALL_REASON_DEVICE_SETUP = 3
        const val INSTALL_REASON_USER = 4
        const val FEATURE_AUDIO_OUTPUT = "android.hardware.audio.output"
        const val FEATURE_BLUETOOTH = "android.hardware.bluetooth"
        const val FEATURE_CAMERA = "android.hardware.camera"
        const val FEATURE_CAMERA_FRONT = "android.hardware.camera.front"
        const val FEATURE_LOCATION = "android.hardware.location"
        const val FEATURE_LOCATION_GPS = "android.hardware.location.gps"
        const val FEATURE_LOCATION_NETWORK = "android.hardware.location.network"
        const val FEATURE_MICROPHONE = "android.hardware.microphone"
        const val FEATURE_NFC = "android.hardware.nfc"
        const val FEATURE_SCREEN_LANDSCAPE = "android.hardware.screen.landscape"
        const val FEATURE_SCREEN_PORTRAIT = "android.hardware.screen.portrait"
        const val FEATURE_SENSORS_ACCELEROMETER = "android.hardware.sensor.accelerometer"
        const val FEATURE_TELEPHONY = "android.hardware.telephony"
        const val FEATURE_TOUCHSCREEN = "android.hardware.touchscreen"
        const val FEATURE_WIFI = "android.hardware.wifi"
        const val FEATURE_LIVE_WALLPAPER = "android.software.live_wallpaper"
        const val FEATURE_APP_WIDGETS = "android.software.app_widgets"
        const val FEATURE_FILE_BASED_ENCRYPTION = "android.software.file_based_encryption"
        const val FEATURE_WEBVIEW = "android.software.webview"
    }
}
