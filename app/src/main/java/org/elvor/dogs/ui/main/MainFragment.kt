package org.elvor.dogs.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.elvor.dogs.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        const val ARG_CURRENT_TAB = "ARG_CURRENT_TAB"
        const val BREEDS_TAB = 0
        const val FAVOURITES_TAB = 1
    }

    private lateinit var binding: FragmentMainBinding
    private var currentTab =
        BREEDS_TAB

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(ARG_CURRENT_TAB, currentTab)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentTab = savedInstanceState?.getInt(ARG_CURRENT_TAB) ?: BREEDS_TAB
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.let {
            it.pager.adapter = MainViewPagerAdapter(this)
            it.favourites.isEnabled = false
            it.pager.registerOnPageChangeCallback(MainViewPagerChangeCallback { position ->
                when (position) {
                    BREEDS_TAB -> switchToBreeds()
                    FAVOURITES_TAB -> switchToFavourites()
                }
            })
            it.breeds.setOnClickListener {
                run {
//                    switchToBreeds()
                    binding.pager.setCurrentItem(BREEDS_TAB, false)
                }
            }
            it.favourites.setOnClickListener {
                run {
//                    switchToFavourites()
                    binding.pager.setCurrentItem(FAVOURITES_TAB, false)
                }
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.pager.setCurrentItem(currentTab, false)
    }

    private fun switchToBreeds() {
        currentTab = BREEDS_TAB
        binding.favourites.isEnabled = true
        binding.breeds.isEnabled = false
    }

    private fun switchToFavourites() {
        currentTab = FAVOURITES_TAB
        binding.favourites.isEnabled = false
        binding.breeds.isEnabled = true
    }
}