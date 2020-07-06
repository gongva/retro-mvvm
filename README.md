# retro-mvvm

### TargetSdkVersion 

28 / Android9.0

### CompileSdkVersion

28

### MinSdkVersion

19 /Android 4.4

### Tips：

support-v7 libraries,not android x.



### In the whole process of using this framework, you need to pay attention to the following files, look for "todo "in the file and change it to meet the actual information of your project or company.

↓↓↓↓↓↓↓↓↓↓



#### Module lib.security's todo:

Encryption and decryption key repository, used to generate "so" files.

- 1.getkey.cpp
  field:SECURITY_KEY

#### Module lib.core's todo:

Povide a collection of tools based on Java and Android.

- 1.build.gradle
  task uploadArchives

#### Module lib.library's todo:

Basic function components, provide the BaseApplication, BaseActivity, BaseFragment, Custom views, manager, loading controller, config, lifecycle, etc. provide plug-ins: Glide, Retrofit, EventBus,ARouter etc.

- 1.build.gradle
  task uploadArchives
- 2.AppConfig
  user agent
- 3.JsBridgeProtocolHandler
  data and action echo
- 4.FileUtil
  APP_ROOT_FILE

#### Module lib.buildsrc's todo:

Complete the whole process of release apk, signature, reinforcement and channel package，based on wall and 360.

- 1.keystore.jks
- 2.changeParam.py
  keystore setting
- 3.com.gongva.plugin.properties
  package name
- 4.ChannelPlugin

#### Module project's todo:

Project settings.

- 1.project's build.gradle
  maven:repository url
- 2.app's build.gradl
  keystore setting
  plugin setting
  channel setting
- 3.String.xml
  link_scheme and link_scheme_host
- 4.keystore.jks

#### Other todo:

Classes to be modified in app Module.

- 1.GvContext
- 2.ARouterPath
- 3.GvConfig
- 4.HeaderUtils
- 5.NetInterceptorConfig
- 6.GvApplicationCreate
- 7.GvJsBridgeProtocolHandler
- 8.provider_paths.xml
- 9.styles.xml

#### other dependencies：

- 1.python environment