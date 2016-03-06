Word Frequency 文本文件词频统计程序 (encode: utf-8)
作者: 艾硕 3011209031 信管1班

1.文件简介
  (1)src: 该文件夹中存放源代码
  (2)classes: 该文件夹中存放编译好的ByteCode文件
  (3)lib: 该文件夹中存放程序依赖的类库
  (4)result: 该文件夹中存放程序运行后保存的统计结果
  (4)manifest.mf: 该文件为使用jar打包时指定的清单
  (5)frequency.jar: 可执行的jar文件，可以直接通过$ java -jar frequency.jar运行。
  (6)README.txt: 文档

2.功能概述
  2.1启动方式:
     (1)命令行启动(for linux or dos)
        切换(cd)到frequency.jar文件所在目录.
        运行命令: $ java -jar frequency.jar 即可.
     (2)使用鼠标双击
        切换到frequency.jar文件所在目录.
        双击frequency.jar文件即可.
        (注: 在linux下需要为文件增加执行权限: $ chmod u+x frequency.jar)
        (注: 由于此程序依赖lib文件夹中的jfreechart类库，而程序对此类库的引用使用
        相对路径，所以请在frequency文件夹中运行，否则会运行失败)

  2.2运行方式:
     (1)词频计算
        启动后，会出现程序界面。点击'文件'菜单，点击'打开'，选择一个文本文件(英文)
        确定后，程序会将计算好的词频统计图和统计表显示出来。(如图frequency1.png)。
        程序会生成相应的统计文件，存放在'./result/tmp/xxx/'文件中。
        (注: 可以通过设置./config/meanless文件来过滤不想统计的单词，meanless为文本
        文件，直接使用记事本打开编辑即可)

     (2)词频统计
        计算完词频后，可以对文章类型和字频进行统计。点击'文件'菜单，点击'类型'，
        填写文章类型和需要统计的高频词数(如果词数超过文章最多单词量，则使用最多单词
        量)，确定后，程序会返回'统计完成'信息，生成统计文件，存放在'.result/type/xxx'
        文件中。(如图frequency2.png, frequency3.png, frequency4.png)。

  2.3测试结果:
     (1)在linux mint 15中运行良好。
     (2)在windows xp中运行良好。

3.开发概述
  3.1概述
     (1)开发工具
        本程序使用了jfreechart-1.0.16来实现统计图的绘制。
        本程序开发使用的jdk版本是1.7.0。
     (2)编译程序
        由于程序较小，没有写build.xml，可以直接使用命令行编译。
        $ javac -classpath lib/jfreechart-1.0.16.jar:lib/jcommon-1.0.20.jar \
        src/me/controller/*.java \
        src/me/main/*.java \
        src/me/model/*.java \
        src/me/view/*.java \
        -d classes/

        程序已经打包为jar文件。所以运行可不必指定classpath，这些已经写在了manifest.mf中。

  3.2设计方法
     (1)设计模式
        本程序采用MVC设计模式，实现了业务逻辑和表现的相分离。虽然增加了很多代码，
        但是也方便了我后期调试(debug)。当然，我将代码重构了2遍才实现MVC设计模式。

     (2)设计概述
        源文件结构:
        src/me
        ├── controller
        │   └── FrequencyControl.java
        ├── main
        │   └── Main.java
        ├── model
        │   ├── FrequencyChart.java
        │   ├── Frequency.java
        │   ├── FrequencyModel.java
        │   ├── ProcessResult.java
        │   └── TypeStatistic.java
        └── view
            └── FrequencyView.java

        a.模型(model):
          该文件夹中的类处理业务逻辑，将数据返回。
          # Frequency类接收文本文件计算词频，并生成result.txt文件，此文件中存放
            词频结果。
          # ProcessResult类可以处理result.txt文件，生成相应的数组和集合，供FrequencyChart和
            TypeStatistic类使用。
          # FrequencyChart类使用ProcessResult处理result.txt的数据生成result.jpg统计图。
            该统计图存放在./result/tmp/xxx/文件夹中。
          # TypeStatistic类可以进行文件类型和词频的统计。并将结果存放在./result/type/xxx文件
            中。如果遇到相同类型的文件，TypeStatistic会在原文件基础上添加新单词，
            不添加重复单词。
          # FrequencyModel类综合上述4个类，对外进行信息交换，起到封装的作用。
        b.视图(view)
          该文件夹中的类处理呈现部分。
          # FrequencyView类主要是描绘窗体，实现左边使用一个描画图片的component，
            右边使用JTable。上边是一个简单的菜单。此类与FrequencyModel完全分离，
            没有任何信息交换。
        c.控制器(controller)
          该文件夹中的类通过实现实现了ActionListner接口(interface)的内部类(inner
          class)来使FrequencyModel和FrequencyView两个类的object相互联系，生成最
          终的程序。
        d.主函数(main)
          该文件夹中的Main类只生成一个FrequencyControl实例(Objcet)，从而让程序可以
          正常执行。

  3.3技术细节(关键的业务逻辑)
     (1)Frequency类: 词频统计
        # 使用一个字符串的Set来存储无实意的单词，其中无实意的单词从./config/meanless
          中读取。
        # 使用一个Key为String，Value为Integer类型的HashMap来存储单词和词频，未排序。
          具体过程是遍历一个数组，检验每个单词是否在HashMap中，如果在，就为其Value
          加1，如果不在，就在HashMap中添加新单词，将Value设置为1。
        # 使用一个SortedSet(类型为Map.Entry<String, Integer>)来存储排好序的单词和
          词频，排序方式按Map.Entry中的Integer，从大到小。所以在生成一个SortedSet
          的Object时，要传入一个实现了Comparator接口的对象，并Override里面的compare
          方法。
        # 方法: getWordsListFromFile
          这个方法从文本文件中提取所有的单词，返回一个String类型的ArrayList。这里
          使用了正则表达式，将每个单词分隔开，并将单词放入数组中，以备统计。
        # 单元测试(Unit Test)正常。
    (2)TypeStatistic类: 统计高频词和文章类型
        # 主要流程是根据一个给定的文章类型，如果在./result/type/文件夹中没有该类型
          的话，就创建一个文件，然后将高频词写入。如果有该文件的话，先读取该文件
          将原有的单词存放入一个ArrayList<String>中，然后根据该ArrayList的内容，
          将要写入的单词中重复的单词过滤出去，保证统计文件中没有重复的单词。

4.总结
  通过这次开发，熟悉了Java中关于Collection和I/O的操作，并通过实践MVC设计模式，对
  这种模式有了一定的了解。
