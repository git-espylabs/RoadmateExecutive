package com.roadmate.exe.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.roadmate.exe.R
import com.roadmate.exe.constants.FragmentConstants
import com.roadmate.exe.ui.fragments.ShopDetailsFragment
import com.roadmate.exe.ui.fragments.ShopListFragment

class ShopDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Details"

        setFragment(ShopDetailsFragment(), FragmentConstants.SHOP_DETAILS_FRAGMENT, intent.extras!!, false)
    }

    override fun onBackPressed() {
        finish()
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