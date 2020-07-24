package org.elvor.dogs.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import org.elvor.dogs.databinding.DialogErrorBinding

class ErrorDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DialogErrorBinding.inflate(layoutInflater)
        binding.ok.setOnClickListener { cancel() }
        setContentView(binding.root)
    }
}