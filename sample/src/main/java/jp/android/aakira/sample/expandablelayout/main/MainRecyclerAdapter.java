package jp.android.aakira.sample.expandablelayout.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import jp.android.aakira.sample.expandablelayout.expandablelayout.ExpandableLayoutActivity;
import jp.android.aakira.sample.expandablelayout.expandableweight.ExpandableWeightActivity;
import jp.android.aakira.sample.expandablelayout.R;
import jp.android.aakira.sample.expandablelayout.examplerecyclerview.RecyclerViewActivity;
import jp.android.aakira.sample.expandablelayout.examplesearch.ExampleSearchActivity;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {

    final Map<Integer, String> data;
    final Context context;

    public MainRecyclerAdapter(final Context context, final Map<Integer, String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (position) {
                    case 0:
                        ExpandableLayoutActivity.startActivity(context);
                        break;
                    case 1:
                        ExpandableWeightActivity.startActivity(context);
                        break;
                    case 2:
                        RecyclerViewActivity.startActivity(context);
                        break;
                    case 3:
                        ExampleSearchActivity.startActivity(context);
                        break;
                }
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
