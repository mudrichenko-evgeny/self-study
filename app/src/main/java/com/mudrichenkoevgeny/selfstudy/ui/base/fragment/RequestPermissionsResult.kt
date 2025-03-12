package com.mudrichenkoevgeny.selfstudy.ui.base.fragment

import com.mudrichenkoevgeny.selfstudy.system.model.RequestPermissionData

interface RequestPermissionsResult {
    fun onPermissionsResultReceived(requestPermissionDataList: List<RequestPermissionData>)
}