package com.example.richnoteapp.ui.edit_note

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.richnoteapp.data.model.Note
import com.example.richnoteapp.data.viewmodel.NotesViewModel

class EditNoteFragment : Fragment() {

    private val viewModel: NotesViewModel by activityViewModels()
    private var note: Note? = null
    private var selectedImageUri: Uri = Uri.parse("android.resource://com.example.richnoteapp/drawable/logo")

    private lateinit var imageView: ImageView
    private lateinit var pickImageButton: Button
    private lateinit var saveButton: Button

    private val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            if (uri != null) {
                imageView.setImageURI(uri)
                requireActivity().contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                selectedImageUri = uri
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextTitle = view.findViewById<EditText>(R.id.edit_title)
        val editTextContent = view.findViewById<EditText>(R.id.edit_content)
        imageView = view.findViewById(R.id.imageView)
        pickImageButton = view.findViewById(R.id.edit_image)
        saveButton = view.findViewById(R.id.save)

        note = viewModel.selectedNote.value
        note?.let {
            editTextTitle.setText(it.title)
            editTextContent.setText(it.content)
            imageView.setImageURI(Uri.parse(it.photo))
        }

        pickImageButton.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }

        saveButton.setOnClickListener {
            val newTitle = editTextTitle.text.toString()
            val newContent = editTextContent.text.toString()

            if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                note?.title = newTitle
                note?.content = newContent
                note?.photo = selectedImageUri.toString()

                viewModel.updateNote(note!!)
                Toast.makeText(requireContext(), "Note updated", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
