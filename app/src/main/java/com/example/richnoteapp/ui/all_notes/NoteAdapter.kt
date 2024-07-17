package com.example.richnoteapp.ui.all_notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.NoteLayoutBinding
import com.example.richnoteapp.data.model.Note

class NoteAdapter(private val notes: List<Note>, private val callBack: NoteListener)
    : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    interface NoteListener {
        fun onNoteClicked(index: Int)
        fun onNoteLongClicked(index: Int)
    }

    inner class NoteViewHolder(private val binding: NoteLayoutBinding)
        : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callBack.onNoteClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callBack.onNoteLongClicked(adapterPosition)
            return true
        }

        fun bind(note: Note) {
            binding.note = note
            Glide.with(binding.root).load(note.photo).circleCrop().into(binding.noteImage)
            when {
                note.content.contains("youtube.com", true) -> {
                    Glide.with(binding.root)
                        .load(R.drawable.img_2)
                        .circleCrop()
                        .into(binding.noteImage)
                }
                note.content.contains("youtu.be", true) -> {
                    Glide.with(binding.root)
                        .load(R.drawable.img_2)
                        .circleCrop()
                        .into(binding.noteImage)
                }
                note.content.contains("spotify.com", true) -> {
                    Glide.with(binding.root)
                        .load(R.drawable.img_3)
                        .circleCrop()
                        .into(binding.noteImage)
                }
                note.title.contains("Address", true) -> {
                    Glide.with(binding.root)
                        .load(R.drawable.img_1)
                        .circleCrop()
                        .into(binding.noteImage)
                }
            }
        }
    }

    fun noteAt(position: Int) = notes[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(notes[position])

    override fun getItemCount() = notes.size
}
