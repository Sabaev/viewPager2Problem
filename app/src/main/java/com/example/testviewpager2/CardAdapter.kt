package com.example.testviewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

internal class CardAdapter : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    var items: List<TestData> = emptyList()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(Callback(old, value))
                .dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, color) = items[position]
        holder.name.text = name
        holder.background.setBackgroundColor(color)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].name.toLong()
    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.cardName)
        val background: View = itemView.findViewById(R.id.cardContainer)
    }

    private class Callback(
        private val oldItems: List<TestData>,
        private val newItems: List<TestData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].name == newItems[newItemPosition].name &&
                    oldItems[oldItemPosition]::class == newItems[newItemPosition]::class

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition] == newItems[newItemPosition]
    }


}
