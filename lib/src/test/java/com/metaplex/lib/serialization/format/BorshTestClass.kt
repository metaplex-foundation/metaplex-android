/*
 * BorshTestClass
 * Metaplex
 * 
 * Created by Funkatronics on 7/25/2022
 */
package com.metaplex.lib.serialization.format

import kotlinx.serialization.Serializable

enum class BorshTestEnum {
    ENUM1, ENUM2, ENUM3
}

@Serializable
internal data class BorshTestClass(
    val u8: UByte, val u16: UShort, val u32: UInt, val u64: ULong,
    val i8: Byte, val i16: Short, val i32: Int, val i64: Long,
    val f32: Float, val f64: Double,
    val string: String, val enum: BorshTestEnum,
    val optionalString: String?, val optionalNum: Int?,
    val list: List<Int>, val map: Map<String, Int>,
    val hashMap: HashMap<String, Int>, val hashSet: HashSet<Int>,
    val struct: BorshTestSubclass)

@Serializable
internal data class BorshTestSubclass(val name: String, val id: Int)

