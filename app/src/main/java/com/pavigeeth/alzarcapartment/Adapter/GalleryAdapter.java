package com.pavigeeth.alzarcapartment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.pavigeeth.alzarcapartment.Activities.ViewImageActivity;
import com.pavigeeth.alzarcapartment.Model.ImagesDetail;
import com.pavigeeth.alzarcapartment.R;
import com.pavigeeth.alzarcapartment.ViewHolder.GalleryImagesViewHolder;

import java.util.List;

/**
 * Created by sehalsein on 20/12/17.
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ImagesDetail> imagesDetailList;
    private Context context;
    private String portal;
    private static final String IMAGE_KEY = "ImageURL";
    //private static final String PORTAL_KEY = "portal";


    public GalleryAdapter(List<ImagesDetail> imagesDetailList, Context context) {
        this.imagesDetailList = imagesDetailList;
        this.context = context;
    }

    public GalleryAdapter(List<ImagesDetail> imagesDetailList, Context context, String portal) {
        this.imagesDetailList = imagesDetailList;
        this.context = context;
        this.portal = portal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_image_view, parent, false);
        return new GalleryImagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ImagesDetail item = imagesDetailList.get(position);
        GalleryImagesViewHolder holder1 = (GalleryImagesViewHolder) holder;
        Glide.with(context).load(item.getImageURL()).fitCenter().crossFade(100).into(holder1.imageView);

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewImage(item.getImageURL());
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagesDetailList.size();
    }

    private void viewImage(String URL){
        Intent intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra(IMAGE_KEY, URL);
        context.startActivity(intent);
    }
}
