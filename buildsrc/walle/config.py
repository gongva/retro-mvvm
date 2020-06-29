#!/usr/bin/python
#-*-coding:utf-8-*-
import os
import changeParam
import common

#加固中间环节参数
signPythonName = "apkResigner.py"
finishPath="apk"
#加固成功参数的apk路径
srcPath= common.curFileDir() + common.getBackslash()
f_list = os.listdir(srcPath)
for i in f_list:
	if os.path.splitext(i)[1] == '.apk':
	    waitJiaguApkName = os.path.splitext(i)[0]+os.path.splitext(i)[1]


protectedSourceApkDirPath = ""
#渠道包输出路径
channelsOutputFilePath = ""
#渠道名配置文件路径，默认在此文件夹根目录
channelFilePath = ""
#额外信息配置文件
extraChannelFilePath = ""


