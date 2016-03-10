package com.namh.poddown

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by namh on 2016-03-10.
 */
class MyAdapter(private var _dataset: Array<String>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    // each data item is just a string in this case
    class ViewHolder(var mTextView: TextView) : RecyclerView.ViewHolder(mTextView)

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,
                parent, false) as TextView
        val vh = ViewHolder(v)
        return vh
    }// set the view's size, margins, paddings and layout parameters

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(_dataset[position])

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return _dataset.size
    }

    fun replaceDataset(dataset: Array<String>){
        _dataset = dataset
    }
}