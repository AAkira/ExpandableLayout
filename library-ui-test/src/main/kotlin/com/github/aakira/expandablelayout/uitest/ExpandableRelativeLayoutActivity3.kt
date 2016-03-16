package com.github.aakira.expandablelayout.uitest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import kotlin.properties.Delegates

class ExpandableRelativeLayoutActivity3 : AppCompatActivity() {

    companion object {

        @JvmStatic fun startActivity(context: Context) {
            context.startActivity(Intent(context, ExpandableRelativeLayoutActivity3::class.java))
        }
    }

    private var expandLayout: ExpandableRelativeLayout by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_relative_layout_3)
        supportActionBar?.title = ExpandableRelativeLayoutActivity3::class.java.simpleName

        expandLayout = findViewById(R.id.expandableLayout) as ExpandableRelativeLayout
        findViewById(R.id.expandButton)?.setOnClickListener { expandLayout.toggle() }
        findViewById(R.id.moveChildButton)?.setOnClickListener { expandLayout.moveChild(2) }
    }
}