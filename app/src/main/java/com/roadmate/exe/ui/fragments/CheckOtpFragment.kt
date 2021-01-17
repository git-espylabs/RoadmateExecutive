package com.roadmate.exe.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.roadmate.exe.R
import com.roadmate.exe.api.manager.APIManager
import com.roadmate.exe.api.response.OtpMaster
import com.roadmate.exe.api.response.OtpTrans
import com.roadmate.exe.api.response.RoadmateApiResponse
import com.roadmate.exe.api.service.ApiServices
import com.roadmate.exe.extensions.*
import com.roadmate.exe.log.AppLogger
import com.roadmate.exe.preference.FcmDetails
import com.roadmate.exe.preference.UserDetails
import com.roadmate.exe.rmapp.AppSession
import com.roadmate.exe.ui.activities.ExecutiveHomeActivity
import com.roadmate.exe.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_check_otp.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class CheckOtpFragment : Fragment(), View.OnClickListener {

    private fun otpJsonRequest() : RequestBody {
        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("otp", otp_view.text.toString())
//            json.put("otp", AppSession.otpTemp)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun processVerification(){
        btnVerifyOtp.visibility = View.GONE
        spin_kit.visibility = View.VISIBLE
        if (otp_view.text.toString() != ""){
            lifecycleScope.launch{
                val  response = APIManager.call<ApiServices, Response<OtpMaster>> {
                    verifyOTP(otpJsonRequest())  }

                if (response.isSuccessful && response.body()?.message.equals("success", true)){
                    initializeUserSetting(response.body()?.data?.get(0)!!)
                }
            }
        }else{
            activity?.toast {
                message = "Enter a valid OTP"
                duration = Toast.LENGTH_LONG
            }
        }
    }

    private fun initializeUserSetting(data: OtpTrans){
        UserDetails().isLoggedIn = true
        UserDetails().userId = data.user_id
        UserDetails().userName = data.name
        UserDetails().userMob = data.phonenum
        UserDetails().userEmail = data.email
        UserDetails().userAddress = data.addrress
        UserDetails().userDistrict = data.district
        UserDetails().userLocation = data.location
        UserDetails().userWorkLocation = ""
        UserDetails().userOtherData = ""
        UserDetails().userImage = data.image

        registerForFcm()
    }

    private fun moveToHome(){
        activity!!.startActivity<ExecutiveHomeActivity>()
        activity!!.finish()
    }

    private fun registerForFcm(){
        if (FcmDetails().fcmToken != ""){
            FcmDetails().isFcmRegistered.doIfFalse {
                processFcmRegistration()
            }
        }else{
            requestFcmToke()
        }
    }

    private fun requestFcmToke(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
            it.isSuccessful.doIfTrue {
                AppLogger.info("FCM Token", it.result!!.token)
                if (it.result!!.token != "") {
                    FcmDetails().fcmToken = it.result!!.token
                    processFcmRegistration()
                }
            }elseDo {
                AppLogger.info("FCM Token", "getInstanceId failed: " + it.exception)
                moveToHome()
            }
        })
    }

    private fun processFcmRegistration(){
        lifecycleScope.launch {
            val response = APIManager.call<ApiServices, Response<RoadmateApiResponse>> {
                registerForFcm(fcmRegistrationJsonRequest())
            }
            if (response.isSuccessful && response.body()?.message == "Success"){
                AppLogger.info("FCM Registered")
            }
            moveToHome()
        }
    }

    private fun fcmRegistrationJsonRequest() : RequestBody {
        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("fcm_token", FcmDetails().fcmToken)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_check_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnVerifyOtp.setOnClickListener(this)

        phone.text = getString(R.string.otp_prompt) + AppSession.userMobile
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnVerifyOtp ->{
                NetworkUtils.isNetworkConnected(activity).doIfTrue {
                    processVerification()
                }elseDo {
                    activity!!.toast {
                    message = "No Internet Connectivity!"
                    duration = Toast.LENGTH_LONG }}
            }
        }
    }
}