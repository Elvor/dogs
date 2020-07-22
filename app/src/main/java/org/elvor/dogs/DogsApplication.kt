package org.elvor.dogs

import android.app.Application
import org.elvor.dogs.di.AppComponent
import org.elvor.dogs.di.DaggerAppComponent

class DogsApplication : Application() {

    val appComponent: AppComponent by lazy { DaggerAppComponent.create() }
}