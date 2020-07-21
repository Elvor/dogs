package org.elvor.dogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.elvor.dogs.databinding.FragmentBreedListBinding

class BreedListFragment : Fragment() {

    private lateinit var binding: FragmentBreedListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBreedListBinding.inflate(inflater, container, false)
        return binding.root
    }
}