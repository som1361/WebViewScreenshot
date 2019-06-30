package com.example.webviewscreenshot.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.domain.model.Content
import kotlinx.android.synthetic.main.content_item.view.*

//Make the class extend RecyclerView.ViewHolder, allowing the adapter to use it as as a ViewHolder
class ContentAdapter(val clickListener: ContentListener) : RecyclerView.Adapter<ContentAdapter.ContentHolder>() {
    var contents: List<Content> = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentAdapter.ContentHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.content_item, parent, false)
        return ContentHolder(view!!)
    }

    override fun getItemCount() = contents.size

    override fun onBindViewHolder(holder: ContentAdapter.ContentHolder, position: Int) {
        holder.bindContent(clickListener, contents[position])
    }

    fun updateList(contentList: List<Content>) {
        contents = contentList

    }

    class ContentHolder(v: View) : RecyclerView.ViewHolder(v) {
        //Add a reference to the view you’ve inflated to allow the ViewHolder to access the views as an extension property
        private var view: View = v
        private var content: Content? = null

        companion object {
            //Add a key for easy reference to the item launching the RecyclerView
            private val CONTENT_KEY = "CONTENT"
        }

        fun bindContent(clickListener: ContentListener, content: Content) {
            this.content = content
           // Glide.with(view.context).load(content.imageRef).into(view.itemImage)
            view.itemImage.text = content.imageRef
            view.itemTime.text = content.dateTime.toString()
            view.itemUrl.text = content.url
            view.setOnClickListener { clickListener.onClick(content) }
        }
    }
}
class ContentListener(val clickListener: (content: String) -> Unit) {
    fun onClick(content: Content) = clickListener(content.url.toString())
}
