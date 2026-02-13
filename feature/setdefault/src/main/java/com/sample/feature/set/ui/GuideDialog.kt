package com.sample.feature.set.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog

class GuideDialog(context: Context) : Dialog(context) {
    private var positiveButtonListener: OnPositiveButtonClickListener? = null

    fun setOnPositiveButtonClickListener(listener: OnPositiveButtonClickListener) {
        this.positiveButtonListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("引导弹窗")
        builder.setMessage("这是一个带按钮的弹窗示例")

        builder.setPositiveButton("确定") { _, _ ->
            positiveButtonListener?.onPositiveClick()
            dismiss()
        }

        builder.setNegativeButton("取消") { _, _ ->
            dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}

interface OnPositiveButtonClickListener{
    fun onPositiveClick()
}