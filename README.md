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
![效果图](https://github.com/siyehua/ExtendToolsBar/blob/master/img/xiaoguo.gif)|![效果图](https://github.com/siyehua/ExtendToolsBar/blob/master/img/mt_xiaoguo.gif)
[类似框架](https://github.com/cpoopc/ScrollableLayout)|[类似框架](http://blog.csdn.net/xiaanming/article/details/17761431)


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
