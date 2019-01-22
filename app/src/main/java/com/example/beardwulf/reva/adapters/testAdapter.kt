package com.example.beardwulf.reva.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.beardwulf.reva.domain.Category
import com.example.beardwulf.reva.R
import kotlinx.android.synthetic.main.category_row.view.*
import org.jetbrains.anko.backgroundColor

class testAdapter(private val cats : MutableList<String>, private val onClickListener: View.OnClickListener, private val myDataset: List<String>) :
        RecyclerView.Adapter<testAdapter.testAdapterViewHoder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    class testAdapterViewHoder(val textView: View) : RecyclerView.ViewHolder(textView) {}


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): com.example.beardwulf.reva.adapters.testAdapter.testAdapterViewHoder {
        // create a new view
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_row, parent, false)
            print(viewType)
        // set the view's size, margins, paddings and layout parameters

        return testAdapterViewHoder(textView)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: com.example.beardwulf.reva.adapters.testAdapter.testAdapterViewHoder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.textView.txtCategoryName.text = myDataset[position];

        with(holder.itemView) {

            tag=myDataset[position]
            if(cats.contains(tag as String)) {
                setBackgroundColor(resources.getColor(R.color.Groen))
            } else {
                setBackgroundColor(resources.getColor(R.color.Transparant))
            }
            setOnClickListener(onClickListener);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = myDataset.size
}