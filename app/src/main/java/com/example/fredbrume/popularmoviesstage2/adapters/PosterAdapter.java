package com.example.fredbrume.popularmoviesstage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fredbrume.popularmoviesstage2.R;
import com.example.fredbrume.popularmoviesstage2.model.Poster;
import com.example.fredbrume.popularmoviesstage2.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by fredbrume on 8/26/17.
 */

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.PosterViewHolder> {

    public ArrayList posterList;
    private Context context;

    PosterAdapterOnClickHandler mClickHandler;


    public interface PosterAdapterOnClickHandler {

        void onClick(Poster mPosterDetails);
    }

    public PosterAdapter(PosterAdapterOnClickHandler mClickHandler)
    {
        this.mClickHandler=mClickHandler;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context=parent.getContext();
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.movie_list_item,parent,false);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {

        Poster moviePoster= (Poster) posterList.get(position);

        Picasso.with(context).load(NetworkUtils.buildPosterURL() + moviePoster.getPosterPath()).into(holder.posterView);
        holder.posterRating.setText(moviePoster.getPosterRating());

    }

    @Override
    public int getItemCount() {
        return posterList == null ? 0 : posterList.size();
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView posterView;
        public TextView posterRating;

        public PosterViewHolder(View itemView)
        {
            super(itemView);

            posterView= (ImageView) itemView.findViewById(R.id.movie_poster_id);
            posterRating= (TextView) itemView.findViewById(R.id.rating_value_id);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            Poster poster = (Poster) posterList.get(adapterPosition);
            mClickHandler.onClick(poster);
        }
    }

    public void setLayoutAdapter(ArrayList<Poster> posterList)
    {
        System.out.println("GOT HERE NOW");
        this.posterList=posterList;
        notifyDataSetChanged();
    }
}
