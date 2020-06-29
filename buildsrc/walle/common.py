#!/usr/bin/python
#-*-coding:utf-8-*-

import os
import sys
import platform
import shutil
import datetime

#获取脚本文件的当前路径
def curFileDir():
     #获取脚本路径
     path = sys.path[0]
     #判断为脚本文件还是py2exe编译后的文件，
     #如果是脚本文件，则返回的是脚本的目录，
     #如果是编译后的文件，则返回的是编译后的文件路径
     if os.path.isdir(path):
         return path
     elif os.path.isfile(path):
         return os.path.dirname(path)
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

def get_MD5(file_path):
    files_md5 = os.popen('md5 %s' % file_path).read().strip()
    file_md5 = files_md5.replace('MD5 (%s) = ' % file_path, '')
    return file_md5

def createFilePathOrClear(file_path):
    file_path=file_path.strip()
    # 去除尾部 \ 符号
    #path = path.rstrip("\\")
    isExists=os.path.exists(file_path)
    if not isExists:
       os.makedirs(file_path)
    else:
       del_file_dir(file_path)

def createFileByName(file_path,name):
    path = file_path + getBackslash() + name
    path=path.strip()
    # 去除尾部 \ 符号
    path = path.rstrip("\\")
    isExists=os.path.exists(path)
    if not isExists:
       os.makedirs(path)
       return path
    else:
       del_file(path)
       return path


def del_file(path):
    ls = os.listdir(path)
    for i in ls:
        c_path = os.path.join(path, i)
        if os.path.isdir(c_path):
            del_file(c_path)
        else:
            os.remove(c_path)

def del_file_dir(path):
    ls = os.listdir(path)
    for i in ls:
        c_path = os.path.join(path, i)
        if os.path.isdir(c_path):
            shutil.rmtree(c_path)
        else:
            os.remove(c_path)

def createFileByTime(file_path):
    nowTime=datetime.datetime.now().strftime('%Y-%m-%d-%H-%M-%S')
    path = file_path + getBackslash() + nowTime
    path=path.strip()
    # 去除尾部 \ 符号
    path = path.rstrip("\\")
    isExists=os.path.exists(path)
    if not isExists:
       os.makedirs(path)
       return path
    else:
       return ""


def copyTree(src, dst, symlinks=False, ignore=None):
     for item in os.listdir(src):
         s = os.path.join(src, item)
         d = os.path.join(dst, item)
         if os.path.isdir(s):
             shutil.copytree(s, d, symlinks, ignore)
         else:
             shutil.copy2(s, d)


def copyFileInfo(path, out):
    f_list = os.listdir(path)
    for file in f_list:
    	shutil.copyfile(file, out)