package org.elvor.dogs.ui.subbreed_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import org.elvor.dogs.databinding.FragmentSubbreedListBinding
import org.elvor.dogs.ui.gallery.GalleryFragment
import javax.inject.Inject

class SubbreedListFragment : Fragment() {

    private lateinit var breed: String
    private lateinit var binding: FragmentSubbreedListBinding

    companion object {
        const val ARG_BREED = "ARG_BREED"

        @JvmStatic
        fun newInstance(breed: String) = SubbreedListFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_BREED, breed)
            }
        }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubbreedListBinding.inflate(inflater, container, false)
        binding.title.text = breed
        with(binding.subbreeds) {
            layoutManager = LinearLayoutManager(context)
            adapter = SubbreedListAdapter{subbreed -> openImages(subbreed)}
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            val subbreeds = withContext(Dispatchers.IO) {
                return@withContext dogsService.listSubbreedsAsync(breed).await()
            }
            val subbreedData = subbreeds.message
            (binding.subbreeds.adapter as SubbreedListAdapter).setData(subbreedData)
        }
    }

    private fun openImages(subbreed: String) {
        findNavController().navigate(R.id.galleryFragment, Bundle().apply {
            putString(GalleryFragment.ARG_BREED, breed)
            putString(GalleryFragment.ARG_SUBBREED, subbreed)
            putString(GalleryFragment.ARG_TYPE, GalleryFragment.TYPE_IMAGES)
        })
    }

}