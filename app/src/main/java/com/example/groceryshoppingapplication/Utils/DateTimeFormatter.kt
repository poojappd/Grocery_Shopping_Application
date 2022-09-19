package com.example.groceryshoppingapplication.Utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatter {
    fun Date.formatDate(pattern: String): String {
        return SimpleDateFormat(pattern, Locale.ENGLISH).format(this.time)
    }

}