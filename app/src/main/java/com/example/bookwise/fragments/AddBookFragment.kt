package com.example.bookwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bookwise.R
import com.example.bookwise.activities.HomeActivity


class AddBookFragment :DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        val rootView: View = inflater.inflate(R.layout.add_book_dialog, container, false)

        val btnCancel = rootView.findViewById<Button>(R.id.btnCancel)
        val btnSubmit = rootView.findViewById<Button>(R.id.btnSubmit)

        val bookName = rootView.findViewById<EditText>(R.id.etName)
        val bookAuthor = rootView.findViewById<EditText>(R.id.etAuthor)
        val cbIsRead = rootView.findViewById<CheckBox>(R.id.cbIsRead)

        btnCancel.setOnClickListener{
            dismiss()
         }
        btnSubmit.setOnClickListener{
            if (bookName.text.isNullOrEmpty() || bookAuthor.text.isNullOrEmpty()){
                Toast.makeText(context,"Please, enter your data!",Toast.LENGTH_SHORT).show()
            }
            else{
                val isBookRead: Boolean = cbIsRead.isChecked
                val bookNameString: String = bookName.text.toString()
                val bookAuthorString: String = bookAuthor.text.toString()
                (activity as HomeActivity?)?.setBookInfo(bookAuthorString, bookNameString, isBookRead)
                Toast.makeText(context,"New book added!",Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        return rootView
    }
}