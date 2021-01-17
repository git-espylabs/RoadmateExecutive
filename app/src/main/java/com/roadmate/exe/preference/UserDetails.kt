package com.roadmate.exe.preference

import com.roadmate.exe.constants.SharedPreferenceConstants

class UserDetails {

    var isLoggedIn by Preference(SharedPreferenceConstants.USER_LOGGED_IN, false)
    var userId by Preference(SharedPreferenceConstants.USER_ID, "")
    var userName by Preference(SharedPreferenceConstants.USER_NAME, "")
    var userMob by Preference(SharedPreferenceConstants.USER_MOB, "")
    var userEmail by Preference(SharedPreferenceConstants.USER_EMAIL, "")
    var userAddress by Preference(SharedPreferenceConstants.USER_ADDRESS, "")
    var userDistrict by Preference(SharedPreferenceConstants.USER_DISTRICT, "")
    var userLocation by Preference(SharedPreferenceConstants.USER_LOCATION, "")
    var userWorkLocation by Preference(SharedPreferenceConstants.WORK_LOCATION, "")
    var userOtherData by Preference(SharedPreferenceConstants.OTHER_DATA, "")
    var userImage by Preference(SharedPreferenceConstants.IMAGE, "")
}