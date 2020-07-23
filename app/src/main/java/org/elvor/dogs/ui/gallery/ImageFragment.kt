package org.elvor.dogs.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import org.elvor.dogs.R
import org.elvor.dogs.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private lateinit var imageUrl: String
    private lateinit var binding: FragmentImageBinding

    companion object {
        const val ARG_IMAGE_URL = "ARG_IMAGE_URL"

        fun newInstance(imageUrl: String): ImageFragment = ImageFragment()
            .apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_URL, imageUrl)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments ?: throw IllegalArgumentException("no arguments")
        with(args) {
            imageUrl =
                getString(ARG_IMAGE_URL) ?: throw IllegalArgumentException("no image URL argument")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        Glide.with(this).load(imageUrl).into(binding.image)
            .onLoadFailed(context?.getDrawable(R.drawable.image_error))
        return binding.root
    }

}