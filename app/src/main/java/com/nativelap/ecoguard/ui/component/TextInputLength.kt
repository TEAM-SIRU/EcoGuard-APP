package com.nativelap.ecoguard.ui.component

/** 입력 제한과 카운터가 같은 Unicode code point 기준을 사용하며 이모지를 중간에서 자르지 않는다. */
internal object TextInputLength {
    fun count(value: String): Int = value.codePointCount(0, value.length)

    fun limit(
        value: String,
        maximum: Int,
    ): String {
        require(maximum >= 0)
        return if (count(value) <= maximum) value else value.substring(0, value.offsetByCodePoints(0, maximum))
    }
}
