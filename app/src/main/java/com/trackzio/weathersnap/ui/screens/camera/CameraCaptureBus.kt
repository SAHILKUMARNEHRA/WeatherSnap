package com.trackzio.weathersnap.ui.screens.camera

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object CameraCaptureBus {
    private val _captureRequests = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val captureRequests: SharedFlow<Unit> = _captureRequests

    fun requestCapture() {
        _captureRequests.tryEmit(Unit)
    }
}

