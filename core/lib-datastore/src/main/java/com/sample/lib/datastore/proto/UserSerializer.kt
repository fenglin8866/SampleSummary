package com.sample.lib.datastore.proto

import androidx.datastore.core.Serializer
import com.sample.data.datastore.proto.User
import java.io.InputStream
import java.io.OutputStream


object UserSerializer : Serializer<User> {

    override val defaultValue: User =
        User.getDefaultInstance()

    override suspend fun readFrom(
        input: InputStream
    ): User {
        return User.parseFrom(input)
    }

    override suspend fun writeTo(
        t: User,
        output: OutputStream
    ) {
        t.writeTo(output)
    }
}