package org.elvor.dogs.ui.breed_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.backend.DogsService
import org.elvor.dogs.ui.BaseListFragment
import org.elvor.dogs.ui.gallery.BaseGalleryFragment
import org.elvor.dogs.ui.subbreed_list.SubbreedListFragment
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject

class BreedListFragment : BaseListFragment<Pair<String, Int>, BreedListAdapter.ViewHolder, BreedListAdapter>() {

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

    override suspend fun loadItems(): List<Pair<String, Int>> {
        val breeds = withContext(Dispatchers.IO) {
            return@withContext dogsService.listBreedsAsync().await()
        }
        return breeds.message.map { entry -> Pair(entry.key, entry.value.size) }.toList()
    }
}