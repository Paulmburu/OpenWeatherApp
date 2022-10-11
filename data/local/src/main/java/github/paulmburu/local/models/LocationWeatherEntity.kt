package github.paulmburu.local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
class LocationWeatherEntity(
    @PrimaryKey
    var id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: String,

    @ColumnInfo(name = "lat")
    val lat: Double,

    @ColumnInfo(name = "lon")
    val lon: Double,

    @ColumnInfo(name = "weather_type")
    val weatherType: String,

    @ColumnInfo(name = "weather_type_description")
    val weatherTypeDescription: String,

    @ColumnInfo(name = "temp")
    val temp: Double,

    @ColumnInfo(name = "temp_min")
    val tempMin: Double,

    @ColumnInfo(name = "temp_max")
    val tempMax: Double,

    @ColumnInfo(name = "pressure")
    val pressure: Double,

    @ColumnInfo(name = "humidity")
    val humidity: Double,

    @ColumnInfo(name = "speed")
    val windSpeed: Double,

    @ColumnInfo(name = "sunrise")
    val sunrise: Long,

    @ColumnInfo(name = "sunset")
    val sunset: Long,

    @ColumnInfo(name = "is_favourite")
    var isFavourite: Boolean = false,
)

@Entity(tableName = "weather_forecast_table")
class WeatherForecastEntity(
    @PrimaryKey
    @ColumnInfo(name = "time_stamp")
    val isoTimeStamp: String,

    @ColumnInfo(name = "weather_type")
    val weatherType: String,

    @ColumnInfo(name = "weather_type_description")
    val weatherTypeDescription: String,

    @ColumnInfo(name = "temp")
    val temp: Double,

    @ColumnInfo(name = "temp_min")
    val tempMin: Double,

    @ColumnInfo(name = "temp_max")
    val tempMax: Double,

    @ColumnInfo(name = "pressure")
    val pressure: Double,

    @ColumnInfo(name = "humidity")
    val humidity: Double,
)