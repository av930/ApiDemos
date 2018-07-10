#KJK_TALK MAKE APP: root�� ���� ���� android.mk�� �����ϴ� ���
LOCAL_PATH:= $(call my-dir)
#KJK_TALK MAKE APP: ���� LOCAL_XXX�� ���۵Ǵ� symbol���� ��� clear
include $(CLEAR_VARS)

#KJK_TALK MAKE APP: ���� App�� ApiDemos�� ���ԵǴ� build Varient (eng, tests, user, userdebug�߿���)
LOCAL_MODULE_TAGS := samples tests

# Only compile source java files in this apk.
#KJK_TALK MAKE APP: ���� dir�Ʒ��� ��� java src�� �о�´�.
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_SRC_FILES += \
        src/com/example/android/apis/app/IRemoteService.aidl \
        src/com/example/android/apis/app/IRemoteServiceCallback.aidl \
        src/com/example/android/apis/app/ISecondary.aidl \



LOCAL_JAVA_LIBRARIES := telephony-common

LOCAL_STATIC_JAVA_LIBRARIES = android-support-v4

#KJK_TALK MAKE APP: ���� App�� ��Ī�ϴ� ������ �̸�.
LOCAL_PACKAGE_NAME := ApiDemos

#KJK_TALK MAKE APP: full source��ȯ�濡�� compile������, Android stub���� �Ἥ compile �ϰڴٴ� �� 
LOCAL_SDK_VERSION := current

LOCAL_DEX_PREOPT := false

include $(BUILD_PACKAGE)

# Use the folloing include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
