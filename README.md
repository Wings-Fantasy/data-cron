一个基于Spring boot的定时任务程序
# 特性
- 使用pid文件锁防止重复运行，复制到不同目录下可以运行多个实例
- 内置http相关工具类，更方便的构造请求
- 只需修改配置文件，不改动任何代码即可新增数据源
- 一行代码即可切换数据源，同一个方法中可以使用不同数据源操作
- 支持无数据库运行
# 运行截图
![运行时控制台截图](https://github.com/user-attachments/assets/2ad67009-c9cf-42ac-8c3c-7853789ecc6b)
# 运行环境
- Java 17 及以上
- Maven 3.9 及以上
# 依赖信息
- Spring boot 3.3.1
- MyBatis 3.0.3
