package jp.android.aakira.sample.expandablelayout.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import jp.android.aakira.sample.expandablelayout.util.DividerItemDecoration;
import jp.android.aakira.sample.expandablelayout.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Map<Integer, String> data = new HashMap<>();
        data.put(0, "Normal\n[ExpandableRelativeLayout]");
        data.put(1, "Normal\n[ExpandableWeightLayout]");
        data.put(2, "Example(RecyclerView)\n[ExpandableRelativeLayout]");
        data.put(3, "Example(Search screen)\n[ExpandableRelativeLayout]");

        recyclerView.setAdapter(new MainRecyclerAdapter(this, data));
    }
}
