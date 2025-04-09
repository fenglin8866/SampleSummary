package com.sample.core.basic.ui

interface ViewStateBinder<B, S> {
    fun bind(binding: B, state: S)
}