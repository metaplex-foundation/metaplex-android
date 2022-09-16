//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-16
//
package com.metaplex.lib.experimental.jen.candymachinecore

import kotlin.Int
import kotlin.String

sealed interface CandyMachineCoreError {
    val code: Int

    val message: String
}

class IncorrectOwner : CandyMachineCoreError {
    override val code: Int = 6000

    override val message: String = "Account does not have correct owner"
}

class Uninitialized : CandyMachineCoreError {
    override val code: Int = 6001

    override val message: String = "Account is not initialized"
}

class MintMismatch : CandyMachineCoreError {
    override val code: Int = 6002

    override val message: String = "Mint Mismatch"
}

class IndexGreaterThanLength : CandyMachineCoreError {
    override val code: Int = 6003

    override val message: String = "Index greater than length"
}

class NumericalOverflowError : CandyMachineCoreError {
    override val code: Int = 6004

    override val message: String = "Numerical overflow error"
}

class TooManyCreators : CandyMachineCoreError {
    override val code: Int = 6005

    override val message: String =
            "Can only provide up to 4 creators to candy machine (because candy machine is one)"
}

class CandyMachineEmpty : CandyMachineCoreError {
    override val code: Int = 6006

    override val message: String = "Candy machine is empty"
}

class HiddenSettingsDoNotHaveConfigLines : CandyMachineCoreError {
    override val code: Int = 6007

    override val message: String =
            "Candy machines using hidden uris do not have config lines, they have a single hash representing hashed order"
}

class CannotChangeNumberOfLines : CandyMachineCoreError {
    override val code: Int = 6008

    override val message: String = "Cannot change number of lines unless is a hidden config"
}

class CannotSwitchToHiddenSettings : CandyMachineCoreError {
    override val code: Int = 6009

    override val message: String =
            "Cannot switch to hidden settings after items available is greater than 0"
}

class IncorrectCollectionAuthority : CandyMachineCoreError {
    override val code: Int = 6010

    override val message: String = "Incorrect collection NFT authority"
}

class MetadataAccountMustBeEmpty : CandyMachineCoreError {
    override val code: Int = 6011

    override val message: String =
            "The metadata account has data in it, and this must be empty to mint a new NFT"
}

class NoChangingCollectionDuringMint : CandyMachineCoreError {
    override val code: Int = 6012

    override val message: String =
            "Can't change collection settings after items have begun to be minted"
}

class ExceededLengthError : CandyMachineCoreError {
    override val code: Int = 6013

    override val message: String = "Value longer than expected maximum value"
}

class MissingConfigLinesSettings : CandyMachineCoreError {
    override val code: Int = 6014

    override val message: String = "Missing config lines settings"
}

class CannotIncreaseLength : CandyMachineCoreError {
    override val code: Int = 6015

    override val message: String = "Cannot increase the length in config lines settings"
}

class CannotSwitchFromHiddenSettings : CandyMachineCoreError {
    override val code: Int = 6016

    override val message: String = "Cannot switch from hidden settings"
}

class CannotChangeSequentialIndexGeneration : CandyMachineCoreError {
    override val code: Int = 6017

    override val message: String =
            "Cannot change sequential index generation after items have begun to be minted"
}

class CollectionKeyMismatch : CandyMachineCoreError {
    override val code: Int = 6018

    override val message: String = "Collection public key mismatch"
}

class CouldNotRetrieveConfigLineData : CandyMachineCoreError {
    override val code: Int = 6019

    override val message: String = "Could not retrive config line data"
}
