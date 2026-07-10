package com.jn.glint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.glint.model.HistoryEntry
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.theme.CoinGold
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonMagenta

@Composable
fun LeaderboardScreen(
    entries: List<HistoryEntry>,
    onBackClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GlintTopBar(
            title = "My History",
            onBackClick = onBackClicked
        )

        HistoryList(entries)
    }
}

@Composable
fun HistoryList(entries: List<HistoryEntry>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "DATE & TIME",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1.5f)
                )
                Text(
                    "SCORE",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Text(
                    "REWARD",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }

        itemsIndexed(entries) { _, entry ->
            HistoryRow(entry = entry)
        }
    }
}

@Composable
fun HistoryRow(entry: HistoryEntry) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.dateTime,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.weight(1.5f)
            )

            Text(
                text = entry.score.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = NeonMagenta,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "+${entry.reward}",
                style = MaterialTheme.typography.titleMedium,
                color = CoinGold,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    val mockHistory = listOf(
        HistoryEntry("2024-07-07 14:30", 7200, 350),
        HistoryEntry("2024-07-06 18:15", 6800, 300),
        HistoryEntry("2024-07-05 09:45", 8100, 400),
        HistoryEntry("2024-07-04 21:00", 5500, 250)
    )
    GlintTheme {
        LeaderboardScreen(entries = mockHistory, onBackClicked = {})
    }
}
