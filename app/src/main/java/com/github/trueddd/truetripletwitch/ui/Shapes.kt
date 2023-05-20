package com.github.trueddd.truetripletwitch.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.tan

class TiltedRectangle(private val tiltAngle: Float = 0f): Shape {

    private fun drawTiltedRectanglePath(size: Size): Path {
        val xShift = tan(tiltAngle * PI.toFloat() / 180) * size.height
        return Path().apply {
            reset()
            moveTo(x = 0f, y = size.height)
            lineTo(x = xShift, y = 0f)
            lineTo(x = size.width, y = 0f)
            lineTo(x = size.width - xShift, y = size.height)
            lineTo(x = 0f, y = size.height)
        }
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(path = drawTiltedRectanglePath(size))
    }
}
