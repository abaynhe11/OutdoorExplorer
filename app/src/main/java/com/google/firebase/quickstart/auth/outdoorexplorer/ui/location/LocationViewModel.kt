package com.google.firebase.quickstart.auth.outdoorexplorer.ui.location

import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRepository
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomDatabase
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val outdoorRepository: OutdoorRepository

    init {
        val outdoorDao = OutdoorRoomDatabase.getInstance(application).outdoorDao()
        outdoorRepository = OutdoorRoomRepository(outdoorDao)
    }

    fun getLocation(locationId: Int) =
        outdoorRepository.getLocationWithActivities(locationId)
}