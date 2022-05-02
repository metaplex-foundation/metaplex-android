package com.metaplex.lib.programs.token_metadata

import com.google.protobuf.UInt64Value
import com.solana.core.PublicKey
import com.solana.vendor.borshj.BorshCodable
import com.solana.vendor.borshj.FieldOrder

data class MasterEditionAccount(@FieldOrder(0) val type: Byte): BorshCodable

data class MasterEditionV1 (
    @FieldOrder(0) val supply: UInt64Value?,
    @FieldOrder(1) val max_supply: UInt64Value?,
    @FieldOrder(2) val printing_mint: PublicKey,
    @FieldOrder(3) val one_time_printing_authorization_mint: PublicKey
): BorshCodable

data class MasterEditionV2 (
    @FieldOrder(0) val supply: UInt64Value?,
    @FieldOrder(1) val max_supply: UInt64Value?
): BorshCodable