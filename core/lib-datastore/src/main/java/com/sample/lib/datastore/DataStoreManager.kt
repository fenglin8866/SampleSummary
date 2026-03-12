package com.sample.lib.datastore

import com.sample.lib.datastore.preferences.SettingsStore
import com.sample.lib.datastore.proto.UserStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    val settings: SettingsStore,
    val user: UserStore
)