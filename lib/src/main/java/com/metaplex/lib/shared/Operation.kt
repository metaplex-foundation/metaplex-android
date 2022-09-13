package com.metaplex.lib.shared

import com.metaplex.lib.drivers.solana.Connection
import kotlinx.coroutines.*

sealed class Retry(open val exception: ResultError) : ResultError() {
    data class retry(override val exception: ResultError) : Retry(exception)
    data class doNotRetry(override val exception: ResultError) : Retry(exception)
}

class Operation<out A>(val run: ((A) -> Unit) -> Unit) {

    fun <B> map(f: (A) -> B): Operation<B> {
        return Operation { cb ->
            this.run { a -> cb(f(a)) }
        }
    }

    fun <B> flatMap(f: (A) -> Operation<B>): Operation<B> {
        return Operation { cb ->
            this.run { a -> f(a).run(cb) }
        }
    }

    fun <B> then(f: () -> Operation<B>): Operation<B> {
        return Operation { cb ->
            this.run { f().run(cb) }
        }
    }

    companion object {
        fun <A> pure(a: A): Operation<A> = Operation { cb -> cb(a) }
    }
}

class OperationResult<out A, out E : ResultError>(val operation: Operation<ResultWithCustomError<A, E>>) {

    constructor(f: ((ResultWithCustomError<A, E>) -> Unit) -> Unit) : this(Operation(f))

    fun <B> map(f: (A) -> B): OperationResult<B, E> {
        return OperationResult(operation.map { result -> result.map(f) })
    }

    fun <E2 : ResultError> mapError(f: (E) -> E2): OperationResult<A, E2> {
        return OperationResult(operation.map { result -> result.mapError(f) })
    }

    fun onSuccess(action: (A) -> Unit): OperationResult<A, E> {
        return OperationResult(operation.flatMap {
            it.onSuccess(action)
            Operation.pure(it)
        })
    }

    fun run(action: (ResultWithCustomError<A, E>) -> Unit) {
        operation.run(action)
    }

    companion object {
        fun <A, E : ResultError> pure(r: ResultWithCustomError<A, E>): OperationResult<A, E> = OperationResult(Operation { cb -> cb(r) })
        fun <A> success(a: A): OperationResult<A, Nothing> = OperationResult(Operation { cb -> cb(ResultWithCustomError.success(a)) })
        fun <E : ResultError> failure(e: E): OperationResult<Nothing, E> = OperationResult(Operation { cb -> cb(ResultWithCustomError.failure(e)) })

        fun <A> retry(attempts: Int, operation: () -> OperationResult<A, Retry>): OperationResult<A, ResultError> {
            return operation().recover {
                when (it) {
                    is Retry.retry -> if (attempts > 0) retry(
                        attempts - 1,
                        operation
                    ) else failure(it.exception)
                    is Retry.doNotRetry -> failure(it.exception)
                }
            }
        }

        fun <A, B, C, E : ResultError> map2(ra: OperationResult<A, E>, rb: OperationResult<B, E>, f: (A, B) -> C): OperationResult<C, E> {
            return ra.flatMap { a -> rb.map { b -> f(a, b) } }
        }

        fun <A, B, C, E : ResultError> flatMap2(ra: OperationResult<A, E>, rb: OperationResult<B, E>, f: (A, B) -> OperationResult<C, E>): OperationResult<C, E> {
            return ra.flatMap { a -> rb.flatMap { b -> f(a, b) } }
        }
    }
}

fun <A, B, E : ResultError> OperationResult<A, E>.flatMap(f: (A) -> OperationResult<B, E>): OperationResult<B, E> {
    return OperationResult(operation.flatMap { result ->
        Operation<ResultWithCustomError<B, E>> { cb ->
            result.onSuccess { f(it).operation.run(cb) }
                .onFailure { cb(ResultWithCustomError.failure(it)) }
        }
    })
}

fun <A, E : ResultError, E2 : ResultError> OperationResult<A, E>.recover(f: (E) -> OperationResult<A, E2>): OperationResult<A, E2> {
    return OperationResult(operation.flatMap { result ->
        Operation<ResultWithCustomError<A, E2>> { cb ->
            result.onSuccess { cb(ResultWithCustomError.success(it)) }
                .onFailure { f(it).operation.run(cb) }
        }
    })
}

interface SuspendOperation<I, O> {
    val connection: Connection
    suspend fun run(input: I): Result<O>
}