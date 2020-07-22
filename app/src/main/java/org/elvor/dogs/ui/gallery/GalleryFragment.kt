package org.elvor.dogs.ui.gallery

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.backend.DogsService
import org.elvor.dogs.databinding.FragmentGalleryBinding
import org.elvor.dogs.ui.breed_list.BreedListAdapter
import javax.inject.Inject

class GalleryFragment : Fragment() {
    private lateinit var breed: String
    private var subbreed: String? = null
    private lateinit var type: String
    private lateinit var binding: FragmentGalleryBinding

    companion object {
        const val ARG_BREED = "ARG_BREED"
        const val ARG_SUBBREED = "ARG_SUBBREED"
        const val ARG_TYPE = "ARG_TYPE"
        const val TYPE_FAVOURITES = "TYPE_FAVOURITES"
        const val TYPE_IMAGES = "TYPE_IMAGES"
    }

    @Inject
    lateinit var dogsService: DogsService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments ?: throw IllegalArgumentException("no arguments")
        with(args) {
            breed = getString(ARG_BREED) ?: throw java.lang.IllegalArgumentException("no breed argument provided")
            subbreed = getString(ARG_SUBBREED)
            type = getString(ARG_TYPE) ?: throw java.lang.IllegalArgumentException("no type argument provided")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        binding.title.text = if (subbreed != null) "$subbreed $breed".capitalize() else breed.capitalize()
        binding.pager.adapter = GalleryViewPagerAdapter(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (type == TYPE_IMAGES) {
            CoroutineScope(Dispatchers.Main).launch {
                var imageUrls = withContext(kotlinx.coroutines.Dispatchers.IO) {
                    return@withContext dogsService.getImagesForBreedAsync(breed).await()
                }.message
                if (subbreed != null) {
                    val subbreedName = "$breed-$subbreed"
                    imageUrls = imageUrls.filter { s ->
                        val split = s.split('/')
                        return@filter subbreedName == split[split.size - 2]
                    }
                }
                (binding.pager.adapter as GalleryViewPagerAdapter).setItems(imageUrls)
            }
        }
    }
}