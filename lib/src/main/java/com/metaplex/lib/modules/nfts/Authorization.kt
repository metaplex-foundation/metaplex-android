package com.metaplex.lib.modules.nfts

import com.metaplex.lib.drivers.indenty.IdentityDriver
import com.metaplex.lib.experimental.jen.tokenmetadata.AuthorityType
import com.metaplex.lib.experimental.jen.tokenmetadata.AuthorizationData
import com.metaplex.lib.experimental.jen.tokenmetadata.DelegateArgs
import com.solana.core.PublicKey

data class TokenMetadataAuthorizationDetails(val rules: PublicKey, val data: AuthorizationData?)

data class ParsedTokenMetadataAuthorizationData(
    var authorityType: AuthorityType? = null,
    var authorizationData: AuthorizationData? = null
)

data class ParsedTokenMetadataAuthorizationAccounts(
    /** The authority that will sign the transaction. */
    var authority: PublicKey? = null,
    /**
     * If "holder" or "token delegate" authority,
     * the address of the token account.
     */
    var token: PublicKey? = null,
    /**
     * If "delegate" authority, the address of the update
     * authority or the token owner depending on the type.
     */
    var approver: PublicKey? = null,
    /**
     * If "delegate" authority, the address of the token record
     * or the metdata delegate record PDA depending on the type.
     */
    var delegateRecord: PublicKey? = null,
    /** If any auth rules are provided, the address of the auth rule account. */
    var authorizationRules: PublicKey? = null,
)
data class ParsedTokenMetadataAuthorization(
    val accounts: ParsedTokenMetadataAuthorizationAccounts,
    val signers: MutableList<AccountOrPK>,
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

    val auth = ParsedTokenMetadataAuthorization(
        accounts = ParsedTokenMetadataAuthorizationAccounts(
            authorizationRules = authorizationDetails?.rules
        ),
        signers= mutableListOf(),
        data= ParsedTokenMetadataAuthorizationData(
            authorizationData= authorizationDetails?.data
        )
    )

    when(authority){
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadata -> {
            auth.accounts.authority = authority.updateAuthority.publicKey()
            auth.signers.add(authority.updateAuthority)
            auth.data.authorityType = AuthorityType.Metadata
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityHolder -> {
            auth.accounts.authority = authority.owner.publicKey()
            auth.accounts.token = authority.token
            auth.signers.add(authority.owner)
            auth.data.authorityType = AuthorityType.Holder
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityMetadataDelegate -> {
            val parsedTokenMetadataDelegate = parseTokenMetadataDelegateInput(
                mint,
                DelegateInput.MetadataDelegateInputWithDataOption(
                    metadataDelegateInputWithData = MetadataDelegateInputConcrete(
                        delegate= authority.delegate,
                        updateAuthority= authority.updateAuthority,
                        typeA = authority.typeA,
                        typeU = authority.typeU
                    )
                ),
            )
            val delegateRecord = parsedTokenMetadataDelegate.delegateRecord
            val approver = parsedTokenMetadataDelegate.approver

            auth.accounts.authority = authority.delegate.publicKey()
            auth.accounts.delegateRecord = delegateRecord
            auth.accounts.approver = approver
            auth.signers.add(authority.delegate)
            auth.data.authorityType = AuthorityType.Delegate
        }
        is TokenMetadataAuthority.Auth.TokenMetadataAuthorityTokenDelegate -> {
            val parsedTokenMetadataDelegate = parseTokenMetadataDelegateInput(
                mint,
                DelegateInput.TokenDelegateInputWithDataOption(
                    tokenDelegateInputWithData = TokenDelegateInputConcrete(
                        typeA = authority.typeA,
                        typeU = authority.typeU,
                        delegate= authority.delegate,
                        owner= authority.owner,
                        token= authority.token
                    )
                ),
            )
            val delegateRecord = parsedTokenMetadataDelegate.delegateRecord
            val approver = parsedTokenMetadataDelegate.approver
            val tokenAccount = parsedTokenMetadataDelegate.tokenAccount

            auth.accounts.authority = authority.delegate.publicKey()
            auth.accounts.token = tokenAccount
            auth.accounts.delegateRecord = delegateRecord
            auth.accounts.approver = approver
            auth.signers.add(authority.delegate)
            auth.data.authorityType = AuthorityType.Delegate
        }
    }
    return auth
}