package com.sparkx.fairyos.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sparkx.fairyos.FairyForm
import com.sparkx.fairyos.palette
import com.sparkx.fairyos.domain.mood.SparkMood
import kotlin.math.*

@Composable
fun SparkBabyAvatar(
    modifier: Modifier = Modifier,
    mood: SparkMood,
    isSpeaking: Boolean,
    reactionKey: Int = 0,
    form: FairyForm = FairyForm.Androgynous,
    visualIntensity: Float = 1f,
    size: Dp = 220.dp
) {
    val fairyPalette = form.palette()

    val infiniteTransition = rememberInfiniteTransition(label = "fairy")

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    val speakingPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(if (isSpeaking) 160 else 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "speaking"
    )

    val blinkPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )

    val idleShimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "idleShimmer"
    )

    val isBlinking = blinkPhase > 0.93f && mood != SparkMood.ALERT && mood != SparkMood.SLEEPY

    val reactionScale by animateFloatAsState(
        targetValue = if (reactionKey % 2 == 0) 1f else 1.07f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "reaction"
    )

    Canvas(modifier = modifier.size(size)) {
        val canvasSize = this.size
        val cx = canvasSize.width / 2f
        val baseCy = canvasSize.height / 2f + 10f

        // Organic breathing + slight head tilt + body sway
        val breath = sin(phase * 0.85f) * 6.8f * visualIntensity
        val sway = sin(phase * 0.42f) * 3.4f
        val headTilt = sin(phase * 0.31f) * 0.04f
        val cy = baseCy + breath
        val s = canvasSize.minDimension * 0.36f * reactionScale

        // Soft multi-layer aura
        val auraBase = when {
            isSpeaking -> 0.42f
            mood == SparkMood.HAPPY -> 0.32f
            mood == SparkMood.ALERT -> 0.36f
            else -> 0.22f
        }
        for (layer in 0..2) {
            val falloff = 1f - layer * 0.28f
            drawCircle(
                color = fairyPalette.aura.copy(alpha = auraBase * falloff * visualIntensity),
                radius = s * (1.35f + layer * 0.22f) + sin(phase * 0.6f + layer) * 12f,
                center = Offset(cx + sway * 0.3f, cy)
            )
        }

        // Wings (layered membranes with subtle structure)
        val baseFlap = sin(phase * 0.55f) * 0.07f
        val activeFlap = if (isSpeaking || mood == SparkMood.HAPPY) sin(speakingPhase * 1.8f) * 0.28f else 0f
        val flap = baseFlap + activeFlap
        drawLivingWing(this, cx - s * 0.28f, cy - s * 0.05f, s, flap, left = true, palette = fairyPalette, phase = phase)
        drawLivingWing(this, cx + s * 0.28f, cy - s * 0.05f, s, flap, left = false, palette = fairyPalette, phase = phase)

        // Body with soft gradient falloff
        val bodyPath = Path().apply {
            moveTo(cx, cy - s * 0.18f)
            quadraticTo(cx - s * 0.32f, cy + s * 0.05f, cx - s * 0.18f, cy + s * 0.42f)
            quadraticTo(cx, cy + s * 0.55f, cx + s * 0.18f, cy + s * 0.42f)
            quadraticTo(cx + s * 0.32f, cy + s * 0.05f, cx, cy - s * 0.18f)
            close()
        }
        drawPath(bodyPath, brush = Brush.radialGradient(
            colors = listOf(fairyPalette.core.copy(alpha = 0.55f), fairyPalette.shadow.copy(alpha = 0.9f)),
            center = Offset(cx, cy + s * 0.08f),
            radius = s * 0.55f
        ))

        // Head with subtle tilt
        val headY = cy - s * 0.42f + sin(phase * 0.7f) * 1.5f
        val headCx = cx + sway * 0.4f
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFF1E4), Color(0xFFFFE0C8), Color(0xFFE8C8B0)),
                center = Offset(headCx - s * 0.04f, headY - s * 0.06f),
                radius = s * 0.42f
            ),
            radius = s * 0.34f,
            center = Offset(headCx, headY)
        )

        // Soft cheek blush
        drawCircle(
            color = Color(0xFFFFB8A0).copy(alpha = 0.28f),
            radius = s * 0.08f,
            center = Offset(headCx - s * 0.16f, headY + s * 0.06f)
        )
        drawCircle(
            color = Color(0xFFFFB8A0).copy(alpha = 0.28f),
            radius = s * 0.08f,
            center = Offset(headCx + s * 0.16f, headY + s * 0.06f)
        )

        // Crown / living halo
        val crownPulse = if (isSpeaking) 0.95f else 0.72f + sin(phase) * 0.12f
        drawCircle(
            color = fairyPalette.accent.copy(alpha = crownPulse),
            radius = s * 0.12f + sin(phase * 1.1f) * 2f,
            center = Offset(headCx, headY - s * 0.32f)
        )

        // Eyes
        val eyeY = headY - s * 0.04f
        val eyeDist = s * 0.15f
        val lookX = sin(phase * 0.22f) * s * 0.012f
        val lookY = cos(phase * 0.18f) * s * 0.008f

        when {
            mood == SparkMood.SLEEPY -> {
                drawLine(Color(0xFF2A1C14), Offset(headCx - eyeDist - s * 0.05f, eyeY), Offset(headCx - eyeDist + s * 0.09f, eyeY), strokeWidth = 4.5f)
                drawLine(Color(0xFF2A1C14), Offset(headCx + eyeDist - s * 0.09f, eyeY), Offset(headCx + eyeDist + s * 0.05f, eyeY), strokeWidth = 4.5f)
            }
            isBlinking -> {
                drawLine(Color(0xFF2A1C14), Offset(headCx - eyeDist - s * 0.04f, eyeY), Offset(headCx - eyeDist + s * 0.08f, eyeY), strokeWidth = 3.5f)
                drawLine(Color(0xFF2A1C14), Offset(headCx + eyeDist - s * 0.08f, eyeY), Offset(headCx + eyeDist + s * 0.04f, eyeY), strokeWidth = 3.5f)
            }
            else -> {
                val eyeR = if (mood == SparkMood.ALERT) s * 0.09f else s * 0.072f
                // Sclera
                drawCircle(Color(0xFFF8F4F0), radius = eyeR * 1.15f, center = Offset(headCx - eyeDist, eyeY))
                drawCircle(Color(0xFFF8F4F0), radius = eyeR * 1.15f, center = Offset(headCx + eyeDist, eyeY))
                // Iris
                drawCircle(Color(0xFF1A120C), radius = eyeR, center = Offset(headCx - eyeDist + lookX, eyeY + lookY))
                drawCircle(Color(0xFF1A120C), radius = eyeR, center = Offset(headCx + eyeDist + lookX, eyeY + lookY))
                // Catchlights
                drawCircle(Color.White.copy(alpha = 0.85f), radius = eyeR * 0.28f, center = Offset(headCx - eyeDist + lookX + eyeR * 0.22f, eyeY + lookY - eyeR * 0.22f))
                drawCircle(Color.White.copy(alpha = 0.85f), radius = eyeR * 0.28f, center = Offset(headCx + eyeDist + lookX + eyeR * 0.22f, eyeY + lookY - eyeR * 0.22f))
            }
        }

        // Mouth
        val mouthOpen = if (isSpeaking) {
            (sin(speakingPhase * 3.8f) * 0.55f + 0.5f).coerceIn(0.15f, 1f) * s * 0.1f
        } else {
            s * 0.018f + sin(phase * 0.4f) * s * 0.004f
        }
        drawOval(
            color = Color(0xFF6B3A3A),
            topLeft = Offset(headCx - s * 0.09f, headY + s * 0.12f - mouthOpen / 2),
            size = Size(s * 0.18f, mouthOpen)
        )

        // Living particles with varied size and soft trails
        val particleCount = when {
            isSpeaking -> 18
            mood == SparkMood.HAPPY -> 15
            mood == SparkMood.THINKING -> 12
            else -> 9
        }
        for (i in 0 until particleCount) {
            val speed = if (isSpeaking) 1.25f else 0.82f
            val ang = phase * speed + i * 0.88f
            val r = s * 0.68f + sin(phase + i * 1.4f) * 14f
            val px = cx + cos(ang) * r + sway * 0.2f
            val py = cy + sin(ang) * r * 0.68f
            val pSize = 2.4f + (i % 4) * 0.9f
            val alpha = when {
                isSpeaking -> 0.85f
                mood == SparkMood.HAPPY -> 0.78f
                else -> 0.48f + idleShimmer * 0.3f
            }
            drawCircle(
                fairyPalette.accent.copy(alpha = alpha * visualIntensity),
                radius = pSize,
                center = Offset(px, py)
            )
        }

        // Core pulse
        val corePulse = if (isSpeaking) {
            sin(speakingPhase * 3.6f) * 0.22f + 0.9f
        } else {
            sin(phase * 0.65f) * 0.14f + 0.86f
        }
        drawCircle(
            color = fairyPalette.core.copy(alpha = 0.55f * corePulse),
            radius = s * 0.14f * corePulse,
            center = Offset(cx, cy + s * 0.06f)
        )

        // Reaction burst
        if (reactionKey % 2 == 1) {
            drawCircle(
                fairyPalette.accent.copy(alpha = 0.35f),
                radius = s * 0.6f,
                center = Offset(cx, cy)
            )
        }
    }
}

private fun drawLivingWing(
    scope: DrawScope,
    x: Float,
    y: Float,
    s: Float,
    flap: Float,
    left: Boolean,
    palette: com.sparkx.fairyos.FairyPalette,
    phase: Float
) {
    val sign = if (left) -1f else 1f
    val micro = sin(phase * 1.4f) * 0.03f

    // Outer membrane
    val outer = Path().apply {
        moveTo(x, y)
        quadraticTo(
            x + sign * s * 0.72f, y - s * (0.48f + flap + micro),
            x + sign * s * 0.22f, y + s * 0.22f
        )
        quadraticTo(
            x + sign * s * 0.08f, y + s * 0.08f,
            x, y
        )
        close()
    }
    scope.drawPath(outer, color = palette.wingA.copy(alpha = 0.55f))

    // Inner membrane
    val inner = Path().apply {
        moveTo(x, y - s * 0.02f)
        quadraticTo(
            x + sign * s * 0.48f, y - s * (0.32f + flap * 0.7f),
            x + sign * s * 0.14f, y + s * 0.14f
        )
        close()
    }
    scope.drawPath(inner, color = palette.wingA.copy(alpha = 0.78f))

    // Subtle vein
    scope.drawLine(
        color = palette.accent.copy(alpha = 0.35f),
        start = Offset(x, y),
        end = Offset(x + sign * s * 0.42f, y - s * (0.28f + flap * 0.5f)),
        strokeWidth = 1.2f
    )
}
