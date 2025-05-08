package paging.android.example.com.pagingsample

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.dev.paging.data.CheeseDb
import com.sample.dev.paging.ui.CheeseViewModel

/**
 * A [ViewModelProvider.Factory] that provides dependencies to [com.sample.dev.paging.ui.CheeseViewModel],
 * allowing tests to switch out [CheeseDao] implementation via constructor injection.
 */
class CheeseViewModelFactory(
    private val app: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheeseViewModel::class.java)) {
            val cheeseDao = CheeseDb.get(app).cheeseDao()
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return CheeseViewModel(cheeseDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
