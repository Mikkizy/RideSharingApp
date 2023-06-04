package com.ukaka.ridesharingapp.presentation.permissions

sealed class PermissionAction {
    object PermissionGranted : PermissionAction()
    object PermissionDenied : PermissionAction()
}
