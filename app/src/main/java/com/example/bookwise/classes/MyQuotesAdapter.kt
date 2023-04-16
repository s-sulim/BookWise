package com.example.bookwise.classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookwise.R

class MyQuotesAdapter(private val quotes:MutableList<Quote>, private val clickListener:(Quote) ->Unit):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item_quote,parent,false)
        return MyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val quote =  quotes[position]
        holder.bind(quote, clickListener)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }
}

class MyViewHolder(private val view: View):RecyclerView.ViewHolder(view){

    fun bind(quote:Quote, clickListener:(Quote) ->Unit){
        val tvQuote = view.findViewById<TextView>(R.id.tvQuote)
        tvQuote.text = quote.text
        view.setOnClickListener{
            clickListener(quote)
        }
    }

}