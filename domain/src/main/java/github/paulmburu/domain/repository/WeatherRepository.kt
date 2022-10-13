package github.paulmburu.domain.repository

import github.paulmburu.common.Resource
import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.domain.models.WeatherForecast
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun fetchCurrentWeather(location: String): Flow<Resource<CurrentLocationWeather>>
    fun fetchWeatherForecast(location: String): Flow<Resource<List<WeatherForecast>>>

    suspend fun insertCurrentWeather(currentWeather: List<CurrentLocationWeather>)
    suspend fun insertLocationToFavourites(weatherForecast: List<CurrentLocationWeather>)
    suspend fun insertWeatherForecast(weatherForecast: List<WeatherForecast>)

    fun getCurrentWeather(): Flow<Resource<CurrentLocationWeather>>
    fun getWeatherForecast(): Flow<Resource<List<WeatherForecast>>>

}

