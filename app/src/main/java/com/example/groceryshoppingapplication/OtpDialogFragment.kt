package com.example.groceryshoppingapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*


class OtpDialogFragment :DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_otp_dialog, container,false)
        if (dialog != null && dialog!!.getWindow() != null) {
            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            getDialog()!!.getWindow()!!.requestFeature(Window.FEATURE_NO_TITLE);
        }

        return view

    }

    override fun onResume() {
        super.onResume()
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val otp = OtpGeneratorUtil.generateOtp()
                view?.findViewById<TextView>(R.id.otp_1)?.text = otp[0].toString()
                view?.findViewById<TextView>(R.id.otp_2)?.text = otp[1].toString()
                view?.findViewById<TextView>(R.id.otp_3)?.text = otp[2].toString()
                view?.findViewById<TextView>(R.id.otp_4)?.text = otp[3].toString()
                dismiss()
                timer.cancel()
            }
        }, 1000)


    }
}