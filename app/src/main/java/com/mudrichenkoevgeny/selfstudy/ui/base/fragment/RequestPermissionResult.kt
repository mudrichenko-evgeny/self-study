package com.mudrichenkoevgeny.selfstudy.ui.base.fragment

interface RequestPermissionResult {
    fun onPermissionGranted()
    fun onPermissionNotGranted()
}