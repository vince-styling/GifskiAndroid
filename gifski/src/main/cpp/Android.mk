LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE            := gifski
LOCAL_SRC_FILES         := $(TARGET_ARCH_ABI)/libgifski.a
LOCAL_EXPORT_C_INCLUDES := include/
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := gifskiad
GIFSKI_SOURCE:= $(wildcard $(LOCAL_PATH)/gifski/*.cpp)
LOCAL_SRC_FILES += $(GIFSKI_SOURCE)

# Using android logging library
LOCAL_LDLIBS := -llog -ljnigraphics
# Define pre-processor macro for log levels
LOCAL_CFLAGS := -DLOG_LEVEL=LOG_VERBOSE

LOCAL_CPPFLAGS += -ffunction-sections -fdata-sections -fvisibility=hidden -frtti -fexceptions
LOCAL_CFLAGS += -ffunction-sections -fdata-sections
LOCAL_LDFLAGS += -Wl,--gc-sections

# Link with the prebuilt module that we defined and the top part of this file
LOCAL_STATIC_LIBRARIES := gifski

include $(BUILD_SHARED_LIBRARY)