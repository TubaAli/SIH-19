package com.example.sih19;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jerry on 3/19/2018.
 */

public class CustomRecyclerViewDataAdapter extends Adapter<CustomRecyclerViewHolder> {

    private List<CustomRecyclerViewItem> viewItemList;
    private Context context;

    public CustomRecyclerViewDataAdapter(List<CustomRecyclerViewItem> viewItemList, Context context) {
        this.viewItemList = viewItemList;
        this.context = context;
    }

    @Override
    public CustomRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        View itemView = layoutInflater.inflate(R.layout.activity_custom_refresh_recycler_view_item, parent, false);
//        // Get car title text view object.
//        final TextView carTitleView = (TextView)carItemView.findViewById(R.id.card_view_image_title);
//        // Get car image view object.
//        final ImageView carImageView = (ImageView)carItemView.findViewById(R.id.card_view_image);
//        // When click the image.
//        carImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get car title text.
//                String carTitle = carTitleView.getText().toString();
//                // Create a snackbar and show it.
//                Snackbar snackbar = Snackbar.make(carImageView, "You click " + carTitle +" image", Snackbar.LENGTH_LONG);
//                snackbar.show();
//            }
//        });

        // Create and return our customRecycler View Holder object.
        CustomRecyclerViewHolder ret = new CustomRecyclerViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(final CustomRecyclerViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get car item dto in list.
            final CustomRecyclerViewItem viewItem = viewItemList.get(position);

            if(viewItem != null) {
                // Set car item title.
                holder.getTextView().setText(viewItem.getText());
                holder.getTextView().setBackgroundResource(viewItem.getBackgroundId());
                holder.getTextView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri uri = Uri.parse(viewItem.getNewsLink()); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }
}