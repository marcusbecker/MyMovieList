package br.com.mvbos.mml;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.mvbos.mml.data.Movie;

/**
 * Movie adapter view
 * Created by Marcus Becker on 03/01/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private String[] imagesPath;
    private Context context;
    private ClickListItemListener clickListItemListener;


    public interface ClickListItemListener {
        void onClickListItem(int clickedItemIndex);
    }


    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_list_item, parent, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Picasso.with(context).load(imagesPath[position]).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return imagesPath == null ? 0 : imagesPath.length;
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView movieImage;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            movieImage = (ImageView) itemView.findViewById(R.id.iv_movie_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListItemListener != null) {
                int position = getAdapterPosition();
                clickListItemListener.onClickListItem(position);
            }
        }
    }

    public void setImagesPath(String imageURL, Movie[] movies) {
        imagesPath = new String[movies.length];
        for (int i = 0; i < movies.length; i++) {
            imagesPath[i] = imageURL + movies[i].getImagePath();
        }

        notifyDataSetChanged();
    }


    public ClickListItemListener getClickListItemListener() {
        return clickListItemListener;
    }

    public void setClickListItemListener(ClickListItemListener clickListItemListener) {
        this.clickListItemListener = clickListItemListener;
    }
}
