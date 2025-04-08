package com.sample.summary.test

sealed class UIIntent {
    object OnLoginClicked : UIIntent()
    object OnNavigateToHome : UIIntent()
}
