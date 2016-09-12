package com.github.aakira.expandablelayout.uitest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.aakira.expandablelayout.ExpandableLinearLayout
import rx.subscriptions.CompositeSubscription
import kotlin.properties.Delegates

/**
 * test for [com.github.aakira.expandablelayout.ExpandableLinearLayout#initlayout]
 *
 * The default value is  {@link android.view.animation.AccelerateDecelerateInterpolator}
 *
 */
class ExpandableLinearLayoutActivity3 : AppCompatActivity() {

    private var expandableLayout: ExpandableLinearLayout by Delegates.notNull()
    private val subscriptions: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expandable_linear_layout3)
        supportActionBar?.title = ExpandableLinearLayoutActivity::class.java.simpleName

        expandableLayout = findViewById(R.id.expandableLayout) as ExpandableLinearLayout
        findViewById(R.id.expandButton)?.setOnClickListener { expandableLayout.toggle() }
        findViewById(R.id.moveChildButton)?.setOnClickListener { expandableLayout.moveChild(0) }
        findViewById(R.id.moveChildButton2)?.setOnClickListener { expandableLayout.moveChild(1) }

        // uncomment if you want to check the #ExpandableLayout.initLayout()
//        val child1 = findViewById(R.id.child1) as TextView
//        subscriptions.add(Observable.timer(5, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    child1.text =
//                            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
//                                    "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
//                    expandableLayout.initLayout()
//                    expandableLayout.expand(0, null)
//                })
    }

    override fun onDestroy() {
        subscriptions.unsubscribe()
        super.onDestroy()
    }
}