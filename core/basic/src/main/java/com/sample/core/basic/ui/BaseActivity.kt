package com.sample.core.basic.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.sample.core.basic.R
import com.sample.core.basic.eventbus.GlobalEvent
import com.sample.core.basic.eventbus.GlobalEventBus
import kotlinx.coroutines.launch

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    // Binding object instance corresponding to the fragment_add_item.xml layout
    // This property is non-null between the onCreateView() and onDestroyView() lifecycle callbacks,
    // when the view hierarchy is attached to the fragment
    private var _binding: T? = null
    protected val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=bindView(layoutInflater)
        setContentView(mBinding.root)
        globalEventReceive()
        setupViews()
    }

    abstract fun bindView(inflater: LayoutInflater): T

    open fun setupViews(){

    }

    fun globalEventReceive(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                GlobalEventBus.events.collect { event ->
                    when (event) {
                        is GlobalEvent.Toast -> {
                            Toast.makeText(this@BaseActivity, event.message, Toast.LENGTH_SHORT).show()
                        }
                        is GlobalEvent.Snackbar -> {
                           // Snackbar.make(findViewById(R.id.content), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is GlobalEvent.Navigate -> {
                            Toast.makeText(this@BaseActivity, "跳转至：${event.route}", Toast.LENGTH_SHORT).show()
                            // startActivity(Intent(...)) or navController.navigate(event.route)
                        }
                    }
                }
            }
        }
    }
}