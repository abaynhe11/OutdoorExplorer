package com.google.firebase.quickstart.auth.outdoorexplorer.ui.map

import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRepository
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomDatabase
import com.google.firebase.quickstart.auth.outdoorexplorer.data.OutdoorRoomRepository
import android.app.Application
import androidx.lifecycle.AndroidViewModel


class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val outdoorRepository: OutdoorRepository

    init {
        val outdoorDao = OutdoorRoomDatabase.getInstance(application).outdoorDao()
        outdoorRepository = OutdoorRoomRepository(outdoorDao)
    }

    val allLocations = outdoorRepository.getAllLocations()
}