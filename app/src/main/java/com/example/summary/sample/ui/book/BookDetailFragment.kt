package com.example.summary.sample.ui.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.summary.databinding.FragmentBookDetailBinding


@AndroidEntryPoint
class BookDetailFragment : Fragment() {

    private val viewModel: BookViewModel by activityViewModels()

    private lateinit var binding: FragmentBookDetailBinding

    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getInt("ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        id?.let {
            viewModel.getBookForId(it).observe(viewLifecycleOwner) { value ->
                binding.bookDetail.text = value.toString()
            }
        }
    }

}