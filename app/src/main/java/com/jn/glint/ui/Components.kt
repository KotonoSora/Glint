package com.jn.glint.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.MonetizationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.glint.ui.theme.NeonYellow
import com.jn.glint.ui.theme.PressStart2PFontFamily
import com.jn.glint.ui.theme.ShopButtonColor

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

/**
 * A reusable top bar for the Glint app.
 * Supports a back icon, a title that resizes to fit, and a coin display.
 */
@Composable
fun GlintTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    coins: Int? = null,
    onShopClicked: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBackClick != null) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
        }

        AutoResizeText(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        if (coins != null) {
            Spacer(modifier = Modifier.width(16.dp))

            CoinCapsule(coins = coins, onClick = onShopClicked)
        }

        if (onShopClicked != null) {
            IconButton(onClick = onShopClicked) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Shop",
                    tint = ShopButtonColor
                )
            }
        }
    }
}

/**
 * A text component that automatically reduces its font size to fit within [maxLines]
 * without overflowing.
 */
@Composable
fun AutoResizeText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = 2,
    minFontSize: TextUnit = 10.sp
) {
    var resizedTextStyle by remember { mutableStateOf(style) }
    var shouldDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw) drawContent()
        },
        softWrap = true,
        maxLines = maxLines,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowHeight) {
                if (resizedTextStyle.fontSize > minFontSize) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = resizedTextStyle.fontSize * 0.95f
                    )
                } else {
                    shouldDraw = true
                }
            } else {
                shouldDraw = true
            }
        }
    )
}

@Composable
fun CoinCapsule(
    coins: Int,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(NeonYellow.copy(alpha = 0.1f))
            .border(1.dp, NeonYellow.copy(alpha = 0.5f), RoundedCornerShape(50))
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                Icons.Rounded.MonetizationOn,
                contentDescription = null,
                tint = NeonYellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            NeonText(
                text = coins.toString(),
                color = NeonYellow,
                fontSize = 12,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    fontSize: Int = 16,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        fontFamily = PressStart2PFontFamily,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow
    )
}

