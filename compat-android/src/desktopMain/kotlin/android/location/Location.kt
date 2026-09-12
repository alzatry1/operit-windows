package android.location

import android.content.Context
import android.os.Bundle
import java.util.Locale

/**
 * android.location（P3-B2 新增，轻 stub）。
 * 桌面无 GPS：getLastKnownLocation→null、isProviderEnabled→false、requestLocationUpdates no-op。
 * distanceBetween 为真实 haversine 实现（纯数学）。
 */

/** android.location.Location。 */
open class Location {

    var provider: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var altitude: Double = 0.0
    var accuracy: Float = 0f
    var bearing: Float = 0f
    var speed: Float = 0f
    var time: Long = 0L
    var elapsedRealtimeNanos: Long = 0L
    var verticalAccuracyMeters: Float = 0f
    var bearingAccuracyDegrees: Float = 0f
    var speedAccuracyMetersPerSecond: Float = 0f
    var extras: Bundle? = null

    constructor(provider: String?) {
        this.provider = provider
    }

    constructor(l: Location?) {
        if (l != null) set(l)
    }

    open fun set(l: Location) {
        provider = l.provider
        latitude = l.latitude
        longitude = l.longitude
        altitude = l.altitude
        accuracy = l.accuracy
        bearing = l.bearing
        speed = l.speed
        time = l.time
        elapsedRealtimeNanos = l.elapsedRealtimeNanos
    }

    open fun reset() {
        latitude = 0.0
        longitude = 0.0
        altitude = 0.0
        accuracy = 0f
        bearing = 0f
        speed = 0f
        time = 0
    }

    open fun hasAccuracy(): Boolean = accuracy > 0f
    open fun hasAltitude(): Boolean = altitude != 0.0
    open fun hasBearing(): Boolean = bearing != 0f
    open fun hasSpeed(): Boolean = speed != 0f
    open fun removeAccuracy() { accuracy = 0f }
    open fun removeAltitude() { altitude = 0.0 }
    open fun removeBearing() { bearing = 0f }
    open fun removeSpeed() { speed = 0f }

    open fun distanceTo(dest: Location?): Float {
        if (dest == null) return 0f
        val results = FloatArray(1)
        distanceBetween(latitude, longitude, dest.latitude, dest.longitude, results)
        return results[0]
    }

    open fun bearingTo(dest: Location?): Float {
        if (dest == null) return 0f
        val lat1 = Math.toRadians(latitude)
        val lat2 = Math.toRadians(dest.latitude)
        val dLon = Math.toRadians(dest.longitude - longitude)
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon)
        return ((Math.toDegrees(Math.atan2(y, x)) + 360.0) % 360.0).toFloat()
    }

    override fun toString(): String = "Location[$provider $latitude,$longitude acc=$accuracy]"

    companion object {
        const val FORMAT_DEGREES = 0
        const val FORMAT_MINUTES = 1
        const val FORMAT_SECONDS = 2

        const val KEY_MOCK_LOCATION = "mock_location"

        @JvmStatic
        fun distanceBetween(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double, results: FloatArray?) {
            if (results == null || results.isEmpty()) return
            // haversine
            val earthRadius = 6371000.0
            val dLat = Math.toRadians(endLatitude - startLatitude)
            val dLon = Math.toRadians(endLongitude - startLongitude)
            val lat1 = Math.toRadians(startLatitude)
            val lat2 = Math.toRadians(endLatitude)
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            results[0] = (earthRadius * c).toFloat()
        }

        @JvmStatic
        fun convert(coordinate: String?): Double {
            if (coordinate == null) return 0.0
            return try {
                val parts = coordinate.split(":")
                var value = 0.0
                var divisor = 1.0
                for (p in parts) {
                    value += p.toDouble() / divisor
                    divisor *= 60.0
                }
                value
            } catch (e: Exception) {
                0.0
            }
        }

        @JvmStatic
        fun convert(coordinate: Double, format: Int): String = coordinate.toString()
    }
}

/** android.location.LocationListener。 */
interface LocationListener {
    fun onLocationChanged(location: Location)
    fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    fun onProviderEnabled(provider: String) {}
    fun onProviderDisabled(provider: String) {}
}

/** android.location.Criteria。 */
open class Criteria {
    var accuracy: Int = NO_REQUIREMENT
    var powerRequirement: Int = NO_REQUIREMENT
    var isAltitudeRequired: Boolean = false
    var isBearingRequired: Boolean = false
    var isSpeedRequired: Boolean = false
    var isCostAllowed: Boolean = false

    companion object {
        const val NO_REQUIREMENT = 0
        const val ACCURACY_FINE = 1
        const val ACCURACY_COARSE = 2
        const val ACCURACY_LOW = 3
        const val ACCURACY_MEDIUM = 2
        const val ACCURACY_HIGH = 1

        const val POWER_LOW = 1
        const val POWER_MEDIUM = 2
        const val POWER_HIGH = 3
    }
}

/** android.location.LocationManager。 */
class LocationManager private constructor() {

    fun getLastKnownLocation(provider: String?): Location? = null
    fun isProviderEnabled(provider: String?): Boolean = false

    fun requestLocationUpdates(provider: String, minTimeMs: Long, minDistanceM: Float, listener: LocationListener?) {}
    fun requestLocationUpdates(provider: String, minTimeMs: Long, minDistanceM: Float, listener: LocationListener?, looper: android.os.Looper?) {}
    fun requestLocationUpdates(provider: String, minTimeMs: Long, minDistanceM: Float, executor: java.util.concurrent.Executor?, listener: LocationListener?) {}
    fun requestSingleUpdate(provider: String, listener: LocationListener?, looper: android.os.Looper?) {}
    fun removeUpdates(listener: LocationListener?) {}
    fun removeUpdates(pendingIntent: Any?) {}

    fun getCurrentLocation(
        provider: String,
        cancellationSignal: android.os.CancellationSignal?,
        executor: java.util.concurrent.Executor?,
        consumer: java.util.function.Consumer<Location?>?,
    ) {
        consumer?.accept(null)
    }

    fun getProviders(enabledOnly: Boolean): List<String> = emptyList()
    fun getAllProviders(): List<String> = listOf(GPS_PROVIDER, NETWORK_PROVIDER, PASSIVE_PROVIDER)
    fun getBestProvider(criteria: Criteria?, enabledOnly: Boolean): String? = null
    fun getProvider(name: String?): Any? = null

    fun isLocationEnabled(): Boolean = false
    fun addGpsStatusListener(listener: Any?): Boolean = false
    fun removeGpsStatusListener(listener: Any?) {}

    companion object {
        const val GPS_PROVIDER = "gps"
        const val NETWORK_PROVIDER = "network"
        const val PASSIVE_PROVIDER = "passive"
        const val FUSED_PROVIDER = "fused"

        const val KEY_LOCATION_CHANGED = "location"
        const val KEY_PROVIDER_ENABLED = "providerEnabled"
        const val KEY_PROXIMITY_ENTERING = "proximity"
        const val KEY_STATUS_CHANGED = "status"

        const val PROVIDERS_CHANGED_ACTION = "android.location.PROVIDERS_CHANGED"
        const val MODE_CHANGED_ACTION = "android.location.MODE_CHANGED"

        @JvmStatic
        fun getService(context: Context?): LocationManager = LocationManager()
    }
}

/** android.location.Address。 */
open class Address(val locale: Locale? = Locale.getDefault()) {
    var adminArea: String? = null
    var countryCode: String? = null
    var countryName: String? = null
    var featureName: String? = null
    var locality: String? = null
    var subLocality: String? = null
    var postalCode: String? = null
    var subAdminArea: String? = null
    var thoroughfare: String? = null
    var subThoroughfare: String? = null
    var phone: String? = null
    var premises: String? = null
    var url: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var extras: Bundle? = null

    private val addressLines = mutableMapOf<Int, String>()

    var maxAddressLineIndex: Int = -1
        get() = addressLines.keys.maxOrNull() ?: -1

    fun getAddressLine(index: Int): String? = addressLines[index]
    fun setAddressLine(index: Int, line: String?) {
        if (line != null) addressLines[index] = line else addressLines.remove(index)
    }

    fun hasLatitude(): Boolean = latitude != 0.0
    fun hasLongitude(): Boolean = longitude != 0.0

    override fun toString(): String = "Address[$countryName/$adminArea/$locality]"
}

/** android.location.Geocoder。 */
open class Geocoder {

    private val context: Context?
    private val locale: Locale?

    constructor(context: Context?, locale: Locale?) {
        this.context = context
        this.locale = locale
    }

    constructor(context: Context?) {
        this.context = context
        this.locale = null
    }

    fun interface GeocodeListener {
        fun onGeocode(addresses: MutableList<Address>)
        fun onError(errorMessage: String?) {}
    }

    open fun getFromLocation(latitude: Double, longitude: Double, maxResults: Int): List<Address> = emptyList()

    open fun getFromLocation(latitude: Double, longitude: Double, maxResults: Int, listener: GeocodeListener) {
        listener.onGeocode(emptyList())
    }

    open fun getFromLocationName(locationName: String?, maxResults: Int): List<Address> = emptyList()

    open fun getFromLocationName(
        locationName: String?, maxResults: Int,
        lowerLeftLatitude: Double, lowerLeftLongitude: Double,
        upperRightLatitude: Double, upperRightLongitude: Double,
    ): List<Address> = emptyList()

    companion object {
        @JvmStatic
        fun isPresent(): Boolean = false
    }
}
