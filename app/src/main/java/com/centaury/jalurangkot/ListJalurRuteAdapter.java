package com.centaury.jalurangkot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Centaury on 27/01/2018.
 */

public class ListJalurRuteAdapter extends RecyclerView.Adapter<ListJalurRuteAdapter.CategoryViewHolder> {

    private Context context;

    private ArrayList<JalurRute> listJalurRute;

    public ListJalurRuteAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<JalurRute> getListJalurRute(){
        return listJalurRute;
    }

    public void setListJalurRute(ArrayList<JalurRute> listJalurRute){
        this.listJalurRute = listJalurRute;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_jalurrute, parent, false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

        holder.tvName.setText(getListJalurRute().get(position).getName());

        Glide.with(context)
                .load(getListJalurRute().get(position).getPhoto())
                .into(holder.imgPhoto);

    }

    @Override
    public int getItemCount() {
        return getListJalurRute().size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        ImageView imgPhoto;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.txtname);
            imgPhoto = (ImageView) itemView.findViewById(R.id.imgphoto);
        }
    }
}
