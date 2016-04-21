package jp.android.aakira.sample.expandablelayout.examplechanging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import jp.android.aakira.sample.expandablelayout.R;

public class ExampleChangingActivity extends AppCompatActivity implements View.OnClickListener {

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ExampleChangingActivity.class));
    }

    private Button mExpandButton;
    private ExpandableLinearLayout mExpandLayout;
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_changing);

        getSupportActionBar().setTitle(ExampleChangingActivity.class.getSimpleName());

        mExpandButton = (Button) findViewById(R.id.expandButton);
        mExpandLayout = (ExpandableLinearLayout) findViewById(R.id.expandableLayout);
        mText = (TextView) findViewById(R.id.text);
        mExpandButton.setOnClickListener(this);
        findViewById(R.id.addMoreButton).setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.expandButton:
                if (mExpandLayout.isExpanded()) {
                    mExpandLayout.collapse();
                } else {
                    mExpandLayout.expand();
                }
                break;
            case R.id.addMoreButton:
                mText.append("\nsample");
                mExpandLayout.initLayout(true);
                break;
        }
    }
}