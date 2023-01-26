/*
 * idl
 * Metaplex
 * 
 * Created by Funkatronics on 9/29/2022
 */

package com.metaplex.lib.experimental.jen.tokenmetadata

import org.intellij.lang.annotations.Language

@Language("json")
val tokenMetadataJson = """
    {
      "version": "1.7.0-beta.2",
      "name": "mpl_token_metadata",
      "instructions": [
        {
          "name": "CreateMetadataAccount",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "update authority info"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            }
          ],
          "args": [
            {
              "name": "createMetadataAccountArgs",
              "type": {
                "defined": "CreateMetadataAccountArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 0
          }
        },
        {
          "name": "UpdateMetadataAccount",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority key"
            }
          ],
          "args": [
            {
              "name": "updateMetadataAccountArgs",
              "type": {
                "defined": "UpdateMetadataAccountArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 1
          }
        },
        {
          "name": "DeprecatedCreateMasterEdition",
          "accounts": [
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Unallocated edition V1 account with address as pda of ['metadata', program id, mint, 'edition']"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata mint"
            },
            {
              "name": "printingMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Printing mint - A mint you control that can mint tokens that can be exchanged for limited editions of your master edition via the MintNewEditionFromMasterEditionViaToken endpoint"
            },
            {
              "name": "oneTimePrintingAuthorizationMint",
              "isMut": true,
              "isSigner": false,
              "desc": "One time authorization printing mint - A mint you control that prints tokens that gives the bearer permission to mint any number of tokens from the printing mint one time via an endpoint with the token-metadata program for your metadata. Also burns the token."
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Current Update authority key"
            },
            {
              "name": "printingMintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Printing mint authority - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY."
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority on the metadata's mint - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "payer",
              "isMut": false,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            },
            {
              "name": "oneTimePrintingAuthorizationMintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "One time authorization printing mint authority - must be provided if using max supply. THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY."
            }
          ],
          "args": [
            {
              "name": "createMasterEditionArgs",
              "type": {
                "defined": "CreateMasterEditionArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 2
          }
        },
        {
          "name": "DeprecatedMintNewEditionFromMasterEditionViaPrintingToken",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "New Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "New Edition V1 (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Master Record Edition V1 (pda of ['metadata', program id, master metadata mint id, 'edition'])"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of new token - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority of new mint"
            },
            {
              "name": "printingMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Printing Mint of master record edition"
            },
            {
              "name": "masterTokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account containing Printing mint token to be transferred"
            },
            {
              "name": "editionMarker",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition pda to mark creation - will be checked for pre-existence. (pda of ['metadata', program id, master mint id, edition_number])"
            },
            {
              "name": "burnAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Burn authority for this token"
            },
            {
              "name": "payer",
              "isMut": false,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "masterUpdateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "update authority info for new metadata account"
            },
            {
              "name": "masterMetadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Master record metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            },
            {
              "name": "reservationList",
              "isMut": true,
              "isSigner": false,
              "desc": "Reservation List - If present, and you are on this list, you can get an edition number given by your position on the list.",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 3
          }
        },
        {
          "name": "UpdatePrimarySaleHappenedViaToken",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "owner",
              "isMut": false,
              "isSigner": true,
              "desc": "Owner on the token account"
            },
            {
              "name": "token",
              "isMut": false,
              "isSigner": false,
              "desc": "Account containing tokens from the metadata's mint"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 4
          }
        },
        {
          "name": "DeprecatedSetReservationList",
          "accounts": [
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Master Edition V1 key (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "reservationList",
              "isMut": true,
              "isSigner": false,
              "desc": "PDA for ReservationList of ['metadata', program id, master edition key, 'reservation', resource-key]"
            },
            {
              "name": "resource",
              "isMut": false,
              "isSigner": true,
              "desc": "The resource you tied the reservation list too"
            }
          ],
          "args": [
            {
              "name": "setReservationListArgs",
              "type": {
                "defined": "SetReservationListArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 5
          }
        },
        {
          "name": "DeprecatedCreateReservationList",
          "accounts": [
            {
              "name": "reservationList",
              "isMut": true,
              "isSigner": false,
              "desc": "PDA for ReservationList of ['metadata', program id, master edition key, 'reservation', resource-key]"
            },
            {
              "name": "payer",
              "isMut": false,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": " Master Edition V1 key (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "resource",
              "isMut": false,
              "isSigner": false,
              "desc": "A resource you wish to tie the reservation list to. This is so your later visitors who come to redeem can derive your reservation list PDA with something they can easily get at. You choose what this should be."
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 6
          }
        },
        {
          "name": "SignMetadata",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "creator",
              "isMut": false,
              "isSigner": true,
              "desc": "Creator"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 7
          }
        },
        {
          "name": "DeprecatedMintPrintingTokensViaToken",
          "accounts": [
            {
              "name": "destination",
              "isMut": true,
              "isSigner": false,
              "desc": "Destination account"
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account containing one time authorization token"
            },
            {
              "name": "oneTimePrintingAuthorizationMint",
              "isMut": true,
              "isSigner": false,
              "desc": "One time authorization mint"
            },
            {
              "name": "printingMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Printing mint"
            },
            {
              "name": "burnAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Burn authority"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": "Master Edition V1 key (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent"
            }
          ],
          "args": [
            {
              "name": "mintPrintingTokensViaTokenArgs",
              "type": {
                "defined": "MintPrintingTokensViaTokenArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 8
          }
        },
        {
          "name": "DeprecatedMintPrintingTokens",
          "accounts": [
            {
              "name": "destination",
              "isMut": true,
              "isSigner": false,
              "desc": "Destination account"
            },
            {
              "name": "printingMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Printing mint"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": "Master Edition V1 key (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent"
            }
          ],
          "args": [
            {
              "name": "mintPrintingTokensViaTokenArgs",
              "type": {
                "defined": "MintPrintingTokensViaTokenArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 9
          }
        },
        {
          "name": "CreateMasterEdition",
          "accounts": [
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Unallocated edition V2 account with address as pda of ['metadata', program id, mint, 'edition']"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata mint"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority on the metadata's mint - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            }
          ],
          "args": [
            {
              "name": "createMasterEditionArgs",
              "type": {
                "defined": "CreateMasterEditionArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 10
          }
        },
        {
          "name": "MintNewEditionFromMasterEditionViaToken",
          "accounts": [
            {
              "name": "newMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "New Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "newEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "New Edition (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Master Record Edition V2 (pda of ['metadata', program id, master metadata mint id, 'edition'])"
            },
            {
              "name": "newMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of new token - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "editionMarkPda",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition pda to mark creation - will be checked for pre-existence. (pda of ['metadata', program id, master metadata mint id, 'edition', edition_number]) where edition_number is NOT the edition number you pass in args but actually edition_number = floor(edition/EDITION_MARKER_BIT_SIZE)."
            },
            {
              "name": "newMintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority of new mint"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "tokenAccountOwner",
              "isMut": false,
              "isSigner": true,
              "desc": "owner of token account containing master token (#8)"
            },
            {
              "name": "tokenAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "token account containing token from master metadata mint"
            },
            {
              "name": "newMetadataUpdateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "Update authority info for new metadata"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Master record metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "mintNewEditionFromMasterEditionViaTokenArgs",
              "type": {
                "defined": "MintNewEditionFromMasterEditionViaTokenArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 11
          }
        },
        {
          "name": "ConvertMasterEditionV1ToV2",
          "accounts": [
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Master Record Edition V1 (pda of ['metadata', program id, master metadata mint id, 'edition'])"
            },
            {
              "name": "oneTimeAuth",
              "isMut": true,
              "isSigner": false,
              "desc": "One time authorization mint"
            },
            {
              "name": "printingMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Printing mint"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 12
          }
        },
        {
          "name": "MintNewEditionFromMasterEditionViaVaultProxy",
          "accounts": [
            {
              "name": "newMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "New Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "newEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "New Edition (pda of ['metadata', program id, mint id, 'edition'])"
            },
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Master Record Edition V2 (pda of ['metadata', program id, master metadata mint id, 'edition']"
            },
            {
              "name": "newMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of new token - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "editionMarkPda",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition pda to mark creation - will be checked for pre-existence. (pda of ['metadata', program id, master metadata mint id, 'edition', edition_number]) where edition_number is NOT the edition number you pass in args but actually edition_number = floor(edition/EDITION_MARKER_BIT_SIZE)."
            },
            {
              "name": "newMintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority of new mint"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "vaultAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Vault authority"
            },
            {
              "name": "safetyDepositStore",
              "isMut": false,
              "isSigner": false,
              "desc": "Safety deposit token store account"
            },
            {
              "name": "safetyDepositBox",
              "isMut": false,
              "isSigner": false,
              "desc": "Safety deposit box"
            },
            {
              "name": "vault",
              "isMut": false,
              "isSigner": false,
              "desc": "Vault"
            },
            {
              "name": "newMetadataUpdateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "Update authority info for new metadata"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Master record metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "tokenVaultProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token vault program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "mintNewEditionFromMasterEditionViaTokenArgs",
              "type": {
                "defined": "MintNewEditionFromMasterEditionViaTokenArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 13
          }
        },
        {
          "name": "PuffMetadata",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 14
          }
        },
        {
          "name": "UpdateMetadataAccountV2",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority key"
            }
          ],
          "args": [
            {
              "name": "updateMetadataAccountArgsV2",
              "type": {
                "defined": "UpdateMetadataAccountArgsV2"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 15
          }
        },
        {
          "name": "CreateMetadataAccountV2",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "update authority info"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "createMetadataAccountArgsV2",
              "type": {
                "defined": "CreateMetadataAccountArgsV2"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 16
          }
        },
        {
          "name": "CreateMasterEditionV3",
          "accounts": [
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Unallocated edition V2 account with address as pda of ['metadata', program id, mint, 'edition']"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata mint"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority on the metadata's mint - THIS WILL TRANSFER AUTHORITY AWAY FROM THIS KEY"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "createMasterEditionArgs",
              "type": {
                "defined": "CreateMasterEditionArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 17
          }
        },
        {
          "name": "VerifyCollection",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 18
          }
        },
        {
          "name": "Utilize",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "tokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token Account Of NFT"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of the Metadata"
            },
            {
              "name": "useAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "A Use Authority / Can be the current Owner of the NFT"
            },
            {
              "name": "owner",
              "isMut": false,
              "isSigner": false,
              "desc": "Owner"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "ataProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Associated Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info"
            },
            {
              "name": "useAuthorityRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Use Authority Record PDA If present the program Assumes a delegated use authority",
              "optional": true
            },
            {
              "name": "burner",
              "isMut": false,
              "isSigner": false,
              "desc": "Program As Signer (Burner)",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "utilizeArgs",
              "type": {
                "defined": "UtilizeArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 19
          }
        },
        {
          "name": "ApproveUseAuthority",
          "accounts": [
            {
              "name": "useAuthorityRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Use Authority Record PDA"
            },
            {
              "name": "owner",
              "isMut": true,
              "isSigner": true,
              "desc": "Owner"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "user",
              "isMut": false,
              "isSigner": false,
              "desc": "A Use Authority"
            },
            {
              "name": "ownerTokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Owned Token Account Of Mint"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of Metadata"
            },
            {
              "name": "burner",
              "isMut": false,
              "isSigner": false,
              "desc": "Program As Signer (Burner)"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "approveUseAuthorityArgs",
              "type": {
                "defined": "ApproveUseAuthorityArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 20
          }
        },
        {
          "name": "RevokeUseAuthority",
          "accounts": [
            {
              "name": "useAuthorityRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Use Authority Record PDA"
            },
            {
              "name": "owner",
              "isMut": true,
              "isSigner": true,
              "desc": "Owner"
            },
            {
              "name": "user",
              "isMut": false,
              "isSigner": false,
              "desc": "A Use Authority"
            },
            {
              "name": "ownerTokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Owned Token Account Of Mint"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of Metadata"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 21
          }
        },
        {
          "name": "UnverifyCollection",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Authority"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 22
          }
        },
        {
          "name": "ApproveCollectionAuthority",
          "accounts": [
            {
              "name": "collectionAuthorityRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Collection Authority Record PDA"
            },
            {
              "name": "newCollectionAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "A Collection Authority"
            },
            {
              "name": "updateAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Update Authority of Collection NFT"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Metadata account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of Collection Metadata"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 23
          }
        },
        {
          "name": "RevokeCollectionAuthority",
          "accounts": [
            {
              "name": "collectionAuthorityRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Collection Authority Record PDA"
            },
            {
              "name": "delegateAuthority",
              "isMut": true,
              "isSigner": false,
              "desc": "Delegated Collection Authority"
            },
            {
              "name": "revokeAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Update Authority, or Delegated Authority, of Collection NFT"
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of Metadata"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 24
          }
        },
        {
          "name": "SetAndVerifyCollection",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "Update Authority of Collection NFT and NFT"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 25
          }
        },
        {
          "name": "FreezeDelegatedAccount",
          "accounts": [
            {
              "name": "delegate",
              "isMut": true,
              "isSigner": true,
              "desc": "Delegate"
            },
            {
              "name": "tokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account to freeze"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Token mint"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Program"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 26
          }
        },
        {
          "name": "ThawDelegatedAccount",
          "accounts": [
            {
              "name": "delegate",
              "isMut": true,
              "isSigner": true,
              "desc": "Delegate"
            },
            {
              "name": "tokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account to thaw"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Token mint"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Program"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 27
          }
        },
        {
          "name": "RemoveCreatorVerification",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "creator",
              "isMut": false,
              "isSigner": true,
              "desc": "Creator"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 28
          }
        },
        {
          "name": "BurnNft",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "owner",
              "isMut": true,
              "isSigner": true,
              "desc": "NFT owner"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of the NFT"
            },
            {
              "name": "tokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account to close"
            },
            {
              "name": "masterEditionAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "MasterEdition2 of the NFT"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program"
            },
            {
              "name": "collectionMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata of the Collection",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 29
          }
        },
        {
          "name": "VerifySizedCollectionItem",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 30
          }
        },
        {
          "name": "UnverifySizedCollectionItem",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Collection Authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 31
          }
        },
        {
          "name": "SetAndVerifySizedCollectionItem",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "Update Authority of Collection NFT and NFT"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collection",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata Account of the Collection"
            },
            {
              "name": "collectionMasterEditionAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "MasterEdition2 Account of the Collection Token"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 32
          }
        },
        {
          "name": "CreateMetadataAccountV3",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata key (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "mintAuthority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "update authority info"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false,
              "desc": "Rent info",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "createMetadataAccountArgsV3",
              "type": {
                "defined": "CreateMetadataAccountArgsV3"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 33
          }
        },
        {
          "name": "SetCollectionSize",
          "accounts": [
            {
              "name": "collectionMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Collection Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "setCollectionSizeArgs",
              "type": {
                "defined": "SetCollectionSizeArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 34
          }
        },
        {
          "name": "SetTokenStandard",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "updateAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Metadata update authority"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition account",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 35
          }
        },
        {
          "name": "BubblegumSetCollectionSize",
          "accounts": [
            {
              "name": "collectionMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Collection Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the Collection"
            },
            {
              "name": "bubblegumSigner",
              "isMut": false,
              "isSigner": true,
              "desc": "Signing PDA of Bubblegum program"
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection Authority Record PDA",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "setCollectionSizeArgs",
              "type": {
                "defined": "SetCollectionSizeArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 36
          }
        },
        {
          "name": "BurnEditionNft",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "owner",
              "isMut": true,
              "isSigner": true,
              "desc": "NFT owner"
            },
            {
              "name": "printEditionMint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of the print edition NFT"
            },
            {
              "name": "masterEditionMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of the original/master NFT"
            },
            {
              "name": "printEditionTokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account the print edition NFT is in"
            },
            {
              "name": "masterEditionTokenAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account the Master Edition NFT is in"
            },
            {
              "name": "masterEditionAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "MasterEdition2 of the original NFT"
            },
            {
              "name": "printEditionAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Print Edition account of the NFT"
            },
            {
              "name": "editionMarkerAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition Marker PDA of the NFT"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 37
          }
        },
        {
          "name": "CreateEscrowAccount",
          "accounts": [
            {
              "name": "escrow",
              "isMut": true,
              "isSigner": false,
              "desc": "Escrow account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "tokenAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account of the token"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition account"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Wallet paying for the transaction and new account"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Authority/creator of the escrow account",
              "optional": true
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 38
          }
        },
        {
          "name": "CloseEscrowAccount",
          "accounts": [
            {
              "name": "escrow",
              "isMut": true,
              "isSigner": false,
              "desc": "Escrow account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "tokenAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition account"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Wallet paying for the transaction and new account"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            }
          ],
          "args": [],
          "discriminant": {
            "type": "u8",
            "value": 39
          }
        },
        {
          "name": "TransferOutOfEscrow",
          "accounts": [
            {
              "name": "escrow",
              "isMut": false,
              "isSigner": false,
              "desc": "Escrow account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Wallet paying for the transaction and new account"
            },
            {
              "name": "attributeMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account for the new attribute"
            },
            {
              "name": "attributeSrc",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account source for the new attribute"
            },
            {
              "name": "attributeDst",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account, owned by TM, destination for the new attribute"
            },
            {
              "name": "escrowMint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account that the escrow is attached"
            },
            {
              "name": "escrowAccount",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account that holds the token the escrow is attached to"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "ataProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Associated Token program"
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Authority/creator of the escrow account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "transferOutOfEscrowArgs",
              "type": {
                "defined": "TransferOutOfEscrowArgs"
              }
            }
          ],
          "discriminant": {
            "type": "u8",
            "value": 40
          }
        },
        {
          "name": "Burn",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "owner",
              "isMut": true,
              "isSigner": true,
              "desc": "Asset owner"
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "tokenAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account to close"
            },
            {
              "name": "masterEditionAccount",
              "isMut": true,
              "isSigner": false,
              "desc": "MasterEdition of the asset"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program"
            },
            {
              "name": "collectionMetadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata of the Collection",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "burnArgs",
              "type": {
                "defined": "BurnArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 41
          }
        },
        {
          "name": "Create",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Unallocated metadata account with address as pda of ['metadata', program id, mint id]"
            },
            {
              "name": "masterEdition",
              "isMut": true,
              "isSigner": false,
              "desc": "Unallocated edition account with address as pda of ['metadata', program id, mint, 'edition']",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Mint authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "updateAuthority",
              "isMut": false,
              "isSigner": false,
              "desc": "Update authority for the metadata account"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token program"
            }
          ],
          "args": [
            {
              "name": "createArgs",
              "type": {
                "defined": "CreateArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 42
          }
        },
        {
          "name": "Mint",
          "accounts": [
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token or Associated Token account"
            },
            {
              "name": "tokenOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Owner of the token account",
              "optional": true
            },
            {
              "name": "metadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata account (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": "Master Edition account",
              "optional": true
            },
            {
              "name": "tokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": true,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "(Mint or Update) authority"
            },
            {
              "name": "delegateRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Metadata delegate record",
              "optional": true
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token program"
            },
            {
              "name": "splAtaProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Associated Token Account program"
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "mintArgs",
              "type": {
                "defined": "MintArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 43
          }
        },
        {
          "name": "Delegate",
          "accounts": [
            {
              "name": "delegateRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Delegate record account",
              "optional": true
            },
            {
              "name": "delegate",
              "isMut": false,
              "isSigner": false,
              "desc": "Owner of the delegated account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": "Master Edition account",
              "optional": true
            },
            {
              "name": "tokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of metadata"
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account of mint",
              "optional": true
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority or token owner"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System Program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "delegateArgs",
              "type": {
                "defined": "DelegateArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 44
          }
        },
        {
          "name": "Revoke",
          "accounts": [
            {
              "name": "delegateRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Delegate record account",
              "optional": true
            },
            {
              "name": "delegate",
              "isMut": false,
              "isSigner": false,
              "desc": "Owner of the delegated account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "masterEdition",
              "isMut": false,
              "isSigner": false,
              "desc": "Master Edition account",
              "optional": true
            },
            {
              "name": "tokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of metadata"
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account of mint",
              "optional": true
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority or token owner"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System Program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "revokeArgs",
              "type": {
                "defined": "RevokeArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 45
          }
        },
        {
          "name": "Lock",
          "accounts": [
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Delegate account"
            },
            {
              "name": "tokenOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Token owner account",
              "optional": true
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition account",
              "optional": true
            },
            {
              "name": "tokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "lockArgs",
              "type": {
                "defined": "LockArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 46
          }
        },
        {
          "name": "Unlock",
          "accounts": [
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Delegate account"
            },
            {
              "name": "tokenOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Token owner account",
              "optional": true
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition account",
              "optional": true
            },
            {
              "name": "tokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "unlockArgs",
              "type": {
                "defined": "UnlockArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 47
          }
        },
        {
          "name": "Migrate",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition account"
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account"
            },
            {
              "name": "tokenOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account owner"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority"
            },
            {
              "name": "collectionMetadata",
              "isMut": false,
              "isSigner": false,
              "desc": "Collection metadata account"
            },
            {
              "name": "delegateRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Delegate record account"
            },
            {
              "name": "tokenRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Token record account"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instruction sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Program"
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "migrateArgs",
              "type": {
                "defined": "MigrateArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 48
          }
        },
        {
          "name": "Transfer",
          "accounts": [
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account"
            },
            {
              "name": "tokenOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account owner"
            },
            {
              "name": "destination",
              "isMut": true,
              "isSigner": false,
              "desc": "Destination token account"
            },
            {
              "name": "destinationOwner",
              "isMut": false,
              "isSigner": false,
              "desc": "Destination token account owner"
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint of token asset"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata (pda of ['metadata', program id, mint id])"
            },
            {
              "name": "edition",
              "isMut": false,
              "isSigner": false,
              "desc": "Edition of token asset",
              "optional": true
            },
            {
              "name": "ownerTokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "destinationTokenRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Token record account",
              "optional": true
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Transfer authority (token or delegate owner)"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System Program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "Instructions sysvar account"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program"
            },
            {
              "name": "splAtaProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Associated Token Account program"
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "transferArgs",
              "type": {
                "defined": "TransferArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 49
          }
        },
        {
          "name": "Update",
          "accounts": [
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Update authority or delegate"
            },
            {
              "name": "delegateRecord",
              "isMut": false,
              "isSigner": false,
              "desc": "Delegate record PDA",
              "optional": true
            },
            {
              "name": "token",
              "isMut": false,
              "isSigner": false,
              "desc": "Token account",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition account",
              "optional": true
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "updateArgs",
              "type": {
                "defined": "UpdateArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 50
          }
        },
        {
          "name": "Use",
          "accounts": [
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true,
              "desc": "Token owner or delegate"
            },
            {
              "name": "delegateRecord",
              "isMut": true,
              "isSigner": false,
              "desc": "Delegate record PDA",
              "optional": true
            },
            {
              "name": "token",
              "isMut": true,
              "isSigner": false,
              "desc": "Token account",
              "optional": true
            },
            {
              "name": "mint",
              "isMut": false,
              "isSigner": false,
              "desc": "Mint account"
            },
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "edition",
              "isMut": true,
              "isSigner": false,
              "desc": "Edition account",
              "optional": true
            },
            {
              "name": "payer",
              "isMut": false,
              "isSigner": true,
              "desc": "Payer"
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "sysvarInstructions",
              "isMut": false,
              "isSigner": false,
              "desc": "System program"
            },
            {
              "name": "splTokenProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "SPL Token Program",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "useArgs",
              "type": {
                "defined": "UseArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 51
          }
        },
        {
          "name": "Verify",
          "accounts": [
            {
              "name": "metadata",
              "isMut": true,
              "isSigner": false,
              "desc": "Metadata account"
            },
            {
              "name": "collectionAuthority",
              "isMut": true,
              "isSigner": true,
              "desc": "Collection Update authority"
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true,
              "desc": "payer"
            },
            {
              "name": "authorizationRules",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules account",
              "optional": true
            },
            {
              "name": "authorizationRulesProgram",
              "isMut": false,
              "isSigner": false,
              "desc": "Token Authorization Rules Program",
              "optional": true
            }
          ],
          "args": [
            {
              "name": "verifyArgs",
              "type": {
                "defined": "VerifyArgs"
              }
            }
          ],
          "defaultOptionalAccounts": true,
          "discriminant": {
            "type": "u8",
            "value": 52
          }
        }
      ],
      "accounts": [
        {
          "name": "CollectionAuthorityRecord",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "bump",
                "type": "u8"
              },
              {
                "name": "updateAuthority",
                "type": {
                  "option": "publicKey"
                }
              }
            ]
          }
        },
        {
          "name": "MetadataDelegateRecord",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "bump",
                "type": "u8"
              },
              {
                "name": "mint",
                "type": "publicKey"
              },
              {
                "name": "delegate",
                "type": "publicKey"
              },
              {
                "name": "updateAuthority",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "Edition",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "parent",
                "type": "publicKey"
              },
              {
                "name": "edition",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "EditionMarker",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "ledger",
                "type": {
                  "array": [
                    "u8",
                    31
                  ]
                }
              }
            ]
          }
        },
        {
          "name": "TokenOwnedEscrow",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "baseToken",
                "type": "publicKey"
              },
              {
                "name": "authority",
                "type": {
                  "defined": "EscrowAuthority"
                }
              },
              {
                "name": "bump",
                "type": "u8"
              }
            ]
          }
        },
        {
          "name": "MasterEditionV2",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "supply",
                "type": "u64"
              },
              {
                "name": "maxSupply",
                "type": {
                  "option": "u64"
                }
              }
            ]
          }
        },
        {
          "name": "MasterEditionV1",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "supply",
                "type": "u64"
              },
              {
                "name": "maxSupply",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "printingMint",
                "type": "publicKey"
              },
              {
                "name": "oneTimePrintingAuthorizationMint",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "Metadata",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "updateAuthority",
                "type": "publicKey"
              },
              {
                "name": "mint",
                "type": "publicKey"
              },
              {
                "name": "data",
                "type": {
                  "defined": "Data"
                }
              },
              {
                "name": "primarySaleHappened",
                "type": "bool"
              },
              {
                "name": "isMutable",
                "type": "bool"
              },
              {
                "name": "editionNonce",
                "type": {
                  "option": "u8"
                }
              },
              {
                "name": "tokenStandard",
                "type": {
                  "option": {
                    "defined": "TokenStandard"
                  }
                }
              },
              {
                "name": "collection",
                "type": {
                  "option": {
                    "defined": "Collection"
                  }
                }
              },
              {
                "name": "uses",
                "type": {
                  "option": {
                    "defined": "Uses"
                  }
                }
              },
              {
                "name": "collectionDetails",
                "type": {
                  "option": {
                    "defined": "CollectionDetails"
                  }
                }
              },
              {
                "name": "programmableConfig",
                "type": {
                  "option": {
                    "defined": "ProgrammableConfig"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "TokenRecord",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "bump",
                "type": "u8"
              },
              {
                "name": "state",
                "type": {
                  "defined": "TokenState"
                }
              },
              {
                "name": "ruleSetRevision",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "delegate",
                "type": {
                  "option": "publicKey"
                }
              },
              {
                "name": "delegateRole",
                "type": {
                  "option": {
                    "defined": "TokenDelegateRole"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "ReservationListV2",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "masterEdition",
                "type": "publicKey"
              },
              {
                "name": "supplySnapshot",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "reservations",
                "type": {
                  "vec": {
                    "defined": "Reservation"
                  }
                }
              },
              {
                "name": "totalReservationSpots",
                "type": "u64"
              },
              {
                "name": "currentReservationSpots",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "ReservationListV1",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "masterEdition",
                "type": "publicKey"
              },
              {
                "name": "supplySnapshot",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "reservations",
                "type": {
                  "vec": {
                    "defined": "ReservationV1"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "UseAuthorityRecord",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "key",
                "type": {
                  "defined": "Key"
                }
              },
              {
                "name": "allowedUses",
                "type": "u64"
              },
              {
                "name": "bump",
                "type": "u8"
              }
            ]
          }
        }
      ],
      "types": [
        {
          "name": "SetCollectionSizeArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "size",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "CreateMetadataAccountArgsV2",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "data",
                "type": {
                  "defined": "DataV2"
                }
              },
              {
                "name": "isMutable",
                "type": "bool"
              }
            ]
          }
        },
        {
          "name": "CreateMetadataAccountArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "data",
                "type": {
                  "defined": "Data"
                }
              },
              {
                "name": "isMutable",
                "type": "bool"
              }
            ]
          }
        },
        {
          "name": "UpdateMetadataAccountArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "data",
                "type": {
                  "option": {
                    "defined": "Data"
                  }
                }
              },
              {
                "name": "updateAuthority",
                "type": {
                  "option": "publicKey"
                }
              },
              {
                "name": "primarySaleHappened",
                "type": {
                  "option": "bool"
                }
              }
            ]
          }
        },
        {
          "name": "MintPrintingTokensViaTokenArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "supply",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "SetReservationListArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "reservations",
                "type": {
                  "vec": {
                    "defined": "Reservation"
                  }
                }
              },
              {
                "name": "totalReservationSpots",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "offset",
                "type": "u64"
              },
              {
                "name": "totalSpotOffset",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "CreateMasterEditionArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "maxSupply",
                "type": {
                  "option": "u64"
                }
              }
            ]
          }
        },
        {
          "name": "MintNewEditionFromMasterEditionViaTokenArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "edition",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "TransferOutOfEscrowArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "CreateMetadataAccountArgsV3",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "data",
                "type": {
                  "defined": "DataV2"
                }
              },
              {
                "name": "isMutable",
                "type": "bool"
              },
              {
                "name": "collectionDetails",
                "type": {
                  "option": {
                    "defined": "CollectionDetails"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "UpdateMetadataAccountArgsV2",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "data",
                "type": {
                  "option": {
                    "defined": "DataV2"
                  }
                }
              },
              {
                "name": "updateAuthority",
                "type": {
                  "option": "publicKey"
                }
              },
              {
                "name": "primarySaleHappened",
                "type": {
                  "option": "bool"
                }
              },
              {
                "name": "isMutable",
                "type": {
                  "option": "bool"
                }
              }
            ]
          }
        },
        {
          "name": "ApproveUseAuthorityArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "numberOfUses",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "UtilizeArgs",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "numberOfUses",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "AuthorizationData",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "payload",
                "type": {
                  "defined": "Payload"
                }
              }
            ]
          }
        },
        {
          "name": "AssetData",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "updateAuthority",
                "type": "publicKey"
              },
              {
                "name": "name",
                "type": "string"
              },
              {
                "name": "symbol",
                "type": "string"
              },
              {
                "name": "uri",
                "type": "string"
              },
              {
                "name": "sellerFeeBasisPoints",
                "type": "u16"
              },
              {
                "name": "creators",
                "type": {
                  "option": {
                    "vec": {
                      "defined": "Creator"
                    }
                  }
                }
              },
              {
                "name": "primarySaleHappened",
                "type": "bool"
              },
              {
                "name": "isMutable",
                "type": "bool"
              },
              {
                "name": "tokenStandard",
                "type": {
                  "defined": "TokenStandard"
                }
              },
              {
                "name": "collection",
                "type": {
                  "option": {
                    "defined": "Collection"
                  }
                }
              },
              {
                "name": "uses",
                "type": {
                  "option": {
                    "defined": "Uses"
                  }
                }
              },
              {
                "name": "collectionDetails",
                "type": {
                  "option": {
                    "defined": "CollectionDetails"
                  }
                }
              },
              {
                "name": "ruleSet",
                "type": {
                  "option": "publicKey"
                }
              }
            ]
          }
        },
        {
          "name": "Collection",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "verified",
                "type": "bool"
              },
              {
                "name": "key",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "Creator",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "address",
                "type": "publicKey"
              },
              {
                "name": "verified",
                "type": "bool"
              },
              {
                "name": "share",
                "type": "u8"
              }
            ]
          }
        },
        {
          "name": "Data",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "name",
                "type": "string"
              },
              {
                "name": "symbol",
                "type": "string"
              },
              {
                "name": "uri",
                "type": "string"
              },
              {
                "name": "sellerFeeBasisPoints",
                "type": "u16"
              },
              {
                "name": "creators",
                "type": {
                  "option": {
                    "vec": {
                      "defined": "Creator"
                    }
                  }
                }
              }
            ]
          }
        },
        {
          "name": "DataV2",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "name",
                "type": "string"
              },
              {
                "name": "symbol",
                "type": "string"
              },
              {
                "name": "uri",
                "type": "string"
              },
              {
                "name": "sellerFeeBasisPoints",
                "type": "u16"
              },
              {
                "name": "creators",
                "type": {
                  "option": {
                    "vec": {
                      "defined": "Creator"
                    }
                  }
                }
              },
              {
                "name": "collection",
                "type": {
                  "option": {
                    "defined": "Collection"
                  }
                }
              },
              {
                "name": "uses",
                "type": {
                  "option": {
                    "defined": "Uses"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "Reservation",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "address",
                "type": "publicKey"
              },
              {
                "name": "spotsRemaining",
                "type": "u64"
              },
              {
                "name": "totalSpots",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "ReservationV1",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "address",
                "type": "publicKey"
              },
              {
                "name": "spotsRemaining",
                "type": "u8"
              },
              {
                "name": "totalSpots",
                "type": "u8"
              }
            ]
          }
        },
        {
          "name": "SeedsVec",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "seeds",
                "type": {
                  "vec": "bytes"
                }
              }
            ]
          }
        },
        {
          "name": "LeafInfo",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "leaf",
                "type": {
                  "array": [
                    "u8",
                    32
                  ]
                }
              },
              {
                "name": "proof",
                "type": {
                  "vec": {
                    "array": [
                      "u8",
                      32
                    ]
                  }
                }
              }
            ]
          }
        },
        {
          "name": "Payload",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "map",
                "type": {
                  "hashMap": [
                    {
                      "defined": "PayloadKey"
                    },
                    {
                      "defined": "PayloadType"
                    }
                  ]
                }
              }
            ]
          }
        },
        {
          "name": "Uses",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "useMethod",
                "type": {
                  "defined": "UseMethod"
                }
              },
              {
                "name": "remaining",
                "type": "u64"
              },
              {
                "name": "total",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "BurnArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "VerifyArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "DelegateArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "CollectionV1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "SaleV1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "TransferV1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "UpdateV1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "UtilityV1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "StakingV1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              },
              {
                "name": "StandardV1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "RevokeArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "CollectionV1"
              },
              {
                "name": "SaleV1"
              },
              {
                "name": "TransferV1"
              },
              {
                "name": "UpdateV1"
              },
              {
                "name": "UtilityV1"
              },
              {
                "name": "StakingV1"
              },
              {
                "name": "StandardV1"
              }
            ]
          }
        },
        {
          "name": "MetadataDelegateRole",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Authority"
              },
              {
                "name": "Collection"
              },
              {
                "name": "Use"
              },
              {
                "name": "Update"
              }
            ]
          }
        },
        {
          "name": "CreateArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "asset_data",
                    "type": {
                      "defined": "AssetData"
                    }
                  },
                  {
                    "name": "decimals",
                    "type": {
                      "option": "u8"
                    }
                  },
                  {
                    "name": "print_supply",
                    "type": {
                      "option": {
                        "defined": "PrintSupply"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "MintArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "TransferArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "amount",
                    "type": "u64"
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "UpdateArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "new_update_authority",
                    "type": {
                      "option": "publicKey"
                    }
                  },
                  {
                    "name": "data",
                    "type": {
                      "option": {
                        "defined": "Data"
                      }
                    }
                  },
                  {
                    "name": "primary_sale_happened",
                    "type": {
                      "option": "bool"
                    }
                  },
                  {
                    "name": "is_mutable",
                    "type": {
                      "option": "bool"
                    }
                  },
                  {
                    "name": "collection",
                    "type": {
                      "defined": "CollectionToggle"
                    }
                  },
                  {
                    "name": "collection_details",
                    "type": {
                      "defined": "CollectionDetailsToggle"
                    }
                  },
                  {
                    "name": "uses",
                    "type": {
                      "defined": "UsesToggle"
                    }
                  },
                  {
                    "name": "rule_set",
                    "type": {
                      "defined": "RuleSetToggle"
                    }
                  },
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "CollectionToggle",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "None"
              },
              {
                "name": "Clear"
              },
              {
                "name": "Set",
                "fields": [
                  {
                    "defined": "Collection"
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "UsesToggle",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "None"
              },
              {
                "name": "Clear"
              },
              {
                "name": "Set",
                "fields": [
                  {
                    "defined": "Uses"
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "CollectionDetailsToggle",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "None"
              },
              {
                "name": "Clear"
              },
              {
                "name": "Set",
                "fields": [
                  {
                    "defined": "CollectionDetails"
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "RuleSetToggle",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "None"
              },
              {
                "name": "Clear"
              },
              {
                "name": "Set",
                "fields": [
                  "publicKey"
                ]
              }
            ]
          }
        },
        {
          "name": "MigrateArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "migration_type",
                    "type": {
                      "defined": "MigrationType"
                    }
                  },
                  {
                    "name": "rule_set",
                    "type": {
                      "option": "publicKey"
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "LockArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "UnlockArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "UseArgs",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "authorization_data",
                    "type": {
                      "option": {
                        "defined": "AuthorizationData"
                      }
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "TokenStandard",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "NonFungible"
              },
              {
                "name": "FungibleAsset"
              },
              {
                "name": "Fungible"
              },
              {
                "name": "NonFungibleEdition"
              },
              {
                "name": "ProgrammableNonFungible"
              }
            ]
          }
        },
        {
          "name": "Key",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Uninitialized"
              },
              {
                "name": "EditionV1"
              },
              {
                "name": "MasterEditionV1"
              },
              {
                "name": "ReservationListV1"
              },
              {
                "name": "MetadataV1"
              },
              {
                "name": "ReservationListV2"
              },
              {
                "name": "MasterEditionV2"
              },
              {
                "name": "EditionMarker"
              },
              {
                "name": "UseAuthorityRecord"
              },
              {
                "name": "CollectionAuthorityRecord"
              },
              {
                "name": "TokenOwnedEscrow"
              },
              {
                "name": "TokenRecord"
              },
              {
                "name": "MetadataDelegate"
              }
            ]
          }
        },
        {
          "name": "CollectionDetails",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "size",
                    "type": "u64"
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "EscrowAuthority",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "TokenOwner"
              },
              {
                "name": "Creator",
                "fields": [
                  "publicKey"
                ]
              }
            ]
          }
        },
        {
          "name": "PrintSupply",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Zero"
              },
              {
                "name": "Limited",
                "fields": [
                  "u64"
                ]
              },
              {
                "name": "Unlimited"
              }
            ]
          }
        },
        {
          "name": "ProgrammableConfig",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "V1",
                "fields": [
                  {
                    "name": "rule_set",
                    "type": {
                      "option": "publicKey"
                    }
                  }
                ]
              }
            ]
          }
        },
        {
          "name": "MigrationType",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "CollectionV1"
              },
              {
                "name": "ProgrammableV1"
              }
            ]
          }
        },
        {
          "name": "TokenState",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Unlocked"
              },
              {
                "name": "Locked"
              },
              {
                "name": "Listed"
              }
            ]
          }
        },
        {
          "name": "TokenDelegateRole",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Sale"
              },
              {
                "name": "Transfer"
              },
              {
                "name": "Utility"
              },
              {
                "name": "Staking"
              },
              {
                "name": "Standard"
              },
              {
                "name": "Migration"
              }
            ]
          }
        },
        {
          "name": "AuthorityType",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "None"
              },
              {
                "name": "Metadata"
              },
              {
                "name": "Delegate"
              },
              {
                "name": "Holder"
              }
            ]
          }
        },
        {
          "name": "PayloadType",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Pubkey",
                "fields": [
                  "publicKey"
                ]
              },
              {
                "name": "Seeds",
                "fields": [
                  {
                    "defined": "SeedsVec"
                  }
                ]
              },
              {
                "name": "MerkleProof",
                "fields": [
                  {
                    "defined": "LeafInfo"
                  }
                ]
              },
              {
                "name": "Number",
                "fields": [
                  "u64"
                ]
              }
            ]
          }
        },
        {
          "name": "PayloadKey",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Target"
              },
              {
                "name": "Holder"
              },
              {
                "name": "Authority"
              },
              {
                "name": "Amount"
              }
            ]
          }
        },
        {
          "name": "UseMethod",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Burn"
              },
              {
                "name": "Multiple"
              },
              {
                "name": "Single"
              }
            ]
          }
        }
      ],
      "errors": [
        {
          "code": 0,
          "name": "InstructionUnpackError",
          "msg": "Failed to unpack instruction data"
        },
        {
          "code": 1,
          "name": "InstructionPackError",
          "msg": "Failed to pack instruction data"
        },
        {
          "code": 2,
          "name": "NotRentExempt",
          "msg": "Lamport balance below rent-exempt threshold"
        },
        {
          "code": 3,
          "name": "AlreadyInitialized",
          "msg": "Already initialized"
        },
        {
          "code": 4,
          "name": "Uninitialized",
          "msg": "Uninitialized"
        },
        {
          "code": 5,
          "name": "InvalidMetadataKey",
          "msg": " Metadata's key must match seed of ['metadata', program id, mint] provided"
        },
        {
          "code": 6,
          "name": "InvalidEditionKey",
          "msg": "Edition's key must match seed of ['metadata', program id, name, 'edition'] provided"
        },
        {
          "code": 7,
          "name": "UpdateAuthorityIncorrect",
          "msg": "Update Authority given does not match"
        },
        {
          "code": 8,
          "name": "UpdateAuthorityIsNotSigner",
          "msg": "Update Authority needs to be signer to update metadata"
        },
        {
          "code": 9,
          "name": "NotMintAuthority",
          "msg": "You must be the mint authority and signer on this transaction"
        },
        {
          "code": 10,
          "name": "InvalidMintAuthority",
          "msg": "Mint authority provided does not match the authority on the mint"
        },
        {
          "code": 11,
          "name": "NameTooLong",
          "msg": "Name too long"
        },
        {
          "code": 12,
          "name": "SymbolTooLong",
          "msg": "Symbol too long"
        },
        {
          "code": 13,
          "name": "UriTooLong",
          "msg": "URI too long"
        },
        {
          "code": 14,
          "name": "UpdateAuthorityMustBeEqualToMetadataAuthorityAndSigner",
          "msg": "Update authority must be equivalent to the metadata's authority and also signer of this transaction"
        },
        {
          "code": 15,
          "name": "MintMismatch",
          "msg": "Mint given does not match mint on Metadata"
        },
        {
          "code": 16,
          "name": "EditionsMustHaveExactlyOneToken",
          "msg": "Editions must have exactly one token"
        },
        {
          "code": 17,
          "name": "MaxEditionsMintedAlready",
          "msg": "Maximum editions printed already"
        },
        {
          "code": 18,
          "name": "TokenMintToFailed",
          "msg": "Token mint to failed"
        },
        {
          "code": 19,
          "name": "MasterRecordMismatch",
          "msg": "The master edition record passed must match the master record on the edition given"
        },
        {
          "code": 20,
          "name": "DestinationMintMismatch",
          "msg": "The destination account does not have the right mint"
        },
        {
          "code": 21,
          "name": "EditionAlreadyMinted",
          "msg": "An edition can only mint one of its kind!"
        },
        {
          "code": 22,
          "name": "PrintingMintDecimalsShouldBeZero",
          "msg": "Printing mint decimals should be zero"
        },
        {
          "code": 23,
          "name": "OneTimePrintingAuthorizationMintDecimalsShouldBeZero",
          "msg": "OneTimePrintingAuthorization mint decimals should be zero"
        },
        {
          "code": 24,
          "name": "EditionMintDecimalsShouldBeZero",
          "msg": "EditionMintDecimalsShouldBeZero"
        },
        {
          "code": 25,
          "name": "TokenBurnFailed",
          "msg": "Token burn failed"
        },
        {
          "code": 26,
          "name": "TokenAccountOneTimeAuthMintMismatch",
          "msg": "The One Time authorization mint does not match that on the token account!"
        },
        {
          "code": 27,
          "name": "DerivedKeyInvalid",
          "msg": "Derived key invalid"
        },
        {
          "code": 28,
          "name": "PrintingMintMismatch",
          "msg": "The Printing mint does not match that on the master edition!"
        },
        {
          "code": 29,
          "name": "OneTimePrintingAuthMintMismatch",
          "msg": "The One Time Printing Auth mint does not match that on the master edition!"
        },
        {
          "code": 30,
          "name": "TokenAccountMintMismatch",
          "msg": "The mint of the token account does not match the Printing mint!"
        },
        {
          "code": 31,
          "name": "TokenAccountMintMismatchV2",
          "msg": "The mint of the token account does not match the master metadata mint!"
        },
        {
          "code": 32,
          "name": "NotEnoughTokens",
          "msg": "Not enough tokens to mint a limited edition"
        },
        {
          "code": 33,
          "name": "PrintingMintAuthorizationAccountMismatch",
          "msg": "The mint on your authorization token holding account does not match your Printing mint!"
        },
        {
          "code": 34,
          "name": "AuthorizationTokenAccountOwnerMismatch",
          "msg": "The authorization token account has a different owner than the update authority for the master edition!"
        },
        {
          "code": 35,
          "name": "Disabled",
          "msg": "This feature is currently disabled."
        },
        {
          "code": 36,
          "name": "CreatorsTooLong",
          "msg": "Creators list too long"
        },
        {
          "code": 37,
          "name": "CreatorsMustBeAtleastOne",
          "msg": "Creators must be at least one if set"
        },
        {
          "code": 38,
          "name": "MustBeOneOfCreators",
          "msg": "If using a creators array, you must be one of the creators listed"
        },
        {
          "code": 39,
          "name": "NoCreatorsPresentOnMetadata",
          "msg": "This metadata does not have creators"
        },
        {
          "code": 40,
          "name": "CreatorNotFound",
          "msg": "This creator address was not found"
        },
        {
          "code": 41,
          "name": "InvalidBasisPoints",
          "msg": "Basis points cannot be more than 10000"
        },
        {
          "code": 42,
          "name": "PrimarySaleCanOnlyBeFlippedToTrue",
          "msg": "Primary sale can only be flipped to true and is immutable"
        },
        {
          "code": 43,
          "name": "OwnerMismatch",
          "msg": "Owner does not match that on the account given"
        },
        {
          "code": 44,
          "name": "NoBalanceInAccountForAuthorization",
          "msg": "This account has no tokens to be used for authorization"
        },
        {
          "code": 45,
          "name": "ShareTotalMustBe100",
          "msg": "Share total must equal 100 for creator array"
        },
        {
          "code": 46,
          "name": "ReservationExists",
          "msg": "This reservation list already exists!"
        },
        {
          "code": 47,
          "name": "ReservationDoesNotExist",
          "msg": "This reservation list does not exist!"
        },
        {
          "code": 48,
          "name": "ReservationNotSet",
          "msg": "This reservation list exists but was never set with reservations"
        },
        {
          "code": 49,
          "name": "ReservationAlreadyMade",
          "msg": "This reservation list has already been set!"
        },
        {
          "code": 50,
          "name": "BeyondMaxAddressSize",
          "msg": "Provided more addresses than max allowed in single reservation"
        },
        {
          "code": 51,
          "name": "NumericalOverflowError",
          "msg": "NumericalOverflowError"
        },
        {
          "code": 52,
          "name": "ReservationBreachesMaximumSupply",
          "msg": "This reservation would go beyond the maximum supply of the master edition!"
        },
        {
          "code": 53,
          "name": "AddressNotInReservation",
          "msg": "Address not in reservation!"
        },
        {
          "code": 54,
          "name": "CannotVerifyAnotherCreator",
          "msg": "You cannot unilaterally verify another creator, they must sign"
        },
        {
          "code": 55,
          "name": "CannotUnverifyAnotherCreator",
          "msg": "You cannot unilaterally unverify another creator"
        },
        {
          "code": 56,
          "name": "SpotMismatch",
          "msg": "In initial reservation setting, spots remaining should equal total spots"
        },
        {
          "code": 57,
          "name": "IncorrectOwner",
          "msg": "Incorrect account owner"
        },
        {
          "code": 58,
          "name": "PrintingWouldBreachMaximumSupply",
          "msg": "printing these tokens would breach the maximum supply limit of the master edition"
        },
        {
          "code": 59,
          "name": "DataIsImmutable",
          "msg": "Data is immutable"
        },
        {
          "code": 60,
          "name": "DuplicateCreatorAddress",
          "msg": "No duplicate creator addresses"
        },
        {
          "code": 61,
          "name": "ReservationSpotsRemainingShouldMatchTotalSpotsAtStart",
          "msg": "Reservation spots remaining should match total spots when first being created"
        },
        {
          "code": 62,
          "name": "InvalidTokenProgram",
          "msg": "Invalid token program"
        },
        {
          "code": 63,
          "name": "DataTypeMismatch",
          "msg": "Data type mismatch"
        },
        {
          "code": 64,
          "name": "BeyondAlottedAddressSize",
          "msg": "Beyond alotted address size in reservation!"
        },
        {
          "code": 65,
          "name": "ReservationNotComplete",
          "msg": "The reservation has only been partially alotted"
        },
        {
          "code": 66,
          "name": "TriedToReplaceAnExistingReservation",
          "msg": "You cannot splice over an existing reservation!"
        },
        {
          "code": 67,
          "name": "InvalidOperation",
          "msg": "Invalid operation"
        },
        {
          "code": 68,
          "name": "InvalidOwner",
          "msg": "Invalid Owner"
        },
        {
          "code": 69,
          "name": "PrintingMintSupplyMustBeZeroForConversion",
          "msg": "Printing mint supply must be zero for conversion"
        },
        {
          "code": 70,
          "name": "OneTimeAuthMintSupplyMustBeZeroForConversion",
          "msg": "One Time Auth mint supply must be zero for conversion"
        },
        {
          "code": 71,
          "name": "InvalidEditionIndex",
          "msg": "You tried to insert one edition too many into an edition mark pda"
        },
        {
          "code": 72,
          "name": "ReservationArrayShouldBeSizeOne",
          "msg": "In the legacy system the reservation needs to be of size one for cpu limit reasons"
        },
        {
          "code": 73,
          "name": "IsMutableCanOnlyBeFlippedToFalse",
          "msg": "Is Mutable can only be flipped to false"
        },
        {
          "code": 74,
          "name": "CollectionCannotBeVerifiedInThisInstruction",
          "msg": "Collection cannot be verified in this instruction"
        },
        {
          "code": 75,
          "name": "Removed",
          "msg": "This instruction was deprecated in a previous release and is now removed"
        },
        {
          "code": 76,
          "name": "MustBeBurned",
          "msg": "This token use method is burn and there are no remaining uses, it must be burned"
        },
        {
          "code": 77,
          "name": "InvalidUseMethod",
          "msg": "This use method is invalid"
        },
        {
          "code": 78,
          "name": "CannotChangeUseMethodAfterFirstUse",
          "msg": "Cannot Change Use Method after the first use"
        },
        {
          "code": 79,
          "name": "CannotChangeUsesAfterFirstUse",
          "msg": "Cannot Change Remaining or Available uses after the first use"
        },
        {
          "code": 80,
          "name": "CollectionNotFound",
          "msg": "Collection Not Found on Metadata"
        },
        {
          "code": 81,
          "name": "InvalidCollectionUpdateAuthority",
          "msg": "Collection Update Authority is invalid"
        },
        {
          "code": 82,
          "name": "CollectionMustBeAUniqueMasterEdition",
          "msg": "Collection Must Be a Unique Master Edition v2"
        },
        {
          "code": 83,
          "name": "UseAuthorityRecordAlreadyExists",
          "msg": "The Use Authority Record Already Exists, to modify it Revoke, then Approve"
        },
        {
          "code": 84,
          "name": "UseAuthorityRecordAlreadyRevoked",
          "msg": "The Use Authority Record is empty or already revoked"
        },
        {
          "code": 85,
          "name": "Unusable",
          "msg": "This token has no uses"
        },
        {
          "code": 86,
          "name": "NotEnoughUses",
          "msg": "There are not enough Uses left on this token."
        },
        {
          "code": 87,
          "name": "CollectionAuthorityRecordAlreadyExists",
          "msg": "This Collection Authority Record Already Exists."
        },
        {
          "code": 88,
          "name": "CollectionAuthorityDoesNotExist",
          "msg": "This Collection Authority Record Does Not Exist."
        },
        {
          "code": 89,
          "name": "InvalidUseAuthorityRecord",
          "msg": "This Use Authority Record is invalid."
        },
        {
          "code": 90,
          "name": "InvalidCollectionAuthorityRecord",
          "msg": "This Collection Authority Record is invalid."
        },
        {
          "code": 91,
          "name": "InvalidFreezeAuthority",
          "msg": "Metadata does not match the freeze authority on the mint"
        },
        {
          "code": 92,
          "name": "InvalidDelegate",
          "msg": "All tokens in this account have not been delegated to this user."
        },
        {
          "code": 93,
          "name": "CannotAdjustVerifiedCreator",
          "msg": "Creator can not be adjusted once they are verified."
        },
        {
          "code": 94,
          "name": "CannotRemoveVerifiedCreator",
          "msg": "Verified creators cannot be removed."
        },
        {
          "code": 95,
          "name": "CannotWipeVerifiedCreators",
          "msg": "Can not wipe verified creators."
        },
        {
          "code": 96,
          "name": "NotAllowedToChangeSellerFeeBasisPoints",
          "msg": "Not allowed to change seller fee basis points."
        },
        {
          "code": 97,
          "name": "EditionOverrideCannotBeZero",
          "msg": "Edition override cannot be zero"
        },
        {
          "code": 98,
          "name": "InvalidUser",
          "msg": "Invalid User"
        },
        {
          "code": 99,
          "name": "RevokeCollectionAuthoritySignerIncorrect",
          "msg": "Revoke Collection Authority signer is incorrect"
        },
        {
          "code": 100,
          "name": "TokenCloseFailed",
          "msg": "Token close failed"
        },
        {
          "code": 101,
          "name": "UnsizedCollection",
          "msg": "Can't use this function on unsized collection"
        },
        {
          "code": 102,
          "name": "SizedCollection",
          "msg": "Can't use this function on a sized collection"
        },
        {
          "code": 103,
          "name": "MissingCollectionMetadata",
          "msg": "Can't burn a verified member of a collection w/o providing collection metadata account"
        },
        {
          "code": 104,
          "name": "NotAMemberOfCollection",
          "msg": "This NFT is not a member of the specified collection."
        },
        {
          "code": 105,
          "name": "NotVerifiedMemberOfCollection",
          "msg": "This NFT is not a verified member of the specified collection."
        },
        {
          "code": 106,
          "name": "NotACollectionParent",
          "msg": "This NFT is not a collection parent NFT."
        },
        {
          "code": 107,
          "name": "CouldNotDetermineTokenStandard",
          "msg": "Could not determine a TokenStandard type."
        },
        {
          "code": 108,
          "name": "MissingEditionAccount",
          "msg": "This mint account has an edition but none was provided."
        },
        {
          "code": 109,
          "name": "NotAMasterEdition",
          "msg": "This edition is not a Master Edition"
        },
        {
          "code": 110,
          "name": "MasterEditionHasPrints",
          "msg": "This Master Edition has existing prints"
        },
        {
          "code": 111,
          "name": "BorshDeserializationError",
          "msg": "Borsh Deserialization Error"
        },
        {
          "code": 112,
          "name": "CannotUpdateVerifiedCollection",
          "msg": "Cannot update a verified collection in this command"
        },
        {
          "code": 113,
          "name": "CollectionMasterEditionAccountInvalid",
          "msg": "Edition account doesnt match collection "
        },
        {
          "code": 114,
          "name": "AlreadyVerified",
          "msg": "Item is already verified."
        },
        {
          "code": 115,
          "name": "AlreadyUnverified",
          "msg": "Item is already unverified."
        },
        {
          "code": 116,
          "name": "NotAPrintEdition",
          "msg": "This edition is not a Print Edition"
        },
        {
          "code": 117,
          "name": "InvalidMasterEdition",
          "msg": "Invalid Master Edition"
        },
        {
          "code": 118,
          "name": "InvalidPrintEdition",
          "msg": "Invalid Print Edition"
        },
        {
          "code": 119,
          "name": "InvalidEditionMarker",
          "msg": "Invalid Edition Marker"
        },
        {
          "code": 120,
          "name": "ReservationListDeprecated",
          "msg": "Reservation List is Deprecated"
        },
        {
          "code": 121,
          "name": "PrintEditionDoesNotMatchMasterEdition",
          "msg": "Print Edition does not match Master Edition"
        },
        {
          "code": 122,
          "name": "EditionNumberGreaterThanMaxSupply",
          "msg": "Edition Number greater than max supply"
        },
        {
          "code": 123,
          "name": "MustUnverify",
          "msg": "Must unverify before migrating collections."
        },
        {
          "code": 124,
          "name": "InvalidEscrowBumpSeed",
          "msg": "Invalid Escrow Account Bump Seed"
        },
        {
          "code": 125,
          "name": "MustBeEscrowAuthority",
          "msg": "Must Escrow Authority"
        },
        {
          "code": 126,
          "name": "InvalidSystemProgram",
          "msg": "Invalid System Program"
        },
        {
          "code": 127,
          "name": "MustBeNonFungible",
          "msg": "Must be a Non Fungible Token"
        },
        {
          "code": 128,
          "name": "InsufficientTokens",
          "msg": "Insufficient tokens for transfer"
        },
        {
          "code": 129,
          "name": "BorshSerializationError",
          "msg": "Borsh Serialization Error"
        },
        {
          "code": 130,
          "name": "NoFreezeAuthoritySet",
          "msg": "Cannot create NFT with no Freeze Authority."
        },
        {
          "code": 131,
          "name": "InvalidCollectionSizeChange",
          "msg": "Invalid collection size change"
        },
        {
          "code": 132,
          "name": "InvalidBubblegumSigner",
          "msg": "Invalid bubblegum signer"
        },
        {
          "code": 133,
          "name": "EscrowParentHasDelegate",
          "msg": "Escrow parent cannot have a delegate"
        },
        {
          "code": 134,
          "name": "MintIsNotSigner",
          "msg": "Mint needs to be signer to initialize the account"
        },
        {
          "code": 135,
          "name": "InvalidTokenStandard",
          "msg": "Invalid token standard"
        },
        {
          "code": 136,
          "name": "InvalidMintForTokenStandard",
          "msg": "Invalid mint account for specified token standard"
        },
        {
          "code": 137,
          "name": "InvalidAuthorizationRules",
          "msg": "Invalid authorization rules account"
        },
        {
          "code": 138,
          "name": "MissingAuthorizationRules",
          "msg": "Missing authorization rules account"
        },
        {
          "code": 139,
          "name": "MissingProgrammableConfig",
          "msg": "Missing programmable configuration"
        },
        {
          "code": 140,
          "name": "InvalidProgrammableConfig",
          "msg": "Invalid programmable configuration"
        },
        {
          "code": 141,
          "name": "DelegateAlreadyExists",
          "msg": "Delegate already exists"
        },
        {
          "code": 142,
          "name": "DelegateNotFound",
          "msg": "Delegate not found"
        },
        {
          "code": 143,
          "name": "MissingAccountInBuilder",
          "msg": "Required account not set in instruction builder"
        },
        {
          "code": 144,
          "name": "MissingArgumentInBuilder",
          "msg": "Required argument not set in instruction builder"
        },
        {
          "code": 145,
          "name": "FeatureNotSupported",
          "msg": "Feature not supported currently"
        },
        {
          "code": 146,
          "name": "InvalidSystemWallet",
          "msg": "Invalid system wallet"
        },
        {
          "code": 147,
          "name": "OnlySaleDelegateCanTransfer",
          "msg": "Only the sale delegate can transfer while its set"
        },
        {
          "code": 148,
          "name": "MissingTokenAccount",
          "msg": "Missing token account"
        },
        {
          "code": 149,
          "name": "MissingSplTokenProgram",
          "msg": "Missing SPL token program"
        },
        {
          "code": 150,
          "name": "MissingAuthorizationRulesProgram",
          "msg": "Missing authorization rules program"
        },
        {
          "code": 151,
          "name": "InvalidDelegateRoleForTransfer",
          "msg": "Invalid delegate role for transfer"
        },
        {
          "code": 152,
          "name": "InvalidTransferAuthority",
          "msg": "Invalid transfer authority"
        },
        {
          "code": 153,
          "name": "InstructionNotSupported",
          "msg": "Instruction not supported for ProgrammableNonFungible assets"
        },
        {
          "code": 154,
          "name": "KeyMismatch",
          "msg": "Public key does not match expected value"
        },
        {
          "code": 155,
          "name": "LockedToken",
          "msg": "Token is locked"
        },
        {
          "code": 156,
          "name": "UnlockedToken",
          "msg": "Token is unlocked"
        },
        {
          "code": 157,
          "name": "MissingDelegateRole",
          "msg": "Missing delegate role"
        },
        {
          "code": 158,
          "name": "InvalidAuthorityType",
          "msg": "Invalid authority type"
        },
        {
          "code": 159,
          "name": "MissingTokenRecord",
          "msg": "Missing token record account"
        },
        {
          "code": 160,
          "name": "MintSupplyMustBeZero",
          "msg": "Mint supply must be zero for programmable assets"
        },
        {
          "code": 161,
          "name": "DataIsEmptyOrZeroed",
          "msg": "Data is empty or zeroed"
        },
        {
          "code": 162,
          "name": "MissingTokenOwnerAccount",
          "msg": "Missing token owner"
        },
        {
          "code": 163,
          "name": "InvalidMasterEditionAccountLength",
          "msg": "Master edition account has an invalid length"
        },
        {
          "code": 164,
          "name": "IncorrectTokenState",
          "msg": "Incorrect token state"
        },
        {
          "code": 165,
          "name": "InvalidDelegateRole",
          "msg": "Invalid delegate role"
        },
        {
          "code": 166,
          "name": "MissingPrintSupply",
          "msg": "Print supply is required for non-fungibles"
        },
        {
          "code": 167,
          "name": "MissingMasterEditionAccount",
          "msg": "Missing master edition account"
        },
        {
          "code": 168,
          "name": "AmountMustBeGreaterThanZero",
          "msg": "Amount must be greater than zero"
        }
      ],
      "metadata": {
        "origin": "shank",
        "address": "metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s",
        "binaryVersion": "0.0.11",
        "libVersion": "0.0.11"
      }
    }
""".trimIndent()