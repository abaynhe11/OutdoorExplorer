package com.google.firebase.quickstart.auth.outdoorexplorer.ui.locations

import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRepository
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomDatabase
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationsViewModel(application: Application) : AndroidViewModel(application) {
    private val outdoorRepository: OutdoorRepository

    init {
        val outdoorDao = OutdoorRoomDatabase.getInstance(application).outdoorDao()
        outdoorRepository = OutdoorRoomRepository(outdoorDao)
    }

    val allLocations = outdoorRepository.getAllLocations()

    fun locationsWithActivity(activityId: Int) =
        outdoorRepository.getActivityWithLocations(activityId)
}