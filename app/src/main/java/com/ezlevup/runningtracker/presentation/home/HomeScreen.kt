package com.ezlevup.runningtracker.presentation.home

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val isTracking by viewModel.isTracking.observeAsState(false)
    val pathPoints by viewModel.pathPoints.observeAsState(mutableListOf())

    val permissionsToRequest =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            } else {
                arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
            }

    val launcher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // 권한 결과 처리 (생략 가능, 실제 위치 수집 시점에서 서비스가 처리)
            }

    LaunchedEffect(Unit) { launcher.launch(permissionsToRequest) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            val lastLatLng =
                    pathPoints.lastOrNull()?.lastOrNull() ?: LatLng(37.5665, 126.9780) // 서울 기본
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(lastLatLng, 15f)
            }

            GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true)
            ) { pathPoints.forEach { polyline -> Polyline(points = polyline) } }
        }

        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                    onClick = {
                        if (isTracking) {
                            viewModel.pauseService(context)
                        } else {
                            viewModel.startOrResumeService(context)
                        }
                    }
            ) { Text(if (isTracking) "일시정지" else "시작") }

            Button(
                    onClick = { viewModel.stopService(context) },
                    enabled = isTracking || pathPoints.isNotEmpty()
            ) { Text("종료") }
        }
    }
}
