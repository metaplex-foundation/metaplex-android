package com.metaplex.lib.modules.nfts

import com.metaplex.lib.Metaplex
import com.metaplex.lib.drivers.storage.StorageDriverError
import com.metaplex.lib.modules.nfts.models.JsonMetadata
import com.metaplex.lib.modules.nfts.models.JsonMetadataAttributeAdapter
import com.metaplex.lib.modules.nfts.models.NFT
import com.metaplex.lib.shared.ResultWithCustomError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.net.URL

class JsonMetadataTask(private val metaplex: Metaplex, val nft: NFT) {
    fun use(onComplete: (ResultWithCustomError<JsonMetadata, StorageDriverError>) -> Unit){
        var url = try {
            URL(nft.metadataAccount.data.uri)
        } catch (e: Exception){
            onComplete(ResultWithCustomError.failure(StorageDriverError.InvalidURL))
            return
        }
        metaplex.storage().download(url){
            it.onSuccess { response ->
                val moshi: Moshi = Moshi.Builder().add(JsonMetadataAttributeAdapter()).build()
                val jsonAdapter: JsonAdapter<JsonMetadata> = moshi.adapter(JsonMetadata::class.java)
                jsonAdapter.fromJson(response.data)?.let { jsonMetadata ->
                    onComplete(ResultWithCustomError.success(jsonMetadata))
                }?:run {
                    onComplete(ResultWithCustomError.failure(StorageDriverError.JsonParsedError))
                }
            }.onFailure { error ->
                onComplete(ResultWithCustomError.failure(error))
            }
        }
    }
}