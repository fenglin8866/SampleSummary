package com.sample.core.basic.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.core.basic.R


class StringAdapter(private val dataSet: Array<String>) :
    RecyclerView.Adapter<StringAdapter.ViewHolder>() {

    private var callback: ((String) -> Unit)? = null

    private var callback2: ((Int, String) -> Unit)? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Define click listener for the ViewHolder's View
        val textView: TextView = view.findViewById(R.id.text)
    }

    fun setItemClickCallback(itemCallback: (String) -> Unit) {
        callback = itemCallback
    }

    fun setItemClickCallback2(itemCallback2: (Int, String) -> Unit) {
        callback2 = itemCallback2
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.text = dataSet[position]
        viewHolder.textView.setOnClickListener {
            callback?.let { it -> it(dataSet[position]) }
            callback2?.let { it -> it(position, dataSet[position]) }
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
