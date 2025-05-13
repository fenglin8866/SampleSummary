
============================项目说明START=================================

sample项目
地址：https://github.com/android/architecture-components-samples
位置：architecture-components-samples中的PagingSample
分支：main
最后提交记录：
Author: Sergey Vasilinets <sergeyv@google.com>
Date:   Sat Jul 1 13:36:11 2023 +0100
    Use ActivityContracts in WorkManagerSample
    bug: 208344986
    Test: Manual

是否维护：后续应该不维护
集成时间：2024.11.6
改动点
1、app改造为lib


networksample项目，lib项目：是networksample项目的模块
地址：https://github.com/android/architecture-components-samples
位置：architecture-components-samples中的PagingWithNetworkSample
分支：main
最后提交记录：
Author: Sergey Vasilinets <sergeyv@google.com>
Date:   Sat Jul 1 13:36:11 2023 +0100
    Use ActivityContracts in WorkManagerSample
    bug: 208344986
    Test: Manual

是否维护：后续应该不维护
集成时间：2024.11.6
改动点：
1、GlideRequest改为RequestManager
2、computeNextLoadStateAndMergedState方法需要修复。
3、移除network_state_item，reddit_post_item布局

============================项目说明END===================================


============================项目分析START=================================

设计思路:
-------------------------------------

功能解析:
-------------------------------------

知识点解析:
-------------------------------------
RecyclerView
1、添加header和footer
2、不同布局的处理
3、点击，更新，滑动删除
4、形成通用布局模板


问题与解决:
-------------------------------------

总结:
PagingConfig
1、第一次默认加载数量是pageSize*3，可以通过initialLoadSize参数改变。

2、maxSize，默认是Int的最大值。超过该值，会丢弃多余的item。
   对应的key值怎么变化？
3、enablePlaceholders占位符的使用

4、触发下一页加载的时机？
默认是大于pageSize，使用prefetchDistance修改触发时机。
-------------------------------------

============================项目分析END===================================


============================项目重构START=================================


============================项目重构END===================================







