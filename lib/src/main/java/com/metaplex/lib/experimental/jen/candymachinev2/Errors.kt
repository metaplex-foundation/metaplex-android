//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-27
//
package com.metaplex.lib.experimental.jen.candymachinev2

import kotlin.Int
import kotlin.String

sealed interface CandyMachineV2Error {
    val code: Int

    val message: String
}

class IncorrectOwner : CandyMachineV2Error {
    override val code: Int = 6000

    override val message: String = "Account does not have correct owner!"
}

class Uninitialized : CandyMachineV2Error {
    override val code: Int = 6001

    override val message: String = "Account is not initialized!"
}

class MintMismatch : CandyMachineV2Error {
    override val code: Int = 6002

    override val message: String = "Mint Mismatch!"
}

class IndexGreaterThanLength : CandyMachineV2Error {
    override val code: Int = 6003

    override val message: String = "Index greater than length!"
}

class NumericalOverflowError : CandyMachineV2Error {
    override val code: Int = 6004

    override val message: String = "Numerical overflow error!"
}

class TooManyCreators : CandyMachineV2Error {
    override val code: Int = 6005

    override val message: String =
            "Can only provide up to 4 creators to candy machine (because candy machine is one)!"
}

class UuidMustBeExactly6Length : CandyMachineV2Error {
    override val code: Int = 6006

    override val message: String = "Uuid must be exactly of 6 length"
}

class NotEnoughTokens : CandyMachineV2Error {
    override val code: Int = 6007

    override val message: String = "Not enough tokens to pay for this minting"
}

class NotEnoughSOL : CandyMachineV2Error {
    override val code: Int = 6008

    override val message: String = "Not enough SOL to pay for this minting"
}

class TokenTransferFailed : CandyMachineV2Error {
    override val code: Int = 6009

    override val message: String = "Token transfer failed"
}

class CandyMachineEmpty : CandyMachineV2Error {
    override val code: Int = 6010

    override val message: String = "Candy machine is empty!"
}

class CandyMachineNotLive : CandyMachineV2Error {
    override val code: Int = 6011

    override val message: String = "Candy machine is not live!"
}

class HiddenSettingsConfigsDoNotHaveConfigLines : CandyMachineV2Error {
    override val code: Int = 6012

    override val message: String =
            "Configs that are using hidden uris do not have config lines, they have a single hash representing hashed order"
}

class CannotChangeNumberOfLines : CandyMachineV2Error {
    override val code: Int = 6013

    override val message: String = "Cannot change number of lines unless is a hidden config"
}

class DerivedKeyInvalid : CandyMachineV2Error {
    override val code: Int = 6014

    override val message: String = "Derived key invalid"
}

class PublicKeyMismatch : CandyMachineV2Error {
    override val code: Int = 6015

    override val message: String = "Public key mismatch"
}

class NoWhitelistToken : CandyMachineV2Error {
    override val code: Int = 6016

    override val message: String = "No whitelist token present"
}

class TokenBurnFailed : CandyMachineV2Error {
    override val code: Int = 6017

    override val message: String = "Token burn failed"
}

class GatewayAppMissing : CandyMachineV2Error {
    override val code: Int = 6018

    override val message: String = "Missing gateway app when required"
}

class GatewayTokenMissing : CandyMachineV2Error {
    override val code: Int = 6019

    override val message: String = "Missing gateway token when required"
}

class GatewayTokenExpireTimeInvalid : CandyMachineV2Error {
    override val code: Int = 6020

    override val message: String = "Invalid gateway token expire time"
}

class NetworkExpireFeatureMissing : CandyMachineV2Error {
    override val code: Int = 6021

    override val message: String = "Missing gateway network expire feature when required"
}

class CannotFindUsableConfigLine : CandyMachineV2Error {
    override val code: Int = 6022

    override val message: String =
            "Unable to find an unused config line near your random number index"
}

class InvalidString : CandyMachineV2Error {
    override val code: Int = 6023

    override val message: String = "Invalid string"
}

class SuspiciousTransaction : CandyMachineV2Error {
    override val code: Int = 6024

    override val message: String = "Suspicious transaction detected"
}

class CannotSwitchToHiddenSettings : CandyMachineV2Error {
    override val code: Int = 6025

    override val message: String =
            "Cannot Switch to Hidden Settings after items available is greater than 0"
}

class IncorrectSlotHashesPubkey : CandyMachineV2Error {
    override val code: Int = 6026

    override val message: String = "Incorrect SlotHashes PubKey"
}

class IncorrectCollectionAuthority : CandyMachineV2Error {
    override val code: Int = 6027

    override val message: String = "Incorrect collection NFT authority"
}

class MismatchedCollectionPDA : CandyMachineV2Error {
    override val code: Int = 6028

    override val message: String = "Collection PDA address is invalid"
}

class MismatchedCollectionMint : CandyMachineV2Error {
    override val code: Int = 6029

    override val message: String = "Provided mint account doesn't match collection PDA mint"
}

class SlotHashesEmpty : CandyMachineV2Error {
    override val code: Int = 6030

    override val message: String = "Slot hashes Sysvar is empty"
}

class MetadataAccountMustBeEmpty : CandyMachineV2Error {
    override val code: Int = 6031

    override val message: String =
            "The metadata account has data in it, and this must be empty to mint a new NFT"
}

class MissingSetCollectionDuringMint : CandyMachineV2Error {
    override val code: Int = 6032

    override val message: String =
            "Missing set collection during mint IX for Candy Machine with collection set"
}

class NoChangingCollectionDuringMint : CandyMachineV2Error {
    override val code: Int = 6033

    override val message: String =
            "Can't change collection settings after items have begun to be minted"
}

class CandyCollectionRequiresRetainAuthority : CandyMachineV2Error {
    override val code: Int = 6034

    override val message: String =
            "Retain authority must be true for Candy Machines with a collection set"
}

class GatewayProgramError : CandyMachineV2Error {
    override val code: Int = 6035

    override val message: String = "Error within Gateway program"
}

class NoChangingFreezeDuringMint : CandyMachineV2Error {
    override val code: Int = 6036

    override val message: String =
            "Can't change freeze settings after items have begun to be minted. You can only disable."
}

class NoChangingAuthorityWithFreeze : CandyMachineV2Error {
    override val code: Int = 6037

    override val message: String =
            "Can't change authority while freeze is enabled. Disable freeze first."
}

class NoChangingTokenWithFreeze : CandyMachineV2Error {
    override val code: Int = 6038

    override val message: String =
            "Can't change token while freeze is enabled. Disable freeze first."
}

class InvalidThawNft : CandyMachineV2Error {
    override val code: Int = 6039

    override val message: String =
            "Cannot thaw NFT unless all NFTs are minted or Candy Machine authority enables thawing"
}

class IncorrectRemainingAccountsLen : CandyMachineV2Error {
    override val code: Int = 6040

    override val message: String =
            "The number of remaining accounts passed in doesn't match the Candy Machine settings"
}

class MissingFreezeAta : CandyMachineV2Error {
    override val code: Int = 6041

    override val message: String = "FreezePDA ATA needs to be passed in if token mint is enabled."
}

class IncorrectFreezeAta : CandyMachineV2Error {
    override val code: Int = 6042

    override val message: String = "Incorrect freeze ATA address."
}

class FreezePDAMismatch : CandyMachineV2Error {
    override val code: Int = 6043

    override val message: String = "FreezePDA doesn't belong to this Candy Machine."
}

class EnteredFreezeIsMoreThanMaxFreeze : CandyMachineV2Error {
    override val code: Int = 6044

    override val message: String = "Freeze time can't be longer than MAX_FREEZE_TIME."
}

class NoWithdrawWithFreeze : CandyMachineV2Error {
    override val code: Int = 6045

    override val message: String =
            "Can't withdraw Candy Machine while freeze is active. Disable freeze first."
}

class NoWithdrawWithFrozenFunds : CandyMachineV2Error {
    override val code: Int = 6046

    override val message: String =
            "Can't withdraw Candy Machine while frozen funds need to be redeemed. Unlock funds first."
}

class MissingRemoveFreezeTokenAccounts : CandyMachineV2Error {
    override val code: Int = 6047

    override val message: String =
            "Missing required remaining accounts for remove_freeze with token mint."
}

class InvalidFreezeWithdrawTokenAddress : CandyMachineV2Error {
    override val code: Int = 6048

    override val message: String = "Can't withdraw SPL Token from freeze PDA into itself"
}

class NoUnlockWithNFTsStillFrozen : CandyMachineV2Error {
    override val code: Int = 6049

    override val message: String =
            "Can't unlock funds while NFTs are still frozen. Run thaw on all NFTs first."
}

class SizedCollectionMetadataMustBeMutable : CandyMachineV2Error {
    override val code: Int = 6050

    override val message: String =
            "Setting a sized collection requires the collection metadata to be mutable."
}
