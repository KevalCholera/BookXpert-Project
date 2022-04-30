package com.example.bookxpertproject.base

open class BaseViewState(private val baseStatus: Status, private val baseError: String?) {
    fun isLoading() = baseStatus == Status.LOADING
    fun isEmpty() = baseStatus == Status.FAIL
    fun isSuccess() = baseStatus == Status.SUCCESS
    fun getErrorMessage() = baseError
    fun shouldShowErrorMessage() = baseError != null
}