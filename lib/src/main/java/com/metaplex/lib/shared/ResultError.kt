package com.metaplex.lib.shared

import java.lang.Exception

typealias ResultError = Exception

sealed class ResultWithCustomError<out A, out E : ResultError> {

    abstract fun onSuccess(f: (A) -> Unit): ResultWithCustomError<A, E>

    abstract fun onFailure(f: (E) -> Unit): ResultWithCustomError<A, E>


    abstract fun throwOnFailure(): ResultWithCustomError<A, E>


    abstract fun <B> map(f: (A) -> B): ResultWithCustomError<B, E>

    abstract fun <E2 : ResultError> mapError(f: (E) -> E2): ResultWithCustomError<A, E2>


    abstract fun getOrThrows(): A

    companion object {

        fun <A> success(a: A): ResultWithCustomError<A, Nothing> {
            return Success(a)
        }

        fun failure(message: String): ResultWithCustomError<Nothing, ResultError> {
            return Failure(ResultError(message))
        }


        fun <E : ResultError> failure(exception: E): ResultWithCustomError<Nothing, E> {
            return Failure(exception)
        }

        fun <A> failable(f: () -> A): ResultWithCustomError<A, ResultError> {
            return try {
                success(f())
            } catch (e: Exception) {
                failure(e.message ?: "Unknown error")
            }
        }

        fun <A, B, C, E : ResultError> map2(ra: ResultWithCustomError<A, E>, rb: ResultWithCustomError<B, E>, f: (A, B) -> C): ResultWithCustomError<C, E> {
            return ra.flatMap { a -> rb.map { b -> f(a, b) } }
        }

        fun <A, B, C, D, E : ResultError> map3(ra: ResultWithCustomError<A, E>, rb: ResultWithCustomError<B, E>, rc: ResultWithCustomError<C, E>, f: (A, B, C) -> D): ResultWithCustomError<D, E> {
            return ra.flatMap { a -> rb.flatMap { b -> rc.map { c -> f(a, b, c) } } }
        }
    }
}

private class Success<A, E : ResultError>(val value: A) : ResultWithCustomError<A, E>() {
    override fun <B> map(f: (A) -> B): ResultWithCustomError<B, E> {
        return Success(f(value))
    }

    override fun <E2 : ResultError> mapError(f: (E) -> E2): ResultWithCustomError<A, E2> {
        return Success(value)
    }

    override fun onSuccess(f: (A) -> Unit): ResultWithCustomError<A, E> {
        f(this.value)
        return this
    }

    override fun onFailure(f: (E) -> Unit): ResultWithCustomError<A, E> { // nothing to do
        return this
    }

    override fun throwOnFailure(): ResultWithCustomError<A, E> { // nothing to do
        return this
    }

    override fun getOrThrows(): A {
        return value
    }
}

private class Failure<A, E : ResultError> internal constructor(e: E) : ResultWithCustomError<A, E>() {
    val exception: E = e

    override fun <B> map(f: (A) -> B): ResultWithCustomError<B, E> {
        return Failure(exception)
    }

    override fun <E2 : ResultError> mapError(f: (E) -> E2): ResultWithCustomError<A, E2> {
        return Failure(f(exception))
    }

    override fun onSuccess(f: (A) -> Unit): ResultWithCustomError<A, E> {
        return this
    }

    override fun onFailure(f: (E) -> Unit): ResultWithCustomError<A, E> {
        f(this.exception)
        return this
    }

    override fun throwOnFailure(): ResultWithCustomError<A, E> {
        throw exception
    }

    override fun getOrThrows(): Nothing {
        throw exception
    }
}

fun <A, E : ResultError> ResultWithCustomError<A, E>.getOrDefault(defaultValue: A): A {
    return when (this) {
        is Success -> this.value
        is Failure -> defaultValue
    }
}

fun <A, E : ResultError, E2 : ResultError> ResultWithCustomError<A, E>.recover(f: (E) -> ResultWithCustomError<A, E2>): ResultWithCustomError<A, E2> {
    return when (this) {
        is Success -> ResultWithCustomError.success(this.value)
        is Failure -> f(this.exception)
    }
}

fun <A, B, E : ResultError, E2 : E> ResultWithCustomError<A, E>.flatMap(f: (A) -> ResultWithCustomError<B, E2>): ResultWithCustomError<B, E> {
    return when (this) {
        is Success -> f(this.value)
        is Failure -> ResultWithCustomError.failure(this.exception)
    }
}

fun <A, E : ResultError> A?.toResult(otherwise: () -> E): ResultWithCustomError<A, E> {
    return if (this != null) ResultWithCustomError.success(this) else ResultWithCustomError.failure(otherwise())
}