package com.ezlevup.runningtracker.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(repository: RunningRepository) : ViewModel() {
    val runs =
            repository
                    .getAllRunsSortedByDate()
                    .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

@Composable
fun HistoryScreen(
// Koin 모듈에 추가 등록이 필요하지만, 여기서는 직접 주입 구조를 보여줍니다.
// (실제로는 AppModule에 HistoryViewModel도 등록해야 함)
) {
    // 임시로 MainViewModel이나 별도 ViewModel을 사용할 수 있으나 구조를 위해 placeholder만 둡니다.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("기록 리스트 (개발 예정)")
    }
}
