package com.example.richnoteapp.ui.all_notes

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAllNotesBinding
import com.example.richnoteapp.data.viewmodel.NotesViewModel

class AllNotesFragment : Fragment() {

    private var _binding: FragmentAllNotesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentAllNotesBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_allNotesFragment_to_addNoteFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateButton.setOnClickListener {
            findNavController().navigate(R.id.action_allNotesFragment_to_mapFragment)
        }

        viewModel.notes?.observe(viewLifecycleOwner) {
            binding.recycler.adapter = NoteAdapter(it, object : NoteAdapter.NoteListener {
                override fun onNoteClicked(index: Int) {
                    val note = (binding.recycler.adapter as NoteAdapter).noteAt(index)
                    viewModel.setNote(note)
                    findNavController().navigate(R.id.action_allNotesFragment_to_detailNoteFragment)
                }

                override fun onNoteLongClicked(index: Int) {
                    val note = (binding.recycler.adapter as NoteAdapter).noteAt(index)
                    viewModel.setNote(note)
                    findNavController().navigate(R.id.action_allNotesFragment_to_editNoteFragment)
                }
            })
            binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = (binding.recycler.adapter as NoteAdapter).noteAt(viewHolder.adapterPosition)

                val builder = AlertDialog.Builder(viewHolder.itemView.context)
                builder.setTitle(viewHolder.itemView.context.getString(R.string.confirm_delete))
                    .setMessage(viewHolder.itemView.context.getString(R.string.are_you_sure_you_want_to_delete_this_note))
                    .setPositiveButton(viewHolder.itemView.context.getString(R.string.yes)) { _, _ ->
                        viewModel.deleteNote(note)
                        Toast.makeText(requireContext(), getString(R.string.note_deleted_success), Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(viewHolder.itemView.context.getString(R.string.no)) { dialog, _ ->
                        dialog.dismiss()
                        binding.recycler.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                    }

                val dialog = builder.create()
                dialog.setOnCancelListener {
                    binding.recycler.adapter!!.notifyItemChanged(viewHolder.adapterPosition)
                }
                dialog.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_delete) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.confirm_delete))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_all_notes))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteAll()
                    Toast.makeText(requireContext(), getString(R.string.notes_deleted), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
