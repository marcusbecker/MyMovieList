package br.com.mvbos.mml.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mvbos.mml.R;
import br.com.mvbos.mml.data.Review;

/**
 * Created by Marcus Becker on 20/01/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private Context context;
    private Review[] reviews;
    private ClickListItemListener clickListItemListener;

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView author;
        private final TextView content;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.tvAuthor);
            content = (TextView) itemView.findViewById(R.id.tvContent);

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

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, parent, false);

        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        holder.author.setText(reviews[position].getAuthor());
        holder.content.setText(reviews[position].getResumedContent());
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.length;
    }

    public interface ClickListItemListener {
        void onClickListItem(int clickedItemIndex);
    }

    public Review[] getReviews() {
        return reviews;
    }

    public void setReviews(Review[] reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public ClickListItemListener getClickListItemListener() {
        return clickListItemListener;
    }

    public void setClickListItemListener(ClickListItemListener clickListItemListener) {
        this.clickListItemListener = clickListItemListener;
    }
}
