主要参考;https://blog.csdn.net/tianyaleixiaowu/article/details/76177583
操作参考：
https://blog.csdn.net/u014201191/article/details/46508311


创建数据测试url
http://localhost:9031/add
http://localhost:9031/query
http://localhost:9031/update
http://localhost:9031/demo/demo3.html
百度地址的打点功能

使用虚拟机为elt的es配置
启动需要登录操作系统，切换用户，启动Es即可

查看es配置信息
http://192.168.75.101:9200/elastic_search_project
修改刷新间隔
curl -XPUT 'http://127.0.0.1:9200/elastic_search_project/_settings' -H 'Content-Type: application/json' -d '{"index":{"refresh_interval" : "5s"}}'

修改了刷新间隔，可实现修改数据立刻更新问题