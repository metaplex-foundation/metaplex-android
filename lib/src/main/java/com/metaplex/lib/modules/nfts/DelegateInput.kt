package com.metaplex.lib.modules.nfts

import com.metaplex.lib.experimental.jen.tokenmetadata.DelegateArgs
import com.metaplex.lib.modules.nfts.NftPdasClient.Companion.metadataDelegateRecord
import com.solana.core.Account
import com.solana.core.PublicKey

interface SplitTypeAndData<T, U>
interface MetadataDelegateInputWithData<T: AccountOrPK>: SplitTypeAndData<DelegateArgs, MetadataDelegateType>{
    val delegate: T
    val updateAuthority: PublicKey
    val typeA: DelegateArgs
    val typeU: MetadataDelegateType
}

interface TokenDelegateInputWithData<T: AccountOrPK>: SplitTypeAndData<DelegateArgs, TokenDelegateType>{
    val delegate: T
    val owner: PublicKey
    val token: PublicKey?
    val typeA: DelegateArgs
    val typeU: TokenDelegateType
}

interface MetadataDelegateInput<T: AccountOrPK> : MetadataDelegateInputWithData<T>{
    override val delegate: T
    override val updateAuthority: PublicKey
}
data class MetadataDelegateInputConcrete<T: AccountOrPK>(
    override val delegate: T,
    override val updateAuthority: PublicKey,
    override val typeA: DelegateArgs,
    override val typeU: MetadataDelegateType
): MetadataDelegateInput<T>

interface TokenDelegateInput<T: AccountOrPK>: TokenDelegateInputWithData<T>{
    override val delegate: T
    override val owner: PublicKey
    override val token: PublicKey?
}
data class TokenDelegateInputConcrete<T: AccountOrPK>(
    override val typeA: DelegateArgs,
    override val typeU: TokenDelegateType,
    override val delegate: T,
    override val owner: PublicKey,
    override val token: PublicKey?
): TokenDelegateInput<T>

typealias DelegateInputSigner = DelegateInput<AccountOrPK.isAccount>
sealed class DelegateInput<T: AccountOrPK>{
    data class MetadataDelegateInputWithDataOption<T: AccountOrPK>(
        val metadataDelegateInputWithData: MetadataDelegateInputWithData<T>
    ): DelegateInput<T>()
    data class TokenDelegateInputWithDataOption<T: AccountOrPK>(
        val tokenDelegateInputWithData: TokenDelegateInputWithData<T>
    ): DelegateInput<T>()
}

data class ParsedTokenMetadataDelegate<T: AccountOrPK>(
    val delegate: T,
    val approver: PublicKey,
    val delegateRecord: PublicKey,
    val tokenAccount: PublicKey?,
    val isTokenDelegate: Boolean
)
sealed class AccountOrPK{
    data class isAccount(val account: Account): AccountOrPK()
    data class isPublickKey(val publicKey: PublicKey): AccountOrPK()

    fun publicKey(): PublicKey = when(this){
        is isAccount -> this.publicKey()
        is isPublickKey -> this.publicKey
    }
}

fun parseTokenMetadataDelegateInput(
    mint: PublicKey,
    input: DelegateInput<AccountOrPK>,
): ParsedTokenMetadataDelegate<AccountOrPK>{
    return when(input){
        is DelegateInput.MetadataDelegateInputWithDataOption -> {
            val delegatePublicKey = input.metadataDelegateInputWithData.delegate.publicKey()
            ParsedTokenMetadataDelegate(
                isTokenDelegate= false,
                delegate= input.metadataDelegateInputWithData.delegate,
                approver= input.metadataDelegateInputWithData.updateAuthority,
                tokenAccount= null,
                delegateRecord = metadataDelegateRecord(
                    mint,
                    type = input.metadataDelegateInputWithData.typeU,
                    updateAuthority= input.metadataDelegateInputWithData.updateAuthority,
                    delegate = delegatePublicKey,
                ).getOrThrow(),

            )
        }
        is DelegateInput.TokenDelegateInputWithDataOption -> {
            val tokenAccount: PublicKey = input.tokenDelegateInputWithData.token ?: PublicKey.associatedTokenAddress(mint, input.tokenDelegateInputWithData.owner).address
            ParsedTokenMetadataDelegate(
                isTokenDelegate= true,
                delegate= input.tokenDelegateInputWithData.delegate,
                approver= input.tokenDelegateInputWithData.owner,
                delegateRecord= NftPdasClient.tokenRecord(mint, tokenAccount).getOrThrow(),
                tokenAccount= tokenAccount
            )
        }
    }
}
