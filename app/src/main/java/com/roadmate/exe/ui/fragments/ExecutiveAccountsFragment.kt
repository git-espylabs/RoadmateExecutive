package com.roadmate.exe.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.roadmate.exe.R
import com.roadmate.exe.extensions.startActivity
import com.roadmate.exe.preference.UserDetails
import com.roadmate.exe.rmapp.AppSession
import com.roadmate.exe.ui.activities.LoginActivity
import kotlinx.android.synthetic.main.fragment_exe_account.*

class ExecutiveAccountsFragment: Fragment() {

    private fun populateDetails(){
        tvName.text = UserDetails().userName
        tvAddress.text = UserDetails().userAddress
        tvMob.text = UserDetails().userMob
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exe_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        populateDetails()

        logoutButton.setOnClickListener {
            AppSession.clearUserSession()
            activity!!.startActivity<LoginActivity>()
            activity!!.finish()
        }
    }
}