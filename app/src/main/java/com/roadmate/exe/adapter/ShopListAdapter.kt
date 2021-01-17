package com.roadmate.exe.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.roadmate.exe.BuildConfig
import com.roadmate.exe.R
import com.roadmate.exe.api.response.ShopListTrans
import com.squareup.picasso.Picasso

class ShopListAdapter  internal constructor(private val context: Context, private val mData: ArrayList<ShopListTrans>, val clickHandler: (shopId: String?) -> Unit) : RecyclerView.Adapter<ShopListAdapter.ViewHolder>()  {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mechanic_image_view: ImageView = itemView.findViewById(R.id.mechanic_image_view)
        internal var mechanic_name: TextView = itemView.findViewById(R.id.mechanic_name)
        internal var mechanic_location: TextView = itemView.findViewById(R.id.mechanic_location)
        internal var mechanic_time: TextView = itemView.findViewById(R.id.mechanic_time)
        internal var package_view: TextView = itemView.findViewById(R.id.package_view)
        internal var authStatus: TextView = itemView.findViewById(R.id.authStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.row_item_shop_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val shopData = mData[position]

        holder.mechanic_name.text = shopData.shopName
        holder.mechanic_location.text = shopData.address
        holder.mechanic_time.text = shopData.open_time + " to " + shopData.close_time

        if (shopData.authorised_status.isNotEmpty() && shopData.authorised_status == "1"){
            holder.authStatus.text = "Authorised"
        }else{
            holder.authStatus.text = "Un Authorised"
        }

        holder.package_view.setOnClickListener {
            clickHandler(shopData.shopId)
        }

        Picasso.with(context).load(BuildConfig.BANNER_URL_ENDPOINT + shopData.shopImage).into(holder.mechanic_image_view)
    }
}