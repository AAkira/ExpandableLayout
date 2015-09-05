package jp.android.aakira.sample.expandablelayout.expandableweight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import jp.android.aakira.sample.expandablelayout.R;

public class ExpandableWeightActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExpandableWeightActivity.class));
    }

    private Button mExpandButton;
    private ExpandableWeightLayout mExpandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_weight);

        getSupportActionBar().setTitle(ExpandableWeightActivity.class.getSimpleName());

        mExpandButton = (Button) findViewById(R.id.expandButton);
        mExpandLayout = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);
        mExpandButton.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.expandButton:
                mExpandLayout.toggle();
                break;
        }
    }
}
