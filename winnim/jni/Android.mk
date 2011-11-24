LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := shape
LOCAL_C_INCLUDES := /usr/include
LOCAL_SRC_FILES := shape.c
LOCAL_LDLIBS := -lGLESv1_CM
include $(BUILD_SHARED_LIBRARY)
