package org.elvor.dogs.ui.gallery

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.backend.DogsService
import org.elvor.dogs.db.AppDatabase
import org.elvor.dogs.db.LikedImage
import javax.inject.Inject

class GalleryFragment : BaseGalleryFragment() {
    private val liked = mutableSetOf<String>()

    @Inject
    lateinit var dogsService: DogsService

    @Inject
    lateinit var database: AppDatabase

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    override fun getLikeImageDrawable(url: String): Int {
        return if (liked.contains(url)) R.drawable.favourites_liked else R.drawable.favourites
    }

    override fun onLikeClick() {
        val url = items[currentPage]
        DogsApplication.mainScope.launch {
            withContext(Dispatchers.IO) {
                with(database.likedImageDao()) {
                    val likedImage = findByUrlAsync(url)
                    if (likedImage == null) {
                        insertAsync(LikedImage(url, breed, subbreed))
                        liked.add(url)
                    } else {
                        deleteAsync(likedImage)
                        liked.remove(url)
                    }
                }
            }
            updateLikeDrawable()
        }
    }

    override suspend fun fetchImageUrls(): List<String> {
        var imageUrls = withContext(Dispatchers.IO) {
            return@withContext dogsService.getImagesForBreedAsync(breed).await()
        }.message

        if (subbreed != null) {
            val subbreedName = "$breed-$subbreed"
            imageUrls = imageUrls.filter { s ->
                val split = s.split('/')
                return@filter subbreedName == split[split.size - 2]
            }
        }
        return imageUrls
    }

    override suspend fun postImageFetch() {
        liked.clear()
        liked.addAll(fetchLiked(database.likedImageDao()))
        updateLikeDrawable()
    }

    override fun getBackLabel(): String = getString(R.string.back)
}