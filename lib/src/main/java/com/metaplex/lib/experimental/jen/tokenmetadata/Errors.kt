//
// Errors
// Metaplex
//
// This code was generated locally by Funkatronics on 2022-10-03
//
package com.metaplex.lib.experimental.jen.tokenmetadata

import kotlin.Int
import kotlin.String

sealed interface TokenMetadataError {
    val code: Int

    val message: String
}

class InstructionUnpackError : TokenMetadataError {
    override val code: Int = 0

    override val message: String = "Failed to unpack instruction data"
}

class InstructionPackError : TokenMetadataError {
    override val code: Int = 1

    override val message: String = "Failed to pack instruction data"
}

class NotRentExempt : TokenMetadataError {
    override val code: Int = 2

    override val message: String = "Lamport balance below rent-exempt threshold"
}

class AlreadyInitialized : TokenMetadataError {
    override val code: Int = 3

    override val message: String = "Already initialized"
}

class Uninitialized : TokenMetadataError {
    override val code: Int = 4

    override val message: String = "Uninitialized"
}

class InvalidMetadataKey : TokenMetadataError {
    override val code: Int = 5

    override val message: String =
            " Metadata's key must match seed of ['metadata', program id, mint] provided"
}

class InvalidEditionKey : TokenMetadataError {
    override val code: Int = 6

    override val message: String =
            "Edition's key must match seed of ['metadata', program id, name, 'edition'] provided"
}

class UpdateAuthorityIncorrect : TokenMetadataError {
    override val code: Int = 7

    override val message: String = "Update Authority given does not match"
}

class UpdateAuthorityIsNotSigner : TokenMetadataError {
    override val code: Int = 8

    override val message: String = "Update Authority needs to be signer to update metadata"
}

class NotMintAuthority : TokenMetadataError {
    override val code: Int = 9

    override val message: String = "You must be the mint authority and signer on this transaction"
}

class InvalidMintAuthority : TokenMetadataError {
    override val code: Int = 10

    override val message: String =
            "Mint authority provided does not match the authority on the mint"
}

class NameTooLong : TokenMetadataError {
    override val code: Int = 11

    override val message: String = "Name too long"
}

class SymbolTooLong : TokenMetadataError {
    override val code: Int = 12

    override val message: String = "Symbol too long"
}

class UriTooLong : TokenMetadataError {
    override val code: Int = 13

    override val message: String = "URI too long"
}

class UpdateAuthorityMustBeEqualToMetadataAuthorityAndSigner : TokenMetadataError {
    override val code: Int = 14

    override val message: String =
            "Update authority must be equivalent to the metadata's authority and also signer of this transaction"
}

class MintMismatch : TokenMetadataError {
    override val code: Int = 15

    override val message: String = "Mint given does not match mint on Metadata"
}

class EditionsMustHaveExactlyOneToken : TokenMetadataError {
    override val code: Int = 16

    override val message: String = "Editions must have exactly one token"
}

class MaxEditionsMintedAlready : TokenMetadataError {
    override val code: Int = 17

    override val message: String = "Maximum editions printed already"
}

class TokenMintToFailed : TokenMetadataError {
    override val code: Int = 18

    override val message: String = "Token mint to failed"
}

class MasterRecordMismatch : TokenMetadataError {
    override val code: Int = 19

    override val message: String =
            "The master edition record passed must match the master record on the edition given"
}

class DestinationMintMismatch : TokenMetadataError {
    override val code: Int = 20

    override val message: String = "The destination account does not have the right mint"
}

class EditionAlreadyMinted : TokenMetadataError {
    override val code: Int = 21

    override val message: String = "An edition can only mint one of its kind!"
}

class PrintingMintDecimalsShouldBeZero : TokenMetadataError {
    override val code: Int = 22

    override val message: String = "Printing mint decimals should be zero"
}

class OneTimePrintingAuthorizationMintDecimalsShouldBeZero : TokenMetadataError {
    override val code: Int = 23

    override val message: String = "OneTimePrintingAuthorization mint decimals should be zero"
}

class EditionMintDecimalsShouldBeZero : TokenMetadataError {
    override val code: Int = 24

    override val message: String = "EditionMintDecimalsShouldBeZero"
}

class TokenBurnFailed : TokenMetadataError {
    override val code: Int = 25

    override val message: String = "Token burn failed"
}

class TokenAccountOneTimeAuthMintMismatch : TokenMetadataError {
    override val code: Int = 26

    override val message: String =
            "The One Time authorization mint does not match that on the token account!"
}

class DerivedKeyInvalid : TokenMetadataError {
    override val code: Int = 27

    override val message: String = "Derived key invalid"
}

class PrintingMintMismatch : TokenMetadataError {
    override val code: Int = 28

    override val message: String = "The Printing mint does not match that on the master edition!"
}

class OneTimePrintingAuthMintMismatch : TokenMetadataError {
    override val code: Int = 29

    override val message: String =
            "The One Time Printing Auth mint does not match that on the master edition!"
}

class TokenAccountMintMismatch : TokenMetadataError {
    override val code: Int = 30

    override val message: String = "The mint of the token account does not match the Printing mint!"
}

class TokenAccountMintMismatchV2 : TokenMetadataError {
    override val code: Int = 31

    override val message: String =
            "The mint of the token account does not match the master metadata mint!"
}

class NotEnoughTokens : TokenMetadataError {
    override val code: Int = 32

    override val message: String = "Not enough tokens to mint a limited edition"
}

class PrintingMintAuthorizationAccountMismatch : TokenMetadataError {
    override val code: Int = 33

    override val message: String =
            "The mint on your authorization token holding account does not match your Printing mint!"
}

class AuthorizationTokenAccountOwnerMismatch : TokenMetadataError {
    override val code: Int = 34

    override val message: String =
            "The authorization token account has a different owner than the update authority for the master edition!"
}

class Disabled : TokenMetadataError {
    override val code: Int = 35

    override val message: String = "This feature is currently disabled."
}

class CreatorsTooLong : TokenMetadataError {
    override val code: Int = 36

    override val message: String = "Creators list too long"
}

class CreatorsMustBeAtleastOne : TokenMetadataError {
    override val code: Int = 37

    override val message: String = "Creators must be at least one if set"
}

class MustBeOneOfCreators : TokenMetadataError {
    override val code: Int = 38

    override val message: String =
            "If using a creators array, you must be one of the creators listed"
}

class NoCreatorsPresentOnMetadata : TokenMetadataError {
    override val code: Int = 39

    override val message: String = "This metadata does not have creators"
}

class CreatorNotFound : TokenMetadataError {
    override val code: Int = 40

    override val message: String = "This creator address was not found"
}

class InvalidBasisPoints : TokenMetadataError {
    override val code: Int = 41

    override val message: String = "Basis points cannot be more than 10000"
}

class PrimarySaleCanOnlyBeFlippedToTrue : TokenMetadataError {
    override val code: Int = 42

    override val message: String = "Primary sale can only be flipped to true and is immutable"
}

class OwnerMismatch : TokenMetadataError {
    override val code: Int = 43

    override val message: String = "Owner does not match that on the account given"
}

class NoBalanceInAccountForAuthorization : TokenMetadataError {
    override val code: Int = 44

    override val message: String = "This account has no tokens to be used for authorization"
}

class ShareTotalMustBe100 : TokenMetadataError {
    override val code: Int = 45

    override val message: String = "Share total must equal 100 for creator array"
}

class ReservationExists : TokenMetadataError {
    override val code: Int = 46

    override val message: String = "This reservation list already exists!"
}

class ReservationDoesNotExist : TokenMetadataError {
    override val code: Int = 47

    override val message: String = "This reservation list does not exist!"
}

class ReservationNotSet : TokenMetadataError {
    override val code: Int = 48

    override val message: String =
            "This reservation list exists but was never set with reservations"
}

class ReservationAlreadyMade : TokenMetadataError {
    override val code: Int = 49

    override val message: String = "This reservation list has already been set!"
}

class BeyondMaxAddressSize : TokenMetadataError {
    override val code: Int = 50

    override val message: String = "Provided more addresses than max allowed in single reservation"
}

class NumericalOverflowError : TokenMetadataError {
    override val code: Int = 51

    override val message: String = "NumericalOverflowError"
}

class ReservationBreachesMaximumSupply : TokenMetadataError {
    override val code: Int = 52

    override val message: String =
            "This reservation would go beyond the maximum supply of the master edition!"
}

class AddressNotInReservation : TokenMetadataError {
    override val code: Int = 53

    override val message: String = "Address not in reservation!"
}

class CannotVerifyAnotherCreator : TokenMetadataError {
    override val code: Int = 54

    override val message: String = "You cannot unilaterally verify another creator, they must sign"
}

class CannotUnverifyAnotherCreator : TokenMetadataError {
    override val code: Int = 55

    override val message: String = "You cannot unilaterally unverify another creator"
}

class SpotMismatch : TokenMetadataError {
    override val code: Int = 56

    override val message: String =
            "In initial reservation setting, spots remaining should equal total spots"
}

class IncorrectOwner : TokenMetadataError {
    override val code: Int = 57

    override val message: String = "Incorrect account owner"
}

class PrintingWouldBreachMaximumSupply : TokenMetadataError {
    override val code: Int = 58

    override val message: String =
            "printing these tokens would breach the maximum supply limit of the master edition"
}

class DataIsImmutable : TokenMetadataError {
    override val code: Int = 59

    override val message: String = "Data is immutable"
}

class DuplicateCreatorAddress : TokenMetadataError {
    override val code: Int = 60

    override val message: String = "No duplicate creator addresses"
}

class ReservationSpotsRemainingShouldMatchTotalSpotsAtStart : TokenMetadataError {
    override val code: Int = 61

    override val message: String =
            "Reservation spots remaining should match total spots when first being created"
}

class InvalidTokenProgram : TokenMetadataError {
    override val code: Int = 62

    override val message: String = "Invalid token program"
}

class DataTypeMismatch : TokenMetadataError {
    override val code: Int = 63

    override val message: String = "Data type mismatch"
}

class BeyondAlottedAddressSize : TokenMetadataError {
    override val code: Int = 64

    override val message: String = "Beyond alotted address size in reservation!"
}

class ReservationNotComplete : TokenMetadataError {
    override val code: Int = 65

    override val message: String = "The reservation has only been partially alotted"
}

class TriedToReplaceAnExistingReservation : TokenMetadataError {
    override val code: Int = 66

    override val message: String = "You cannot splice over an existing reservation!"
}

class InvalidOperation : TokenMetadataError {
    override val code: Int = 67

    override val message: String = "Invalid operation"
}

class InvalidOwner : TokenMetadataError {
    override val code: Int = 68

    override val message: String = "Invalid Owner"
}

class PrintingMintSupplyMustBeZeroForConversion : TokenMetadataError {
    override val code: Int = 69

    override val message: String = "Printing mint supply must be zero for conversion"
}

class OneTimeAuthMintSupplyMustBeZeroForConversion : TokenMetadataError {
    override val code: Int = 70

    override val message: String = "One Time Auth mint supply must be zero for conversion"
}

class InvalidEditionIndex : TokenMetadataError {
    override val code: Int = 71

    override val message: String =
            "You tried to insert one edition too many into an edition mark pda"
}

class ReservationArrayShouldBeSizeOne : TokenMetadataError {
    override val code: Int = 72

    override val message: String =
            "In the legacy system the reservation needs to be of size one for cpu limit reasons"
}

class IsMutableCanOnlyBeFlippedToFalse : TokenMetadataError {
    override val code: Int = 73

    override val message: String = "Is Mutable can only be flipped to false"
}

class CollectionCannotBeVerifiedInThisInstruction : TokenMetadataError {
    override val code: Int = 74

    override val message: String = "Cannont Verify Collection in this Instruction"
}

class Removed : TokenMetadataError {
    override val code: Int = 75

    override val message: String =
            "This instruction was deprecated in a previous release and is now removed"
}

class MustBeBurned : TokenMetadataError {
    override val code: Int = 76

    override val message: String =
            "This token use method is burn and there are no remaining uses, it must be burned"
}

class InvalidUseMethod : TokenMetadataError {
    override val code: Int = 77

    override val message: String = "This use method is invalid"
}

class CannotChangeUseMethodAfterFirstUse : TokenMetadataError {
    override val code: Int = 78

    override val message: String = "Cannot Change Use Method after the first use"
}

class CannotChangeUsesAfterFirstUse : TokenMetadataError {
    override val code: Int = 79

    override val message: String = "Cannot Change Remaining or Available uses after the first use"
}

class CollectionNotFound : TokenMetadataError {
    override val code: Int = 80

    override val message: String = "Collection Not Found on Metadata"
}

class InvalidCollectionUpdateAuthority : TokenMetadataError {
    override val code: Int = 81

    override val message: String = "Collection Update Authority is invalid"
}

class CollectionMustBeAUniqueMasterEdition : TokenMetadataError {
    override val code: Int = 82

    override val message: String = "Collection Must Be a Unique Master Edition v2"
}

class UseAuthorityRecordAlreadyExists : TokenMetadataError {
    override val code: Int = 83

    override val message: String =
            "The Use Authority Record Already Exists, to modify it Revoke, then Approve"
}

class UseAuthorityRecordAlreadyRevoked : TokenMetadataError {
    override val code: Int = 84

    override val message: String = "The Use Authority Record is empty or already revoked"
}

class Unusable : TokenMetadataError {
    override val code: Int = 85

    override val message: String = "This token has no uses"
}

class NotEnoughUses : TokenMetadataError {
    override val code: Int = 86

    override val message: String = "There are not enough Uses left on this token."
}

class CollectionAuthorityRecordAlreadyExists : TokenMetadataError {
    override val code: Int = 87

    override val message: String = "This Collection Authority Record Already Exists."
}

class CollectionAuthorityDoesNotExist : TokenMetadataError {
    override val code: Int = 88

    override val message: String = "This Collection Authority Record Does Not Exist."
}

class InvalidUseAuthorityRecord : TokenMetadataError {
    override val code: Int = 89

    override val message: String = "This Use Authority Record is invalid."
}

class InvalidCollectionAuthorityRecord : TokenMetadataError {
    override val code: Int = 90

    override val message: String = "This Collection Authority Record is invalid."
}

class InvalidFreezeAuthority : TokenMetadataError {
    override val code: Int = 91

    override val message: String = "Metadata does not match the freeze authority on the mint"
}

class InvalidDelegate : TokenMetadataError {
    override val code: Int = 92

    override val message: String =
            "All tokens in this account have not been delegated to this user."
}

class CannotAdjustVerifiedCreator : TokenMetadataError {
    override val code: Int = 93

    override val message: String = "Creator can not be adjusted once they are verified."
}

class CannotRemoveVerifiedCreator : TokenMetadataError {
    override val code: Int = 94

    override val message: String = "Verified creators cannot be removed."
}

class CannotWipeVerifiedCreators : TokenMetadataError {
    override val code: Int = 95

    override val message: String = "Can not wipe verified creators."
}

class NotAllowedToChangeSellerFeeBasisPoints : TokenMetadataError {
    override val code: Int = 96

    override val message: String = "Not allowed to change seller fee basis points."
}

class EditionOverrideCannotBeZero : TokenMetadataError {
    override val code: Int = 97

    override val message: String = "Edition override cannot be zero"
}

class InvalidUser : TokenMetadataError {
    override val code: Int = 98

    override val message: String = "Invalid User"
}

class RevokeCollectionAuthoritySignerIncorrect : TokenMetadataError {
    override val code: Int = 99

    override val message: String = "Revoke Collection Authority signer is incorrect"
}

class TokenCloseFailed : TokenMetadataError {
    override val code: Int = 100

    override val message: String = "Token close failed"
}

class UnsizedCollection : TokenMetadataError {
    override val code: Int = 101

    override val message: String = "Can't use this function on unsized collection"
}

class SizedCollection : TokenMetadataError {
    override val code: Int = 102

    override val message: String = "Can't use this function on a sized collection"
}

class MissingCollectionMetadata : TokenMetadataError {
    override val code: Int = 103

    override val message: String =
            "Can't burn a verified member of a collection w/o providing collection metadata account"
}

class NotAMemberOfCollection : TokenMetadataError {
    override val code: Int = 104

    override val message: String = "This NFT is not a member of the specified collection."
}

class NotVerifiedMemberOfCollection : TokenMetadataError {
    override val code: Int = 105

    override val message: String = "This NFT is not a verified member of the specified collection."
}

class NotACollectionParent : TokenMetadataError {
    override val code: Int = 106

    override val message: String = "This NFT is not a collection parent NFT."
}

class CouldNotDetermineTokenStandard : TokenMetadataError {
    override val code: Int = 107

    override val message: String = "Could not determine a TokenStandard type."
}

class MissingEditionAccount : TokenMetadataError {
    override val code: Int = 108

    override val message: String = "This mint account has an edition but none was provided."
}

class NotAMasterEdition : TokenMetadataError {
    override val code: Int = 109

    override val message: String = "This edition is not a Master Edition"
}

class MasterEditionHasPrints : TokenMetadataError {
    override val code: Int = 110

    override val message: String = "This Master Edition has existing prints"
}

class BorshDeserializationError : TokenMetadataError {
    override val code: Int = 111

    override val message: String = "Borsh Deserialization Error"
}

class CannotUpdateVerifiedCollection : TokenMetadataError {
    override val code: Int = 112

    override val message: String = "Cannot update a verified colleciton in this command"
}

class CollectionMasterEditionAccountInvalid : TokenMetadataError {
    override val code: Int = 113

    override val message: String = "Edition account doesnt match collection "
}

class AlreadyVerified : TokenMetadataError {
    override val code: Int = 114

    override val message: String = "Item is already verified."
}

class AlreadyUnverified : TokenMetadataError {
    override val code: Int = 115

    override val message: String = "Item is already unverified."
}

class NotAPrintEdition : TokenMetadataError {
    override val code: Int = 116

    override val message: String = "This edition is not a Print Edition"
}

class InvalidMasterEdition : TokenMetadataError {
    override val code: Int = 117

    override val message: String = "Invalid Master Edition"
}

class InvalidPrintEdition : TokenMetadataError {
    override val code: Int = 118

    override val message: String = "Invalid Print Edition"
}

class InvalidEditionMarker : TokenMetadataError {
    override val code: Int = 119

    override val message: String = "Invalid Edition Marker"
}

class ReservationListDeprecated : TokenMetadataError {
    override val code: Int = 120

    override val message: String = "Reservation List is Deprecated"
}

class PrintEditionDoesNotMatchMasterEdition : TokenMetadataError {
    override val code: Int = 121

    override val message: String = "Print Edition does not match Master Edition"
}
