package com.mudrichenkoevgeny.selfstudy.extensions

import com.mudrichenkoevgeny.selfstudy.system.model.RequestPermissionData

fun Map<String, Boolean>.toRequestPermissionDataList(): List<RequestPermissionData> {
    val list: MutableList<RequestPermissionData> = mutableListOf()
    map { entry ->
        list.add(
            RequestPermissionData(
                permission = entry.key,
                granted = entry.value
            )
        )
    }
    return list
}

fun List<RequestPermissionData>.isPermissionGranted(permission: String): Boolean {
    return find { it.permission == permission }?.granted == true
}