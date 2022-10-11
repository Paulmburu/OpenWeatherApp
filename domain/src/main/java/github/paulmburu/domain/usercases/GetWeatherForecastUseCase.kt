package github.paulmburu.domain.usercases

import github.paulmburu.common.Resource
import github.paulmburu.domain.models.Coordinates
import github.paulmburu.domain.models.WeatherForecast
import github.paulmburu.domain.repository.WeatherRepository
import github.paulmburu.domain.usercases.base.BaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow



typealias GetWeatherForecastBaseUseCase = BaseUseCase<Unit, Flow<Resource<List<WeatherForecast>>>>

class GetWeatherForecastUseCase constructor(private val weatherRepository: WeatherRepository) :
    GetWeatherForecastBaseUseCase {
    override suspend fun invoke(params: Unit): Flow<Resource<List<WeatherForecast>>> = flow {
        val result = weatherRepository.getWeatherForecast()
        result.collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    val data = resource.data!!
                    emit(
                        Resource.Success(data)
                    )
                }
                is Resource.Error -> {
                    emit(Resource.Error(message = resource.message))
                }
            }
        }
    }
}