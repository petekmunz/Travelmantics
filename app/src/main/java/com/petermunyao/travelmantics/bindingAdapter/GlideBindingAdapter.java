package com.petermunyao.travelmantics.bindingAdapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.petermunyao.travelmantics.R;

public class GlideBindingAdapter {
    @BindingAdapter("dealImage")
    public static void setImage(ImageView view, String imageUrl){

        Context context = view.getContext();

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);

        Glide.with(context)
                .setDefaultRequestOptions(options)
                .load(imageUrl)
                .centerCrop()
                .into(view);
    }
}
