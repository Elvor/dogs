package org.elvor.dogs.di

import dagger.Component
import org.elvor.dogs.ui.breed_list.BreedListFragment
import org.elvor.dogs.ui.gallery.GalleryFragment
import org.elvor.dogs.ui.subbreed_list.SubbreedListFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface AppComponent {
    fun inject(breedListFragment: BreedListFragment)
    fun inject(breedListFragment: SubbreedListFragment)
    fun inject(galleryFragment: GalleryFragment)
}