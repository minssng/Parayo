package com.example.parayo.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable

private fun borderBG(
    borderColor: String = "#1F000000",
    bgColor: String = "#FFFFFF",
    borderWidthLeft: Int = 0,
    borderWidthTop: Int = 0,
    borderWidthRight: Int = 0,
    borderWidthBottom: Int = 0
) : LayerDrawable {
    val layerDrawable = arrayOf<Drawable>(
        ColorDrawable(Color.parseColor(borderColor)), // 사각형 영역을 특정 색으로 채워주는 Drawable 클래스. 선이 될 ColorDrawable 과 배경이 될 ColorDrawable 객체를 각각 만들어줌.
        ColorDrawable(Color.parseColor(bgColor))
    ).let(::LayerDrawable) // LayerDrawable(...) 과 같은 생성자를 호출하는 코드.
    // val drawables = arrayOf<Drawable>(
    //          ColorDrawable(Color.parseColor(borderColor)),
    //          ColorDrawable(Color.parseColor(bgColor))
    //     )
    // val layerDrawable = LayerDrawable(drawables)

    layerDrawable.setLayerInset(
        1,
        borderWidthLeft,
        borderWidthTop,
        borderWidthRight,
        borderWidthBottom
    )

    return layerDrawable
}

fun borderLeft(
    color: String = "#1F000000",
    width: Int
) = borderBG(
    borderColor = color,
    borderWidthLeft= width
)

fun borderTop(
    color: String = "#1F000000",
    width: Int
) = borderBG(
    borderColor = color,
    borderWidthTop= width
)

fun borderRight(
    color: String = "#1F000000",
    width: Int
) = borderBG(
    borderColor = color,
    borderWidthRight = width
)

fun borderBottom(
    color: String = "#1F000000",
    width: Int
) = borderBG(
    borderColor = color,
    borderWidthBottom = width
)