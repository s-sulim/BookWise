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

class AddIdeaFragment : DialogFragment() {

    private lateinit var currentBookIdea:String
    private lateinit var btnCancel:Button
    private lateinit var btnSubmit: Button
    private lateinit var etIdea:EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        currentBookIdea = arguments?.getString("Current Book Idea")!!
        val rootView: View = inflater.inflate(R.layout.add_idea_dialog, container, false)
        btnCancel = rootView.findViewById(R.id.btnCancel)
        btnSubmit = rootView.findViewById(R.id.btnSubmit)
        etIdea = rootView.findViewById(R.id.etTheme)

        etIdea.setText(currentBookIdea)

        btnCancel.setOnClickListener{
            dismiss()
        }
        btnSubmit.setOnClickListener{
            if (etIdea.text.isNullOrEmpty()){
                Toast.makeText(context,"Please, enter the idea!", Toast.LENGTH_SHORT).show()
            }
            else{
                val idea = etIdea.text.toString()
                (activity as BookDetailsActivity?)?.setBookIdea(idea)
                Toast.makeText(context,"Idea set!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        return rootView
    }
}