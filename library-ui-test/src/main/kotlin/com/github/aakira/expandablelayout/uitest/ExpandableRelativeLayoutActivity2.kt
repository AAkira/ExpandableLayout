package com.github.aakira.expandablelayout.uitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import kotlin.properties.Delegates

class ExpandableRelativeLayoutActivity2 : AppCompatActivity() {

    companion object {

        @JvmStatic fun startActivity(context: Context) {
            context.startActivity(Intent(context, ExpandableRelativeLayoutActivity2::class.java))
        }
    }

    private var expandableLayout: ExpandableRelativeLayout by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_relative_layout_2)
        supportActionBar?.title = ExpandableRelativeLayoutActivity2::class.java.simpleName

        expandableLayout = findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        findViewById(R.id.expandButton)?.setOnClickListener { expandableLayout.toggle() }
        findViewById(R.id.moveChildButton)?.setOnClickListener { expandableLayout.moveChild(1) }
    }
}