package com.example.dbm.data.converters

import android.net.Uri
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class UriTypeAdapter : JsonSerializer<Uri>, JsonDeserializer<Uri> {
    override fun serialize(src: Uri?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        // Always serialize the Uri as its string representation.
        return JsonPrimitive(src?.toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Uri {
        // Expecting a primitive, but in case we get an object, handle it gracefully.
        return if (json.isJsonPrimitive) {
            Uri.parse(json.asString)
        } else if (json.isJsonObject) {
            // If a JsonObject is encountered, try to extract a string value from a known key.
            // For example, if the object was previously stored with a "uri" field:
            val obj = json.asJsonObject
            if (obj.has("uri")) {
                Uri.parse(obj.get("uri").asString)
            } else {
                // Fallback: convert the entire object to a string.
                Uri.parse(obj.toString())
            }
        } else {
            throw JsonParseException("Unexpected JSON type for Uri: ${json.javaClass}")
        }
    }
}
