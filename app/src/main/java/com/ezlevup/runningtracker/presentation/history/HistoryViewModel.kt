package com.ezlevup.runningtracker.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val runningRepository: RunningRepository) :
        ViewModel() {

    val runsSortedByDate = runningRepository.getAllRunsSortedByDate().asLiveData()

    // 필요한 경우 다른 정렬 방식이나 통계 데이터 추가 가능
}
