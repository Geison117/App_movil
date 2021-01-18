package com.example.redesaplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.redesaplication.Interface.ItemClickListener;
import com.example.redesaplication.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtFoodName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;


    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFoodName = (TextView) itemView.findViewById(R.id.food_name);
        imageView = (ImageView)  itemView.findViewById(R.id.food_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
