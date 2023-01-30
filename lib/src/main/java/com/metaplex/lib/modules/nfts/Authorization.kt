package com.metaplex.lib.modules.nfts

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.experimental.jen.tokenmetadata.AuthorityType
import com.metaplex.lib.experimental.jen.tokenmetadata.AuthorizationData
import com.metaplex.lib.experimental.jen.tokenmetadata.DelegateArgs
import com.solana.core.PublicKey

data class TokenMetadataAuthorizationDetails(val rules: PublicKey, val data: AuthorizationData?)

data class ParsedTokenMetadataAuthorizationData(
    val authorityType: AuthorityType,
    val authorizationData: AuthorizationData? = null
)

data class ParsedTokenMetadataAuthorizationAccounts(
    /** The authority that will sign the transaction. */
    val authority: PublicKey,
    /**
     * If "holder" or "token delegate" authority,
     * the address of the token account.
     */
    val token: PublicKey? = null,
    /**
     * If "delegate" authority, the address of the update
     * authority or the token owner depending on the type.
     */
    val approver: PublicKey? = null,
    /**
     * If "delegate" authority, the address of the token record
     * or the metdata delegate record PDA depending on the type.
     */
    val delegateRecord: PublicKey? = null,
    /** If any auth rules are provided, the address of the auth rule account. */
    val authorizationRules: PublicKey? = null,
)
data class ParsedTokenMetadataAuthorization(
    val accounts: ParsedTokenMetadataAuthorizationAccounts,
    val signers: List<AccountOrPK>,
    val data: ParsedTokenMetadataAuthorizationData
)

sealed class TokenMetadataAuthority{
    sealed class Auth: TokenMetadataAuthority() {
        data class TokenMetadataAuthorityMetadata(
            val updateAuthority: AccountOrPK
        ): Auth()
        data class TokenMetadataAuthorityHolder(
            val owner: AccountOrPK,
            val token: PublicKey
        ): Auth()
        data class TokenMetadataAuthorityMetadataDelegate(
            override val delegate: AccountOrPK,
            override val updateAuthority: PublicKey,
            override val typeA: DelegateArgs,
            override val typeU: MetadataDelegateType
        ): Auth(), MetadataDelegateInput<AccountOrPK>
        data class TokenMetadataAuthorityTokenDelegate(
            override val delegate: AccountOrPK,
            override val owner: PublicKey,
            override val token: PublicKey?,
            override val typeA: DelegateArgs,
            override val typeU: TokenDelegateType
        ): Auth(), TokenDelegateInput<AccountOrPK>
    }
    data class Signer(val identityDriver: IdentityDriver): TokenMetadataAuthority()
}



fun getSignerFromTokenMetadataAuthority(authority: TokenMetadataAuthority): AccountOrPK {
    return when(authority){
        is TokenMetadataAuthority.Signer -> AccountOrPK.isAccount(authority.identityDriver)
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadata -> authority.updateAuthority
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityHolder -> authority.owner
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadataDelegate -> authority.delegate
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityTokenDelegate -> authority.delegate
    }
}

fun parseTokenMetadataAuthorization(
    mint: PublicKey,
    authority: TokenMetadataAuthority.Auth,
    authorizationDetails: TokenMetadataAuthorizationDetails?
): ParsedTokenMetadataAuthorization{



    when(authority){
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadata -> {
            return ParsedTokenMetadataAuthorization(
                accounts = ParsedTokenMetadataAuthorizationAccounts(
                    authorizationRules = authorizationDetails?.rules,
                    authority = authority.updateAuthority.publicKey()
                ),
                signers = listOf(authority.updateAuthority),
                data = ParsedTokenMetadataAuthorizationData(
                    authorizationData = authorizationDetails?.data,
                    authorityType = AuthorityType.Metadata
                )
            )
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityHolder -> {

            return ParsedTokenMetadataAuthorization(
                accounts = ParsedTokenMetadataAuthorizationAccounts(
                    authorizationRules = authorizationDetails?.rules,
                    authority = authority.owner.publicKey(),
                    token = authority.token
                ),
                signers = listOf(authority.owner),
                data = ParsedTokenMetadataAuthorizationData(
                    authorizationData = authorizationDetails?.data,
                    authorityType = AuthorityType.Holder
                )
            )
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadataDelegate -> {
            val parsedTokenMetadataDelegate = parseTokenMetadataDelegateInput(
                mint,
                DelegateInput.MetadataDelegateInputWithDataOption(
                    metadataDelegateInputWithData = MetadataDelegateInputConcrete(
                        delegate = authority.delegate,
                        updateAuthority = authority.updateAuthority,
                        typeA = authority.typeA,
                        typeU = authority.typeU
                    )
                ),
            )
            val delegateRecord = parsedTokenMetadataDelegate.delegateRecord
            val approver = parsedTokenMetadataDelegate.approver

            return ParsedTokenMetadataAuthorization(
                accounts = ParsedTokenMetadataAuthorizationAccounts(
                    authorizationRules = authorizationDetails?.rules,
                    authority = authority.delegate.publicKey(),
                    delegateRecord = delegateRecord,
                    approver = approver
                ),
                signers = listOf(authority.delegate),
                data = ParsedTokenMetadataAuthorizationData(
                    authorizationData = authorizationDetails?.data,
                    authorityType = AuthorityType.Delegate
                )
            )
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityTokenDelegate -> {
            val parsedTokenMetadataDelegate = parseTokenMetadataDelegateInput(
                mint,
                DelegateInput.TokenDelegateInputWithDataOption(
                    tokenDelegateInputWithData = TokenDelegateInputConcrete(
                        typeA = authority.typeA,
                        typeU = authority.typeU,
                        delegate = authority.delegate,
                        owner = authority.owner,
                        token = authority.token
                    )
                ),
            )
            val delegateRecord = parsedTokenMetadataDelegate.delegateRecord
            val approver = parsedTokenMetadataDelegate.approver
            val tokenAccount = parsedTokenMetadataDelegate.tokenAccount

            return ParsedTokenMetadataAuthorization(
                accounts = ParsedTokenMetadataAuthorizationAccounts(
                    authorizationRules = authorizationDetails?.rules,
                    authority = authority.delegate.publicKey(),
                    token = tokenAccount,
                    delegateRecord = delegateRecord,
                    approver = approver
                ),
                signers = listOf(authority.delegate),
                data = ParsedTokenMetadataAuthorizationData(
                    authorizationData = authorizationDetails?.data,
                    authorityType = AuthorityType.Delegate
                )
            )
        }
    }
}