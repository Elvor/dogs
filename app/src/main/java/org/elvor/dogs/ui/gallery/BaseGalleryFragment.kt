package org.elvor.dogs.ui.gallery

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.databinding.FragmentGalleryBinding
import org.elvor.dogs.db.LikedImageDao
import org.elvor.dogs.ui.SimpleViewPagerChangeCallback
import org.elvor.dogs.ui.dialog.ShareDialog

abstract class BaseGalleryFragment : Fragment() {
    companion object {
        const val ARG_BREED = "ARG_BREED"
        const val ARG_SUBBREED = "ARG_SUBBREED"
        private const val PARAM_CURRENT_PAGE = "PARAM_CURRENT_PAGE"
    }

    protected var items: List<String>
        get() = galleryAdapter.items
        set(value) {
            if (currentPage >= value.size) {
                currentPage--
                binding.pager.currentItem = currentPage
            }
            galleryAdapter.items = value
        }

    protected lateinit var binding: FragmentGalleryBinding
        private set

    private lateinit var galleryAdapter: GalleryViewPagerAdapter

    protected lateinit var breed: String
        private set

    protected var subbreed: String? = null
        private set

    protected var currentPage = -1
        private set

    private val onPageChangeCallback = SimpleViewPagerChangeCallback { page ->
        currentPage = page
        if (items.isEmpty()) {
            return@SimpleViewPagerChangeCallback
        }
        updateLikeDrawable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments ?: throw IllegalArgumentException("no arguments")
        with(args) {
            breed = getString(ARG_BREED)
                ?: throw java.lang.IllegalArgumentException("no breed argument provided")
            subbreed = getString(ARG_SUBBREED)
        }
        currentPage = savedInstanceState?.getInt(PARAM_CURRENT_PAGE) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        binding.title.text =
            if (subbreed != null) "$subbreed $breed".capitalize() else breed.capitalize()
        galleryAdapter = GalleryViewPagerAdapter(this)
        binding.pager.adapter = galleryAdapter
        binding.like.setOnClickListener { onLikeClick() }
        with(binding.backText) {
            text = getBackLabel()
            setOnClickListener { navigateBack() }
        }
        binding.backImage.setOnClickListener { navigateBack() }
        binding.share.setOnClickListener {
            openShareDialog(
                breed,
                subbreed,
                galleryAdapter.items[currentPage]
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        DogsApplication.mainScope.launch {
            val imageUrls = fetchImageUrls()
            galleryAdapter.items = imageUrls
            postImageFetch()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.pager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onPause() {
        binding.pager.unregisterOnPageChangeCallback(onPageChangeCallback)
        super.onPause()
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    protected suspend fun fetchLiked(dao: LikedImageDao): List<String> {
        return withContext(kotlinx.coroutines.Dispatchers.IO) {
            val subBreedLocal = subbreed
            return@withContext (if (subBreedLocal == null) dao.listAllByBreedAsync(breed) else dao.listAllByBreedAsync(
                breed,
                subBreedLocal
            )).map { likedImage -> likedImage.url }
        }
    }

    protected fun updateLikeDrawable() {
        binding.like.setImageDrawable(context?.getDrawable(getLikeImageDrawable(galleryAdapter.items[currentPage])))
    }

    protected abstract fun getLikeImageDrawable(url: String): Int

    protected abstract fun onLikeClick()

    protected abstract suspend fun fetchImageUrls(): List<String>

    protected open suspend fun postImageFetch() {

    }

    protected abstract fun getBackLabel(): String

    private fun openShareDialog(breed: String, subbreed: String?, imageUrl: String) {
        val dialog = ShareDialog(requireContext()) { onShare(breed, subbreed, imageUrl) }
        val window = dialog.window ?: return
        dialog.show()
        with(window) {
            attributes.gravity = Gravity.BOTTOM
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

    }

    private fun onShare(breed: String, subbreed: String?, imageUrl: String) {
        Log.d("share", "shared ${subbreed ?: ""} $breed url: $imageUrl")
    }
}