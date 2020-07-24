package org.elvor.dogs.ui.subbreed_list

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.backend.DogsService
import org.elvor.dogs.ui.BaseListFragment
import org.elvor.dogs.ui.gallery.BaseGalleryFragment
import javax.inject.Inject

class SubbreedListFragment :
    BaseListFragment<String, SubbreedListAdapter.ViewHolder, SubbreedListAdapter>() {

    private lateinit var breed: String

    companion object {
        const val ARG_BREED = "ARG_BREED"
    }

    @Inject
    lateinit var dogsService: DogsService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        breed = arguments?.getString(ARG_BREED) ?: throw Exception("no breed in args")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    private fun openImages(subbreed: String) {
        findNavController().navigate(R.id.galleryFragment, Bundle().apply {
            putString(BaseGalleryFragment.ARG_BREED, breed)
            putString(BaseGalleryFragment.ARG_SUBBREED, subbreed)
        })
    }

    override fun createAdapter(): SubbreedListAdapter =
        SubbreedListAdapter { subbreed -> openImages(subbreed) }

    override suspend fun loadItems(): List<String> {
        return withContext(Dispatchers.IO) {
            return@withContext dogsService.listSubbreedsAsync(breed).await()
        }.message
    }

    override fun getBackLabel(): String? = getString(R.string.breeds)
    override fun getTitle(): String = breed.capitalize()
}