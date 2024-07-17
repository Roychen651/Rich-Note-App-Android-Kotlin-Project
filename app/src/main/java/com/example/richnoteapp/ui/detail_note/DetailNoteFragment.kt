package com.example.richnoteapp.ui.detail_note

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentDetailNoteBinding
import com.example.richnoteapp.data.viewmodel.NotesViewModel

class DetailNoteFragment : Fragment() {
    private var _binding: FragmentDetailNoteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.link.visibility = View.GONE
        viewModel.chosenNote.observe(viewLifecycleOwner) {
            binding.noteTitle.text = it.title
            binding.noteContent.text = it.content
            Glide.with(requireContext()).load(it.photo).circleCrop().into(binding.noteImage)
            if (binding.noteTitle.text == "Address") {
                binding.noteContent.setTextColor(Color.BLUE)
                binding.noteContent.setTypeface(null, Typeface.ITALIC)
                Glide.with(requireContext()).load(R.drawable.img_1).circleCrop().into(binding.noteImage)
                binding.noteContent.setOnClickListener {
                    val query = binding.noteContent.text
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("geo:0,0?q=${Uri.encode(query.toString())}")
                    }
                    startActivity(intent)
                }
            }
            val noteContentText = binding.noteContent.text.toString()
            if (noteContentText.contains("youtube.com") || noteContentText.contains("youtu.be")) {
                binding.link.visibility = View.VISIBLE
                binding.noteImage.visibility = View.GONE
                setupWebView(noteContentText)
                binding.noteContent.visibility = View.GONE
            }
            if (noteContentText.contains("spotify.com")){
                binding.link.visibility = View.VISIBLE
                binding.noteImage.visibility = View.GONE
                setupSpotifyWebView(noteContentText)
                binding.noteContent.visibility = View.GONE
            }
        }

        binding.editNoteButton.setOnClickListener {
            findNavController().navigate(R.id.action_detailNoteFragment_to_editNoteFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupWebView(youtubeUrl: String) {
        binding.link.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        val webSettings: WebSettings = binding.link.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true // Enable DOM Storage for handling local storage

        val embedUrl = getEmbedUrl(youtubeUrl)
        binding.link.loadUrl(embedUrl)
    }

    private fun getEmbedUrl(youtubeUrl: String): String {
        val videoId = extractVideoId(youtubeUrl)
        return "https://www.youtube.com/embed/$videoId"
    }

    private fun extractVideoId(youtubeUrl: String): String {
        val regex = "(?<=v=|youtu.be/)([^&]*)".toRegex()
        val matchResult = regex.find(youtubeUrl)
        return matchResult?.value ?: youtubeUrl
    }


    private fun setupSpotifyWebView(spotifyUrl: String) {
        binding.link.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        val webSettings: WebSettings = binding.link.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true // Enable DOM Storage for handling local storage

        val embedUrl = getSpotifyEmbedUrl(spotifyUrl)
        binding.link.loadUrl(embedUrl)
    }

    private fun getSpotifyEmbedUrl(spotifyUrl: String): String {
        val regex = "(?<=spotify.com/)([^&]*)".toRegex()
        val matchResult = regex.find(spotifyUrl)
        val embedUrl = matchResult?.value?.let {
            "https://open.spotify.com/embed/$it"
        }
        return embedUrl ?: spotifyUrl
    }
}