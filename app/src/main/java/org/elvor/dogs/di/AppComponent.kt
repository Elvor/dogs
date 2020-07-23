package org.elvor.dogs.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import org.elvor.dogs.db.AppDatabase
import org.elvor.dogs.ui.breed_list.BreedListFragment
import org.elvor.dogs.ui.favourites.FavoriteListFragment
import org.elvor.dogs.ui.gallery.FavouritesGalleryFragment
import org.elvor.dogs.ui.gallery.GalleryFragment
import org.elvor.dogs.ui.subbreed_list.SubbreedListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance appContext: Context) : AppComponent
    }

    fun appDatabase(): AppDatabase

    fun inject(breedListFragment: BreedListFragment)
    fun inject(breedListFragment: SubbreedListFragment)
    fun inject(galleryFragment: GalleryFragment)
    fun inject(favouritesGalleryFragment: FavouritesGalleryFragment)
    fun inject(favoriteListFragment: FavoriteListFragment)
}