package com.example.menzago.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.menzago.ui.theme.ClosedRed
import com.example.menzago.ui.theme.OpenGreen

@Composable
fun StatusBadge(
    isOpen: Boolean
) {
    val backgroundColor = if (isOpen) OpenGreen.copy(alpha = 0.12f) else ClosedRed.copy(alpha = 0.12f)
    val textColor = if (isOpen) OpenGreen else ClosedRed

    Text(
        text = if (isOpen) "Otvoreno" else "Zatvoreno",
        color = textColor,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    )
}