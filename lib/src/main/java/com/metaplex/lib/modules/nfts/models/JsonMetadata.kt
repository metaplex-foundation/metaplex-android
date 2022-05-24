package com.metaplex.lib.modules.nfts.models

import com.squareup.moshi.*

@JsonClass(generateAdapter = true)
data class JsonMetadata(
    val name: String?,
    val symbol: String?,
    val description: String?,
    val seller_fee_basis_points: Double?,
    val image: String?,
    val external_url: String?,
    val attributes: List<JsonMetadataAttribute>?,
    val properties: JsonMetadataProperties?,
)

@JsonClass(generateAdapter = true)
data class JsonMetadataProperties(
    val creators: List<JsonMetadataCreator>?,
    val files: List<JsonMetadataFile>?
)

sealed class Value {
    class number(val value: Double) : Value()
    class string(val value: String) : Value()
    object unkown : Value()
}

@JsonClass(generateAdapter = false)
data class JsonMetadataAttribute(
    val display_type: String?,
    val trait_type: String?,
    val value: Value?
)

class JsonMetadataAttributeAdapter {
    val topLevelKeys: JsonReader.Options =
        JsonReader.Options.of("display_type", "trait_type", "value")

    @FromJson
    fun fromJson(reader: JsonReader): JsonMetadataAttribute {
        var display_type: String? = null
        var trait_type: String? = null
        var value: Value? = null

        with(reader) {
            beginObject()
            while (hasNext()) {
                when (selectName(topLevelKeys)) {
                    0 -> {
                        try {
                            nextNull<Any>()
                        } catch (_: JsonDataException) {
                            display_type = nextString()
                        }
                    }
                    1 -> {
                        try {
                            nextNull<Any>()
                        } catch (_: JsonDataException) {
                            trait_type = nextString()
                        }
                    }
                    2 -> {
                        try {
                            nextNull<Any>()
                        } catch (_: JsonDataException) {
                            value = try {
                                Value.number(nextDouble())
                            } catch (_: JsonDataException) {
                                try {
                                    Value.string(nextString())
                                } catch (_: JsonDataException) {
                                    Value.unkown
                                }
                            }
                        }
                    }
                }
            }
            endObject()
        }
        return JsonMetadataAttribute(display_type, trait_type, value)
    }

    @ToJson
    fun toJson(attribute: JsonMetadataAttribute): String {
        throw NotImplementedError()
    }
}


@JsonClass(generateAdapter = true)
data class JsonMetadataCreator(
    val address: String?,
    val share: Double?
)

@JsonClass(generateAdapter = false)
data class JsonMetadataFile(
    val type: String?,
    val uri: String?
)

class JsonMetadataFileAdapter {
    val topLevelKeys: JsonReader.Options = JsonReader.Options.of("type", "uri")

    @FromJson
    fun fromJson(reader: JsonReader): JsonMetadataFile? {
        var type: String? = null
        var uri: String? = null
        with(reader) {
            try {
                beginObject()
            } catch (_: JsonDataException) {
                uri = nextString()
                return JsonMetadataFile(null, uri)
            }
            while (hasNext()) {
                when (selectName(topLevelKeys)) {
                    0 -> {
                        try {
                            nextNull<Any>()
                        } catch (_: JsonDataException) {
                            type = nextString()
                        }
                    }
                    1 -> {
                        try {
                            nextNull<Any>()
                        } catch (_: JsonDataException) {
                            uri = nextString()
                        }
                    }
                    -1 -> {
                        // Unknown name, skip it.
                        skipName()
                        skipValue()
                    }
                }
            }
            reader.endObject()
        }
        return JsonMetadataFile(
            type = type,
            uri = uri
        )
    }

    @ToJson
    fun toJson(attribute: JsonMetadataFile): String {
        throw NotImplementedError()
    }
}
