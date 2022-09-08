//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-08
//
package com.metaplex.lib.experimental.jen.candymachine

import kotlin.Int
import kotlin.String

sealed interface CandyMachineError {
    val code: Int

    val message: String
}

class IncorrectOwner : CandyMachineError {
    override val code: Int = 6000

    override val message: String = "Account does not have correct owner!"
}

class Uninitialized : CandyMachineError {
    override val code: Int = 6001

    override val message: String = "Account is not initialized!"
}

class MintMismatch : CandyMachineError {
    override val code: Int = 6002

    override val message: String = "Mint Mismatch!"
}

class IndexGreaterThanLength : CandyMachineError {
    override val code: Int = 6003

    override val message: String = "Index greater than length!"
}

class NumericalOverflowError : CandyMachineError {
    override val code: Int = 6004

    override val message: String = "Numerical overflow error!"
}

class TooManyCreators : CandyMachineError {
    override val code: Int = 6005

    override val message: String =
            "Can only provide up to 4 creators to candy machine (because candy machine is one)!"
}

class UuidMustBeExactly6Length : CandyMachineError {
    override val code: Int = 6006

    override val message: String = "Uuid must be exactly of 6 length"
}

class NotEnoughTokens : CandyMachineError {
    override val code: Int = 6007

    override val message: String = "Not enough tokens to pay for this minting"
}

class NotEnoughSOL : CandyMachineError {
    override val code: Int = 6008

    override val message: String = "Not enough SOL to pay for this minting"
}

class TokenTransferFailed : CandyMachineError {
    override val code: Int = 6009

    override val message: String = "Token transfer failed"
}

class CandyMachineEmpty : CandyMachineError {
    override val code: Int = 6010

    override val message: String = "Candy machine is empty!"
}

class CandyMachineNotLive : CandyMachineError {
    override val code: Int = 6011

    override val message: String = "Candy machine is not live!"
}

class HiddenSettingsConfigsDoNotHaveConfigLines : CandyMachineError {
    override val code: Int = 6012

    override val message: String =
            "Configs that are using hidden uris do not have config lines, they have a single hash representing hashed order"
}

class CannotChangeNumberOfLines : CandyMachineError {
    override val code: Int = 6013

    override val message: String = "Cannot change number of lines unless is a hidden config"
}

class DerivedKeyInvalid : CandyMachineError {
    override val code: Int = 6014

    override val message: String = "Derived key invalid"
}

class PublicKeyMismatch : CandyMachineError {
    override val code: Int = 6015

    override val message: String = "Public key mismatch"
}

class NoWhitelistToken : CandyMachineError {
    override val code: Int = 6016

    override val message: String = "No whitelist token present"
}

class TokenBurnFailed : CandyMachineError {
    override val code: Int = 6017

    override val message: String = "Token burn failed"
}

class GatewayAppMissing : CandyMachineError {
    override val code: Int = 6018

    override val message: String = "Missing gateway app when required"
}

class GatewayTokenMissing : CandyMachineError {
    override val code: Int = 6019

    override val message: String = "Missing gateway token when required"
}

class GatewayTokenExpireTimeInvalid : CandyMachineError {
    override val code: Int = 6020

    override val message: String = "Invalid gateway token expire time"
}

class NetworkExpireFeatureMissing : CandyMachineError {
    override val code: Int = 6021

    override val message: String = "Missing gateway network expire feature when required"
}

class CannotFindUsableConfigLine : CandyMachineError {
    override val code: Int = 6022

    override val message: String =
            "Unable to find an unused config line near your random number index"
}

class InvalidString : CandyMachineError {
    override val code: Int = 6023

    override val message: String = "Invalid string"
}

class SuspiciousTransaction : CandyMachineError {
    override val code: Int = 6024

    override val message: String = "Suspicious transaction detected"
}

class CannotSwitchToHiddenSettings : CandyMachineError {
    override val code: Int = 6025

    override val message: String =
            "Cannot Switch to Hidden Settings after items available is greater than 0"
}

class IncorrectSlotHashesPubkey : CandyMachineError {
    override val code: Int = 6026

    override val message: String = "Incorrect SlotHashes PubKey"
}

class IncorrectCollectionAuthority : CandyMachineError {
    override val code: Int = 6027

    override val message: String = "Incorrect collection NFT authority"
}

class MismatchedCollectionPDA : CandyMachineError {
    override val code: Int = 6028

    override val message: String = "Collection PDA address is invalid"
}

class MismatchedCollectionMint : CandyMachineError {
    override val code: Int = 6029

    override val message: String = "Provided mint account doesn't match collection PDA mint"
}

class SlotHashesEmpty : CandyMachineError {
    override val code: Int = 6030

    override val message: String = "Slot hashes Sysvar is empty"
}

class MetadataAccountMustBeEmpty : CandyMachineError {
    override val code: Int = 6031

    override val message: String =
            "The metadata account has data in it, and this must be empty to mint a new NFT"
}

class MissingSetCollectionDuringMint : CandyMachineError {
    override val code: Int = 6032

    override val message: String =
            "Missing set collection during mint IX for Candy Machine with collection set"
}

class NoChangingCollectionDuringMint : CandyMachineError {
    override val code: Int = 6033

    override val message: String =
            "Can't change collection settings after items have begun to be minted"
}

class CandyCollectionRequiresRetainAuthority : CandyMachineError {
    override val code: Int = 6034

    override val message: String =
            "Retain authority must be true for Candy Machines with a collection set"
}

class GatewayProgramError : CandyMachineError {
    override val code: Int = 6035

    override val message: String = "Error within Gateway program"
}

class NoChangingFreezeDuringMint : CandyMachineError {
    override val code: Int = 6036

    override val message: String =
            "Can't change freeze settings after items have begun to be minted. You can only disable."
}

class NoChangingAuthorityWithFreeze : CandyMachineError {
    override val code: Int = 6037

    override val message: String =
            "Can't change authority while freeze is enabled. Disable freeze first."
}

class NoChangingTokenWithFreeze : CandyMachineError {
    override val code: Int = 6038

    override val message: String =
            "Can't change token while freeze is enabled. Disable freeze first."
}

class InvalidThawNft : CandyMachineError {
    override val code: Int = 6039

    override val message: String =
            "Cannot thaw NFT unless all NFTs are minted or Candy Machine authority enables thawing"
}

class IncorrectRemainingAccountsLen : CandyMachineError {
    override val code: Int = 6040

    override val message: String =
            "The number of remaining accounts passed in doesn't match the Candy Machine settings"
}

class MissingFreezeAta : CandyMachineError {
    override val code: Int = 6041

    override val message: String = "FreezePDA ATA needs to be passed in if token mint is enabled."
}

class IncorrectFreezeAta : CandyMachineError {
    override val code: Int = 6042

    override val message: String = "Incorrect freeze ATA address."
}

class FreezePDAMismatch : CandyMachineError {
    override val code: Int = 6043

    override val message: String = "FreezePDA doesn't belong to this Candy Machine."
}

class EnteredFreezeIsMoreThanMaxFreeze : CandyMachineError {
    override val code: Int = 6044

    override val message: String = "Freeze time can't be longer than MAX_FREEZE_TIME."
}

class NoWithdrawWithFreeze : CandyMachineError {
    override val code: Int = 6045

    override val message: String =
            "Can't withdraw Candy Machine while freeze is active. Disable freeze first."
}

class NoWithdrawWithFrozenFunds : CandyMachineError {
    override val code: Int = 6046

    override val message: String =
            "Can't withdraw Candy Machine while frozen funds need to be redeemed. Unlock funds first."
}

class MissingRemoveFreezeTokenAccounts : CandyMachineError {
    override val code: Int = 6047

    override val message: String =
            "Missing required remaining accounts for remove_freeze with token mint."
}

class InvalidFreezeWithdrawTokenAddress : CandyMachineError {
    override val code: Int = 6048

    override val message: String = "Can't withdraw SPL Token from freeze PDA into itself"
}

class NoUnlockWithNFTsStillFrozen : CandyMachineError {
    override val code: Int = 6049

    override val message: String =
            "Can't unlock funds while NFTs are still frozen. Run thaw on all NFTs first."
}

class SizedCollectionMetadataMustBeMutable : CandyMachineError {
    override val code: Int = 6050

    override val message: String =
            "Setting a sized collection requires the collection metadata to be mutable."
}
