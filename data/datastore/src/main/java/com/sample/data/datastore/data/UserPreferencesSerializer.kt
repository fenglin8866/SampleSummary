package com.sample.data.datastore.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.sample.data.datastore.proto.UserPreferences
import kotlinx.serialization.SerializationException
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences
        get() = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Settings", serialization)
        }
    }

    override suspend fun writeTo(
        t: UserPreferences,
        output: OutputStream
    ) {
        return t.writeTo(output)
    }
}