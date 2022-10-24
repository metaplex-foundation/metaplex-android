/*
 * TestAuctionHouse
 * Metaplex
 * 
 * Created by Funkatronics on 8/2/2022
 */

package com.metaplex.data.model

import com.metaplex.lib.modules.auctions.models.AuctionHouse
import com.metaplex.lib.experimental.jen.auctionhouse.AuctionHouse as AuctionHouseAccount
import com.solana.core.PublicKey
import java.util.Base64

internal typealias TestAuctionHouse = AuctionHouse

internal val TestAuctionHouse.publicKey get() = "5xN42RZCk7wA4GjQU2VVDhda8LBL8fAnrKZK921sybLF"
internal val TestAuctionHouse.borsh get() = Base64.getDecoder().decode(
    "KGzXa9VV9TC9WuTFjCbG0RR7CXM/o6Jb3sLJjqqmeNyI+uBYcn8mGLvtruiZlJOUc5O5XrLpZ+PbnWq5BpAKKCT" +
        "7T9RhxMiSeA6Gza4qx8mdg9W+r6hT4lSELlTjkLzxG/JeSifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVOOQvPEb8l" +
        "5KJ8RmrAabiFf+q4GE+2h/Y0YYwDXaxDncGus7VZig8AAAAAABeA6Gza4qx8mdg9W+r6hT4lSELlTjkLzxG/JeS" +
        "ifEZqx4DobNrirHyZ2D1b6vqFPiVIQuVOOQvPEb8l5KJ8RmrP3+/MgAAAAAAAA="
)

internal fun TestAuctionHouse(hasAuctioneer: Boolean = false) = AuctionHouse(
    treasuryWithdrawalDestinationOwner = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    feeWithdrawalDestination = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    treasuryMint = PublicKey("So11111111111111111111111111111111111111112"),
    authority = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    creator = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    sellerFeeBasisPoints = 200u,
    requiresSignOff = false,
    canChangeSalePrice = false,
    hasAuctioneer = hasAuctioneer,
)

internal fun TestAuctionHouseAccount(hasAuctioneer: Boolean = false) = AuctionHouseAccount(
    auctionHouseFeeAccount = PublicKey("DkAScnZa6GqjXkPYPAU4kediZmR2EESHXutFzR4U6TGs"),
    auctionHouseTreasury = PublicKey("DebSyCbsnzMppVLt1umD4tUcJV6bSQW4z3nQVXQpWhCV"),
    treasuryWithdrawalDestination = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    feeWithdrawalDestination = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    treasuryMint = PublicKey("So11111111111111111111111111111111111111112"),
    authority = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    creator = PublicKey("95emj1a33Ei7B6ciu7gbPm7zRMRpFGs86g5nK5NiSdEK"),
    bump = 253u,
    treasuryBump = 254u,
    feePayerBump = 252u,
    sellerFeeBasisPoints = 200u,
    requiresSignOff = false,
    canChangeSalePrice = false,
    escrowPaymentBump = 0u,
    hasAuctioneer = hasAuctioneer,
    auctioneerAddress = PublicKey(ByteArray(PublicKey.PUBLIC_KEY_LENGTH)),
    scopes = listOf()
)