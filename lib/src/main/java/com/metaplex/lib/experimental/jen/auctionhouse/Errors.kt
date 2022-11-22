//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-20
//
package com.metaplex.lib.experimental.jen.auctionhouse

import kotlin.Int
import kotlin.String

sealed interface AuctionHouseError {
    val code: Int

    val message: String
}

class PublicKeyMismatch : AuctionHouseError {
    override val code: Int = 6000

    override val message: String = "PublicKeyMismatch"
}

class InvalidMintAuthority : AuctionHouseError {
    override val code: Int = 6001

    override val message: String = "InvalidMintAuthority"
}

class UninitializedAccount : AuctionHouseError {
    override val code: Int = 6002

    override val message: String = "UninitializedAccount"
}

class IncorrectOwner : AuctionHouseError {
    override val code: Int = 6003

    override val message: String = "IncorrectOwner"
}

class PublicKeysShouldBeUnique : AuctionHouseError {
    override val code: Int = 6004

    override val message: String = "PublicKeysShouldBeUnique"
}

class StatementFalse : AuctionHouseError {
    override val code: Int = 6005

    override val message: String = "StatementFalse"
}

class NotRentExempt : AuctionHouseError {
    override val code: Int = 6006

    override val message: String = "NotRentExempt"
}

class NumericalOverflow : AuctionHouseError {
    override val code: Int = 6007

    override val message: String = "NumericalOverflow"
}

class ExpectedSolAccount : AuctionHouseError {
    override val code: Int = 6008

    override val message: String = "Expected a sol account but got an spl token account instead"
}

class CannotExchangeSOLForSol : AuctionHouseError {
    override val code: Int = 6009

    override val message: String = "Cannot exchange sol for sol"
}

class SOLWalletMustSign : AuctionHouseError {
    override val code: Int = 6010

    override val message: String = "If paying with sol, sol wallet must be signer"
}

class CannotTakeThisActionWithoutAuctionHouseSignOff : AuctionHouseError {
    override val code: Int = 6011

    override val message: String = "Cannot take this action without auction house signing too"
}

class NoPayerPresent : AuctionHouseError {
    override val code: Int = 6012

    override val message: String = "No payer present on this txn"
}

class DerivedKeyInvalid : AuctionHouseError {
    override val code: Int = 6013

    override val message: String = "Derived key invalid"
}

class MetadataDoesntExist : AuctionHouseError {
    override val code: Int = 6014

    override val message: String = "Metadata doesn't exist"
}

class InvalidTokenAmount : AuctionHouseError {
    override val code: Int = 6015

    override val message: String = "Invalid token amount"
}

class BothPartiesNeedToAgreeToSale : AuctionHouseError {
    override val code: Int = 6016

    override val message: String = "Both parties need to agree to this sale"
}

class CannotMatchFreeSalesWithoutAuctionHouseOrSellerSignoff : AuctionHouseError {
    override val code: Int = 6017

    override val message: String =
            "Cannot match free sales unless the auction house or seller signs off"
}

class SaleRequiresSigner : AuctionHouseError {
    override val code: Int = 6018

    override val message: String = "This sale requires a signer"
}

class OldSellerNotInitialized : AuctionHouseError {
    override val code: Int = 6019

    override val message: String = "Old seller not initialized"
}

class SellerATACannotHaveDelegate : AuctionHouseError {
    override val code: Int = 6020

    override val message: String = "Seller ata cannot have a delegate set"
}

class BuyerATACannotHaveDelegate : AuctionHouseError {
    override val code: Int = 6021

    override val message: String = "Buyer ata cannot have a delegate set"
}

class NoValidSignerPresent : AuctionHouseError {
    override val code: Int = 6022

    override val message: String = "No valid signer present"
}

class InvalidBasisPoints : AuctionHouseError {
    override val code: Int = 6023

    override val message: String = "BP must be less than or equal to 10000"
}

class TradeStateDoesntExist : AuctionHouseError {
    override val code: Int = 6024

    override val message: String = "The trade state account does not exist"
}

class TradeStateIsNotEmpty : AuctionHouseError {
    override val code: Int = 6025

    override val message: String = "The trade state is not empty"
}

class ReceiptIsEmpty : AuctionHouseError {
    override val code: Int = 6026

    override val message: String = "The receipt is empty"
}

class InstructionMismatch : AuctionHouseError {
    override val code: Int = 6027

    override val message: String = "The instruction does not match"
}

class InvalidAuctioneer : AuctionHouseError {
    override val code: Int = 6028

    override val message: String = "Invalid Auctioneer for this Auction House instance."
}

class MissingAuctioneerScope : AuctionHouseError {
    override val code: Int = 6029

    override val message: String = "The Auctioneer does not have the correct scope for this action."
}

class MustUseAuctioneerHandler : AuctionHouseError {
    override val code: Int = 6030

    override val message: String = "Must use auctioneer handler."
}

class NoAuctioneerProgramSet : AuctionHouseError {
    override val code: Int = 6031

    override val message: String = "No Auctioneer program set."
}

class TooManyScopes : AuctionHouseError {
    override val code: Int = 6032

    override val message: String = "Too many scopes."
}

class AuctionHouseNotDelegated : AuctionHouseError {
    override val code: Int = 6033

    override val message: String = "Auction House not delegated."
}

class BumpSeedNotInHashMap : AuctionHouseError {
    override val code: Int = 6034

    override val message: String = "Bump seed not in hash map."
}

class EscrowUnderRentExemption : AuctionHouseError {
    override val code: Int = 6035

    override val message: String =
            "The instruction would drain the escrow below rent exemption threshold"
}

class InvalidSeedsOrAuctionHouseNotDelegated : AuctionHouseError {
    override val code: Int = 6036

    override val message: String = "Invalid seeds or Auction House not delegated"
}

class BuyerTradeStateNotValid : AuctionHouseError {
    override val code: Int = 6037

    override val message: String = "The buyer trade state was unable to be initialized."
}

class MissingElementForPartialOrder : AuctionHouseError {
    override val code: Int = 6038

    override val message: String =
            "Partial order size and price must both be provided in a partial buy."
}

class NotEnoughTokensAvailableForPurchase : AuctionHouseError {
    override val code: Int = 6039

    override val message: String =
            "Amount of tokens available for purchase is less than the partial order amount."
}

class PartialPriceMismatch : AuctionHouseError {
    override val code: Int = 6040

    override val message: String =
            "Calculated partial price does not not partial price that was provided."
}

class AuctionHouseAlreadyDelegated : AuctionHouseError {
    override val code: Int = 6041

    override val message: String = "Auction House already delegated."
}

class AuctioneerAuthorityMismatch : AuctionHouseError {
    override val code: Int = 6042

    override val message: String = "Auctioneer Authority Mismatch"
}

class InsufficientFunds : AuctionHouseError {
    override val code: Int = 6043

    override val message: String = "Insufficient funds in escrow account to purchase."
}
