package com.example.richnoteapp.ui.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ViewModel(application: Application) : AndroidViewModel(application) {

    val address: LiveData<String> =
        com.example.richnoteapp.ui.location.LiveData(application.applicationContext)
}
