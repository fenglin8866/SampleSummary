package com.xxh.lang.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "手机系统国家和语言为：\n ${Locale.getDefault().country}-${Locale.getDefault().language}"
    }
    val text: LiveData<String> = _text

    fun getNewTextStr() {
        _text.value = "点击按钮获取的当前国家和语言为：\n ${Locale.getDefault().country}-${Locale.getDefault().language}"
    }
}