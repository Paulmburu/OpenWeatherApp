package github.paulmburu.weatherapp.mappers


import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.domain.models.WeatherForecast
import github.paulmburu.weatherapp.models.CurrentLocationWeatherPresentation
import github.paulmburu.weatherapp.models.WeatherForecastPresentation
import github.paulmburu.weatherapp.util.convertKelvinToCelsius
import github.paulmburu.weatherapp.util.convertTimestampToDate
import github.paulmburu.weatherapp.util.convertTimestampToFullDate

fun CurrentLocationWeather.toPresentation(): CurrentLocationWeatherPresentation {
    return CurrentLocationWeatherPresentation(
        id = id,
        lat = coord.lat.toString(),
        lon = coord.lon.toString(),
        weatherType = weatherInfo[0].main,
        weatherTypeDescription = weatherInfo[0].description,
        temp = mainInfo.temp,
        temp_min = mainInfo.temp_min,
        temp_max = mainInfo.temp_max,
        pressure = mainInfo.pressure,
        humidity = mainInfo.humidity,
        windSpeed = wind.speed,
        sunrise = sys.sunrise,
        sunset =- sys.sunset,
        timeForecast = timeStamp
    )
}

fun WeatherForecast.toPresentation(): WeatherForecastPresentation{
    return WeatherForecastPresentation(
        isoTimeStamp = isoTimeStamp,
        dayOfTheWeek = isoTimeStamp.convertTimestampToDate(),
        isoTimeStamp.convertTimestampToFullDate(),
        weatherType = weatherInfo[0].main,
        weatherInfo[0].description.replaceFirstChar { it.uppercaseChar() },
        temperature = mainInfo.temp.convertKelvinToCelsius(),
        pressure = "${mainInfo.pressure} m/s",
        humidity = mainInfo.humidity.toString(),
        windSpeed = "${wind.speed} m/s"
    )
}