package com.example.beardwulf.reva

import com.google.gson.JsonDeserializer
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.lang.reflect.Type


class GenericDeserializer<T> : JsonDeserializer<T> {

    /**
     * Get the "content" element from the parsed JSON
     * Deserialize it. You use a new instance of Gson to avoid infinite recursion to this deserializer
     */
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): T {
        val content = json!!.asJsonObject.get("content")
        return Gson().fromJson<T>(content, typeOfT)
    }
}