//
// Types
// Metaplex
//
// This code was generated locally by Funkatronics on 2023-01-24
//
@file:UseSerializers(PublicKeyAs32ByteSerializer::class)

package com.metaplex.lib.experimental.jen.tokenmetadata

import com.metaplex.lib.serialization.serializers.solana.PublicKeyAs32ByteSerializer
import com.solana.core.PublicKey
import java.util.HashMap
import kotlin.Boolean
import kotlin.ByteArray
import kotlin.String
import kotlin.UByte
import kotlin.ULong
import kotlin.UShort
import kotlin.collections.List
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class SetCollectionSizeArgs(val size: ULong)

@Serializable
data class CreateMetadataAccountArgsV2(val data: DataV2, val isMutable: Boolean)

@Serializable
data class CreateMetadataAccountArgs(val data: Data, val isMutable: Boolean)

@Serializable
data class UpdateMetadataAccountArgs(
    val data: Data?,
    val updateAuthority: PublicKey?,
    val primarySaleHappened: Boolean?
)

@Serializable
data class MintPrintingTokensViaTokenArgs(val supply: ULong)

@Serializable
data class SetReservationListArgs(
    val reservations: List<Reservation>,
    val totalReservationSpots: ULong?,
    val offset: ULong,
    val totalSpotOffset: ULong
)

@Serializable
data class CreateMasterEditionArgs(val maxSupply: ULong?)

@Serializable
data class MintNewEditionFromMasterEditionViaTokenArgs(val edition: ULong)

@Serializable
data class TransferOutOfEscrowArgs(val amount: ULong)

@Serializable
data class CreateMetadataAccountArgsV3(
    val data: DataV2,
    val isMutable: Boolean,
    val collectionDetails: CollectionDetails?
)

@Serializable
data class UpdateMetadataAccountArgsV2(
    val data: DataV2?,
    val updateAuthority: PublicKey?,
    val primarySaleHappened: Boolean?,
    val isMutable: Boolean?
)

@Serializable
data class ApproveUseAuthorityArgs(val numberOfUses: ULong)

@Serializable
data class UtilizeArgs(val numberOfUses: ULong)

@Serializable
data class AuthorizationData(val payload: Payload)

@Serializable
data class AssetData(
    val updateAuthority: PublicKey,
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UShort,
    val creators: List<Creator>?,
    val primarySaleHappened: Boolean,
    val isMutable: Boolean,
    val tokenStandard: TokenStandard,
    val collection: Collection?,
    val uses: Uses?,
    val collectionDetails: CollectionDetails?,
    val ruleSet: PublicKey?
)

@Serializable
data class Collection(val verified: Boolean, val key: PublicKey)

@Serializable
data class Creator(
    val address: PublicKey,
    val verified: Boolean,
    val share: UByte
)

@Serializable
data class Data(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UShort,
    val creators: List<Creator>?
)

@Serializable
data class DataV2(
    val name: String,
    val symbol: String,
    val uri: String,
    val sellerFeeBasisPoints: UShort,
    val creators: List<Creator>?,
    val collection: Collection?,
    val uses: Uses?
)

@Serializable
data class Reservation(
    val address: PublicKey,
    val spotsRemaining: ULong,
    val totalSpots: ULong
)

@Serializable
data class ReservationV1(
    val address: PublicKey,
    val spotsRemaining: UByte,
    val totalSpots: UByte
)

@Serializable
data class SeedsVec(val seeds: List<ByteArray>)

@Serializable
data class LeafInfo(val leaf: List<UByte>, val proof: List<List<UByte>>)

@Serializable
data class Payload(val map: HashMap<PayloadKey, PayloadType>)

@Serializable
data class Uses(
    val useMethod: UseMethod,
    val remaining: ULong,
    val total: ULong
)

@Serializable
sealed class BurnArgs {
    data class V1(val authorization_data: AuthorizationData?) : BurnArgs()
}

@Serializable
sealed class VerifyArgs {
    data class V1(val authorization_data: AuthorizationData?) : VerifyArgs()
}

@Serializable
sealed class DelegateArgs {
    data class CollectionV1(val authorization_data: AuthorizationData?) : DelegateArgs()

    data class SaleV1(val amount: ULong, val authorization_data: AuthorizationData?) :
            DelegateArgs()

    data class TransferV1(val amount: ULong, val authorization_data: AuthorizationData?) :
            DelegateArgs()

    data class UpdateV1(val authorization_data: AuthorizationData?) : DelegateArgs()

    data class UtilityV1(val amount: ULong, val authorization_data: AuthorizationData?) :
            DelegateArgs()

    data class StakingV1(val amount: ULong, val authorization_data: AuthorizationData?) :
            DelegateArgs()

    data class StandardV1(val amount: ULong) : DelegateArgs()
}

enum class RevokeArgs {
    CollectionV1,

    SaleV1,

    TransferV1,

    UpdateV1,

    UtilityV1,

    StakingV1,

    StandardV1
}

enum class MetadataDelegateRole {
    Authority,

    Collection,

    Use,

    Update
}

@Serializable
sealed class CreateArgs {
    data class V1(
        val asset_data: AssetData,
        val decimals: UByte?,
        val max_supply: ULong?
    ) : CreateArgs()
}

@Serializable
sealed class MintArgs {
    data class V1(val amount: ULong, val authorization_data: AuthorizationData?) : MintArgs()
}

@Serializable
sealed class TransferArgs {
    data class V1(val amount: ULong, val authorization_data: AuthorizationData?) : TransferArgs()
}

@Serializable
sealed class UpdateArgs {
    data class V1(
        val new_update_authority: PublicKey?,
        val data: Data?,
        val primary_sale_happened: Boolean?,
        val is_mutable: Boolean?,
        val collection: CollectionToggle,
        val collection_details: CollectionDetailsToggle,
        val uses: UsesToggle,
        val rule_set: RuleSetToggle,
        val authorization_data: AuthorizationData?
    ) : UpdateArgs()
}

@Serializable
sealed class CollectionToggle {
    object None : CollectionToggle()

    object Clear : CollectionToggle()

    data class Set(val Collection: Collection) : CollectionToggle()
}

@Serializable
sealed class UsesToggle {
    object None : UsesToggle()

    object Clear : UsesToggle()

    data class Set(val Uses: Uses) : UsesToggle()
}

@Serializable
sealed class CollectionDetailsToggle {
    object None : CollectionDetailsToggle()

    object Clear : CollectionDetailsToggle()

    data class Set(val CollectionDetails: CollectionDetails) : CollectionDetailsToggle()
}

@Serializable
sealed class RuleSetToggle {
    object None : RuleSetToggle()

    object Clear : RuleSetToggle()

    data class Set(val publicKey: PublicKey) : RuleSetToggle()
}

@Serializable
sealed class MigrateArgs {
    data class V1(val migration_type: MigrationType, val rule_set: PublicKey?) : MigrateArgs()
}

@Serializable
sealed class LockArgs {
    data class V1(val authorization_data: AuthorizationData?) : LockArgs()
}

@Serializable
sealed class UnlockArgs {
    data class V1(val authorization_data: AuthorizationData?) : UnlockArgs()
}

@Serializable
sealed class UseArgs {
    data class V1(val authorization_data: AuthorizationData?) : UseArgs()
}

enum class TokenStandard {
    NonFungible,

    FungibleAsset,

    Fungible,

    NonFungibleEdition,

    ProgrammableNonFungible
}

enum class Key {
    Uninitialized,

    EditionV1,

    MasterEditionV1,

    ReservationListV1,

    MetadataV1,

    ReservationListV2,

    MasterEditionV2,

    EditionMarker,

    UseAuthorityRecord,

    CollectionAuthorityRecord,

    TokenOwnedEscrow,

    TokenRecord,

    MetadataDelegate
}

@Serializable
sealed class CollectionDetails {
    data class V1(val size: ULong) : CollectionDetails()
}

@Serializable
sealed class EscrowAuthority {
    object TokenOwner : EscrowAuthority()

    data class Creator(val publicKey: PublicKey) : EscrowAuthority()
}

@Serializable
sealed class ProgrammableConfig {
    data class V1(val rule_set: PublicKey?) : ProgrammableConfig()
}

enum class MigrationType {
    CollectionV1,

    ProgrammableV1
}

enum class TokenState {
    Unlocked,

    Locked,

    Listed
}

enum class TokenDelegateRole {
    Sale,

    Transfer,

    Utility,

    Staking,

    Standard,

    Migration
}

enum class AuthorityType {
    None,

    Metadata,

    Delegate,

    Holder
}

@Serializable
sealed class PayloadType {
    data class Pubkey(val publicKey: PublicKey) : PayloadType()

    data class Seeds(val SeedsVec: SeedsVec) : PayloadType()

    data class MerkleProof(val LeafInfo: LeafInfo) : PayloadType()

    data class Number(val u64: ULong) : PayloadType()
}

enum class PayloadKey {
    Target,

    Holder,

    Authority,

    Amount
}

enum class UseMethod {
    Burn,

    Multiple,

    Single
}
