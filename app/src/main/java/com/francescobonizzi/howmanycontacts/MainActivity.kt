package com.francescobonizzi.howmanycontacts

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.francescobonizzi.howmanycontacts.databinding.ActivityMainBinding
import com.francescobonizzi.howmanycontacts.logging.DebugAndFireBaseLogger
import com.francescobonizzi.howmanycontacts.logic.AppContactsCounter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val logger = DebugAndFireBaseLogger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRefresh.setOnClickListener {
            refreshData()
        }

        refreshData()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        try {

            if ((grantResults.isNotEmpty()
                        && requestCode == PermissionHelpers.PermissionSelectContacts
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                refreshData()
            } else {
                throw Exception("Without read contacts permission I cannot count your contacts")
            }

        } catch (ex: Exception) {
            logger.error(ex)
            // Mostrare l'errore da qualche parte
        }
    }

    private fun refreshData() {
        try {

            if (!PermissionHelpers.hasOrAskReadContactsPermission(this)) {
                return
            }

            val appContactsCounter = AppContactsCounter()
            val contactsCountResult = appContactsCounter.countContacts(this)

            binding.txtContactsCount.text = "${contactsCountResult.allContactsCount} contacts"
            binding.txtTelegramContactsCount.text =
                "${contactsCountResult.telegramContactsCount} contacts (${contactsCountResult.telegramContactsPercentage}%)"
            binding.txtWhatsAppContactsCount.text =
                "${contactsCountResult.whatsAppContactsCount} contacts (${contactsCountResult.whatsAppContactsPercentage}%)"
            binding.txtSignalContactsCount.text =
                "${contactsCountResult.signalContactsCount} contacts (${contactsCountResult.signalContactsPercentage}%)"

        } catch (ex: Exception) {
            logger.error(ex)
            // Mostrare l'errore da qualche parte
        }
    }

}