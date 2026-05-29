package com.example.romero.proyectofinalappsmoviles.util

object Constants {
    // Cambia por la IP de tu servidor Flask
    // 10.0.2.2 apunta a localhost cuando usas el emulador
    const val BASE_URL = "https://proyectofinal2026.pythonanywhere.com/"

    const val PREFS_NAME          = "nodocivico_prefs"
    const val PREF_THEME          = "pref_theme"
    const val PREF_NOTIFICATIONS  = "pref_notifications"
    const val PREF_STATUS_FILTER  = "pref_status_filter"

    const val NOTIF_CHANNEL_ID    = "nodocivico_channel"
    const val NOTIF_CHANNEL_NAME  = "Nodo Cívico"

    const val ACTION_SYNC_MANUAL  = "com.example.romero.proyectofinalappsmoviles.ACTION_SYNC_MANUAL"
    const val ACTION_CLEAR_PENDING= "com.example.romero.proyectofinalappsmoviles.ACTION_CLEAR_PENDING"
    const val EXTRA_REMINDER_ID   = "reminder_id"
    const val EXTRA_REMINDER_MSG  = "reminder_message"

    const val STATUS_ABIERTO      = "ABIERTO"
    const val STATUS_EN_PROCESO   = "EN_PROCESO"
    const val STATUS_CERRADO      = "CERRADO"

    const val PRIORITY_BAJA       = "BAJA"
    const val PRIORITY_MEDIA      = "MEDIA"
    const val PRIORITY_ALTA       = "ALTA"

    const val PREF_USERNAME = "pref_username"
    const val PREF_IS_LOGGED = "pref_is_logged"
    const val HARDCODED_USER = "admin"
    const val HARDCODED_PASS = "1234"


}