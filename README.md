# 工程简介
本项目由个人开放，项目由一个人完成，从开始到完成大概做了5天</br>
这也是本人第一次做项目，相信还是由很多的地方没有考虑周到</br>
# 延伸阅读
#### 项目如何运行：

1. 将resources目录下的application.properties.txt文件的.txt后缀名去掉，并填入自己的配置

2. 本项目需要使用redis，如果没有redis也不会使用redis，可以使用resources目录下的redis，这里的是绿色免安装版本，

   解压后点击里面的redis-server.exe文件启动，然后application.properties里有关redis的配置不用动了

3. 在mysql中执行resources目录下的takeout.sql文件

4. 运行Application的main方法启动项目（项目中的图片需要在application.properties文件的local.img=下指定图片存放位置，如果没有指定图片会存放到resources目录下static下的img文件夹中）
