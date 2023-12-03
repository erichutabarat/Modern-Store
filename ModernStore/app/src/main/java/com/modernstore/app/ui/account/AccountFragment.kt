package com.modernstore.app.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.db.preferencemanager.SharedPreferencesHelper
import com.modernstore.app.ui.login.LoginActivity
import com.modernstore.app.ui.register.RegisterActivity

class AccountFragment : Fragment() {
    private lateinit var sharedpreferences : SharedPreferencesHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.account_fragment, container, false)
        sharedpreferences = SharedPreferencesHelper(requireContext())
        rootView.findViewById<View>(R.id.guestViews).visibility = if (sharedpreferences.getLoggedIn()) View.GONE else View.VISIBLE
        rootView.findViewById<View>(R.id.userViews).visibility = if (sharedpreferences.getLoggedIn()) View.VISIBLE else View.GONE
        val loginButton : Button = rootView.findViewById(R.id.fromguest_tologin)
        val registerButton : Button = rootView.findViewById(R.id.fromguest_toregister)
        loginButton.setOnClickListener {
            val i = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(i)
        }
        registerButton.setOnClickListener {
            val i = Intent(requireActivity(), RegisterActivity::class.java)
            startActivity(i)
        }
        val logoutButton : Button = rootView.findViewById(R.id.logoutbutton)
        logoutButton.setOnClickListener {
            sharedpreferences.clearPreferences()
            sharedpreferences.LoggedIn(false)
            val i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
        }
        val userShow : TextView = rootView.findViewById(R.id.showuser)
        userShow.text = sharedpreferences.getUserLogged().toString()
        return rootView
    }
}
