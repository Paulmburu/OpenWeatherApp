package github.paulmburu.domain.usercases

import github.paulmburu.common.Resource
import github.paulmburu.domain.models.Coordinates
import github.paulmburu.domain.models.CurrentLocationWeather
import github.paulmburu.domain.repository.WeatherRepository
import github.paulmburu.domain.usercases.base.FlowBaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

typealias FetchCurrentWeatherBaseUseCase = FlowBaseUseCase<String, Flow<Resource<CurrentLocationWeather>>>

class FetchCurrentWeatherUseCase constructor(private val weatherRepository: WeatherRepository) :
    FetchCurrentWeatherBaseUseCase {
    override fun invoke(location: String): Flow<Resource<CurrentLocationWeather>> = flow {
        val result = weatherRepository.fetchCurrentWeather(location)
        result.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val data = resource.data!!
                    emit(
                        Resource.Success(data)
                    )
                }
                is Resource.Error -> {
                    emit(Resource.Error<CurrentLocationWeather>(message = resource.message))
                }
            }
        }
    }
}
