package com.metaplex.lib.solana

import com.metaplex.lib.programs.token_metadata.MasterEditionAccountJsonAdapterFactory
import com.metaplex.lib.programs.token_metadata.MasterEditionAccountRule
import com.metaplex.lib.programs.token_metadata.accounts.*
import com.metaplex.lib.shared.AccountPublicKeyJsonAdapterFactory
import com.metaplex.lib.shared.AccountPublicKeyRule
import com.solana.api.*
import com.solana.core.PublicKey
import com.solana.models.*
import com.solana.models.buffer.BufferInfo
import com.solana.networking.NetworkingRouter
import com.solana.networking.NetworkingRouterConfig
import com.solana.networking.RPCEndpoint
import com.solana.vendor.borshj.BorshCodable

interface Connection {
    fun <T: BorshCodable> getProgramAccounts(account: PublicKey,
                                             programAccountConfig: ProgramAccountConfig,
                                             decodeTo: Class<T>,
                                             onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
    )

    fun <T: BorshCodable> getAccountInfo(account: PublicKey,
                              decodeTo: Class<T>,
                              onComplete: ((Result<BufferInfo<T>>) -> Unit))

    fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    )
    fun getSignatureStatuses(signatures: List<String>,
                             configs: SignatureStatusRequestConfiguration?,
                             onComplete: ((Result<SignatureStatus>) -> Unit))
}

class SolanaConnectionDriver(endpoint: RPCEndpoint): Connection {
    val solanaRPC: Api = Api(
        NetworkingRouter(endpoint,
        config = NetworkingRouterConfig(
            listOf(
                MetadataAccountRule(),
                MetaplexDataRule(),
                MetaplexCollectionRule(),
                AccountPublicKeyRule(),
                MasterEditionAccountRule(),
                MetaplexCreatorRule()
            ),
            listOf(
                MetadataAccountJsonAdapterFactory(),
                MetaplexDataAdapterJsonAdapterFactory(),
                AccountPublicKeyJsonAdapterFactory(),
                MasterEditionAccountJsonAdapterFactory()
            )
        ))
    )

    override fun <T: BorshCodable> getProgramAccounts(account: PublicKey,
                                                      programAccountConfig: ProgramAccountConfig,
                                                      decodeTo: Class<T>,
                                                      onComplete: (Result<List<ProgramAccount<T>>>) -> Unit
    ){
        solanaRPC.getProgramAccounts(account, programAccountConfig, decodeTo, onComplete)
    }

    override fun <T : BorshCodable> getAccountInfo(
        account: PublicKey,
        decodeTo: Class<T>,
        onComplete: (Result<BufferInfo<T>>) -> Unit
    ) {
        solanaRPC.getAccountInfo(account, decodeTo) { result ->
            result.onSuccess { onComplete(Result.success(it)) }
            result.onFailure { onComplete(Result.failure(it)) }
        }
    }

    override fun <T: BorshCodable> getMultipleAccountsInfo(
        accounts: List<PublicKey>,
        decodeTo: Class<T>,
        onComplete: ((Result<List<BufferInfo<T>?>>) -> Unit)
    ){
        solanaRPC.getMultipleAccounts(accounts, decodeTo, onComplete)
    }

    override fun getSignatureStatuses(signatures: List<String>,
                             configs: SignatureStatusRequestConfiguration?,
                             onComplete: ((Result<SignatureStatus>) -> Unit)) {
        solanaRPC.getSignatureStatuses(signatures, configs, onComplete)
    }
}