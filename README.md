# sdas
the station of data staistic system to guangzhou mobile<br>
<small>基于r语言和大数据等技术实现对移动工单的验证，通过海量数据的分析显示对未来时刻的预警</small><br>
<small>本系统作为上述算法验证的演示系统</small><br>
<h1>introduce</h1>
1.工单验证<br>
2、预警<br>


<h2>项目进展</h2>
2017/10/16 为小区内各类工单添加了时间查询，修改因为恢复库不能正常显示的历史健康曲线<br>
2017/10/28 小区模型修改，后台数据使用t_normal_model_detail_info<br>
2017/10/30 添加小区异常指标预警和任务调度<br>
2017/10/31 controller、service、dao、dto都基于相应的基类，在基类中写了大量的通用方法，减少的代码量<br>
2017/11/6 前端大量代码优化，生成通用的工具类<br>
2017/11/7 添加用户管理，登录验证和权限管理<br> 
2017/11/9 增加登陆状态和验证码<br>
2017/11/13 添加数据导入模块的日志，后期计划做一个独立的日志模块<br>
2017/11/15 移植代码为新版系统，修改首页预警，支持最新一小时预警信息的查询<br>
2017/12/6 原始数据上传采用ftp协议，利用了Apache的Commons-net包中ftpclient，支持断点续传；使用websocket向前台发送上传进度,
	web项目中使用websocket两种方式：1、采用j2ee中支持的注解的方式2、在spring中注入，其中注册类有两种实现方式，包括shiyongxml配置文件和利用注解使用java注册类，本项目中使用了
	第二种，在使用配置文件时，总会报出找不到相应的adapter异常，原因尚待查找；<br>
	新增投诉数据的导入、实现原理同性能工单<br>
2018/1/10 应对内网条件下，改用百度离线地图，参考了<a href="http://www.jb51.net/article/92380.htm">最全面的百度地图JavaScript离线版开发</a>

<h2>技术选型</h2>
1. spring4<br>
2. mybatis3.4.5<br>
3. log4j<br>
4. poi<br>
5. pagehelper<br>
6. echarts<br>
7. bootsrap.table.notify,validator..<br>
8. hplus jqgrid<br>
9. websocket <br>
10. ......

