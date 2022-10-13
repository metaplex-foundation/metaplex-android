//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-09-30
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

    override val message: String = "Account does not have correct owner"
}

class Uninitialized : CandyMachineError {
    override val code: Int = 6001

    override val message: String = "Account is not initialized"
}

class MintMismatch : CandyMachineError {
    override val code: Int = 6002

    override val message: String = "Mint Mismatch"
}

class IndexGreaterThanLength : CandyMachineError {
    override val code: Int = 6003

    override val message: String = "Index greater than length"
}

class NumericalOverflowError : CandyMachineError {
    override val code: Int = 6004

    override val message: String = "Numerical overflow error"
}

class TooManyCreators : CandyMachineError {
    override val code: Int = 6005

    override val message: String =
            "Can only provide up to 4 creators to candy machine (because candy machine is one)"
}

class CandyMachineEmpty : CandyMachineError {
    override val code: Int = 6006

    override val message: String = "Candy machine is empty"
}

class HiddenSettingsDoNotHaveConfigLines : CandyMachineError {
    override val code: Int = 6007

    override val message: String =
            "Candy machines using hidden uris do not have config lines, they have a single hash representing hashed order"
}

class CannotChangeNumberOfLines : CandyMachineError {
    override val code: Int = 6008

    override val message: String = "Cannot change number of lines unless is a hidden config"
}

class CannotSwitchToHiddenSettings : CandyMachineError {
    override val code: Int = 6009

    override val message: String =
            "Cannot switch to hidden settings after items available is greater than 0"
}

class IncorrectCollectionAuthority : CandyMachineError {
    override val code: Int = 6010

    override val message: String = "Incorrect collection NFT authority"
}

class MetadataAccountMustBeEmpty : CandyMachineError {
    override val code: Int = 6011

    override val message: String =
            "The metadata account has data in it, and this must be empty to mint a new NFT"
}

class NoChangingCollectionDuringMint : CandyMachineError {
    override val code: Int = 6012

    override val message: String =
            "Can't change collection settings after items have begun to be minted"
}

class ExceededLengthError : CandyMachineError {
    override val code: Int = 6013

    override val message: String = "Value longer than expected maximum value"
}

class MissingConfigLinesSettings : CandyMachineError {
    override val code: Int = 6014

    override val message: String = "Missing config lines settings"
}

class CannotIncreaseLength : CandyMachineError {
    override val code: Int = 6015

    override val message: String = "Cannot increase the length in config lines settings"
}

class CannotSwitchFromHiddenSettings : CandyMachineError {
    override val code: Int = 6016

    override val message: String = "Cannot switch from hidden settings"
}

class CannotChangeSequentialIndexGeneration : CandyMachineError {
    override val code: Int = 6017

    override val message: String =
            "Cannot change sequential index generation after items have begun to be minted"
}

class CollectionKeyMismatch : CandyMachineError {
    override val code: Int = 6018

    override val message: String = "Collection public key mismatch"
}

class CouldNotRetrieveConfigLineData : CandyMachineError {
    override val code: Int = 6019

    override val message: String = "Could not retrive config line data"
}

class NotFullyLoaded : CandyMachineError {
    override val code: Int = 6020

    override val message: String = "Not all config lines were added to the candy machine"
}
