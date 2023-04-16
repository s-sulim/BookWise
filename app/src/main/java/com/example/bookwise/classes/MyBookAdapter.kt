package com.example.bookwise.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookwise.R

class MyBookAdapter(
    private val books: MutableList<Book>,
    private val clickListener:(Book) ->Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ITEM_VIEW_TYPE_READ = 1
        private const val ITEM_VIEW_TYPE_UNREAD = 2
    }
    inner class UnreadBookViewHolder(private val view: View):RecyclerView.ViewHolder(view){

        fun bind(book: Book, clickListener:(Book) ->Unit){
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvAuthor = view.findViewById<TextView>(R.id.tvAuthor)
            tvName.text = book.title
            tvAuthor.text = book.author
            view.setOnClickListener{
                clickListener(book)
            }
        }
    }
    inner class ReadBookViewHolder(private val view: View):RecyclerView.ViewHolder(view){

        fun bind(book: Book, clickListener:(Book) ->Unit){
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvAuthor = view.findViewById<TextView>(R.id.tvAuthor)
            tvName.text = book.title
            tvAuthor.text = book.author
            view.setOnClickListener{
                clickListener(book)
            }
        }
    }
    //Step 2 : depending on the item type we inflate different layouts
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_READ) {
            val listItem = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ReadBookViewHolder(listItem)
        } else {
            val listItem = LayoutInflater.from(parent.context).inflate(R.layout.list_item_to_read, parent, false)
            UnreadBookViewHolder(listItem)
        }
    }
    // Step 1 : setting the item type
    override fun getItemViewType(position: Int): Int {
        return if (books[position].isRead) ITEM_VIEW_TYPE_READ else ITEM_VIEW_TYPE_UNREAD
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val book = books[position]
        if (holder.itemViewType == ITEM_VIEW_TYPE_READ) {
            (holder as ReadBookViewHolder).bind(book, clickListener)
        } else {
            (holder as UnreadBookViewHolder).bind(book,clickListener)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}
