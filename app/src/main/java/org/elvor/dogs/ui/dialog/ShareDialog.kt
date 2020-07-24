package org.elvor.dogs.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import org.elvor.dogs.databinding.DialogShareBinding

class ShareDialog(context: Context, private val onShare: () -> Unit) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogShareBinding.inflate(layoutInflater)
        binding.share.setOnClickListener { onShare(); cancel() }
        binding.cancel.setOnClickListener { cancel() }
        setContentView(binding.root)
    }
}