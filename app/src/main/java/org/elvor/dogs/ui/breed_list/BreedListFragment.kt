package org.elvor.dogs.ui.breed_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.*
import org.elvor.dogs.DogsApplication
import org.elvor.dogs.R
import org.elvor.dogs.backend.DogsService
import org.elvor.dogs.databinding.FragmentBreedListBinding
import org.elvor.dogs.ui.gallery.GalleryFragment
import org.elvor.dogs.ui.gallery.ImageFragment
import org.elvor.dogs.ui.subbreed_list.SubbreedListFragment
import javax.inject.Inject

class BreedListFragment : Fragment() {

    private lateinit var binding: FragmentBreedListBinding

    @Inject
    lateinit var dogsService: DogsService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as DogsApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedListBinding.inflate(inflater, container, false)
        with(binding.breeds) {
            layoutManager = LinearLayoutManager(context)
            adapter = BreedListAdapter(
                withSubbreedsClickListener = {breed -> openSubbreeds(breed)} ,
                withoutSubbreedsClickListener = {breed -> openImages(breed)}
            )
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            val breeds = withContext(Dispatchers.IO) {
                return@withContext dogsService.listBreedsAsync().await()
            }
            val breedData = breeds.message.map { entry -> Pair(entry.key, entry.value.size) }.toList()
            (binding.breeds.adapter as BreedListAdapter).setData(breedData)
        }
    }

    private fun openSubbreeds(breed: String) {
        findNavController().navigate(R.id.subbreedListFragment, Bundle().apply { putString(SubbreedListFragment.ARG_BREED, breed) })
    }

    private fun openImages(breed: String) {
        findNavController().navigate(R.id.galleryFragment, Bundle().apply {
            putString(GalleryFragment.ARG_BREED, breed)
            putString(GalleryFragment.ARG_TYPE, GalleryFragment.TYPE_IMAGES)
        })
    }
}