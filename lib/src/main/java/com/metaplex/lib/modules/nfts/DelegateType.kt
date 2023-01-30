package com.metaplex.lib.modules.nfts

import com.metaplex.lib.experimental.jen.tokenmetadata.DelegateArgs
import com.metaplex.lib.experimental.jen.tokenmetadata.MetadataDelegateRole
import com.metaplex.lib.experimental.jen.tokenmetadata.TokenDelegateRole

enum class TokenDelegateType{
    StandardV1,
    TransferV1,
    SaleV1,
    UtilityV1,
    StakingV1
}

enum class MetadataDelegateType {
    CollectionV1,
    UpdateV1
}

val tokenDelegateRoleMap = mapOf(
    TokenDelegateType.StandardV1 to TokenDelegateRole.Standard,
    TokenDelegateType.TransferV1 to TokenDelegateRole.Transfer,
    TokenDelegateType.SaleV1 to TokenDelegateRole.Sale,
    TokenDelegateType.UtilityV1 to TokenDelegateRole.Utility,
    TokenDelegateType.StakingV1 to TokenDelegateRole.Staking,
)

val metadataDelegateRoleMap = mapOf(
    MetadataDelegateType.CollectionV1 to MetadataDelegateRole.Collection,
    MetadataDelegateType.UpdateV1 to MetadataDelegateRole.Update,
)

val metadataDelegateSeedMap = mapOf(
    MetadataDelegateRole.Authority to "authority_delegate",
    MetadataDelegateRole.Collection to "collection_delegate",
    MetadataDelegateRole.Use to "use_delegate",
    MetadataDelegateRole.Update to  "update_delegate",
)

val metadataDelegateTypeCustomDataMap = mapOf(
    MetadataDelegateType.CollectionV1 to false,
    MetadataDelegateType.UpdateV1 to false,
)

val tokenDelegateTypeCustomDataMap = mapOf(
    TokenDelegateType.StandardV1 to true,
    TokenDelegateType.TransferV1 to true,
    TokenDelegateType.SaleV1 to true,
    TokenDelegateType.UtilityV1 to true,
    TokenDelegateType.StakingV1 to true,
)

fun getTokenDelegateRole(type: TokenDelegateType): TokenDelegateRole
            = tokenDelegateRoleMap[type] ?: throw Exception("UnreachableCaseError")

fun getMetadataDelegateRole(type: MetadataDelegateType): MetadataDelegateRole
        = metadataDelegateRoleMap[type] ?: throw Exception("UnreachableCaseError")

fun getMetadataDelegateRoleSeed(type: MetadataDelegateType): String
        = metadataDelegateSeedMap[getMetadataDelegateRole(type)] ?: throw Exception("UnreachableCaseError")

fun getDefaultDelegateArgs(type: TokenDelegateType): DelegateArgs {
    val hasCustomData = tokenDelegateTypeCustomDataMap[type]  ?: throw Exception("UnreachableCaseError")
    if (hasCustomData) throw Exception("DelegateRoleRequiredDataError($type)")
    return when(type){
        TokenDelegateType.StandardV1  -> DelegateArgs.StandardV1(1u)
        TokenDelegateType.TransferV1 -> DelegateArgs.TransferV1(1u, null)
        TokenDelegateType.SaleV1 -> DelegateArgs.SaleV1(1u, null)
        TokenDelegateType.UtilityV1 -> DelegateArgs.UtilityV1(1u, null)
        TokenDelegateType.StakingV1 -> DelegateArgs.StakingV1(1u, null)
    }
}

fun getDefaultDelegateArgs(type: MetadataDelegateType): DelegateArgs {
    throw Exception("DelegateRoleRequiredDataError($type)")
}