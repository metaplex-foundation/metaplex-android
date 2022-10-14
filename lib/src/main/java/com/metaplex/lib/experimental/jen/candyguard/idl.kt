/*
 * idl
 * Metaplex
 * 
 * Created by Funkatronics on 9/16/2022
 */

package com.metaplex.lib.experimental.jen.candyguard

val candyGuardJson = """
    {
      "version": "0.0.1",
      "name": "candy_guard",
      "instructions": [
        {
          "name": "initialize",
          "docs": [
            "Create a new candy guard account."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "base",
              "isMut": true,
              "isSigner": true
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false
            }
          ],
          "args": [
            {
              "name": "data",
              "type": {
                "defined": "CandyGuardData"
              }
            }
          ]
        },
        {
          "name": "mint",
          "docs": [
            "Mint an NFT from a candy machine wrapped in the candy guard."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "candyMachineProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "candyMachine",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "candyMachineAuthorityPda",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true
            },
            {
              "name": "nftMetadata",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "nftMint",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "nftMintAuthority",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "nftMasterEdition",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "collectionAuthorityRecord",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "collectionMint",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "collectionMetadata",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "collectionMasterEdition",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "collectionUpdateAuthority",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "tokenMetadataProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "tokenProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "rent",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "recentSlothashes",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "instructionSysvarAccount",
              "isMut": false,
              "isSigner": false
            }
          ],
          "args": [
            {
              "name": "mintArgs",
              "type": "bytes"
            },
            {
              "name": "label",
              "type": {
                "option": "string"
              }
            }
          ]
        },
        {
          "name": "unwrap",
          "docs": [
            "Remove a candy guard from a candy machine, setting the authority to the",
            "candy guard authority."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "candyMachine",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "candyMachineProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "candyMachineAuthority",
              "isMut": false,
              "isSigner": true
            }
          ],
          "args": []
        },
        {
          "name": "update",
          "docs": [
            "Update the candy guard configuration."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "payer",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "systemProgram",
              "isMut": false,
              "isSigner": false
            }
          ],
          "args": [
            {
              "name": "data",
              "type": {
                "defined": "CandyGuardData"
              }
            }
          ]
        },
        {
          "name": "withdraw",
          "docs": [
            "Withdraw the rent SOL from the candy guard account."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "authority",
              "isMut": true,
              "isSigner": true
            }
          ],
          "args": []
        },
        {
          "name": "wrap",
          "docs": [
            "Add a candy guard to a candy machine. After the guard is added, mint",
            "is only allowed through the candy guard."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "authority",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "candyMachine",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "candyMachineProgram",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "candyMachineAuthority",
              "isMut": false,
              "isSigner": true
            }
          ],
          "args": []
        }
      ],
      "accounts": [
        {
          "name": "MintCounter",
          "docs": [
            "PDA to track the number of mints for an individual address."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "count",
                "type": "u16"
              }
            ]
          }
        },
        {
          "name": "CandyGuard",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "base",
                "type": "publicKey"
              },
              {
                "name": "bump",
                "type": "u8"
              },
              {
                "name": "authority",
                "type": "publicKey"
              }
            ]
          }
        }
      ],
      "types": [
        {
          "name": "AllowList",
          "docs": [
            "Configurations options for allow list."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "merkleRoot",
                "docs": [
                  "Merkle root of the addresses allowed to mint."
                ],
                "type": {
                  "array": [
                    "u8",
                    32
                  ]
                }
              }
            ]
          }
        },
        {
          "name": "BotTax",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "lamports",
                "type": "u64"
              },
              {
                "name": "lastInstruction",
                "type": "bool"
              }
            ]
          }
        },
        {
          "name": "EndSettings",
          "docs": [
            "Configurations options for end settings."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "endSettingType",
                "type": {
                  "defined": "EndSettingType"
                }
              },
              {
                "name": "number",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "Gatekeeper",
          "docs": [
            "Configurations options for the gatekeeper."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "gatekeeperNetwork",
                "docs": [
                  "The network for the gateway token required"
                ],
                "type": "publicKey"
              },
              {
                "name": "expireOnUse",
                "docs": [
                  "Whether or not the token should expire after minting.",
                  "The gatekeeper network must support this if true."
                ],
                "type": "bool"
              }
            ]
          }
        },
        {
          "name": "Lamports",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "destination",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "LiveDate",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "date",
                "type": {
                  "option": "i64"
                }
              }
            ]
          }
        },
        {
          "name": "MintLimit",
          "docs": [
            "Configurations options for mint limit."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "id",
                "docs": [
                  "Unique identifier of the mint limit."
                ],
                "type": "u8"
              },
              {
                "name": "limit",
                "docs": [
                  "Limit of mints per individual address."
                ],
                "type": "u16"
              }
            ]
          }
        },
        {
          "name": "NftPayment",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "burn",
                "type": "bool"
              },
              {
                "name": "requiredCollection",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "SplToken",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "tokenMint",
                "type": "publicKey"
              },
              {
                "name": "destinationAta",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "ThirdPartySigner",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "signerKey",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "Whitelist",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "mint",
                "type": "publicKey"
              },
              {
                "name": "presale",
                "type": "bool"
              },
              {
                "name": "discountPrice",
                "type": {
                  "option": "u64"
                }
              },
              {
                "name": "mode",
                "type": {
                  "defined": "WhitelistTokenMode"
                }
              }
            ]
          }
        },
        {
          "name": "CandyGuardData",
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "default",
                "type": {
                  "defined": "GuardSet"
                }
              },
              {
                "name": "groups",
                "type": {
                  "option": {
                    "vec": {
                      "defined": "Group"
                    }
                  }
                }
              }
            ]
          }
        },
        {
          "name": "Group",
          "docs": [
            "A group represent a specific set of guards. When groups are used, transactions",
            "must specify which group should be used during validation."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "label",
                "type": "string"
              },
              {
                "name": "guards",
                "type": {
                  "defined": "GuardSet"
                }
              }
            ]
          }
        },
        {
          "name": "GuardSet",
          "docs": [
            "The set of guards available."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "botTax",
                "docs": [
                  "Last instruction check and bot tax (penalty for invalid transactions)."
                ],
                "type": {
                  "option": {
                    "defined": "BotTax"
                  }
                }
              },
              {
                "name": "lamports",
                "docs": [
                  "Lamports guard (set the price for the mint in lamports)."
                ],
                "type": {
                  "option": {
                    "defined": "Lamports"
                  }
                }
              },
              {
                "name": "splToken",
                "docs": [
                  "Spl-token guard (set the price for the mint in spl-token amount)."
                ],
                "type": {
                  "option": {
                    "defined": "SplToken"
                  }
                }
              },
              {
                "name": "liveDate",
                "docs": [
                  "Live data guard (controls when minting is allowed)."
                ],
                "type": {
                  "option": {
                    "defined": "LiveDate"
                  }
                }
              },
              {
                "name": "thirdPartySigner",
                "docs": [
                  "Third party signer guard."
                ],
                "type": {
                  "option": {
                    "defined": "ThirdPartySigner"
                  }
                }
              },
              {
                "name": "whitelist",
                "docs": [
                  "Whitelist guard (whitelist mint settings)."
                ],
                "type": {
                  "option": {
                    "defined": "Whitelist"
                  }
                }
              },
              {
                "name": "gatekeeper",
                "docs": [
                  "Gatekeeper guard"
                ],
                "type": {
                  "option": {
                    "defined": "Gatekeeper"
                  }
                }
              },
              {
                "name": "endSettings",
                "docs": [
                  "End settings guard"
                ],
                "type": {
                  "option": {
                    "defined": "EndSettings"
                  }
                }
              },
              {
                "name": "allowList",
                "docs": [
                  "Allow list guard"
                ],
                "type": {
                  "option": {
                    "defined": "AllowList"
                  }
                }
              },
              {
                "name": "mintLimit",
                "docs": [
                  "Mint limit guard"
                ],
                "type": {
                  "option": {
                    "defined": "MintLimit"
                  }
                }
              },
              {
                "name": "nftPayment",
                "docs": [
                  "NFT Payment"
                ],
                "type": {
                  "option": {
                    "defined": "NftPayment"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "EndSettingType",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Date"
              },
              {
                "name": "Amount"
              }
            ]
          }
        },
        {
          "name": "WhitelistTokenMode",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "BurnEveryTime"
              },
              {
                "name": "NeverBurn"
              }
            ]
          }
        }
      ],
      "errors": [
        {
          "code": 6000,
          "name": "InvalidAccountSize",
          "msg": "Could not save guard to account"
        },
        {
          "code": 6001,
          "name": "DeserializationError",
          "msg": "Could not deserialize guard"
        },
        {
          "code": 6002,
          "name": "PublicKeyMismatch",
          "msg": "Public key mismatch"
        },
        {
          "code": 6003,
          "name": "DataIncrementLimitExceeded",
          "msg": "Missing expected remaining account"
        },
        {
          "code": 6004,
          "name": "IncorrectOwner",
          "msg": "Account does not have correct owner"
        },
        {
          "code": 6005,
          "name": "Uninitialized",
          "msg": "Account is not initialized"
        },
        {
          "code": 6006,
          "name": "MissingRemainingAccount",
          "msg": "Missing expected remaining account"
        },
        {
          "code": 6007,
          "name": "NumericalOverflowError",
          "msg": "Numerical overflow error"
        },
        {
          "code": 6008,
          "name": "RequiredGroupLabelNotFound",
          "msg": "Missing required group label"
        },
        {
          "code": 6009,
          "name": "GroupNotFound",
          "msg": "Group not found"
        },
        {
          "code": 6010,
          "name": "LabelExceededLength",
          "msg": "Group not found"
        },
        {
          "code": 6011,
          "name": "CollectionKeyMismatch",
          "msg": "Collection public key mismatch"
        },
        {
          "code": 6012,
          "name": "MissingCollectionAccounts",
          "msg": "Missing collection accounts"
        },
        {
          "code": 6013,
          "name": "CollectionUpdateAuthorityKeyMismatch",
          "msg": "Collection update authority public key mismatch"
        },
        {
          "code": 6014,
          "name": "MintNotLastTransaction",
          "msg": "Mint must be the last instructions of the transaction"
        },
        {
          "code": 6015,
          "name": "MintNotLive",
          "msg": "Mint is not live"
        },
        {
          "code": 6016,
          "name": "NotEnoughSOL",
          "msg": "Not enough SOL to pay for the mint"
        },
        {
          "code": 6017,
          "name": "TokenTransferFailed",
          "msg": "Token transfer failed"
        },
        {
          "code": 6018,
          "name": "NotEnoughTokens",
          "msg": "Not enough tokens to pay for this minting"
        },
        {
          "code": 6019,
          "name": "MissingRequiredSignature",
          "msg": "A signature was required but not found"
        },
        {
          "code": 6020,
          "name": "TokenBurnFailed",
          "msg": "Token burn failed"
        },
        {
          "code": 6021,
          "name": "NoWhitelistToken",
          "msg": "No whitelist token present"
        },
        {
          "code": 6022,
          "name": "GatewayTokenInvalid",
          "msg": "Gateway token is not valid"
        },
        {
          "code": 6023,
          "name": "AfterEndSettingsDate",
          "msg": "Current time is after the set end settings date"
        },
        {
          "code": 6024,
          "name": "AfterEndSettingsMintAmount",
          "msg": "Current items minted is at the set end settings amount"
        },
        {
          "code": 6025,
          "name": "InvalidMintTime",
          "msg": "Current time is not within the allowed mint time"
        },
        {
          "code": 6026,
          "name": "AddressNotFoundInAllowedList",
          "msg": "Address not found on the allowed list"
        },
        {
          "code": 6027,
          "name": "MissingAllowedListProof",
          "msg": "Missing allowed list proof"
        },
        {
          "code": 6028,
          "name": "AllowedMintLimitReached",
          "msg": "The maximum number of allowed mints was reached"
        },
        {
          "code": 6029,
          "name": "InvalidNFTCollectionPayment",
          "msg": "Invalid NFT Collection Payment"
        }
      ],
      "metadata": {
        "address": "CnDYGRdU51FsSyLnVgSd19MCFxA4YHT5h3nacvCKMPUJ",
        "origin": "anchor",
        "binaryVersion": "0.25.0",
        "libVersion": "0.25.0"
      }
    }
""".trimIndent()