package com.example.webviewscreenshot.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.webviewscreenshot.R
import com.example.webviewscreenshot.domain.model.Content
import kotlinx.android.synthetic.main.content_item.view.*

//Make the class extend RecyclerView.ViewHolder, allowing the adapter to use it as as a ViewHolder
class ContentAdapter() : RecyclerView.Adapter<ContentAdapter.ContentHolder>() {
    lateinit var clickListener: AdapterActionListener
    var contents: ArrayList<Content> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentAdapter.ContentHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.content_item, parent, false)
        return ContentHolder(view!!)
    }

    override fun getItemCount() = contents.size

    override fun onBindViewHolder(holder: ContentAdapter.ContentHolder, position: Int) {
        holder.bindContent(contents[position], clickListener, position)
    }

    fun updateList(contentList: ArrayList<Content>) {
        contents = contentList

    }

    fun setItemClickListener(clickListener: AdapterActionListener) {
        this.clickListener = clickListener
    }

    fun removeItem(position: Int) {
        contents.removeAt(position)
        notifyItemRemoved(position)
    }

    class ContentHolder(v: View) : RecyclerView.ViewHolder(v) {
        //Add a reference to the view you’ve inflated to allow the ViewHolder to access the views as an extension property
        private var view: View = v
        private var content: Content? = null

        fun bindContent(
            content: Content,
            clickListener: AdapterActionListener,
            position: Int
        ) {
            this.content = content
            Glide.with(view.context).load(content.imageRef).into(view.item_image)
            view.item_time.text = content.timestamp.toString()
            view.item_url.text = content.url
            view.item_delete_button.setOnClickListener {
                clickListener.doWhenDeleteItemIsClicked(position, content)
            }
            view.item_image.setOnClickListener {
                clickListener.doWhenItemImageIsClicked(content)
            }
            view.item_url.setOnClickListener {
                clickListener.doWhenItemUrlIsClicked(content)
            }
        }
    }
}

interface AdapterActionListener {
    fun doWhenDeleteItemIsClicked(position: Int, content: Content)
    fun doWhenItemUrlIsClicked(content: Content)
    fun doWhenItemImageIsClicked(content: Content)
}

