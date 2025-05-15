package com.airalo.example.offer.presentation.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Offset

internal fun Modifier.shadow(): Modifier {
    return drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            frameworkPaint.color = android.graphics.Color.TRANSPARENT

            frameworkPaint.setShadowLayer(
                Offset.dropShadow.radius.toPx(),
                Offset.dropShadow.x,
                Offset.dropShadow.y.toPx(),
                Color.shadowBlack,
            )

            canvas.drawRect(
                Rect(androidx.compose.ui.geometry.Offset.Zero, size),
                paint,
            )
        }
    }
}
