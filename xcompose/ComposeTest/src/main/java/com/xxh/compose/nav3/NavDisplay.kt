package com.xxh.compose.nav3

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavExample() {
    val backStack = remember {
        mutableStateListOf<Any>(HomePager)
    }

    /*NavDisplay(backStack, onBack = {
        backStack.removeLastOrNull()
    }, entryProvider = entryProvider {
        entry<HomePager>{
            ContentRed(title = "Home", onNext = {
                backStack.add(ProductDetail("Product XXH"))
            })
        }
       entry<ProductDetail>{
           ContentOrange(title = "Detail name="+it.name)
       }
    })*/

    NavDisplay(backStack = backStack, onBack = {
        backStack.removeLastOrNull()
    }) { key ->
        when (key) {
            is HomePager -> NavEntry(key) {
                ContentRed(title = "Home", onNext = {
                    backStack.add(ProductDetail("product1"))
                })
            }

            is ProductDetail -> NavEntry(key) {
                ContentOrange(title = "Detail name=" + key.id)
            }

            else -> NavEntry(Unit) {
                ContentRed(title = "Home")
            }
        }
    }
}

@Composable
fun NavExample2() {
    val backStack = rememberNavBackStack(HomePager)
    /* NavDisplay(backStack, onBack = {
         backStack.removeLastOrNull()
     }, entryProvider = entryProvider {
         entry<HomePager>{
             ContentRed(title = "Home", onNext = {
                 backStack.add(ProductDetail("Product XXH"))
             })
         }
         entry<ProductDetail>{
             ContentOrange(title = "Detail name="+it.name)
         }
     })
 */
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = {
            backStack.removeLastOrNull()
        }) { key ->
        when (key) {
            is HomePager -> NavEntry(key) {
                ContentRed(title = "Home", onNext = {
                    backStack.add(ProductDetail("product1"))
                })
            }

            is ProductDetail -> NavEntry(key) {
                ContentOrange(title = "Detail name=" + key.id)
            }

            else -> NavEntry(HomePager) {
                ContentRed(title = "Home")
            }
        }
    }
}

@Composable
fun NavExample3() {
    val backStack = rememberNavBackStack(HomePager)
    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator(),
            // Add a custom decorator
            remember {
                CustomNavEntryDecorator()
            }
        ),
        onBack = {
            backStack.removeLastOrNull()
        }, entryProvider = entryProvider {
            entry<HomePager> {
                ContentRed(title = "Home", onNext = {
                    backStack.add(ProductDetail("Product XXH"))
                })
            }
            entry<ProductDetail> {
                ContentOrange(title = "Detail name=" + it.id)
            }
        })
}

@Composable
fun NavExample4() {
    val backStack = rememberNavBackStack(ScreenA)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<ScreenA> {
                ContentOrange("This is Screen A") {
                    Button(onClick = { backStack.add(ScreenB) }) {
                        Text("Go to Screen B")
                    }
                }
            }
            entry<ScreenB> {
                ContentRed("This is Screen B") {
                    Button(onClick = { backStack.add(ScreenC) }) {
                        Text("Go to Screen C")
                    }
                }
            }
            entry<ScreenC>(
                metadata = NavDisplay.transitionSpec {
                    // Slide new content up, keeping the old content in place underneath
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(1000)
                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                } + NavDisplay.popTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                } + NavDisplay.predictivePopTransitionSpec {
                    // Slide old content down, revealing the new content in place underneath
                    EnterTransition.None togetherWith
                            slideOutVertically(
                                targetOffsetY = { it },
                                animationSpec = tween(1000)
                            )
                }
            ) {
                ContentYellow("This is Screen C")
            }
        },
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        }
    )
}