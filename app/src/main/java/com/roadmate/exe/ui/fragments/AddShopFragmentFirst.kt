package com.roadmate.exe.ui.fragments
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.roadmate.exe.R
import com.roadmate.exe.adapter.ShopTypesAdapter
import com.roadmate.exe.api.manager.APIManager
import com.roadmate.exe.api.response.MoreServicesMaster
import com.roadmate.exe.api.response.MoreServicesTrans
import com.roadmate.exe.api.service.ApiServices
import com.roadmate.exe.constants.FragmentConstants
import com.roadmate.exe.extensions.doIfTrue
import com.roadmate.exe.extensions.elseDo
import com.roadmate.exe.extensions.toast
import com.roadmate.exe.utils.NetworkUtils
import kotlinx.android.synthetic.main.fragment_add_shop_first.*
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*


class AddShopFragmentFirst : BaseFragment(), View.OnClickListener {
    var shopTypeId:String?=""
    var check_status:String?="1"

    private fun  populateServicesList(list: ArrayList<MoreServicesTrans>){

        val dialog: android.app.AlertDialog
        val builder = android.app.AlertDialog.Builder(activity)
        val layoutInflater = layoutInflater

        val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(view)
        dialog = builder.create()
        val textView = view.findViewById<TextView>(R.id.alert_heading)
        textView.text = "Select Shop Type:"
        val recyclerView: RecyclerView = view.findViewById(R.id.customAlertRecyclerView)
        dialog.show()
        recyclerView.layoutManager =
            LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        val serviceAdapter = ShopTypesAdapter(activity!!, list){ shopId, shopName ->
            //pass id to save
            if(shopId!=null) {
                shopTypeId = shopId

                edt_shoptype.text = shopName
                dialog.dismiss()
            }
        }
        recyclerView.adapter = serviceAdapter

    }

    private fun getShopTypesList(){
        showProgress(true)
        lifecycleScope.launch{
            val response =  APIManager.call<ApiServices, Response<MoreServicesMaster>> {
                getShopTypes()
            }

            if (response.isSuccessful && response.body()?.data!!.isNotEmpty()){
                populateServicesList(response.body()?.data!!)
            }else{
                activity!!.toast {
                    message = "Services not available at this moment!"
                    duration = Toast.LENGTH_LONG }
            }
            showProgress(false)
        }
    }

    private fun isInputsValid(): Boolean {
        if (edt_username.text.toString() == "") {
            edt_username.error = "Enter ShopName"
            return false
        } else if (edt_number.text.toString() == "") {
            edt_number.error = "Enter mobile number"
            return false
        } else if (shopTypeId.isNullOrEmpty()) {
            edt_number.error = "Select a shop category"
            return false
        }
        return true
    }

    private fun processRequest() {
        if (isInputsValid()) {
            val mMap = HashMap<String, String>()
            mMap["shoptypeId"] = shopTypeId!!
            mMap["check_status"] = check_status!!
            mMap["shopnameReg"] = edt_username.text.toString()
            mMap["shopMobile"] = edt_number.text.toString()
            if (edt_landline.text.toString().trim().isNullOrEmpty()){
                mMap["shoplandline"] = "0"
            }else{
                mMap["shoplandline"] = edt_landline.text.toString()
            }
            if (edt_description.text.toString().trim().isNullOrEmpty()){
                mMap["shopdesc"] = "0"
            }else{
                mMap["shopdesc"] = edt_description.text.toString()
            }
            if (edt_opentime.text.toString().trim().isNullOrEmpty()){
                mMap["shop_open_time"] = "0"
            }else{
                mMap["shop_open_time"] = edt_opentime.text.toString()
            }
            if (edt_closetime.text.toString().trim().isNullOrEmpty()){
                mMap["shop_close_time"] = "0"
            }else{
                mMap["shop_close_time"] = edt_closetime.text.toString()
            }

            val bundle = Bundle()
            bundle.putSerializable("regMap", mMap)
            bundle.putString("type", arguments!!["type"].toString())

            setFragment(AddShopFragmentSecond(), FragmentConstants.ADD_SHOP_FRAGMENT_SECOND, bundle, false, R.id.container)
        }
    }

    private fun openTermsAndConditions() {
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.terms_and_conditions, null)
        dialogBuilder.setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.setCancelable(true)
        dialogView.findViewById<Button>(R.id.button).setOnClickListener { alertDialog.cancel() }
        alertDialog.show()
    }

    private fun showProgress(visible: Boolean) {
        if (visible) {
            spinBar.visibility = View.VISIBLE
        } else {
            spinBar.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_shop_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_save.setOnClickListener(this)
        tandclayout.setOnClickListener(this)
        edt_shoptype.setOnClickListener(this)
        edt_opentime.setOnClickListener(this)
        edt_closetime.setOnClickListener(this)

        if (arguments!!["type"].toString() == "new"){
            relativeLayout6.visibility = View.VISIBLE
            relativeLayout4.visibility = View.VISIBLE
        }else{
            relativeLayout6.visibility = View.GONE
            relativeLayout4.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                processRequest()
            }
            R.id.edt_shoptype -> {
                NetworkUtils.isNetworkConnected(activity!!).doIfTrue {
                    getShopTypesList()
                } elseDo {
                    activity!!.toast {
                        message = "No Internet connectivity!"
                        duration = Toast.LENGTH_SHORT
                    }
                }
            }
            R.id.tandclayout -> {
                openTermsAndConditions()
            }
            R.id.edt_opentime -> {

                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(
                    activity,
                    { timePicker, selectedHour, selectedMinute ->
                        val AM_PM: String
                        AM_PM = "am"
                        /*if(selectedHour < 12) {

                                        } else {
                                            AM_PM = "pm";
                                        }*/edt_opentime.text =
                        "$selectedHour:$selectedMinute $AM_PM"
                    }, hour, minute, true
                ) //Yes 24 hour time

                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
            R.id.edt_closetime -> {

                // TODO Auto-generated method stub
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(
                    activity,
                    { timePicker, selectedHour, selectedMinute ->
                        val AM_PM: String
                        AM_PM = "pm"
                        edt_closetime.text = "$selectedHour:$selectedMinute $AM_PM"
                    }, hour, minute, true
                ) //Yes 24 hour time

                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
            else -> null
        }
    }
}