package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

import com.mudrichenko.evgeniy.selfstudy.system.model.RequestPermissionData

interface RequestPermissionsResult {
    fun onPermissionsResultReceived(requestPermissionDataList: List<RequestPermissionData>)
}