/*
 * idl
 * Metaplex
 * 
 * Created by Funkatronics on 9/16/2022
 */

package com.metaplex.lib.experimental.jen.candyguard

val candyGuardJson = """
    {
      "version": "0.1.1",
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
              "isMut": false,
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
              "type": "bytes"
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
          "name": "route",
          "docs": [
            "Route the transaction to a guard instruction."
          ],
          "accounts": [
            {
              "name": "candyGuard",
              "isMut": false,
              "isSigner": false
            },
            {
              "name": "candyMachine",
              "isMut": true,
              "isSigner": false
            },
            {
              "name": "payer",
              "isMut": true,
              "isSigner": true
            }
          ],
          "args": [
            {
              "name": "args",
              "type": {
                "defined": "RouteArgs"
              }
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
          "name": "setAuthority",
          "docs": [
            "Set a new authority of the candy guard."
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
            }
          ],
          "args": [
            {
              "name": "newAuthority",
              "type": "publicKey"
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
              "name": "candyMachineAuthority",
              "isMut": false,
              "isSigner": true
            },
            {
              "name": "candyMachineProgram",
              "isMut": false,
              "isSigner": false
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
              "type": "bytes"
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
          "name": "FreezeEscrow",
          "docs": [
            "PDA to store the frozen funds."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "candyGuard",
                "docs": [
                  "Candy guard address associated with this escrow."
                ],
                "type": "publicKey"
              },
              {
                "name": "candyMachine",
                "docs": [
                  "Candy machine address associated with this escrow."
                ],
                "type": "publicKey"
              },
              {
                "name": "frozenCount",
                "docs": [
                  "Number of NFTs frozen."
                ],
                "type": "u64"
              },
              {
                "name": "firstMintTime",
                "docs": [
                  "The timestamp of the first (frozen) mint. This is used to calculate",
                  "when the freeze period is over."
                ],
                "type": {
                  "option": "i64"
                }
              },
              {
                "name": "freezePeriod",
                "docs": [
                  "The amount of time (in seconds) for the freeze. The NFTs will be",
                  "allowed to thaw after this."
                ],
                "type": "i64"
              },
              {
                "name": "destination",
                "docs": [
                  "The destination address for the frozed fund to go to."
                ],
                "type": "publicKey"
              },
              {
                "name": "authority",
                "docs": [
                  "The authority that initialized the freeze. This will be the only",
                  "address able to unlock the funds in case the candy guard account is",
                  "closed."
                ],
                "type": "publicKey"
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
          "name": "AddressGate",
          "docs": [
            "Guard that restricts access to a specific address."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "address",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "AllowList",
          "docs": [
            "Guard that uses a merkle tree to specify the addresses allowed to mint.",
            "",
            "List of accounts required:",
            "",
            "0. `[]` Pda created by the merkle proof instruction (seeds `[\"allow_list\", merke tree root,",
            "payer key, candy guard pubkey, candy machine pubkey]`)."
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
          "name": "AllowListProof",
          "docs": [
            "PDA to track whether an address has been validated or not."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "timestamp",
                "type": "i64"
              }
            ]
          }
        },
        {
          "name": "BotTax",
          "docs": [
            "Guard is used to:",
            "* charge a penalty for invalid transactions",
            "* validate that the mint transaction is the last transaction",
            "* verify that only authorized programs have instructions",
            "",
            "The `bot_tax` is applied to any error that occurs during the",
            "validation of the guards."
          ],
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
          "name": "EndDate",
          "docs": [
            "Guard that sets a specific date for the mint to stop."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "date",
                "type": "i64"
              }
            ]
          }
        },
        {
          "name": "FreezeSolPayment",
          "docs": [
            "Guard that charges an amount in SOL (lamports) for the mint with a freeze period.",
            "",
            "List of accounts required:",
            "",
            "0. `[writable]` Freeze PDA to receive the funds (seeds `[\"freeze_escrow\",",
            "destination pubkey, candy guard pubkey, candy machine pubkey]`).",
            "1. `[]` Associate token account of the NFT (seeds `[payer pubkey, token",
            "program pubkey, nft mint pubkey]`)."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "lamports",
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
          "name": "FreezeTokenPayment",
          "docs": [
            "Guard that charges an amount in a specified spl-token as payment for the mint with a freeze period.",
            "",
            "List of accounts required:",
            "",
            "0. `[writable]` Freeze PDA to receive the funds (seeds `[\"freeze_escrow\",",
            "destination_ata pubkey, candy guard pubkey, candy machine pubkey]`).",
            "1. `[]` Associate token account of the NFT (seeds `[payer pubkey, token",
            "program pubkey, nft mint pubkey]`).",
            "2. `[writable]` Token account holding the required amount.",
            "3. `[writable]` Associate token account of the Freeze PDA (seeds `[freeze PDA",
            "pubkey, token program pubkey, nft mint pubkey]`)."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "mint",
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
          "name": "Gatekeeper",
          "docs": [
            "Guard that validates if the payer of the transaction has a token from a specified",
            "gateway network — in most cases, a token after completing a captcha challenge.",
            "",
            "List of accounts required:",
            "",
            "0. `[writeable]` Gatekeeper token account.",
            "1. `[]` Gatekeeper program account.",
            "2. `[]` Gatekeeper expire account."
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
          "name": "MintLimit",
          "docs": [
            "Gaurd to set a limit of mints per wallet.",
            "",
            "List of accounts required:",
            "",
            "0. `[writable]` Mint counter PDA. The PDA is derived",
            "using the seed `[\"mint_limit\", mint guard id, payer key,",
            "candy guard pubkey, candy machine pubkey]`."
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
          "name": "NftBurn",
          "docs": [
            "Guard that requires another NFT (token) from a specific collection to be burned.",
            "",
            "List of accounts required:",
            "",
            "0. `[writeable]` Token account of the NFT.",
            "1. `[writeable]` Metadata account of the NFT.",
            "2. `[writeable]` Master Edition account of the NFT.",
            "3. `[writeable]` Mint account of the NFT.",
            "4. `[writeable]` Collection metadata account of the NFT."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "requiredCollection",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "NftGate",
          "docs": [
            "Guard that restricts the transaction to holders of a specified collection.",
            "",
            "List of accounts required:",
            "",
            "0. `[]` Token account of the NFT.",
            "1. `[]` Metadata account of the NFT."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "requiredCollection",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "NftPayment",
          "docs": [
            "Guard that charges another NFT (token) from a specific collection as payment",
            "for the mint.",
            "",
            "List of accounts required:",
            "",
            "0. `[writeable]` Token account of the NFT.",
            "1. `[writeable]` Metadata account of the NFT.",
            "2. `[]` Mint account of the NFT.",
            "3. `[]` Account to receive the NFT.",
            "4. `[writeable]` Destination PDA key (seeds [destination pubkey, token program id, nft mint pubkey]).",
            "5. `[]` spl-associate-token program ID."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "requiredCollection",
                "type": "publicKey"
              },
              {
                "name": "destination",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "ProgramGate",
          "docs": [
            "Guard that restricts the programs that can be in a mint transaction. The guard allows the",
            "necessary programs for the mint and any other program specified in the configuration."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "additional",
                "type": {
                  "vec": "publicKey"
                }
              }
            ]
          }
        },
        {
          "name": "RedeemedAmount",
          "docs": [
            "Guard that stop the mint once the specified amount of items",
            "redeenmed is reached."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "maximum",
                "type": "u64"
              }
            ]
          }
        },
        {
          "name": "SolPayment",
          "docs": [
            "Guard that charges an amount in SOL (lamports) for the mint.",
            "",
            "List of accounts required:",
            "",
            "0. `[]` Account to receive the funds."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "lamports",
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
          "name": "StartDate",
          "docs": [
            "Guard that sets a specific start date for the mint."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "date",
                "type": "i64"
              }
            ]
          }
        },
        {
          "name": "ThirdPartySigner",
          "docs": [
            "Guard that requires a specified signer to validate the transaction.",
            "",
            "List of accounts required:",
            "",
            "0. `[signer]` Signer of the transaction."
          ],
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
          "name": "TokenBurn",
          "docs": [
            "Guard that requires addresses that hold an amount of a specified spl-token",
            "and burns them.",
            "",
            "List of accounts required:",
            "",
            "0. `[writable]` Token account holding the required amount.",
            "1. `[writable]` Token mint account."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "mint",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "TokenGate",
          "docs": [
            "Guard that restricts access to addresses that hold the specified spl-token.",
            "",
            "List of accounts required:",
            "",
            "0. `[]` Token account holding the required amount."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "mint",
                "type": "publicKey"
              }
            ]
          }
        },
        {
          "name": "TokenPayment",
          "docs": [
            "Guard that charges an amount in a specified spl-token as payment for the mint.",
            "",
            "List of accounts required:",
            "",
            "0. `[writable]` Token account holding the required amount.",
            "1. `[writable]` Address of the ATA to receive the tokens."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "amount",
                "type": "u64"
              },
              {
                "name": "mint",
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
          "name": "RouteArgs",
          "docs": [
            "Arguments for a route transaction."
          ],
          "type": {
            "kind": "struct",
            "fields": [
              {
                "name": "guard",
                "docs": [
                  "The target guard type."
                ],
                "type": {
                  "defined": "GuardType"
                }
              },
              {
                "name": "data",
                "docs": [
                  "Arguments for the guard instruction."
                ],
                "type": "bytes"
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
                "name": "solPayment",
                "docs": [
                  "Sol payment guard (set the price for the mint in lamports)."
                ],
                "type": {
                  "option": {
                    "defined": "SolPayment"
                  }
                }
              },
              {
                "name": "tokenPayment",
                "docs": [
                  "Token payment guard (set the price for the mint in spl-token amount)."
                ],
                "type": {
                  "option": {
                    "defined": "TokenPayment"
                  }
                }
              },
              {
                "name": "startDate",
                "docs": [
                  "Start data guard (controls when minting is allowed)."
                ],
                "type": {
                  "option": {
                    "defined": "StartDate"
                  }
                }
              },
              {
                "name": "thirdPartySigner",
                "docs": [
                  "Third party signer guard (requires an extra signer for the transaction)."
                ],
                "type": {
                  "option": {
                    "defined": "ThirdPartySigner"
                  }
                }
              },
              {
                "name": "tokenGate",
                "docs": [
                  "Token gate guard (restrict access to holders of a specific token)."
                ],
                "type": {
                  "option": {
                    "defined": "TokenGate"
                  }
                }
              },
              {
                "name": "gatekeeper",
                "docs": [
                  "Gatekeeper guard (captcha challenge)."
                ],
                "type": {
                  "option": {
                    "defined": "Gatekeeper"
                  }
                }
              },
              {
                "name": "endDate",
                "docs": [
                  "End date guard (set an end date to stop the mint)."
                ],
                "type": {
                  "option": {
                    "defined": "EndDate"
                  }
                }
              },
              {
                "name": "allowList",
                "docs": [
                  "Allow list guard (curated list of allowed addresses)."
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
                  "Mint limit guard (add a limit on the number of mints per wallet)."
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
                  "NFT Payment (charge an NFT in order to mint)."
                ],
                "type": {
                  "option": {
                    "defined": "NftPayment"
                  }
                }
              },
              {
                "name": "redeemedAmount",
                "docs": [
                  "Redeemed amount guard (add a limit on the overall number of items minted)."
                ],
                "type": {
                  "option": {
                    "defined": "RedeemedAmount"
                  }
                }
              },
              {
                "name": "addressGate",
                "docs": [
                  "Address gate (check access against a specified address)."
                ],
                "type": {
                  "option": {
                    "defined": "AddressGate"
                  }
                }
              },
              {
                "name": "nftGate",
                "docs": [
                  "NFT gate guard (check access based on holding a specified NFT)."
                ],
                "type": {
                  "option": {
                    "defined": "NftGate"
                  }
                }
              },
              {
                "name": "nftBurn",
                "docs": [
                  "NFT burn guard (burn a specified NFT)."
                ],
                "type": {
                  "option": {
                    "defined": "NftBurn"
                  }
                }
              },
              {
                "name": "tokenBurn",
                "docs": [
                  "Token burn guard (burn a specified amount of spl-token)."
                ],
                "type": {
                  "option": {
                    "defined": "TokenBurn"
                  }
                }
              },
              {
                "name": "freezeSolPayment",
                "docs": [
                  "Freeze sol payment guard (set the price for the mint in lamports with a freeze period)."
                ],
                "type": {
                  "option": {
                    "defined": "FreezeSolPayment"
                  }
                }
              },
              {
                "name": "freezeTokenPayment",
                "docs": [
                  "Freeze token payment guard (set the price for the mint in spl-token amount with a freeze period)."
                ],
                "type": {
                  "option": {
                    "defined": "FreezeTokenPayment"
                  }
                }
              },
              {
                "name": "programGate",
                "docs": [
                  "Program gate guard (restricts the programs that can be in a mint transaction)."
                ],
                "type": {
                  "option": {
                    "defined": "ProgramGate"
                  }
                }
              }
            ]
          }
        },
        {
          "name": "FreezeInstruction",
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "Initialize"
              },
              {
                "name": "Thaw"
              },
              {
                "name": "UnlockFunds"
              }
            ]
          }
        },
        {
          "name": "GuardType",
          "docs": [
            "Available guard types."
          ],
          "type": {
            "kind": "enum",
            "variants": [
              {
                "name": "BotTax"
              },
              {
                "name": "SolPayment"
              },
              {
                "name": "TokenPayment"
              },
              {
                "name": "StartDate"
              },
              {
                "name": "ThirdPartySigner"
              },
              {
                "name": "TokenGate"
              },
              {
                "name": "Gatekeeper"
              },
              {
                "name": "EndDate"
              },
              {
                "name": "AllowList"
              },
              {
                "name": "MintLimit"
              },
              {
                "name": "NftPayment"
              },
              {
                "name": "RedeemedAmount"
              },
              {
                "name": "AddressGate"
              },
              {
                "name": "NftGate"
              },
              {
                "name": "NftBurn"
              },
              {
                "name": "TokenBurn"
              },
              {
                "name": "FreezeSolPayment"
              },
              {
                "name": "FreezeTokenPayment"
              },
              {
                "name": "ProgramGate"
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
          "msg": "Exceeded account increase limit"
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
          "name": "ExceededLength",
          "msg": "Value exceeded maximum length"
        },
        {
          "code": 6011,
          "name": "CandyMachineEmpty",
          "msg": "Candy machine is empty"
        },
        {
          "code": 6012,
          "name": "InstructionNotFound",
          "msg": "No instruction was found"
        },
        {
          "code": 6013,
          "name": "CollectionKeyMismatch",
          "msg": "Collection public key mismatch"
        },
        {
          "code": 6014,
          "name": "MissingCollectionAccounts",
          "msg": "Missing collection accounts"
        },
        {
          "code": 6015,
          "name": "CollectionUpdateAuthorityKeyMismatch",
          "msg": "Collection update authority public key mismatch"
        },
        {
          "code": 6016,
          "name": "MintNotLastTransaction",
          "msg": "Mint must be the last instructions of the transaction"
        },
        {
          "code": 6017,
          "name": "MintNotLive",
          "msg": "Mint is not live"
        },
        {
          "code": 6018,
          "name": "NotEnoughSOL",
          "msg": "Not enough SOL to pay for the mint"
        },
        {
          "code": 6019,
          "name": "TokenBurnFailed",
          "msg": "Token burn failed"
        },
        {
          "code": 6020,
          "name": "NotEnoughTokens",
          "msg": "Not enough tokens on the account"
        },
        {
          "code": 6021,
          "name": "TokenTransferFailed",
          "msg": "Token transfer failed"
        },
        {
          "code": 6022,
          "name": "MissingRequiredSignature",
          "msg": "A signature was required but not found"
        },
        {
          "code": 6023,
          "name": "GatewayTokenInvalid",
          "msg": "Gateway token is not valid"
        },
        {
          "code": 6024,
          "name": "AfterEndDate",
          "msg": "Current time is after the set end date"
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
          "name": "AllowedListNotEnabled",
          "msg": "Allow list guard is not enabled"
        },
        {
          "code": 6029,
          "name": "AllowedMintLimitReached",
          "msg": "The maximum number of allowed mints was reached"
        },
        {
          "code": 6030,
          "name": "InvalidNftCollection",
          "msg": "Invalid NFT collection"
        },
        {
          "code": 6031,
          "name": "MissingNft",
          "msg": "Missing NFT on the account"
        },
        {
          "code": 6032,
          "name": "MaximumRedeemedAmount",
          "msg": "Current redemeed items is at the set maximum amount"
        },
        {
          "code": 6033,
          "name": "AddressNotAuthorized",
          "msg": "Address not authorized"
        },
        {
          "code": 6034,
          "name": "MissingFreezeInstruction",
          "msg": "Missing freeze instruction data"
        },
        {
          "code": 6035,
          "name": "FreezeGuardNotEnabled",
          "msg": "Freeze guard must be enabled"
        },
        {
          "code": 6036,
          "name": "FreezeNotInitialized",
          "msg": "Freeze must be initialized"
        },
        {
          "code": 6037,
          "name": "MissingFreezePeriod",
          "msg": "Missing freeze period"
        },
        {
          "code": 6038,
          "name": "FreezeEscrowAlreadyExists",
          "msg": "The freeze escrow account already exists"
        },
        {
          "code": 6039,
          "name": "ExceededMaximumFreezePeriod",
          "msg": "Maximum freeze period exceeded"
        },
        {
          "code": 6040,
          "name": "ThawNotEnabled",
          "msg": "Thaw is not enabled"
        },
        {
          "code": 6041,
          "name": "UnlockNotEnabled",
          "msg": "Unlock is not enabled (not all NFTs are thawed)"
        },
        {
          "code": 6042,
          "name": "DuplicatedGroupLabel",
          "msg": "Duplicated group label"
        },
        {
          "code": 6043,
          "name": "DuplicatedMintLimitId",
          "msg": "Duplicated mint limit id"
        },
        {
          "code": 6044,
          "name": "UnauthorizedProgramFound",
          "msg": "An unauthorized program was found in the transaction"
        },
        {
          "code": 6045,
          "name": "ExceededProgramListSize",
          "msg": "Exceeded the maximum number of programs in the additional list"
        }
      ],
      "metadata": {
        "address": "Guard1JwRhJkVH6XZhzoYxeBVQe872VH6QggF4BWmS9g",
        "origin": "anchor",
        "binaryVersion": "0.25.0",
        "libVersion": "0.25.0"
      }
    }
""".trimIndent()