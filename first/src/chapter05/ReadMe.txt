三、课堂计分
    首先在本机上测试成功自己的多用户版本TCP服务器，再完成以下工作。
教师计分服务器将开启多个线程连接你的多用户版本TCP服务器（TCPThreadServer.java），你的TCP服务器需要按约定判断关键字，
按要求返回对应信息，信息正确就可以获得本次课堂分，具体步骤如下：
（1）教师计分服务器将会向你的TCPThreadServer.java服务器程序发起多个连接，并发送信息，你的服务器如果收到信息："来自教师服务器的连接" ，
你的服务器应该回发 1 ；如果收到信息："教师服务器再次发送信息" ，则回发 2 。按这个约定修改你的服务器程序，并临时关闭防火墙以及本机上的虚拟网卡，
运行该服务器程序，运行的端口号建议大于1024，不要和常用的端口号冲突，例如可以使用7777；
（2）用第三讲的TCPClientThreadFX客户端连接教师计分服务器202.116.195.71：8008，发送学号验证通过后，
 按如下格式发送你的服务器运行的端口号：我的服务器已经准备好，端口号是&****& 。
注意：信息一定要严格按照要求，*号用真实端口号代替，两个&是信息间隔符；
（3）教师计分服务器获得端口号后，将连接你的服务器，如果一切顺利，就能得到完成练习的反馈信息，