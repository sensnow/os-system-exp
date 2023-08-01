# os-system-exp
操作系统的课程设计 基于JavaFx开发的带有界面的文件系统



# 系统需求

最好是`Java 1.8`版本 因为java8自带了fx组件



# 题目说明

## 课程设计目的

- 了解磁盘文件系统的结构、功能和实现。
- 提高团队合作和程序设计能力。

## 课程设计内容和要求

设计一个简单的文件系统，使用文件模拟磁盘，数组模拟缓冲区，要求实现以下功能：

1. 支持多级目录结构，支持文件的绝对路径。
2. 文件的逻辑结构采用流式结构，物理结构采用链接结构的显式链接方式。
3. 使用文件分配表（FAT）进行文件分配。
4. 包括建立目录、列目录、删除空目录、建立文件、删除文件、显示文件内容、打开文件、读文件、写文件、关闭文件、改变文件属性等命令。
5. 可以采用命令行界面或右键快捷菜单选择方式执行命令。
6. 编写主函数对所作工作进行测试。



## 文件组织结构和磁盘空间管理

- 文件的逻辑结构采用流式文件，物理结构采用显式链接方式。
- 使用文件分配表(FAT)来管理文件的物理空间。
- 模拟一个大小为128个物理块的磁盘，每个物理块大小为64字节。
- 文件分配表放在磁盘的开始处，占用磁盘的前两个物理块。
- 文件分配表的每一项记录了对应盘块的使用情况和下一个盘块的位置。
- 使用特定的数值表示盘块未使用和损坏状态。
- 目录结构采用根目录和子目录的形式，每个目录和文件均有对应的目录项。

## 文件操作

文件操作是对文件进行存取的一组功能模块，用户可以通过访管指令调用这些模块实现对文件的存取。
常用的文件操作包括：

- 建立文件：创建新文件并进行登记。
- 打开文件：打开已存在的文件供读写操作。
- 读文件：从文件中读取数据。
- 写文件：向文件中写入数据。
- 关闭文件：完成文件的读写操作后关闭文件。
- 删除文件：删除不需要的文件。
- 显示文件内容：显示文件的内容。
- 改变文件属性：修改文件的属性。



## 目录结构

- 文件目录是用于检索文件的，包括根目录和子目录两种类型。
- 目录项包括文件的控制信息、文件结构的信息和文件管理的信息。
- 每个目录项占用8个字节，包括文件名、文件类型、文件属性、起始盘块号和文件长度。



# 配置说明

- 桌面配置

  ``` properties
  # 桌面图标的列数
  desktopIconNumCols = 18
  # 桌面图标的行数
  desktopIconNumRows = 9
  # 任务栏图标的列数
  missionIconNumCols = 22
  # 任务栏图标的行数
  missionIconNumRows = 1
  # 桌面壁纸图片路径
  desktopPictureImage = /com/example/file/image/test.jpg
  # 桌面关于我们的图片路径
  desktopAboutUsImage = /com/example/file/image/aboutus.png
  # 桌面磁盘信息图片路径
  desktopDiskImage = /com/example/file/image/harddisk.png
  # 桌面帮助图片路径
  desktopHelpImage = /com/example/file/image/help.png
  # 桌面Window图片路径
  desktopWindowImage = /com/example/file/image/window.png
  # 文件图片路径
  desktopTxtImage = /com/example/file/image/txt.png
  # 文件夹图片路径
  desktopDirectoryImage = /com/example/file/image/directory.png
  
  ```



- 目录配置

  ``` properties
  # 文件夹图标的列数
  directoryIconNumCols = 10
  # 文件夹图标的行数
  directoryIconNumRows = 8
  # 根目录图标路径
  rootDirectoryImage =  /com/example/file/image/rootDirectory.png
  # 朝左的图标(灰色)
  toLeftGreyImage = /com/example/file/image/toleft_grey.png
  # 朝右的图标(灰色)
  toLeftBlueImage = /com/example/file/image/toleft_blue.png
  # 朝右的图标(蓝色)
  toRightGreyImage = /com/example/file/image/toright_grey.png
  # æå³çå¾æ (èè²)
  toRightBlueImage = /com/example/file/image/toright_blue.png
  # 回到主界面的图标(灰色)
  toHomeGreyImage = /com/example/file/image/tohome_grey.png
  # 回到主界面的图标(蓝色)
  toHomeGreenImage = /com/example/file/image/tohome_green.png
  ```



- Window 窗口配置

  ```properties
  # win 关闭窗口图片
  windowCloseImage =  /com/example/file/image/close.png
  # win 最小化窗口图片
  windowMinimizeImage = /com/example/file/image/minimize.png
  ```

# 系统图片

- 主页

  ![image-20230801144724681](https://cdn.sensnow.cn/uPic/image-20230801144724681.png)

- 磁盘信息

  ![image-20230801145016223](https://cdn.sensnow.cn/uPic/image-20230801145016223.png)

- 文件编辑器

  ![image-20230801145057582](https://cdn.sensnow.cn/uPic/image-20230801145057582.png)

- 目录页面

  ![image-20230801145227982](https://cdn.sensnow.cn/uPic/image-20230801145227982.png)

  
