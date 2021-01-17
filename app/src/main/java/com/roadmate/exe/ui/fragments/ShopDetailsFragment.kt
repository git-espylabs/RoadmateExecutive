package com.roadmate.exe.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.roadmate.exe.BuildConfig
import com.roadmate.exe.R
import com.roadmate.exe.api.manager.APIManager
import com.roadmate.exe.api.response.ShopListMaster
import com.roadmate.exe.api.response.ShopListTrans
import com.roadmate.exe.api.service.ApiServices
import com.roadmate.exe.extensions.doIfTrue
import com.roadmate.exe.extensions.elseDo
import com.roadmate.exe.extensions.toast
import com.roadmate.exe.preference.UserDetails
import com.roadmate.exe.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_shop_details.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ShopDetailsFragment: Fragment() {

    private fun getExeShopDetails(){
        showProgress(true)
        lifecycleScope.launch {
            val response = APIManager.call<ApiServices, Response<ShopListMaster>> {
                getShopDetails(createJsonRequest())
            }
            if (response.isSuccessful && response.body()?.data!!.isNotEmpty()){
                populateDetails(response.body()?.data!![0])
            }else{
                activity!!.toast {
                    message ="OOPS! Something went wrong"
                    duration = Toast.LENGTH_SHORT
                }
            }
            showProgress(false)
        }
    }

    private fun populateDetails(data: ShopListTrans){
        Glide.with(activity!!).load(BuildConfig.BANNER_URL_ENDPOINT + data.shopImage).into(viewImages)
        shop_name.text = data.shopName
        shop_address.text = data.address
        shop_time.text = data.open_time + " to " + data.close_time
        shop_number.text = data.phone_number
    }

    private fun createJsonRequest() : RequestBody {
        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("shopid", arguments!!["shopid"])
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun showProgress(show:Boolean){
        if (show){
            loadingFrame.visibility = View.VISIBLE
        }else{
            loadingFrame.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NetworkUtils.isNetworkConnected(activity!!).doIfTrue {
            getExeShopDetails()
        }elseDo {
            activity!!.toast {
                message ="No Internet connectivity!"
                duration = Toast.LENGTH_SHORT
            }
        }
    }
}