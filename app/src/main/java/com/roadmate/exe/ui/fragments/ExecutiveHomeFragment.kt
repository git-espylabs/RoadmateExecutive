package com.roadmate.exe.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.roadmate.exe.R
import com.roadmate.exe.extensions.startActivity
import com.roadmate.exe.ui.activities.AddShopActivity
import com.roadmate.exe.ui.activities.ExecutiveAccountsActivity
import com.roadmate.exe.ui.activities.ExecutiveHomeActivity
import com.roadmate.exe.ui.activities.ShopListActivity
import kotlinx.android.synthetic.main.fragment_exe_home.*

class ExecutiveHomeFragment: Fragment(), View.OnClickListener{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exe_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnAccount.setOnClickListener(this)
        btnAddShop.setOnClickListener(this)
        btnAddedShops.setOnClickListener(this)
        btnVisitShop.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAccount ->{
                activity!!.startActivity<ExecutiveAccountsActivity>()
                activity!!.finish()
            }
            R.id.btnAddShop ->{
                val intent = Intent(context, AddShopActivity::class.java)
                intent.putExtra("type", "new")
                startActivity(intent)
            }
            R.id.btnAddedShops ->{
                val intent = Intent(context, ShopListActivity::class.java)
                intent.putExtra("type", "added_shops")
                startActivity(intent)
            }
            R.id.btnVisitShop ->{
                val intent = Intent(context, ShopListActivity::class.java)
                intent.putExtra("type", "visited_shops")
                startActivity(intent)
            }
        }
    }
}