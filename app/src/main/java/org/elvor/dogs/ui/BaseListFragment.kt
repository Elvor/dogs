package org.elvor.dogs.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.elvor.dogs.R
import org.elvor.dogs.databinding.FragmentListBinding
import java.io.IOException

abstract class BaseListFragment<D, VH : RecyclerView.ViewHolder?, A> :
    Fragment() where A : RecyclerView.Adapter<VH>, A : ListAdapter<D> {

    protected lateinit var binding: FragmentListBinding
    protected lateinit var adapter: A

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!this::adapter.isInitialized) {
            adapter = createAdapter()
        }
        binding = FragmentListBinding.inflate(inflater, container, false)
        with(binding.list) {
            layoutManager = LinearLayoutManager(context)
            adapter = this@BaseListFragment.adapter
        }
        val backLabel = getBackLabel()
        if (backLabel != null) {
            with(binding.backText) {
                visibility = View.VISIBLE
                text = backLabel
                setOnClickListener { navigateBack() }
            }
            with(binding.backImage) {
                visibility = View.VISIBLE
                setOnClickListener { navigateBack() }
            }
        } else {
            binding.backText.visibility = View.GONE
            binding.backImage.visibility = View.GONE
        }

        return binding.root
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            showLoadingView()
            try {
                adapter.items = loadItems()
            } catch (e: IOException) {
                showError()
                adapter.items = emptyList()
            }
            hideLoadingView()
        }
    }


    abstract fun createAdapter(): A

    private fun showLoadingView() {
        binding.loadingScreen.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        binding.loadingScreen.visibility = View.GONE
    }

    private fun showError() {
        AlertDialog.Builder(activity)
            .setTitle(getString(R.string.network_error_title))
            .setMessage(getString(R.string.network_error_message))
            .setNeutralButton(R.string.network_error_ok) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    abstract suspend fun loadItems(): List<D>

    open fun getBackLabel(): String? = null
}