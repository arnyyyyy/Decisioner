package com.decisioner.logics

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

class MainViewModel : ViewModel() {

    private lateinit var ruStoreAppUpdateManager: RuStoreAppUpdateManager

    private val _events = MutableSharedFlow<Event>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val installStateUpdateListener = InstallStateUpdateListener { installState ->
        when (installState.installStatus) {
            InstallStatus.DOWNLOADED -> {
                _events.tryEmit(Event.UpdateCompleted)
            }
            InstallStatus.DOWNLOADING -> {
                val totalBytes = installState.totalBytesToDownload
                val bytesDownloaded = installState.bytesDownloaded

            }
            InstallStatus.FAILED -> {
                Log.e(TAG, "Downloading error")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ruStoreAppUpdateManager.unregisterListener(installStateUpdateListener)
    }

    fun init(context: Context) {
        ruStoreAppUpdateManager = RuStoreAppUpdateManagerFactory.create(context)
        ruStoreAppUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
                    ruStoreAppUpdateManager.registerListener(installStateUpdateListener)
                    ruStoreAppUpdateManager
                        .startUpdateFlow(appUpdateInfo, AppUpdateOptions.Builder().build())
                        .addOnSuccessListener { resultCode ->
                            if (resultCode == Activity.RESULT_CANCELED) {
                            }
                        }
                        .addOnFailureListener { throwable ->
                            Log.e(TAG, "startUpdateFlow error", throwable)
                        }
                }
            }
            .addOnFailureListener { throwable ->
                Log.e(TAG, "getAppUpdateInfo error", throwable)
            }
    }

    fun completeUpdateRequested() {
        ruStoreAppUpdateManager.completeUpdate(AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE).build())
            .addOnFailureListener { throwable ->
                Log.e(TAG, "completeUpdate error", throwable)
            }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}

sealed class Event {
    object UpdateCompleted : Event()
}
