# Expandable Layout

An android library that brings the expandable layout with various animation.
You can include optional contents and use everywhere.

## Preview

### Normal

![ExpandableRelativeLayout][ExpandableRelativeLayout] ![ExpandableWeightLayout][ExpandableWeightLayout]

### Example

![ExampleRecyclerView][ExampleRecyclerView] ![ExampleSearch][ExampleSearch]

## Usage

### ExpandableRelativeLayout

#### Code

```java
ExpandableRelativeLayout expandLayout
 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);

// toggle expand, collapse
expandableLayout.toggle();
// expand
expandableLayout.expand();
// collapse
expandableLayout.collapse();

// move position of child view
expandableLayout.moveChild(0);
// move optional position
expandableLayout.move(500);

// set base position which is close position
expandableLayout.setClosePosition(500);
```

#### Layout xml

add `xmlns:app="http://schemas.android.com/apk/res-auto"`

```xml
<com.github.aakira.expandablelayout.ExpandableRelativeLayout
    android:id="@+id/expandableLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultVisibility="false"
    app:duration="500"
    app:interpolator="bounce"
    app:orientation="vertical">

    <TextView
        android:id="@+id/text"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="sample" />
    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/text"
        android:text="sample2" />
</com.github.aakira.expandablelayout.ExpandableRelativeLayout>
```

### ExpandableWeightLayout

You should use this layout if you want to use weight attributes at expandable layout.

#### Code

```java
ExpandableWeightLayout expandLayout
 = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);

// toggle expand, collapse
expandableLayout.toggle();
// expand
expandableLayout.expand();
// collapse
expandableLayout.collapse();
```

#### Layout xml

add `xmlns:app="http://schemas.android.com/apk/res-auto"`

```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <View
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>

    <com.github.aakira.expandablelayout.ExpandableWeightLayout
        android:id="@+id/expandableLayout"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="3"
        app:duration="1000"
        app:interpolator="anticipateOvershoot">

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            android:src="@drawable/sample" />
    </com.github.aakira.expandablelayout.ExpandableWeightLayout>

    <View
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"/>
</LinearLayout>
```

### Listener

```java

expandableLayout.setListener(new ExpandableLayoutListener() {
    @Override
    public void onAnimationStart() {

    }

    @Override
    public void onAnimationEnd() {

    }

    @Override
    public void onOpened() {

    }

    @Override
    public void onClosed() {

    }
});
```

### Attributes

|attribute name|description|
|:-:|:-:|
|duration|The length of the expand or collapse animation|
|defaultVisibility|The layout is expanded at first if you set true|
|orientation|The orientation of animation(horizontal \| vertical)|
|interpolator|Sets [interpolator](#interpolator)|

### Interpolator

You can use [interpolator](http://developer.android.com/reference/android/view/animation/Interpolator.html).
It helps the layout animates easily.

|Interpolator|attribute name|
|:-:|:-:|
|[AccelerateDecelerateInterpolator](http://developer.android.com/reference/android/view/animation/AccelerateDecelerateInterpolator.html)|accelerateDecelerateInterpolator|
|[AccelerateInterpolator](http://developer.android.com/reference/android/view/animation/AccelerateInterpolator.html)|accelerateInterpolator|
|[AnticipateInterpolator](http://developer.android.com/reference/android/view/animation/AnticipateInterpolator.html)|anticipateInterpolator|
|[AnticipateOvershootInterpolator](http://developer.android.com/reference/android/view/animation/AnticipateOvershootInterpolator.html)|anticipateOvershootInterpolator|
|[BounceInterpolator](http://developer.android.com/reference/android/view/animation/BounceInterpolator.html)|bounceInterpolator|
|[DecelerateInterpolator](http://developer.android.com/reference/android/view/animation/DecelerateInterpolator.html)|decelerateInterpolator|
|[FastOutLinearInInterpolator](http://developer.android.com/reference/android/support/v4/view/animation/FastOutLinearInInterpolator.html)|fastOutLinearInInterpolator|
|[FastOutSlowInInterpolator](http://developer.android.com/reference/android/support/v4/view/animation/FastOutSlowInInterpolator.html)|fastOutSlowInInterpolator|
|[LinearInterpolator](http://developer.android.com/reference/android/view/animation/LinearInterpolator.html)|linearInterpolator|
|[LinearOutSlowInInterpolator](http://developer.android.com/reference/android/support/v4/view/animation/LinearOutSlowInInterpolator.html)|linearOutSlowInInterpolator|
|[OvershootInterpolator](http://developer.android.com/reference/android/view/animation/OvershootInterpolator.html)|overshootInterpolator|

These are support interpolator.
But a case that the expandable layout extends outside doesn't work.
e.g. AnticipateInterpolator, AnticipateOvershootInterpolator, OvershootInterpolator
I recommend you use such a interpolator for child views in the expandable layout.

* Not support
 - CycleInterpolator
 - PathInterpolator

## Setup

### Gradle

Add the dependency in your `build.gradle`

```groovy
buildscript {
	repositories {
		jcenter()
	}
}

dependencies {
	compile 'com.github.aakira:expandable-layout:1.0.0@aar'
}
```

## License

```
Copyright (C) 2015 A.Akira

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[ExpandableRelativeLayout]: /art/ExpandableRelativeLayout.gif
[ExpandableWeightLayout]: /art/ExpandableWeightLayout.gif
[ExampleSearch]: /art/ExampleSearch.gif
[ExampleRecyclerView]: /art/ExampleRecyclerview.gif
