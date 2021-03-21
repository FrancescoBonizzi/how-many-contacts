package com.francescobonizzi.howmanycontacts

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHelpers {

    companion object {
        const val PermissionSelectContacts = 1

        fun hasOrGetReadContactsPermission(activity: Activity): Boolean {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PermissionSelectContacts
                )
                return false
            }

            return true
        }
    }
}