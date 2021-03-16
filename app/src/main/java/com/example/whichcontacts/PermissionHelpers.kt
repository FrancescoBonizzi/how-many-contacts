package com.example.whichcontacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionHelpers {

    companion object
    {
        const val PermissionSelectContacts = 1

        fun hasOrGetReadContactsPermission(fragment: Fragment) : Boolean
        {
            if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            {
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    PermissionSelectContacts)
                return false
            }

            return true
        }
    }
}