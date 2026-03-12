package com.sample.lib.datastore.migration

import android.content.Context
import androidx.datastore.migrations.SharedPreferencesView
import androidx.datastore.preferences.SharedPreferencesMigration


object SharedPreferencesMigrationUtil {

    // Since we're migrating from SharedPreferences, add a migration based on the
    // SharedPreferences name
    fun getPreferenceMigrationList(context: Context, sharedPreferencesName: String) =
        listOf(SharedPreferencesMigration(context, sharedPreferencesName))

    /**
     *
     * val Context.dataStore: DataStore<UserPreferences> by dataStore(
     *     fileName = USER_PREFERENCES_NAME,
     *     serializer = UserPreferencesSerializer,
     *     produceMigrations = { context ->
     *         listOf(
     *             SharedPreferencesMigration(
     *                 context,
     *                 USER_PREFERENCES_NAME
     *             ) { sharedPrefs: SharedPreferencesView, currentData: UserPreferences ->
     *                 // Define the mapping from SharedPreferences to UserPreferences
     *                 if (currentData.sortOrder == SortOrder.UNSPECIFIED) {
     *                     currentData.toBuilder().setSortOrder(
     *                         SortOrder.valueOf(
     *                             sharedPrefs.getString(SORT_ORDER_KEY, SortOrder.NONE.name)!!
     *                         )
     *                     ).build()
     *                 } else {
     *                     currentData
     *                 }
     *             }
     *         )
     *     }
     * )
     */
    fun <T> getProtoMigrationList(
        context: Context,
        sharedPreferencesName: String,
        migrate: (SharedPreferencesView, T) -> T
    ) = listOf(
        androidx.datastore.migrations.SharedPreferencesMigration(
            context,
            sharedPreferencesName,
            migrate = migrate
        )
    )

}