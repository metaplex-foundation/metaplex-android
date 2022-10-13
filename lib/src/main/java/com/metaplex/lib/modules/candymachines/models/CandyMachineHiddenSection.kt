/*
 * CandyMachineHiddenSection
 * metaplex-android
 * 
 * Created by Funkatronics on 10/7/2022
 */

package com.metaplex.lib.modules.candymachines.models

import com.metaplex.lib.experimental.jen.candymachine.ConfigLineSettings
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder

//@Serializable
internal class CandyMachineHiddenSection(val itemsLoaded: Int, val items: List<CandyMachineItem>)

internal class CandyMachineHiddenSectionDeserializer(val itemsAvailable: Int,
                                                     val configLineSettings: ConfigLineSettings)
    : DeserializationStrategy<CandyMachineHiddenSection> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor // who tf cares?

    override fun deserialize(decoder: Decoder): CandyMachineHiddenSection {
        val itemsLoaded = decoder.decodeInt()

        val nameLength: Int = configLineSettings.nameLength.toInt()
        val uriLength: Int = configLineSettings.uriLength.toInt()
        val configLineSize = nameLength + uriLength
//        val configLinesSize = configLineSize * itemsAvailable.toInt()

        val rawConfigLines = (0 until itemsAvailable).map {
            ByteArray(configLineSize) {
                decoder.decodeByte()
            }
        }

        // there is more data here that can be read and added to the hidden
        // section object, but I don't need this stuff yet so leaving as is

        return CandyMachineHiddenSection(itemsLoaded, rawConfigLines.map {
            CandyMachineItem(it.sliceArray(0 until nameLength).toString(Charsets.UTF_8).replace("\u0000", ""),
                it.sliceArray(nameLength until uriLength).toString(Charsets.UTF_8).replace("\u0000", ""))
        })
    }
}