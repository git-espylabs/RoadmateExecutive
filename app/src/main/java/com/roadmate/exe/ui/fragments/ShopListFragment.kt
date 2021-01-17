package com.roadmate.exe.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roadmate.exe.R
import com.roadmate.exe.adapter.ShopListAdapter
import com.roadmate.exe.api.manager.APIManager
import com.roadmate.exe.api.response.ShopListMaster
import com.roadmate.exe.api.response.ShopListTrans
import com.roadmate.exe.api.service.ApiServices
import com.roadmate.exe.extensions.doIfTrue
import com.roadmate.exe.extensions.elseDo
import com.roadmate.exe.extensions.toast
import com.roadmate.exe.preference.UserDetails
import com.roadmate.exe.ui.activities.AddShopActivity
import com.roadmate.exe.ui.activities.ShopDetailsActivity
import com.roadmate.exe.ui.activities.ShopListActivity
import com.roadmate.exe.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_shop_list.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ShopListFragment: Fragment() {

    private fun getExeAddedShopList(){
        showProgress(true)
        lifecycleScope.launch {
            val response = APIManager.call<ApiServices, Response<ShopListMaster>> {
                getAddedShopList(createJsonRequest())
            }
            if (response.isSuccessful && response.body()?.data!!.isNotEmpty()){
                populateList(response.body()?.data!!)
            }else{
                rvShopList.visibility = View.GONE
                empty_caution.visibility = View.VISIBLE
            }
            showProgress(false)
        }
    }

    private fun getExeVisitedShopList(){
        showProgress(true)
        lifecycleScope.launch {
            val response = APIManager.call<ApiServices, Response<ShopListMaster>> {
                getVisitedShopList(createJsonRequest())
            }
            if (response.isSuccessful && response.body()?.data!!.isNotEmpty()){
                populateList(response.body()?.data!!)
            }else{
                rvShopList.visibility = View.GONE
                empty_caution.visibility = View.VISIBLE
            }
            showProgress(false)
        }
    }

    private fun createJsonRequest() : RequestBody {
        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("executiveid", UserDetails().userId)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun populateList(list: ArrayList<ShopListTrans>){
        rvShopList.visibility = View.VISIBLE
        empty_caution.visibility = View.GONE
        rvShopList.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val adapter = ShopListAdapter(activity!!, list){shopid->

            val intent = Intent(context, ShopDetailsActivity::class.java)
            intent.putExtra("shopid", shopid)
            startActivity(intent)
        }
        rvShopList.adapter = adapter
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
        return inflater.inflate(R.layout.fragment_shop_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NetworkUtils.isNetworkConnected(activity!!).doIfTrue {
            if (arguments!!["type"] == "added_shops"){
                btnAddShop.visibility = View.GONE
                getExeAddedShopList()
            }else{
                btnAddShop.visibility = View.VISIBLE
                getExeVisitedShopList()
            }
        }elseDo {
            activity!!.toast {
                message = "No Internet connectivity!"
                duration = Toast.LENGTH_SHORT
            }
        }

        btnAddShop.setOnClickListener {
            val intent = Intent(context, AddShopActivity::class.java)
            intent.putExtra("type", "visit")
            startActivity(intent)
        }
    }
}