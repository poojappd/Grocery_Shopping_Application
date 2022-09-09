package com.example.groceryshoppingapplication

import kotlin.random.Random

object OtpGeneratorUtil {
    fun generateOtp() = Random.nextInt(1000,9999).toString()
    }

