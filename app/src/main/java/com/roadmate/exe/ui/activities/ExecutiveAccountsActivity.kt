package com.roadmate.exe.ui.activities

import android.R
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.roadmate.exe.constants.FragmentConstants
import com.roadmate.exe.extensions.startActivity
import com.roadmate.exe.ui.fragments.CheckOtpFragment
import com.roadmate.exe.ui.fragments.ExecutiveAccountsFragment

class ExecutiveAccountsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Account"

        setFragment(ExecutiveAccountsFragment(), FragmentConstants.EXECUTIVE_ACCOUNTS_FRAGMENT, null, false)
    }

    override fun onBackPressed() {
        this.startActivity<ExecutiveHomeActivity>()
        this.finish()
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