/*
 * CandyMachineClientTests
 * Metaplex
 * 
 * Created by Funkatronics on 9/8/2022
 */

package com.metaplex.lib.modules.candymachine

import com.metaplex.lib.experimental.jen.jenerateCandyMachine
import org.junit.Before
import org.junit.Test

class CandyMachineClientTests {

    @Before
    fun prepare() {
        jenerateCandyMachine()
    }

    @Test
    fun doStuff() {

    }
}