package com.sample.core.basic2.viewmodel

typealias SimpleStateViewModel<S> = BaseViewModel<S>
typealias IntentViewModel<S, I> = BaseIntentViewModel<S, I>
typealias EventViewModel<S, E> = BaseEventViewModel<S, E>
typealias IntentEventViewModel<S, I, E> = BaseIntentEventViewModel<S, I, E>
typealias DefaultEventViewModel<S, I> = BaseDefaultEventViewModel<S, I>