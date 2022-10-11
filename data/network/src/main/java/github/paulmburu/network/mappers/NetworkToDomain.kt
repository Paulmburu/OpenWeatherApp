package github.paulmburu.network.mappers

import github.paulmburu.domain.models.*
import github.paulmburu.network.models.*


fun CoordinatesDto.toDomain(): Coordinates {
    return Coordinates(
        lat = lat,
        lon = lon,
    )
}

fun MainInfoDto.toDomain(): MainInfo {
    return MainInfo(
        temp = temp,
        temp_min = temp_min,
        temp_max = temp_max,
        pressure = pressure,
        humidity = humidity
    )
}

fun WeatherInfoDto.toDomain(): WeatherInfo {
    return WeatherInfo(
        main = main,
        description = description,
    )
}

fun WindInfoDto.toDomain(): WindInfo {
    return WindInfo(
        speed = speed
    )
}

fun SysInfoDto.toDomain(): SysInfo {
    return SysInfo(
        sunrise = sunrise,
        sunset = sunset
    )
}

fun CurrentLocationWeatherDto.toDomain(): CurrentLocationWeather {
    return CurrentLocationWeather(
        id = id,
        name = name,
        timeStamp = timeStamp,
        coord = coord.toDomain(),
        mainInfo = main.toDomain(),
        weatherInfo = weather.map { it.toDomain() },
        wind = wind.toDomain(),
        sys = sys.toDomain()
    )
}

fun WeatherForecastDto.toDomain(): WeatherForecast {
    return WeatherForecast(
        isoTimeStamp = timeStamp,
        mainInfo = main.toDomain(),
        weatherInfo = weather.map { it.toDomain() }
    )
}