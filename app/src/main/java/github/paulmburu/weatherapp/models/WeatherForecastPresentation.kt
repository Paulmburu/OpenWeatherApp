package github.paulmburu.weatherapp.models


data class WeatherForecastPresentation(
    val isoTimeStamp: String,
    var dayOfTheWeek: String,
    var dateTime: String,
    val weatherType: String,
    val weatherDescription: String,
    val temperature: String,
    val pressure: String,
    val humidity: String,
    val windSpeed: String,

)
