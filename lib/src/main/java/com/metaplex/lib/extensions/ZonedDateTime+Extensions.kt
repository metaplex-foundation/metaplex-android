/*
 * ZonedDateTime+Extensions
 * Metaplex
 * 
 * Created by Funkatronics on 9/14/2022
 */

package com.metaplex.lib.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime


fun ZonedDateTime.epochMillis(): Long = this.toEpochSecond() * 1000

//fun LocalDate.epochMillis(): Long = this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000