package com.roadmate.exe.preference

import com.roadmate.exe.constants.SharedPreferenceConstants


class FcmDetails {
    var fcmToken by Preference(SharedPreferenceConstants.FCM_TOKEN, "")
    var isFcmRegistered by Preference(SharedPreferenceConstants.FCM_REGISTERED, false)
}