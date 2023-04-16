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

class AddThemeFragment : DialogFragment() {

    private lateinit var currentBookTheme:String
    private lateinit var etTheme:EditText
    private lateinit var btnCancel:Button
    private lateinit var btnSubmit:Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        currentBookTheme = arguments?.getString("Current Book Theme")!!

        val rootView: View = inflater.inflate(R.layout.add_theme_dialog, container, false)
        btnCancel = rootView.findViewById(R.id.btnCancel)
        btnSubmit = rootView.findViewById(R.id.btnSubmit)
        etTheme = rootView.findViewById(R.id.etTheme)

        etTheme.setText(currentBookTheme)

        btnCancel.setOnClickListener{
            dismiss()
        }
        btnSubmit.setOnClickListener{
            if (etTheme.text.isNullOrEmpty()){
                Toast.makeText(context,"Please, enter the theme!", Toast.LENGTH_SHORT).show()
            }
            else{
                val theme = etTheme.text.toString()
                (activity as BookDetailsActivity?)?.setBookTheme(theme)
                Toast.makeText(context,"Theme set!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        return rootView
    }
}