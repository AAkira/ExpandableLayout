package jp.android.aakira.sample.expandablelayout.examplesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import jp.android.aakira.sample.expandablelayout.util.DividerItemDecoration;
import jp.android.aakira.sample.expandablelayout.R;

public class ExampleSearchActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExampleSearchActivity.class));
    }

    private Button mExpandButton;
    private ExpandableRelativeLayout mExpandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_search);

        getSupportActionBar().setTitle(ExampleSearchActivity.class.getSimpleName());

        mExpandButton = (Button) findViewById(R.id.expandButton);
        mExpandLayout = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        mExpandButton.setOnClickListener(this);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList data = new ArrayList();
        for(int i = 0; i < 15; i++) {
            data.add("Result");
        }
        recyclerView.setAdapter(new ExampleSearchRecyclerAdapter(data));
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.expandButton:
                mExpandLayout.expand();
                break;
        }
    }
}
