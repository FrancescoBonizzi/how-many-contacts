package com.francescobonizzi.howmanycontacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    private fun showLoading() {
        binding.layoutSignal.visibility = View.GONE
        binding.layoutTelegram.visibility = View.GONE
        binding.layoutWhatsApp.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.layoutSignal.visibility = View.VISIBLE
        binding.layoutTelegram.visibility = View.VISIBLE
        binding.layoutWhatsApp.visibility = View.VISIBLE
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
            // Mostrare l'errore da qualche parte... farlo meglio, nascondendo il risultato e mettendo il messaggio bello grosso
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshData() {
        try {

            showLoading()

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

            hideLoading()

        } catch (ex: Exception) {
            logger.error(ex)
            // Mostrare l'errore da qualche parte... farlo meglio, nascondendo il risultato e mettendo il messaggio bello grosso
            Toast.makeText(this, ex.message, Toast.LENGTH_SHORT).show()
        }
    }

}