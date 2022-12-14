package github.paulmburu.domain.usercases.base

interface FlowBaseUseCase<in Params, out T> {
    operator fun invoke(location: Params): T
}