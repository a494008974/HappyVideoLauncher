LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

LOCAL_JAVA_LIBRARIES := bouncycastle telephony-common
LOCAL_STATIC_JAVA_LIBRARIES := guava android-support-v4 jsr305 


LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) \
	src/com/voole/epg/cooperation/aidl/VooleAIDL.aidl 
	
LOCAL_STATIC_JAVA_LIBRARIES +=common afinal umeng

LOCAL_PACKAGE_NAME := CibnHappy
LOCAL_CERTIFICATE := platform

LOCAL_PROGUARD_FLAG_FILES := proguard.cfg

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)   
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := afinal:libs/afinal.jar \
										umeng:libs/umeng-analytics-v5.5.3.jar \
										common:libs/trinea-android-common.jar
include $(BUILD_MULTI_PREBUILT)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
