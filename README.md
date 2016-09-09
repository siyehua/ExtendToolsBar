# ExtendToolsBar
现在有很多应用的设计,都有 头部 + ViewPager 这样的滑动框架,例如bilibili的首页,视频播放详情页,美团团购详情页,应用宝的应用详情,以及一些应用的个人中心等等.

这类框架关键是需要处理父View与子View之间的手势分发问题.

Google的design support包中NestedScrolling则提供了这样的交互.

典型的实现就是CoordinatorLayout,本框架就是基于CoordinatorLayout搭建的.

ViewPager碎片的根布局支持RecycleView,NestedScrollView,以及任何实现了NestedScrollingParent,NestedScrollingChild的View.

关于NestedScrolling相关资料请[点击](http://www.open-open.com/lib/view/open1440332151780.html)

##效果
Head+ViewPager联动滑动|美团团购详情浮动效果
---|---
仿bilibili首页|仿美团团购详情
![效果图](/img/xiaoguo.gif)|![效果图](/img/mt_xiaoguo.gif)
[类似框架](https://github.com/cpoopc/ScrollableLayout)|[类似框架](http://blog.csdn.net/xiaanming/article/details/17761431)


###该框架应用于博主开发的几个商用项目中,目前表现良好.所以就把它开源出来,其实原理相对简单.后面有讲解遇到开发过程中的一些问题.

##框架在项目中遇到的问题
###问题1: ActionBar左边有一点点间距

一般应用的头部设计都没有采用android自带的ActionBar/ToolsBar.
自定义title时, title距离左边始终有一点间距

解决方案:自定义主题
```xml
<!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="toolbarStyle">@style/ClubToolbar</item>
    </style>

    <style name="ClubToolbar" parent="Widget.AppCompat.Toolbar">
        <!-- 设置该属性解决空白部分-->
        <item name="contentInsetStart">0dp</item>
    </style>
```

###问题2: 头部自动 收缩/扩展

滑动头部时,头部由于过长,需要自动收缩/扩展.例如头部长度为100,当用户滑动到30时手指松开,此时的头部只滑动了一点,一般需要自动完全伸展开来

解决方案:监听滑动距离以及手势

自定义MyCoordinatorLayout继承CoordinatorLayout,监听手指松开

```java
  @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && actionListener != null) {
            actionListener.action(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
```

监听AppBarLayout的滑动距离

```java
//监听顶部的滑动距离
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    currentOffY = Math.abs(verticalOffset);
                    maxOffY = currentOffY > maxOffY ? currentOffY : maxOffY;
                }
            });
        }
```


###问题3: ViewPager中的Fragment根布局不是RecycleView,NestedScrollView.

解决方案:只要支持与CoordinatorLayout联动即可.具体可以看NestedScrollView源码中关于对NestedScrollingParent,NestedScrollingChild的实现

###问题4: 应用本身设置了其他的主题,与该框架主题冲突

解决方案:单独设置Activity的主题

###问题5: 初始化的数据莫名其妙不显示,或类似的问题

原因分析:大部分的人写findView与设置适配器,或者设置初始值时,都直接在onCreateView中完成.

解决方案:onCreateView是为了创建界面用的,为了设置布局,初始化最好是在onActivityCreated中完成,才能保证不至于莫名其妙丢失数据等问题.


###问题6: 取消ViewPager的预加载

ViewPager的预加载是因为默认设置了 mViewPager.setOffscreenPageLimit(1) 提前加载数据和界面.这个设置对于大部分的应用体验都是很好的,但是有时候不想提前加载数据.

即使用户没有看到第二个Fragment,也会执行第二个碎片的onResume()方法,所以在onResume()再加载数据也无法阻止预加载.

解决方案:用户可见Fragment时,会调用setUserVisibleHint方法并传入真值.此时加载数据即可.

####需要注意的是,setUserVisibleHint方法与Fragment的生命周期方法并不绑定,也就是说它有可能会在findViewById之前就调用了.
####此时的view还没有赋值成功,极有可能直接空指针异常.所以需要加一个标志位判断是否已经初始化完成了View.

```java
protected boolean onActivityCreatedFlag = false;

 @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //只有可见,并已经初始化完成了View,才加载数据
        if (isVisibleToUser && onActivityCreatedFlag) {
            getData();
        }
    }

@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        init();
        onActivityCreatedFlag = true;//标志已经初始化完成了View
        if (getUserVisibleHint()) {//假如此时已经可见,则直接加载数据.
            getData();
        }
    }
```

###问题7: 当有多个碎片时,非相邻碎片之间的切换非常卡

问题分析:以bilibili为例,假设从"直播"碎片直接切换到"发现"碎片的时候卡顿(实际体验并不卡,好吧我做的应用卡),是因为

* 碎片本身设计过于复杂,可去掉默认动画.从 "直播"" 到 "发现" 界面,默认动画会依次滑过这两个碎片之间所有碎片的布局,大量的初始化造成卡顿.
设置下面方法,设置一个简单动画避免该问题

```java
 viewPager.setPageTransformer();
 ```

* 前面说了,viewPager默认设置setOffscreenPageLimit(1),也就是只预加载相邻的一个碎片的数据,而从"直播"界面切换到"发现"界面,"发现"界面还没有被初始化

    假设"发现"碎片的布局非常复杂,则会产生卡顿现象,同样的,当切换到"发现"碎片后,此处的"直播"碎片view已经被销毁(执行了onDestroyView方法),再切换会"直播"碎片,"直播"碎片的view需要重新渲染,也会导致卡顿问题.

    可以根据需要,设置setOffscreenPageLimit()的参数大小来保留碎片,保证其不会被销毁,但会增加应用占用的内存,所以要合理的使用setOffscreenPageLimit()

###问题8: 有非常多碎片,或碎片布局非常复杂时,首次渲染非常慢

原因分析:博主做应用的时候就遇到过,一个Activity加载了十多个碎片,偏偏每个碎片不是简单ListView类型的可复用型布局,
光第一个碎片的控件就高达几百个,除了应用设计本身的问题,程序也可以做相应的优化.

此时每一个碎片可以采用分布渲染的方式来减少CPU/GPU的瞬间猛增而导致卡顿甚至OOM.

除了布局过于复杂之外,设置了setOffscreenPageLimit(10)也是导致卡顿的原因之一,同时初始化多个碎片,也会导致该问题.

解决方案:除了每个碎片的具体优化外,ViewPager框架本身也可以做界面的延迟加载.之前说过,ViewPager初始化的时候会预加载,不仅加载数据,界面也会加载.
     而大家用微信的时候可以观察下,倘若第一次到"通讯录/发现/我"" 这三个界面,会发现其界面需要一次初始化的过程,速度很快,但还是可见的.
     
     微信就是采用了懒加载布局的方式实现的,设置了懒加载,哪怕viewpager初始化时设置了setOffscreenPageLimit(10),也不会卡顿
 
下面是关键代码(点击查看[详细代码](/app/src/main/java/com/siyehua/extendtoolsbar/BaseFragment.java))

[LazyScrollViewFragment](/app/src/main/java/com/siyehua/extendtoolsbar/LazyScrollViewFragment.java)
就是采用懒加载的方式,大大提高了应用初始化的速度.

```java
  /**
     * init view flag
     */
    protected boolean initViewFlag = false;
    /**
     * get data flag
     */
    protected boolean getDataFlag = false;
    protected boolean onActivityCreatedFlag = false;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onActivityCreatedFlag = true;
        if (getUserVisibleHint()) {
            findView();
            init();
            initViewFlag = true;
            getData();
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && onActivityCreatedFlag && !initViewFlag) {
            findView();
            init();
            initViewFlag = true;
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        initViewFlag = false;
        onActivityCreatedFlag = false;
    }
```
     
###问题9: 美团团购详情中,悬浮View的距离顶部间距计算

界面:

![效果图](/img/mt_eg.jpg)


布局:

```xml
<CoordinatorLayout>

    <Head>
        <Title/>
        <波派克布局>
    <Head>

    <NestedScrollView>
        <团购各种说明/>
    </NestedScrollView

    <悬浮View/>
</CoordinatorLayout>
```

title 对应图中的 title

博派克布局 对应图中的 可口可乐图片

团购各种说明 对应图中的 新用户,随时退,评分等信息

悬浮View 对应图中的 11.9,1元任抢布局.


悬浮View设置两个属性则会悬浮在 博派克布局 上
```xml
    app:layout_anchor="@id/vp_main"
    app:layout_anchorGravity="top"
```
第一个属性是在哪个控件范围内悬浮,第二个是在悬浮方式.

悬浮View的悬浮方式设置为Top,会自动突出自身一半的高度.

![效果图](/img/mt_001.png)

图中红框部分是 团购各种说明,此时突出的悬浮则会遮挡住 可口可乐布局.

此时可以把 悬浮View的高度 * 2 ,并设置顶部边距,这样就不会遮挡了

```xml
<ImageView
        android:id="@+id/_abc"
        android:layout_width="match_parent"
        android:layout_height="100dp"
        android:paddingTop="50dp"
        android:scaleType="fitXY"
        android:src="@drawable/mt_suspension"
        app:layout_anchor="@id/vp_main"
        app:layout_anchorGravity="top"
        />
```

![效果图](/img/mt_002.png)

图中紫色框是悬浮View的真实高度

到这里一般就已经设置好了,如果顶部完全收缩

![效果图](/img/mt_003.jpg)

看图,假设Title的高度大于悬浮View的高度,则悬浮view会有部分被遮挡.如果小于,则悬浮view具体顶部会有一定的间距

正确的布局是,悬浮View的paddingTop始终于要与Title高度相等,再根据 可口可乐 被遮挡的,或相差距离,设置 <团购各种说明/> 的marginTop.


```xml
<android.support.v4.widget.NestedScrollView
        android:id="@+id/vp_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"

        android:layout_marginTop="2.5dp"
        //因为悬浮view高度为105dp,突出的高度为一半:52.5dp,悬浮view的paddingTop只有50dp,则可口可乐会有
        52.5- 50 = 2.5 dp的高度会被遮挡,所以此时要设置距离 可口可乐布局 为 2.5dp


        android:paddingTop="52.5dp"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <ImageView
            android:id="@+id/_123"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:scaleType="fitXY"
            android:src="@drawable/mt_info"/>
    </android.support.v4.widget.NestedScrollView>

    <ImageView
        android:id="@+id/_abc"
        android:layout_width="match_parent"
        android:layout_height="105dp"
        android:paddingTop="50dp"//始终等于Title的高度
        android:scaleType="fitXY"
        android:src="@drawable/mt_suspension"
        app:layout_anchor="@id/vp_main"
        app:layout_anchorGravity="top"
        />
```
#License
```
Copyright 2016 siyehua

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
