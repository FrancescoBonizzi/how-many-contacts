package com.francescobonizzi.howmanycontacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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

    private fun hideMainContent() {
        binding.txtContactsCount.visibility = View.GONE
        binding.layoutSignal.visibility = View.GONE
        binding.layoutTelegram.visibility = View.GONE
        binding.layoutWhatsApp.visibility = View.GONE
    }

    private fun showMainContent() {
        binding.txtContactsCount.visibility = View.VISIBLE
        binding.layoutSignal.visibility = View.VISIBLE
        binding.layoutTelegram.visibility = View.VISIBLE
        binding.layoutWhatsApp.visibility = View.VISIBLE
    }

    private fun showError(message : String) {
        binding.txtError.text = message
        binding.txtError.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.txtError.visibility = View.GONE
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
            showError(ex.message!!)
            hideMainContent()
        }
    }

    private fun refreshData() {
        try {

            hideMainContent()

            if (!PermissionHelpers.hasOrAskReadContactsPermission(this)) {
                throw Exception("Without read contacts permission I cannot count your contacts")
            }

            val appContactsCounter = AppContactsCounter()
            val contactsCountResult = appContactsCounter.countContacts(this)

            binding.txtContactsCount.text = getString(R.string.activity_main_contacts_count_template, contactsCountResult.allContactsCount)
            binding.txtTelegramContactsCount.text = getString(R.string.activity_main_contacts_percentage_template, contactsCountResult.telegramContactsCount, contactsCountResult.telegramContactsPercentage)
            binding.txtSignalContactsCount.text =getString(R.string.activity_main_contacts_percentage_template, contactsCountResult.signalContactsCount, contactsCountResult.signalContactsPercentage)
            binding.txtWhatsAppContactsCount.text =getString(R.string.activity_main_contacts_percentage_template, contactsCountResult.whatsAppContactsCount, contactsCountResult.whatsAppContactsPercentage)

            showMainContent()

        } catch (ex: Exception) {
            logger.error(ex)
            showError(ex.message!!)
            hideMainContent()
        }
    }

}