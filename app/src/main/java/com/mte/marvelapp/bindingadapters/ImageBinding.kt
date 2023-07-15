package com.mte.marvelapp.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun loadImage(view: ImageView, url: String?) {
    if(url != null){
        Glide.with(view).load(url).into(view)
    }
}
