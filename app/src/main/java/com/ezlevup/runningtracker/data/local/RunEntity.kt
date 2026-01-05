package com.ezlevup.runningtracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class RunEntity(
    var img: String? = null, // 운동 경로 이미지 (추후 필요시)
    var timestamp: Long = 0L, // 운동 일시
    var avgSpeedInKMH: Float = 0f, // 평균 속도
    var distanceInMeters: Int = 0, // 이동 거리
    var timeInMillis: Long = 0L, // 운동 시간
    var caloriesBurned: Int = 0 // 소모 칼로리
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
