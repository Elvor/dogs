package org.elvor.dogs.ui.favourites

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.db.AppDatabase
import org.elvor.dogs.db.LikedImage
import org.elvor.dogs.ui.BaseListFragment
import org.elvor.dogs.ui.gallery.BaseGalleryFragment
import javax.inject.Inject

class FavoriteListFragment :
    BaseListFragment<FavouriteInfo, FavouriteListAdapter.ViewHolder, FavouriteListAdapter>() {
    override fun createAdapter() = FavouriteListAdapter { breed: String, subbreed: String? ->
        openFavouriteImages(
            breed,
            subbreed
        )
    }

    @Inject
    lateinit var database: AppDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    private fun openFavouriteImages(breed: String, subbreed: String?) {
        findNavController().navigate(R.id.favouritesGalleryFragment, Bundle().apply {
            putString(BaseGalleryFragment.ARG_BREED, breed)
            putString(BaseGalleryFragment.ARG_SUBBREED, subbreed)
        })
    }

    override suspend fun loadItems(): List<FavouriteInfo> {
        return withContext(Dispatchers.IO) {
            database.likedImageDao().listAllAsync()
                .groupingBy { likedImage: LikedImage ->
                    Pair(
                        likedImage.breed,
                        likedImage.subbreed
                    )
                }
                .eachCount()
                .map { entry -> FavouriteInfo(entry.key.first, entry.key.second, entry.value) }
        }
    }

    override fun getTitle(): String = getString(R.string.favourites)
}