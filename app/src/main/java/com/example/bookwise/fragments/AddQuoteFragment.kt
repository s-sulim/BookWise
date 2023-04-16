package com.example.bookwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bookwise.R
import com.example.bookwise.activities.BookDetailsActivity
import com.example.bookwise.classes.Quote

class AddQuoteFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val rootView: View = inflater.inflate(R.layout.add_quote_dialog, container, false)

        val btnCancel = rootView.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)
        val etQuote = rootView.findViewById<EditText>(R.id.etTheme)

        btnCancel.setOnClickListener{
            dismiss()
        }
        btnSubmit.setOnClickListener{
            if (etQuote.text.isNullOrEmpty()){
                Toast.makeText(context,"Please, enter your quote!", Toast.LENGTH_SHORT).show()
            }
            else{
                val quoteText:String = etQuote.text.toString()
                (activity as BookDetailsActivity?)?.addBookQuote(Quote(quoteText))
                Toast.makeText(context,"New quote added!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        return rootView
    }

}