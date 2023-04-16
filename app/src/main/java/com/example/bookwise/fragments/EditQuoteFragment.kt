package com.example.bookwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.bookwise.R
import com.example.bookwise.activities.BookDetailsActivity
import com.example.bookwise.classes.Quote


class EditQuoteFragment : DialogFragment() {
    private var currentBookIndex:Int = 0
    private lateinit var currentQuoteText:String
    private  var currentQuoteID:Int= 0
    private  var currentQuoteBookID:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        currentBookIndex= arguments?.getInt("Current Book Index")!!
        currentQuoteText = arguments?.getString("Current Quote Text")!!
        currentQuoteID = arguments?.getInt("Current Quote ID")!!
        currentQuoteBookID = arguments?.getInt("Current Quote Book ID")!!
        val rootView: View = inflater.inflate(R.layout.edit_quote_dialog, container, false)
        val btnDelete = rootView.findViewById<Button>(R.id.btnDeleteQuote)
        val btnCancel = rootView.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)
        val etQuote = rootView.findViewById<EditText>(R.id.etTheme)
        val currentQuote = Quote(currentQuoteID,currentQuoteText,currentQuoteBookID)
        etQuote.setText(currentQuoteText)
        btnCancel.setOnClickListener{
            dismiss()
        }
        btnSubmit.setOnClickListener{
            if (etQuote.text.isNullOrEmpty()){
                Toast.makeText(context,"Please, enter your quote!", Toast.LENGTH_SHORT).show()
            }
            else{
                val newQuoteText:String = etQuote.text.toString()
                (activity as BookDetailsActivity?)?.editBookQuote(currentQuote,newQuoteText)
                dismiss()
            }
        }
        btnDelete.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete quote")
            builder.setMessage("Are you sure you want to delete this quote? This action cannot be undone.")
            builder.setPositiveButton("Yes") { _, _ ->
                (activity as BookDetailsActivity?)?.deleteBookQuote(currentQuote)
                dismiss()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        return rootView
    }


}