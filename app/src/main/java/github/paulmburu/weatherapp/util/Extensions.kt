package github.paulmburu.weatherapp.util

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


fun Double.convertKelvinToCelsius(): String = "${this.minus(272.15).toInt()}°c"


fun String.convertTimestampToDate(): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = format.parse(this)
        SimpleDateFormat("EEEE", Locale.ENGLISH).format(date?.time)
    } catch (e: Exception) {
        "undefined"
    }
}

fun Long.convertToTime(): String {
    return DateFormat.getTimeInstance().format(Date(this))
}

fun String.convertTimestampToFullDate(): String {
    return try {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val date = format.parse(this)
        date.toString()
    } catch (e: Exception) {
        "undefined"
    }
}