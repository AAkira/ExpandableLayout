package jp.android.aakira.sample.expandablelayout.examplesearch;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import jp.android.aakira.sample.expandablelayout.R;

public class ExampleSearchRecyclerAdapter
        extends RecyclerView.Adapter<ExampleSearchRecyclerAdapter.ViewHolder> {

    private final List<String> data;

    public ExampleSearchRecyclerAdapter(final List<String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }
}
