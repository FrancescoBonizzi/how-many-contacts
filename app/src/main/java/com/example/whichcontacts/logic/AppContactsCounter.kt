package com.example.whichcontacts.logic

import android.app.Activity
import android.content.ContentResolver
import android.provider.ContactsContract
import com.example.whichcontacts.domain.Contact
import com.example.whichcontacts.domain.ContactsCountResult
import java.lang.Exception

class AppContactsCounter {

    private fun getAllContacts(resolver: ContentResolver): List<Contact> {

        val contacts = mutableListOf<Contact>()
        val contactsQuery = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        contactsQuery?.use { c ->

            while (c.moveToNext()) {
                val id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                contacts.add(Contact(id, "telegram"))
            }
        }

        return contacts
    }

    fun countContacts(activity: Activity) : ContactsCountResult {
        val allContacts = getAllContacts(activity.contentResolver)
        return ContactsCountResult(
            allContacts.count(),
            allContacts.count{ it.type == "telegram" }
        )
    }

    private fun countContactsOf(activity: Activity) : Int {

        val contactQuery = activity.contentResolver.query(
            ContactsContract.Data.CONTENT_URI, null, null, null, null
        )

        contactQuery.use { c ->

            if (c != null && c.moveToFirst())
            {
                return c.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)
            }

        }

        throw Exception("Cannot get contacts data")
    }

}