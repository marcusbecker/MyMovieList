package br.com.mvbos.mml.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.mvbos.mml.R;
import br.com.mvbos.mml.data.Trailer;

/**
 * Created by Marcus Becker on 20/01/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private Trailer[] trailers;
    private ClickListItemListener clickListItemListener;

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView name;
        private final TextView type;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tvTitle);
            type = (TextView) itemView.findViewById(R.id.tvDescription);

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
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list_item, parent, false);

        return new TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        holder.name.setText(trailers[position].getName());
        holder.type.setText(trailers[position].getType());
    }

    @Override
    public int getItemCount() {
        return trailers == null ? 0 : trailers.length;
    }

    public interface ClickListItemListener {
        void onClickListItem(int clickedItemIndex);
    }

    public Trailer[] getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailer[] trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public ClickListItemListener getClickListItemListener() {
        return clickListItemListener;
    }

    public void setClickListItemListener(ClickListItemListener clickListItemListener) {
        this.clickListItemListener = clickListItemListener;
    }
}
