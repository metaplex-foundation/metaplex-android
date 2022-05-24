package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.storage.StorageDriverError
import com.metaplex.lib.modules.nfts.models.JsonMetadata
import com.metaplex.lib.modules.nfts.models.JsonMetadataAttributeAdapter
import com.metaplex.lib.modules.nfts.models.JsonMetadataFileAdapter
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.ResultWithCustomError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import java.net.URL

class JsonMetadataTask(private val metaplex: Metaplex, val nft: NFT) {
    fun use(onComplete: (ResultWithCustomError<JsonMetadata, StorageDriverError>) -> Unit){
        val url = try {
            URL(nft.metadataAccount.data.uri)
        } catch (e: Exception){
            onComplete(ResultWithCustomError.failure(StorageDriverError.InvalidURL))
            return
        }
        metaplex.storage().download(url){
            it.onSuccess { response ->
                val moshi: Moshi = Moshi.Builder().add(JsonMetadataAttributeAdapter()).add(JsonMetadataFileAdapter()).build()
                val jsonAdapter: JsonAdapter<JsonMetadata> = moshi.adapter(JsonMetadata::class.java)
                try {
                    jsonAdapter.fromJson(response.data)?.let { jsonMetadata ->
                        onComplete(ResultWithCustomError.success(jsonMetadata))
                    }?:run {
                        onComplete(ResultWithCustomError.failure(StorageDriverError.JsonParsedError))
                    }
                } catch (e: JsonDataException){
                    onComplete(ResultWithCustomError.failure(StorageDriverError.CanNotParse(e)))
                }
            }.onFailure { error ->
                onComplete(ResultWithCustomError.failure(error))
            }
        }
    }
}