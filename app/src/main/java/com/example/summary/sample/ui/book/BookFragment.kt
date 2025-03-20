package com.example.summary.sample.ui.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.summary.databinding.FragmentBookListBinding
import com.example.summary.sample.vo.Book
import dagger.hilt.android.AndroidEntryPoint
import com.example.summary.R

@AndroidEntryPoint
class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookListBinding

    private val viewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(context)
        val adapter = BookRecyclerViewAdapter { id ->
            val bundle = bundleOf("ID" to id)
            findNavController().navigate(
                R.id.action_bookFragment_to_bookDetailFragment,
                bundle
            )
        }
        binding.apply {
            bookList.layoutManager = linearLayoutManager
            bookList.adapter = adapter
            add.setOnClickListener {
                viewModel.addBook(Book(bookName = textEdit.text.toString()))
            }
            search.setOnClickListener {
                viewModel.getBookForName(textEdit.text.toString())
                    .observe(viewLifecycleOwner) { value ->
                        adapter.setData(value)
                    }
            }

        }

        viewModel.books.observe(viewLifecycleOwner) { value ->
            adapter.setData(value)
        }

    }

}