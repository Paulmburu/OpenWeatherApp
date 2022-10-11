package github.paulmburu.weatherapp.models

data class CurrentLocationWeatherPresentation(
    val id: String,
    val lat: String,
    val lon: String,
    val weatherType: String,
    val weatherTypeDescription: String,
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double,
    val windSpeed: Double,
    val sunrise: Long,
    val sunset: Long,
    val timeForecast: String?
)