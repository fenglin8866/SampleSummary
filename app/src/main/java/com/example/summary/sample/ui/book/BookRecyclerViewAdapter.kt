package com.example.summary.sample.ui.book

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.summary.sample.vo.Book
import com.example.summary.databinding.ListItemBookBinding

class BookRecyclerViewAdapter(
    private val clickCallback: (Int) -> Unit
) : RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder>() {
    private var values: List<Book>? = null


    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Book>) {
        values = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)
        holder.bind(item, clickCallback)
    }

    override fun getItemCount(): Int = values?.size ?: 0

    inner class ViewHolder(private val binding: ListItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val idView: TextView = binding.itemNumber
        private val contentView: TextView = binding.content

        fun bind(book: Book?, clickCallback: (Int) -> Unit) {
            book?.let { mBook->
                idView.text = mBook.bookId.toString()
                contentView.text = mBook.bookName
                binding.root.setOnClickListener {
                    clickCallback(mBook.bookId)
                }
            }
        }


        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}