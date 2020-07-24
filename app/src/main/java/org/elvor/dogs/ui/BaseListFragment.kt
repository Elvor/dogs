package org.elvor.dogs.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.elvor.dogs.R
import org.elvor.dogs.databinding.FragmentListBinding
import org.elvor.dogs.ui.dialog.ErrorDialog
import java.io.IOException


abstract class BaseListFragment<D, VH : RecyclerView.ViewHolder?, A> :
    Fragment() where A : RecyclerView.Adapter<VH>, A : ListAdapter<D> {

    private lateinit var binding: FragmentListBinding
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
        binding.title.text = getTitle()
        with(binding.list) {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            adapter = this@BaseListFragment.adapter
            val dividerItemDecoration = DividerItemDecoration(
                context,
                manager.orientation
            )
            val dividerDrawable = context.getDrawable(R.drawable.divider)
            if (dividerDrawable != null) {
                dividerItemDecoration.setDrawable(dividerDrawable)
            }
            addItemDecoration(dividerItemDecoration)
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

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    abstract fun createAdapter(): A

    private fun showLoadingView() {
        binding.loadingScreen.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        binding.loadingScreen.visibility = View.GONE
    }

    private fun showError() {
        var dialog = ErrorDialog(requireContext())
        val window = dialog.window ?: return
        dialog.show()
        with(window) {
            setBackgroundDrawable(android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT))
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    abstract suspend fun loadItems(): List<D>

    abstract fun getTitle(): String

    open fun getBackLabel(): String? = null
}