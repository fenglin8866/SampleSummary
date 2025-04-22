package com.sample.core.basic.ui.view

/**
 * ViewStateBindingAdapter
 * View与State的绑定器，
 * 关注点分离，将View绑定与Activity/Fragment分离。
 */
interface ViewStateBinder<B, S> {
    fun bind(binding: B, state: S)
}