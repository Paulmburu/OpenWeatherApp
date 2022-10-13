package github.paulmburu.domain.models

data class CurrentLocationWeather(
    var id: String,
    val name: String,
    val coord: Coordinates,
    val weatherInfo: List<WeatherInfo>,
    val mainInfo: MainInfo,
    val wind: WindInfo,
    val sys: SysInfo,
    val timeStamp: String
)