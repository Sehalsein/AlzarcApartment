package com.pavigeeth.alzarcapartment.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.pavigeeth.alzarcapartment.R;

/**
 * Created by sehalsein on 20/12/17.
 */

public class GalleryImagesViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    private View row;

    public GalleryImagesViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image_rent_property_list_image_view);
        this.row = itemView;
    }

}