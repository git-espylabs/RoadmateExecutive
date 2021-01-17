package com.roadmate.exe.ui.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.roadmate.exe.R
import com.roadmate.exe.constants.FragmentConstants
import com.roadmate.exe.location.AppLocation
import com.roadmate.exe.ui.fragments.ExecutiveHomeFragment
import com.roadmate.exe.utils.PermissionUtils
import com.roadmate.exe.utils.PermissionUtils.Companion.ACCESS_FINE_LOCATION
import com.roadmate.exe.utils.PermissionUtils.Companion.EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE

class ExecutiveHomeActivity : BaseActivity() {

    var fragment: Fragment? = null
    var fragmentTag = ""

    private fun exitByBackKey() {
        AlertDialog.Builder(this)
            .setMessage("Do you want to exit from Roadmate?")
            .setPositiveButton("Yes") { _, _ ->
                finish()
            }
            .setNegativeButton(
                "No"
            ) { _, _ -> }
            .show()
    }

    private fun askAppLocationPermission(){
        if (PermissionUtils.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            askAppStoragePermission()
        }else{
            if (PermissionUtils.isPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                PermissionUtils.requestPermissionFromActivity(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionUtils.ACCESS_FINE_LOCATION)
            }else{
                PermissionUtils.requestPermissionFromActivity(this, Manifest.permission.ACCESS_FINE_LOCATION, PermissionUtils.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun askAppStoragePermission(){
        if (!PermissionUtils.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (PermissionUtils.isPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                PermissionUtils.requestPermissionFromActivity(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE)
            }else{
                PermissionUtils.requestPermissionFromActivity(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE)
            }
        }else{
            AppLocation().checkLocationSettings(this)
        }
    }

    private fun showLocationSettingsWarning(){
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this).create()
        alertDialog.setTitle("Location settings warning!")
        alertDialog.setMessage("Your Location settings are not set to High accuracy. You may not be able to get precise location at times.")
        alertDialog.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Check Settings"
        ) { dialog, _ -> AppLocation().checkLocationSettings(this)
            dialog.dismiss()}
        alertDialog.setButton(
            androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "Dismiss"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setLogo(R.mipmap.ic_launcher_round)
        supportActionBar!!.setDisplayUseLogoEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.elevation = 0F;

        askAppLocationPermission()

        setFragment(ExecutiveHomeFragment(), FragmentConstants.HOME_FRAGMENT, null, false)
    }

    override fun onBackPressed() {
        exitByBackKey()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_top_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_search -> {

            }
            R.id.menu_notification -> {

            }
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESS_FINE_LOCATION -> {
                askAppStoragePermission()
            }
            EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE -> {
                AppLocation().checkLocationSettings(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1024 && resultCode != Activity.RESULT_OK){
            showLocationSettingsWarning()
        }
    }
}