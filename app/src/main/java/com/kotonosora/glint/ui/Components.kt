package com.kotonosora.glint.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, if (enabled) color else Color.Gray),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (enabled) color else Color.Gray
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun SmallNeonButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, if (enabled) color else Color.Gray),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (enabled) color else Color.Gray
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}
