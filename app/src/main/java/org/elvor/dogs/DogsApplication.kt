package org.elvor.dogs

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.elvor.dogs.di.AppComponent
import org.elvor.dogs.di.DaggerAppComponent

class DogsApplication : Application() {

    companion object {
        val mainScope = CoroutineScope(Dispatchers.Main)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}