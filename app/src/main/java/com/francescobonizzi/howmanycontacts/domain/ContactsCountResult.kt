package com.francescobonizzi.howmanycontacts.domain

class ContactsCountResult(
    val allContactsCount: Int,
    val telegramContactsCount : Int,
    val whatsAppContactsCount: Int)
{
    val telegramContactsPercentage : Int = ((telegramContactsCount / allContactsCount.toDouble()) * 100.0).toInt()
    val whatsAppContactsPercentage: Int =  ((whatsAppContactsCount / allContactsCount.toDouble()) * 100.0).toInt()
}