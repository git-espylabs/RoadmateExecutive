package com.roadmate.exe.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.roadmate.exe.R
import com.roadmate.exe.constants.FragmentConstants
import com.roadmate.exe.ui.fragments.AddShopFragmentFirst
import com.roadmate.exe.ui.fragments.AddShopFragmentSecond
import com.roadmate.exe.ui.fragments.ShopDetailsFragment

class AddShopActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Add Shop"

        setFragment(AddShopFragmentFirst(), FragmentConstants.ADD_SHOP_FRAGMENT_FIRST, intent.extras, false)
    }

    override fun onBackPressed() {
        var fragment: AddShopFragmentSecond? = supportFragmentManager.findFragmentByTag(FragmentConstants.ADD_SHOP_FRAGMENT_SECOND) as? AddShopFragmentSecond
        if (fragment is AddShopFragmentSecond){
            supportFragmentManager.popBackStack()
        }else{
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}