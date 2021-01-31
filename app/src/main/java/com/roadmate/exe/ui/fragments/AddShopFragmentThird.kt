package com.roadmate.exe.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.roadmate.exe.R
import com.roadmate.exe.api.manager.APIManager
import com.roadmate.exe.api.response.RoadmateApiResponse
import com.roadmate.exe.api.service.ApiServices
import com.roadmate.exe.extensions.toast
import com.roadmate.exe.preference.UserDetails
import com.roadmate.exe.utils.CommonUtils
import kotlinx.android.synthetic.main.fragment_add_new_shop_third.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.http.Part
import java.io.*
import java.util.HashMap

class AddShopFragmentThird: BaseFragment(), View.OnClickListener {


    var handler = Handler()
    var progressHandler: Handler = Handler()
    var paystatus = "0"

    private fun multiPartRequest(){
        showProgress(true, "Processing image..")

        Thread(Runnable {

            var mMap: HashMap<String, String> = this.arguments!!.getSerializable("regMap") as HashMap<String, String>

            val type = MultipartBody.Part.createFormData("type", mMap["shoptypeId"]!!)
            val shopname = MultipartBody.Part.createFormData("shopname", mMap["shopnameReg"]!!)
            val phnum = MultipartBody.Part.createFormData("phnum", mMap["shopMobile"]!!)
            val phnum2 = MultipartBody.Part.createFormData("phnum2", mMap["shoplandline"]!!)
            val desc = MultipartBody.Part.createFormData("desc", mMap["shopdesc"]!!)
            val opentime = MultipartBody.Part.createFormData("opentime", mMap["shop_open_time"]!!)
            val closetime = MultipartBody.Part.createFormData("closetime", mMap["shop_close_time"]!!)
            val agrimentverification_status = MultipartBody.Part.createFormData("agrimentverification_status", "1")

            val address = MultipartBody.Part.createFormData("address", mMap["shopadd"]!!)
            val pincode = MultipartBody.Part.createFormData("pincode", mMap["shoppin"]!!)
            val latitude = MultipartBody.Part.createFormData("latitude", mMap["shoplat"]!!)
            val logitude = MultipartBody.Part.createFormData("logitude", mMap["shoplon"]!!)
            val trans_id = MultipartBody.Part.createFormData("trans_id", "0")
            val exeid = MultipartBody.Part.createFormData("exeid", UserDetails().userId)
            var authorised_status = if (arguments!!["type"].toString() == "new") {
                MultipartBody.Part.createFormData("authorised_status", "1")
            } else {
                MultipartBody.Part.createFormData("authorised_status", "0")
            }
            val pay_status = MultipartBody.Part.createFormData("pay_status", paystatus)
            val image1Multipart = if (mMap["shopimg"]!!.isNotEmpty()) {
                getMultiParImageBody(mMap["shopimg"]!!, "image")
            } else {
                getMultiParImageBody(null, "image")
            }

            handler.post(Runnable {
                showProgress(true, "Uploading..")
                initiateApiCall(image1Multipart, type, shopname, phnum, phnum2, desc,
                    opentime, closetime,agrimentverification_status,address,pincode,latitude,logitude,trans_id, exeid, authorised_status, pay_status)
            })
        }).start()
    }

    private fun getMultiParImageBody(imageFile: String?, fileName: String): MultipartBody.Part{
        var file: File? = null
        var inputStream: InputStream? = null
        if (imageFile != null && imageFile.isNotEmpty()) {
            inputStream  = FileInputStream(imageFile);
        } else {
            val am = context!!.assets
            try {
                inputStream = am.open("defaults/ic_launcher.png")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        val bin = BufferedInputStream(inputStream)
        file = CommonUtils.stream2file(bin)

        val requestFile = file!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val filec = File(imageFile)
        val requestFile2: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            filec
        )

        val body = MultipartBody.Part.createFormData(fileName, filec.name, requestFile2)

        return body/*MultipartBody.Part.createFormData(fileName, file.name, requestFile)*/
    }

    private fun initiateApiCall(@Part image1Multipart: MultipartBody.Part,
                                @Part type: MultipartBody.Part,
                                @Part shopname: MultipartBody.Part,
                                @Part phnum: MultipartBody.Part,
                                @Part phnum2: MultipartBody.Part,
                                @Part desc: MultipartBody.Part,
                                @Part opentime: MultipartBody.Part,
                                @Part closetime: MultipartBody.Part,
                                @Part agrimentverification_status: MultipartBody.Part,
                                @Part address: MultipartBody.Part,
                                @Part pincode: MultipartBody.Part,
                                @Part latitude: MultipartBody.Part,
                                @Part logitude: MultipartBody.Part,
                                @Part trans_id: MultipartBody.Part,
                                @Part exeid: MultipartBody.Part,
                                @Part authorised_status: MultipartBody.Part,
                                @Part pay_status: MultipartBody.Part){


        try {
            lifecycleScope.launch {

                val response = APIManager.call<ApiServices, Response<RoadmateApiResponse>> {
                    addNewShop(image1Multipart, type, shopname, phnum, phnum2, desc,
                        opentime, closetime,agrimentverification_status,address,pincode,latitude,logitude,trans_id,exeid, authorised_status, pay_status)
                }
                if (response.isSuccessful && response.body()?.message!! == "Success"){
                    activity!!.toast {
                        message = "Shop Added Successfully"
                        duration = Toast.LENGTH_LONG
                    }
                    activity!!.finish()
                }else if (response.isSuccessful){
                    activity!!.toast {
                        message = response.body()?.message!!
                        duration = Toast.LENGTH_LONG
                    }
                }
                showProgress(false, "")
            }
        } catch (e: Exception) {
            showProgress(false, "")
            Log.e("Check",e.localizedMessage.toString())
        }
    }

    fun onBackPress(){
        if (btnConfirm.visibility == View.VISIBLE) {
            btnDirectPay.visibility = View.VISIBLE
            btnOnlinePay.visibility = View.VISIBLE
            btnQrPay.visibility = View.VISIBLE
            ivqrcode.visibility = View.GONE
            btnConfirm.visibility = View.GONE
        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onStop() {
        super.onStop()
        if (progressHandler != null){
            progressHandler.removeCallbacksAndMessages(null)
        }
    }

    private fun showProgress(visible: Boolean, text: String) {
        if (visible) {
            spinkit.visibility = View.VISIBLE
            tvstatus.visibility = View.VISIBLE
            btnConfirm.isEnabled = false
        } else {
            spinkit.visibility = View.GONE
            tvstatus.visibility = View.GONE
            btnConfirm.isEnabled = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_shop_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ivqrcode.visibility = View.GONE
        btnConfirm.visibility = View.GONE
        tvDirect.setOnClickListener(this)
        tvOnline.setOnClickListener(this)
        tvQrcode.setOnClickListener(this)
        tvConfirm.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvDirect ->{
                paystatus = "1"
                btnDirectPay.visibility = View.GONE
                btnOnlinePay.visibility = View.GONE
                btnQrPay.visibility = View.GONE
                ivqrcode.visibility = View.GONE
                btnConfirm.visibility = View.VISIBLE
            }
            R.id.tvOnline ->{
                btnDirectPay.visibility = View.GONE
                btnOnlinePay.visibility = View.GONE
                btnQrPay.visibility = View.GONE
                paystatus = "0"
                progressHandler.postDelayed({
                    multiPartRequest()
                }, 500)
            }
            R.id.tvQrcode ->{
                paystatus = "1"
                btnDirectPay.visibility = View.GONE
                btnOnlinePay.visibility = View.GONE
                btnQrPay.visibility = View.GONE
                ivqrcode.visibility = View.VISIBLE
                btnConfirm.visibility = View.VISIBLE
            }
            R.id.tvConfirm ->{

                progressHandler.postDelayed({
                    multiPartRequest()
                }, 500)
            }
        }
    }
}