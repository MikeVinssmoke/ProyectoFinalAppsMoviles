package com.example.romero.proyectofinalappsmoviles.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val sdf     = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val dateOnly= SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatDate(millis: Long): String     = sdf.format(Date(millis))
    fun formatDateOnly(millis: Long): String = dateOnly.format(Date(millis))

    fun timeAgo(millis: Long): String {
        val diff = System.currentTimeMillis() - millis
        return when {
            diff < 60_000        -> "Hace un momento"
            diff < 3_600_000     -> "Hace ${diff / 60_000} min"
            diff < 86_400_000    -> "Hace ${diff / 3_600_000} h"
            diff < 172_800_000   -> "Ayer"
            else                 -> formatDateOnly(millis)
        }
    }
}