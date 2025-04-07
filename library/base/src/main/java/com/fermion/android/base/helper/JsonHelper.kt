package com.fermion.android.base.helper

import android.util.JsonReader
import android.util.JsonToken
import android.util.MalformedJsonException
import java.io.IOException
import java.io.Reader
import java.io.StringReader

/**
 * Created by Bhavesh Auodichya.
 *
 * This class check for valid JsonString
 *
 * Call isJsonValid to check where given string structure is acceptable json structure
 * */

class JsonHelper {
    @Throws(IOException::class)
    fun isJsonValid(json: String): Boolean {
        return isJsonValid(StringReader(json))
    }

    @Throws(IOException::class)
    private fun isJsonValid(reader: Reader): Boolean {
        return isJsonValid(JsonReader(reader))
    }

    @Throws(IOException::class)
    private fun isJsonValid(jsonReader: JsonReader): Boolean {
        return try {
            var token: JsonToken?
            while (jsonReader.peek()
                    .also { token = it } != JsonToken.END_DOCUMENT && token != null
            ) {
                when (token) {
                    JsonToken.BEGIN_ARRAY -> jsonReader.beginArray()
                    JsonToken.END_ARRAY -> jsonReader.endArray()
                    JsonToken.BEGIN_OBJECT -> jsonReader.beginObject()
                    JsonToken.END_OBJECT -> jsonReader.endObject()
                    JsonToken.NAME -> jsonReader.nextName()
                    JsonToken.STRING, JsonToken.NUMBER, JsonToken.BOOLEAN, JsonToken.NULL -> jsonReader.skipValue()
                    JsonToken.END_DOCUMENT -> break
                    else -> throw AssertionError(token)
                }
            }
            true
        } catch (ignored: MalformedJsonException) {
            false
        }
    }
}