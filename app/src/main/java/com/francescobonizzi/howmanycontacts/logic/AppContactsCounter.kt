package com.francescobonizzi.howmanycontacts.logic

import android.app.Activity
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.core.database.getStringOrNull
import com.francescobonizzi.howmanycontacts.domain.Contact
import com.francescobonizzi.howmanycontacts.domain.ContactsCountResult

class AppContactsCounter {

    private fun getAllContacts(resolver: ContentResolver): List<Contact> {

        val contacts = mutableListOf<Contact>()
        val contactsQuery = resolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY, ContactsContract.RawContacts.ACCOUNT_TYPE),
            null,
            null,
            null
        )

        contactsQuery?.use { c ->
            while (c.moveToNext()) {
                val accountDisplayName = c.getStringOrNull(0)
                val accountType = c.getStringOrNull(1)

                if (accountDisplayName == null) {
                    continue
                }

                contacts.add(Contact(
                    displayName = accountDisplayName,
                    type = accountType ?: ""
                ))
            }
        }

        return contacts
    }

    fun countContacts(activity: Activity) : ContactsCountResult {

        val allContacts = getAllContacts(activity.contentResolver)
            .sortedBy { it.displayName } // Just for debugging purposes, it's easier to see each contact App

        return ContactsCountResult(
            // Count only the unique contact name
            allContactsCount = allContacts.distinctBy { it.displayName }.count(),
            telegramContactsCount = allContacts.count {
                it.type == "org.telegram.messenger"
            },
            whatsAppContactsCount = allContacts.count {
                it.type == "com.whatsapp"
            }
        )
    }

}