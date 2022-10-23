package com.mudrichenko.evgeniy.selfstudy.ui.base.fragment

interface RequestPermissionResult {
    fun onPermissionGranted()
    fun onPermissionNotGranted()
}