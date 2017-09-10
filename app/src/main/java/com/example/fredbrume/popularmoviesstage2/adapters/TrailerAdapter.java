package com.example.fredbrume.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.model.Trailer;
import com.example.fredbrume.popularmoviesstage2.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private ArrayList mTrailerData;
    private Context context;
    TrailerAdapterOnClickHandler mClickHandler;


    public interface TrailerAdapterOnClickHandler {

        void onTrailerClick(Trailer mTrailerDetails);
    }

    public TrailerAdapter(TrailerAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        context=viewGroup.getContext();
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.trailer_list_item,viewGroup,false);
        return new TrailerViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final TrailerViewHolder viewHolder, int position) {

        Trailer trailer = (Trailer) mTrailerData.get(position);
        viewHolder.trailerType.setText(trailer.getTrailerType().toUpperCase());
        viewHolder.website.setText("Site - " + trailer.getWebsite());
        viewHolder.quality.setText("Quality - " + trailer.getQuality());

        Picasso.with(context).load(NetworkUtils.buildYoutubeThumbnailURL(trailer.getTrailerId()))
                .into(viewHolder.mImage);



    }

    @Override
    public int getItemCount() {
        return mTrailerData == null ? 0 : mTrailerData.size();

    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener

    {
        public ImageView mImage;
        public TextView trailerType;
        public TextView website;
        public TextView quality;


        public TrailerViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.trailer_view);
            trailerType= (TextView) itemView.findViewById(R.id.trailer_type);
            website= (TextView) itemView.findViewById(R.id.trailer_site);
            quality= (TextView) itemView.findViewById(R.id.trailer_pixel);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            Trailer trailer = (Trailer) mTrailerData.get(adapterPosition);
            mClickHandler.onTrailerClick(trailer);
        }
    }

    public void setTrailerData(ArrayList<Trailer> mTrailerDataList) {

        this.mTrailerData = mTrailerDataList;
        notifyDataSetChanged();
    }
}


