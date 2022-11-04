//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-11-01
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

    override val message: String = "Exceeded account increase limit"
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

class ExceededLength : CandyGuardError {
    override val code: Int = 6010

    override val message: String = "Value exceeded maximum length"
}

class CandyMachineEmpty : CandyGuardError {
    override val code: Int = 6011

    override val message: String = "Candy machine is empty"
}

class InstructionNotFound : CandyGuardError {
    override val code: Int = 6012

    override val message: String = "No instruction was found"
}

class CollectionKeyMismatch : CandyGuardError {
    override val code: Int = 6013

    override val message: String = "Collection public key mismatch"
}

class MissingCollectionAccounts : CandyGuardError {
    override val code: Int = 6014

    override val message: String = "Missing collection accounts"
}

class CollectionUpdateAuthorityKeyMismatch : CandyGuardError {
    override val code: Int = 6015

    override val message: String = "Collection update authority public key mismatch"
}

class MintNotLastTransaction : CandyGuardError {
    override val code: Int = 6016

    override val message: String = "Mint must be the last instructions of the transaction"
}

class MintNotLive : CandyGuardError {
    override val code: Int = 6017

    override val message: String = "Mint is not live"
}

class NotEnoughSOL : CandyGuardError {
    override val code: Int = 6018

    override val message: String = "Not enough SOL to pay for the mint"
}

class TokenBurnFailed : CandyGuardError {
    override val code: Int = 6019

    override val message: String = "Token burn failed"
}

class NotEnoughTokens : CandyGuardError {
    override val code: Int = 6020

    override val message: String = "Not enough tokens on the account"
}

class TokenTransferFailed : CandyGuardError {
    override val code: Int = 6021

    override val message: String = "Token transfer failed"
}

class MissingRequiredSignature : CandyGuardError {
    override val code: Int = 6022

    override val message: String = "A signature was required but not found"
}

class GatewayTokenInvalid : CandyGuardError {
    override val code: Int = 6023

    override val message: String = "Gateway token is not valid"
}

class AfterEndDate : CandyGuardError {
    override val code: Int = 6024

    override val message: String = "Current time is after the set end date"
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

class AllowedListNotEnabled : CandyGuardError {
    override val code: Int = 6028

    override val message: String = "Allow list guard is not enabled"
}

class AllowedMintLimitReached : CandyGuardError {
    override val code: Int = 6029

    override val message: String = "The maximum number of allowed mints was reached"
}

class InvalidNftCollection : CandyGuardError {
    override val code: Int = 6030

    override val message: String = "Invalid NFT collection"
}

class MissingNft : CandyGuardError {
    override val code: Int = 6031

    override val message: String = "Missing NFT on the account"
}

class MaximumRedeemedAmount : CandyGuardError {
    override val code: Int = 6032

    override val message: String = "Current redemeed items is at the set maximum amount"
}

class AddressNotAuthorized : CandyGuardError {
    override val code: Int = 6033

    override val message: String = "Address not authorized"
}

class MissingFreezeInstruction : CandyGuardError {
    override val code: Int = 6034

    override val message: String = "Missing freeze instruction data"
}

class FreezeGuardNotEnabled : CandyGuardError {
    override val code: Int = 6035

    override val message: String = "Freeze guard must be enabled"
}

class FreezeNotInitialized : CandyGuardError {
    override val code: Int = 6036

    override val message: String = "Freeze must be initialized"
}

class MissingFreezePeriod : CandyGuardError {
    override val code: Int = 6037

    override val message: String = "Missing freeze period"
}

class FreezeEscrowAlreadyExists : CandyGuardError {
    override val code: Int = 6038

    override val message: String = "The freeze escrow account already exists"
}

class ExceededMaximumFreezePeriod : CandyGuardError {
    override val code: Int = 6039

    override val message: String = "Maximum freeze period exceeded"
}

class ThawNotEnabled : CandyGuardError {
    override val code: Int = 6040

    override val message: String = "Thaw is not enabled"
}

class UnlockNotEnabled : CandyGuardError {
    override val code: Int = 6041

    override val message: String = "Unlock is not enabled (not all NFTs are thawed)"
}

class DuplicatedGroupLabel : CandyGuardError {
    override val code: Int = 6042

    override val message: String = "Duplicated group label"
}

class DuplicatedMintLimitId : CandyGuardError {
    override val code: Int = 6043

    override val message: String = "Duplicated mint limit id"
}

class UnauthorizedProgramFound : CandyGuardError {
    override val code: Int = 6044

    override val message: String = "An unauthorized program was found in the transaction"
}

class ExceededProgramListSize : CandyGuardError {
    override val code: Int = 6045

    override val message: String = "Exceeded the maximum number of programs in the additional list"
}
