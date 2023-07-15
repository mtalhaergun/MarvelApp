package com.mte.marvelapp.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("downloadImage")
fun downloadImage(view: ImageView, url: String?) {
    if(url != null){
        Glide.with(view).load(url).into(view)
    }
}
