package com.example.whichcontacts.logging

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.example.whichcontacts.BuildConfig

/** Logger che scrive in LogCat e su FirebaseCrashlytics */
class DebugAndFireBaseLogger
{
    fun error(message: String)
    {
        var messageToLog = message
        if (messageToLog.isBlank())
        {
            messageToLog = "Generic exception without any message"
        }

        try
        {
            if (BuildConfig.DEBUG)
            {
                Log.e(getCallerInfo(), messageToLog)
            }

            FirebaseCrashlytics.getInstance().recordException(Exception(messageToLog))
        }
        catch (e: Exception)
        {
            // Il logger non deve rompere l'applicazione, MAI
        }
    }

    fun error(exception: Exception)
    {
        try
        {
            if (BuildConfig.DEBUG)
            {
                Log.e(getCallerInfo(), "Exception thrown!", exception)
            }

            FirebaseCrashlytics.getInstance().recordException(exception)
        }
        catch (e: Exception)
        {
            // Il logger non deve rompere l'applicazione, MAI
        }
    }

    fun error(message: String, exception: Exception)
    {
        try
        {
            if (BuildConfig.DEBUG)
            {
                Log.e(getCallerInfo(), message, exception)
            }

            FirebaseCrashlytics.getInstance().recordException(exception)
        }
        catch (e: Exception)
        {
            // Il logger non deve rompere l'applicazione, MAI
        }
    }

    fun event(context: Context, eventName: String, eventProperties: Bundle)
    {
        FirebaseAnalytics.getInstance(context).logEvent(eventName, eventProperties)
    }

    private fun getCallerInfo(): String
    {
        val caller = Thread.currentThread().stackTrace[4]
        return "${caller.className}.${caller.methodName}"
    }
}