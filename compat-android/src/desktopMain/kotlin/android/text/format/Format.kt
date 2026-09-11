package android.text.format

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/** android.text.format.DateFormat：委托 java.text。 */
object DateFormat {
    const val QUOTE = '\''
    const val SECONDS = 's'
    const val MINUTES = 'm'
    const val HOUR = 'h'
    const val HOUR_OF_DAY = 'H'
    const val DATE = 'd'
    const val MONTH = 'M'
    const val YEAR = 'y'
    const val TIME_ZONE = 'z'
    const val AM_PM = 'a'

    @JvmStatic
    fun getDateFormat(context: android.content.Context): java.text.DateFormat =
        java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT, Locale.getDefault())

    @JvmStatic
    fun getLongDateFormat(context: android.content.Context): java.text.DateFormat =
        java.text.DateFormat.getDateInstance(java.text.DateFormat.LONG, Locale.getDefault())

    @JvmStatic
    fun getMediumDateFormat(context: android.content.Context): java.text.DateFormat =
        java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM, Locale.getDefault())

    @JvmStatic
    fun getTimeFormat(context: android.content.Context): java.text.DateFormat =
        java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT, Locale.getDefault())

    @JvmStatic
    fun getDateTimeFormat(context: android.content.Context): java.text.DateFormat =
        java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.SHORT, java.text.DateFormat.SHORT, Locale.getDefault())

    @JvmStatic
    fun is24HourFormat(context: android.content.Context): Boolean = is24HourLocale(Locale.getDefault())

    @JvmStatic
    fun is24HourLocale(locale: Locale): Boolean = try {
        val df = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT, locale)
        df is SimpleDateFormat && df.toPattern().contains('H')
    } catch (e: Exception) { false }

    @JvmStatic
    fun getBestDateTimePattern(locale: Locale, skeleton: String): String = skeleton

    @JvmStatic
    fun format(inFormat: CharSequence, inDate: Date): CharSequence =
        SimpleDateFormat(inFormat.toString(), Locale.getDefault()).format(inDate)

    @JvmStatic
    fun format(inFormat: CharSequence, inCalendar: Calendar): CharSequence =
        SimpleDateFormat(inFormat.toString(), Locale.getDefault()).format(inCalendar.time)

    @JvmStatic
    fun format(inFormat: CharSequence, inDate: Long): CharSequence =
        SimpleDateFormat(inFormat.toString(), Locale.getDefault()).format(Date(inDate))
}

/** android.text.format.Formatter：文件大小/IP 等真实实现。 */
object Formatter {

    @JvmStatic
    fun formatFileSize(context: android.content.Context?, sizeBytes: Long): String =
        formatFileSizeImpl(sizeBytes, 1000)

    @JvmStatic
    fun formatShortFileSize(context: android.content.Context?, sizeBytes: Long): String =
        formatFileSizeImpl(sizeBytes, 1000)

    @JvmStatic
    fun formatBytes(res: android.content.res.Resources?, sizeBytes: Long, flags: Int): BytesResult =
        BytesResult(formatFileSizeImpl(sizeBytes, 1024), formatFileSizeImpl(sizeBytes, 1024))

    class BytesResult(val formattedBytes: String, val formattedShortBytes: String)

    private fun formatFileSizeImpl(size: Long, divisor: Int): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB", "PB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(divisor.toDouble())).toInt()
            .coerceIn(0, units.size - 1)
        val value = size / Math.pow(divisor.toDouble(), digitGroups.toDouble())
        return "%.1f %s".format(Locale.US, value, units[digitGroups])
    }

    @JvmStatic
    fun formatIpAddress(ipv4Address: Int): String {
        return "${ipv4Address and 0xFF}.${(ipv4Address shr 8) and 0xFF}.${(ipv4Address shr 16) and 0xFF}.${(ipv4Address shr 24) and 0xFF}"
    }

    @JvmStatic
    fun formatIpAddress(addr: java.net.InetAddress): String = addr.hostAddress ?: ""

    @JvmStatic
    fun formatIpPrefix(subnet: java.net.InetAddress, prefixLength: Int): String = "${subnet.hostAddress}/$prefixLength"

    const val FORMAT_SHORT_FILE_SIZE = 1
    const val FORMAT_CALCULATE_ROUNDING = 2
    const val FORMAT_IEC_UNITS = 4
    const val FORMAT_SI_UNITS = 8
}

/** android.text.format.Time 轻 stub。 */
class Time {
    var year: Int = 0
    var month: Int = 0
    var monthDay: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var second: Int = 0
    var allDay: Boolean = false
    var timezone: String? = null

    constructor(timezone: String?) { this.timezone = timezone; setToNow() }
    constructor() : this(null)

    fun setToNow() {
        val cal = Calendar.getInstance()
        year = cal.get(Calendar.YEAR); month = cal.get(Calendar.MONTH)
        monthDay = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR_OF_DAY); minute = cal.get(Calendar.MINUTE); second = cal.get(Calendar.SECOND)
    }

    fun toMillis(ignoreDst: Boolean): Long {
        val cal = Calendar.getInstance()
        cal.set(year, month, monthDay, hour, minute, second)
        return cal.timeInMillis
    }

    fun set(millis: Long) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = millis
        year = cal.get(Calendar.YEAR); month = cal.get(Calendar.MONTH)
        monthDay = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR_OF_DAY); minute = cal.get(Calendar.MINUTE); second = cal.get(Calendar.SECOND)
    }

    fun format(format: String): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(toMillis(true)))
}
