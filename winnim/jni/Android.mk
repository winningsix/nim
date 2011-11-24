LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := shape
LOCAL_SRC_FILES := shape.c

include $(BUILD_SHARED_LIBRARY)
