package com.mudrichenkoevgeny.selfstudy.data.model.`object`

sealed class DataResponse<T> {

    class Successful<T>(val data: T): DataResponse<T>()

    class Error<T>(val appError: AppError): DataResponse<T>()

    fun isSuccessful(): Boolean {
        return this is Successful
    }

    fun data(): T? {
        return if (this is Successful) this.data else null
    }

    fun appError(): AppError? {
        return if (this is Error) this.appError else null
    }

}