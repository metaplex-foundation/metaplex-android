//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-28
//
package com.metaplex.lib.experimental.jen.candyguard

import kotlin.Int
import kotlin.String

sealed interface CandyGuardError {
    val code: Int

    val message: String
}

class InvalidAccountSize : CandyGuardError {
    override val code: Int = 6000

    override val message: String = "Could not save guard to account"
}

class DeserializationError : CandyGuardError {
    override val code: Int = 6001

    override val message: String = "Could not deserialize guard"
}

class PublicKeyMismatch : CandyGuardError {
    override val code: Int = 6002

    override val message: String = "Public key mismatch"
}

class DataIncrementLimitExceeded : CandyGuardError {
    override val code: Int = 6003

    override val message: String = "Missing expected remaining account"
}

class IncorrectOwner : CandyGuardError {
    override val code: Int = 6004

    override val message: String = "Account does not have correct owner"
}

class Uninitialized : CandyGuardError {
    override val code: Int = 6005

    override val message: String = "Account is not initialized"
}

class MissingRemainingAccount : CandyGuardError {
    override val code: Int = 6006

    override val message: String = "Missing expected remaining account"
}

class NumericalOverflowError : CandyGuardError {
    override val code: Int = 6007

    override val message: String = "Numerical overflow error"
}

class RequiredGroupLabelNotFound : CandyGuardError {
    override val code: Int = 6008

    override val message: String = "Missing required group label"
}

class GroupNotFound : CandyGuardError {
    override val code: Int = 6009

    override val message: String = "Group not found"
}

class LabelExceededLength : CandyGuardError {
    override val code: Int = 6010

    override val message: String = "Group not found"
}

class CollectionKeyMismatch : CandyGuardError {
    override val code: Int = 6011

    override val message: String = "Collection public key mismatch"
}

class MissingCollectionAccounts : CandyGuardError {
    override val code: Int = 6012

    override val message: String = "Missing collection accounts"
}

class CollectionUpdateAuthorityKeyMismatch : CandyGuardError {
    override val code: Int = 6013

    override val message: String = "Collection update authority public key mismatch"
}

class MintNotLastTransaction : CandyGuardError {
    override val code: Int = 6014

    override val message: String = "Mint must be the last instructions of the transaction"
}

class MintNotLive : CandyGuardError {
    override val code: Int = 6015

    override val message: String = "Mint is not live"
}

class NotEnoughSOL : CandyGuardError {
    override val code: Int = 6016

    override val message: String = "Not enough SOL to pay for the mint"
}

class TokenTransferFailed : CandyGuardError {
    override val code: Int = 6017

    override val message: String = "Token transfer failed"
}

class NotEnoughTokens : CandyGuardError {
    override val code: Int = 6018

    override val message: String = "Not enough tokens to pay for this minting"
}

class MissingRequiredSignature : CandyGuardError {
    override val code: Int = 6019

    override val message: String = "A signature was required but not found"
}

class TokenBurnFailed : CandyGuardError {
    override val code: Int = 6020

    override val message: String = "Token burn failed"
}

class NoWhitelistToken : CandyGuardError {
    override val code: Int = 6021

    override val message: String = "No whitelist token present"
}

class GatewayTokenInvalid : CandyGuardError {
    override val code: Int = 6022

    override val message: String = "Gateway token is not valid"
}

class AfterEndSettingsDate : CandyGuardError {
    override val code: Int = 6023

    override val message: String = "Current time is after the set end settings date"
}

class AfterEndSettingsMintAmount : CandyGuardError {
    override val code: Int = 6024

    override val message: String = "Current items minted is at the set end settings amount"
}

class InvalidMintTime : CandyGuardError {
    override val code: Int = 6025

    override val message: String = "Current time is not within the allowed mint time"
}

class AddressNotFoundInAllowedList : CandyGuardError {
    override val code: Int = 6026

    override val message: String = "Address not found on the allowed list"
}

class MissingAllowedListProof : CandyGuardError {
    override val code: Int = 6027

    override val message: String = "Missing allowed list proof"
}

class AllowedMintLimitReached : CandyGuardError {
    override val code: Int = 6028

    override val message: String = "The maximum number of allowed mints was reached"
}

class InvalidNFTCollectionPayment : CandyGuardError {
    override val code: Int = 6029

    override val message: String = "Invalid NFT Collection Payment"
}