package com.mudrichenko.evgeniy.selfstudy.extensions

fun Any.invokeDeclaredMethodWithDrawable(
    methodName: String,
    methodParameterTypes: Class<*>?,
    params: Any
) {
    javaClass.getDeclaredMethod(
        methodName,
        methodParameterTypes
    ).also {
        it.isAccessible = true
        it.invoke(this, params)
    }
}