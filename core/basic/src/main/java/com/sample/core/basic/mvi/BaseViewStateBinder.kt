package com.sample.core.basic.mvi

interface BaseViewStateBinder<B, S> {
    fun bind(binding: B, state: S)
}