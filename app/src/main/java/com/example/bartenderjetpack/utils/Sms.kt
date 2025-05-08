package com.example.bartenderjetpack.utils

import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast


fun sendSms(context: Context, phoneNumber: String, message: String) {
    try {
        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        Toast.makeText(context, "Wysłano SMS!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Błąd wysyłania SMS: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

fun getPhoneNumberFromUri(context: Context, contactUri: Uri): String? {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(contactUri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val id = it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
            val hasPhone = it.getInt(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
            if (hasPhone) {
                val phones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(id),
                    null
                )
                phones?.use { phoneCursor ->
                    if (phoneCursor.moveToFirst()) {
                        return phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                }
            }
        }
    }
    return null
}

