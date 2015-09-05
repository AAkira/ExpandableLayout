package jp.android.aakira.sample.expandablelayout.examplerecyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import jp.android.aakira.sample.expandablelayout.util.DividerItemDecoration;
import jp.android.aakira.sample.expandablelayout.R;

public class RecyclerViewActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, RecyclerViewActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        getSupportActionBar().setTitle(RecyclerViewActivity.class.getSimpleName());

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Map<Integer, String> data = new HashMap<>();
        data.put(0, "ACCELERATE_DECELERATE_INTERPOLATOR");
        data.put(1, "ACCELERATE_INTERPOLATOR");
        data.put(2, "BOUNCE_INTERPOLATOR");
        data.put(3, "DECELERATE_INTERPOLATOR");
        data.put(4, "FAST_OUT_LINEAR_IN_INTERPOLATOR");
        data.put(5, "FAST_OUT_SLOW_IN_INTERPOLATOR");
        data.put(6, "LINEAR_INTERPOLATOR");
        data.put(7, "LINEAR_OUT_SLOW_IN_INTERPOLATOR");

        recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));
    }
}
