package jp.android.aakira.sample.expandablelayout.examplerecyclerview;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Map;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.Utils;
import jp.android.aakira.sample.expandablelayout.R;

public class RecyclerViewRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewRecyclerAdapter.ViewHolder> {

    private final Map<Integer, String> data;
    private Context context;
    private TimeInterpolator interpolator;

    public RecyclerViewRecyclerAdapter(final Map<Integer, String> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(data.get(position));

        int colorId1;
        int colorId2;
        switch (position) {
            case 0:
                colorId1 = R.color.material_red_500;
                colorId2 = R.color.material_red_300;
                interpolator = Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR);
                break;
            case 1:
                colorId1 = R.color.material_pink_500;
                colorId2 = R.color.material_pink_300;
                interpolator = Utils.createInterpolator(Utils.ACCELERATE_INTERPOLATOR);
                break;
            case 2:
                colorId1 = R.color.material_purple_500;
                colorId2 = R.color.material_purple_300;
                interpolator = Utils.createInterpolator(Utils.BOUNCE_INTERPOLATOR);
                break;
            case 3:
                colorId1 = R.color.material_deep_purple_500;
                colorId2 = R.color.material_deep_purple_300;
                interpolator = Utils.createInterpolator(Utils.DECELERATE_INTERPOLATOR);
                break;
            case 4:
                colorId1 = R.color.material_indigo_500;
                colorId2 = R.color.material_indigo_300;
                interpolator = Utils.createInterpolator(Utils.FAST_OUT_LINEAR_IN_INTERPOLATOR);
                break;
            case 5:
                colorId1 = R.color.material_blue_500;
                colorId2 = R.color.material_blue_300;
                interpolator = Utils.createInterpolator(Utils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                break;
            case 6:
                colorId1 = R.color.material_light_blue_500;
                colorId2 = R.color.material_light_blue_300;
                interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR);
                break;
            case 7:
                colorId1 = R.color.material_cyan_500;
                colorId2 = R.color.material_cyan_300;
                interpolator = Utils.createInterpolator(Utils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
                break;
            default:
                colorId1 = R.color.material_cyan_500;
                colorId2 = R.color.material_cyan_300;
                interpolator = Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR);
        }
        holder.itemView.setBackgroundColor(context.getResources().getColor(colorId1));
        holder.expandableLayout.setBackgroundColor(context.getResources().getColor(colorId2));
        holder.expandableLayout.setInterpolator(interpolator);
        holder.expandableLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onOpened() {
                holder.buttonLayout.setRotation(180);
            }

            @Override
            public void onClosed() {
                holder.buttonLayout.setRotation(0);
            }
        });

        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public RelativeLayout buttonLayout;
        public ExpandableRelativeLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.textView);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableRelativeLayout) v.findViewById(R.id.expandableLayout);
        }
    }
}
