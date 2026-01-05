package com.ezlevup.runningtracker.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ezlevup.runningtracker.data.local.RunEntity
import com.ezlevup.runningtracker.util.TrackingUtility

@Composable
fun HistoryScreen(viewModel: HistoryViewModel = hiltViewModel()) {
    val runs by viewModel.runsSortedByDate.observeAsState(listOf())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
                text = "운동 기록",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
        )

        if (runs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("아직 저장된 기록이 없습니다.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(runs) { run -> RunItem(run = run) }
            }
        }
    }
}

@Composable
fun RunItem(run: RunEntity) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                    text = TrackingUtility.formatDate(run.timestamp),
                    style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "거리", style = MaterialTheme.typography.labelMedium)
                    Text(
                            text = "${run.distanceInMeters / 1000f}km",
                            style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column {
                    Text(text = "시간", style = MaterialTheme.typography.labelMedium)
                    Text(
                            text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis),
                            style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column {
                    Text(text = "속도", style = MaterialTheme.typography.labelMedium)
                    Text(
                            text = "${run.avgSpeedInKMH}km/h",
                            style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
