package org.elvor.dogs.ui.breed_list

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
import org.elvor.dogs.ui.subbreed_list.SubbreedListFragment
import javax.inject.Inject

class BreedListFragment :
    BaseListFragment<BreedInfo, BreedListAdapter.ViewHolder, BreedListAdapter>() {

    @Inject
    lateinit var dogsService: DogsService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    private fun openSubbreeds(breed: String) {
        findNavController().navigate(
            R.id.subbreedListFragment,
            Bundle().apply { putString(SubbreedListFragment.ARG_BREED, breed) })
    }

    private fun openImages(breed: String) {
        findNavController().navigate(R.id.galleryFragment, Bundle().apply {
            putString(BaseGalleryFragment.ARG_BREED, breed)
        })
    }

    override fun createAdapter(): BreedListAdapter = BreedListAdapter(
        withSubbreedsClickListener = { breed -> openSubbreeds(breed) },
        withoutSubbreedsClickListener = { breed -> openImages(breed) }
    )

    override suspend fun loadItems(): List<BreedInfo> {
        val breeds = withContext(Dispatchers.IO) {
            return@withContext dogsService.listBreedsAsync().await()
        }
        return breeds.message.map { entry -> BreedInfo(entry.key, entry.value.size) }.toList()
    }

    override fun getTitle(): String = getString(R.string.breeds)
}