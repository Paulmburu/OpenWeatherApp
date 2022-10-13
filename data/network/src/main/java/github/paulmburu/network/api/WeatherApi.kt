package github.paulmburu.network.api

import github.paulmburu.network.models.CurrentLocationWeatherDto
import github.paulmburu.network.models.ForecastResponse
import retrofit2.Response
import retrofit2.http.*


interface WeatherApi {

    @POST("data/2.5/weather")
    @Headers("Accept:application/json")
    suspend fun fetchCurrentWeather(
        @Query("q", encoded = true) location: String,
        @Query("appid", encoded = true) appid: String,
    ): Response<CurrentLocationWeatherDto>

    @POST("data/2.5/forecast")
    @Headers("Accept:application/json")
    suspend fun fetchWeatherForecast(
        @Query("q", encoded = true) location: String,
        @Query("appid", encoded = true) appid: String,
    ): Response<ForecastResponse>

}