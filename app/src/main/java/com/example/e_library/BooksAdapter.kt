package com.example.e_library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.content.Context

class BooksAdapter(private val context: Context, private val bookList: ArrayList<book>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return bookList.size;
    }

    override fun getItem(p0: Int): Any {
        return bookList[p0]
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.template_listview_books, p2, false)
        }

        val title = convertView!!.findViewById<TextView>(R.id.TextViewTitle)
        val year= convertView.findViewById<TextView>(R.id.TextViewYear)
        val genre = convertView.findViewById<TextView>(R.id.TextViewGenre)
        val description = convertView.findViewById<TextView>(R.id.TextViewDescription)

        val book = bookList[p0]
        title.text = book.title
        year.text = book.year.toString()
        genre.text = book.genre
        description.text = book.description

        return convertView

    }

}