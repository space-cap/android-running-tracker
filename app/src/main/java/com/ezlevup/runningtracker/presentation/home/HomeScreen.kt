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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel = koinViewModel()) {
    val context = LocalContext.current
    val isTracking by viewModel.isTracking.observeAsState(false)
    val pathPoints: com.ezlevup.runningtracker.data.service.Polylines by
            viewModel.pathPoints.observeAsState(mutableListOf())

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
            ) { _ -> }

    LaunchedEffect(Unit) { launcher.launch(permissionsToRequest) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            val lastLatLng = pathPoints.lastOrNull()?.lastOrNull() ?: LatLng(37.5665, 126.9780)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(lastLatLng, 15f)
            }

            GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(isMyLocationEnabled = true)
            ) {
                pathPoints.forEach { polyline ->
                    com.google.maps.android.compose.Polyline(
                            points = polyline,
                            color = androidx.compose.ui.graphics.Color.Red,
                            width = 10f
                    )
                }
            }
        }

        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                    onClick = {
                        if (isTracking) viewModel.pauseService(context)
                        else viewModel.startOrResumeService(context)
                    }
            ) { Text(if (isTracking) "일시정지" else "시작") }

            Button(
                    onClick = { viewModel.stopService(context) },
                    enabled = isTracking || pathPoints.isNotEmpty()
            ) { Text("종료") }
        }
    }
}
