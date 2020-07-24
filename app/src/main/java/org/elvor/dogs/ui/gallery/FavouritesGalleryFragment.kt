package org.elvor.dogs.ui.gallery

import android.content.Context
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.db.AppDatabase
import javax.inject.Inject

class FavouritesGalleryFragment : BaseGalleryFragment() {

    @Inject
    lateinit var database: AppDatabase

    override fun getLikeImageDrawable(url: String): Int {
        return R.drawable.favourites_liked
    }

    override fun onLikeClick() {
        val url = items[currentPage]
        DogsApplication.mainScope.launch {
            withContext(Dispatchers.IO) {
                with(database.likedImageDao()) {
                    val likedImage = findByUrlAsync(url)
                        ?: throw IllegalStateException("liked image with url $url not found")
                    deleteAsync(likedImage)
                }
            }
            val newItems = items.filter { s -> s != url }
            items = newItems
            if (newItems.isEmpty()) {
                findNavController().navigateUp()
                return@launch
            }
            updateLikeDrawable()
        }
    }

    override suspend fun fetchImageUrls(): List<String> {
        return fetchLiked(database.likedImageDao())
    }

    override fun getBackLabel(): String = getString(R.string.favourites)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }
}