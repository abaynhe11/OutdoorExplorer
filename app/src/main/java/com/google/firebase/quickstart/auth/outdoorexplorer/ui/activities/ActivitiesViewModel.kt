package com.google.firebase.quickstart.auth.outdoorexplorer.ui.activities

import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRepository
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomDatabase
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class ActivitiesViewModel(application: Application) : AndroidViewModel(application) {
    private val outdoorRepository: OutdoorRepository

    init {
        val outdoorDao = OutdoorRoomDatabase.getInstance(application).outdoorDao()
        outdoorRepository = OutdoorRoomRepository(outdoorDao)
    }

    val allActivities = outdoorRepository.getAllActivities()

    fun toggleGeofencing(id: Int) = outdoorRepository.toggleActivityGeofence(id)
}