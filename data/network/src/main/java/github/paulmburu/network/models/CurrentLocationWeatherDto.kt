package github.paulmburu.network.models

import com.google.gson.annotations.SerializedName
import github.paulmburu.domain.models.SysInfo
import github.paulmburu.domain.models.WindInfo

data class CurrentLocationWeatherDto(
    var id: String,
    val coord: CoordinatesDto,
    val weather: List<WeatherInfoDto>,
    val main: MainInfoDto,
    val wind: WindInfoDto,
    val sys: SysInfoDto,
    val name: String,
    @SerializedName("dt")
    val timeStamp: String
)
