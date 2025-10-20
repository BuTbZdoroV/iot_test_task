package com.example.render.utils

fun createColor(red: Int, green: Int, blue: Int, alpha: Int = 255): Int {
    return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
}

fun createColor(rgb: Int, alpha: Int = 255): Int {
    return rgb.toInt() or (alpha shl 24)
}

val BLACK = createColor(0, 0, 0)
val BLUE = createColor(0, 0, 255)