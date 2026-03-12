package com.sample.lib.datastore.proto

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.distinctUntilChanged
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        DataStoreFactory.create(
            serializer = UserSerializer,
            produceFile = {
                File(context.filesDir, "user.pb")
            }
        )

    val userFlow =
        dataStore.data
            .distinctUntilChanged()

    suspend fun updateToken(token: String) {
        dataStore.updateData { user ->
            user.toBuilder()
                .setToken(token)
                .build()
        }
    }
}