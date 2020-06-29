#!/usr/bin/python
# -*- coding: UTF-8 -*-
import os
import config
import sys
import changeParam
import common
import shutil
import platform

#判断当前系统
def isWindows():
  sysstr = platform.system()
  if("Windows" in sysstr):
    return 1
  else:
    return 0

#兼容不同系统的路径分隔符
def getBackslash():
	if(isWindows() == 1):
		return "\\"
	else:
		return "/"


fileList=[]
# 创建存放加固完成输出文件夹
def createApkDir():
  try:
    os.makedirs(lastPath)
    pass
  except Exception:
    pass


# 清空临时资源
def delApkDir():
  try:
    if os.path.exists(lastPath):
       shutil.rmtree(lastPath)
    pass
  except Exception:
    pass

path360=''
buildToolPath=''
srcPath=sys.argv[1]
if sys.argv[2].strip()!='':
    path360= sys.argv[2]
if sys.argv[3].strip()!='':
    buildToolPath= sys.argv[3]  + getBackslash()
f_list = os.listdir(srcPath)
for i in f_list:
	if os.path.splitext(i)[1] == '.apk':
	    apkPath = os.path.splitext(i)[0]+os.path.splitext(i)[1]

#当前脚本文件所在目录
lastPath = common.curFileDir() + common.getBackslash() + config.finishPath
srcApkPath = srcPath  + common.getBackslash() + apkPath
signPythonPath =common.curFileDir() + common.getBackslash() + config.signPythonName

#测试360加固
delApkDir();
createApkDir();
os.chdir(path360)
jiagu_login = "java -jar jiagu.jar -login " + changeParam.name360 + " " + changeParam.password360
os.system(jiagu_login)
jiagu_cmd = "java -jar jiagu.jar -jiagu" + " " + srcApkPath + " " + lastPath
result = os.system(jiagu_cmd)
if not result:
   print ("=========================" + str(result) + "===============================success====start python ApkResigner")
   os.system("""python """ + signPythonPath + " " + buildToolPath)
else:
   print ("加固失败")