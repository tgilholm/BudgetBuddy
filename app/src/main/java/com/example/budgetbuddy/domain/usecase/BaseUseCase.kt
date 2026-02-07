package com.example.budgetbuddy.domain.usecase

import com.example.budgetbuddy.domain.DomainResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


//@Target(AnnotationTarget.CLASS)     // Must mark classes
//@Retention(AnnotationRetention.RUNTIME)      // Executes at runtime
//annotation class DomainUseCase


/**
 * Abstract class providing the base for all domain-level UseCases
 * Executes all operations on a separate Coroutine.
 *
 * Classes should extend this base and override execute() with the
 * usecase operations. The base class overloads the invocation operator
 * meaning the usecase can be called as a class- DoSomethingUseCase()
 */
//@DomainUseCase
abstract class BaseUseCase<in Params>
{
    // Use the IO dispatcher to offload the heavy lifting
    private val dispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    // Extending classes override execute with provided code
    protected abstract suspend fun execute(params: Params): DomainResult

    // Operator () is overloaded, meaning the usecase is executed like a function
    // invoke() simply calls the execute() method using the dispatcher and emits
    // the DomainResult status result on a Flow.
    operator fun invoke(params: Params): Flow<DomainResult> = flow {
        val result = withContext(dispatcher) {execute(params)}
        emit(result)
    }.flowOn(dispatcher)
}