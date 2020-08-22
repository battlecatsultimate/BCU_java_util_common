package common.io.json

import com.google.gson.JsonElement
import common.io.json.JsonExceptionimport

class JsonException(type: JsonException.Type?, elem: JsonElement?, str: String?) : Exception(str) {
    enum class Type {
        TYPE_MISMATCH, TAG, UNEXPECTED_NULL, FUNC, INTERNAL, UNDEFINED
    }

    companion object {
        private const val serialVersionUID = 6451473277106188516L
    }
}
