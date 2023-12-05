package com.modernstore.app.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.modernstore.app.MainActivity
import com.modernstore.app.R
import com.modernstore.app.db.preferencemanager.SharedPreferencesHelper
import com.modernstore.app.db.roomdb.AppDatabase
import com.modernstore.app.ui.login.LoginActivity
import com.modernstore.app.ui.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountFragment : Fragment() {
    private lateinit var sharedpreferences : SharedPreferencesHelper
    private lateinit var appDatabase: AppDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.account_fragment, container, false)
        sharedpreferences = SharedPreferencesHelper(requireContext())
        appDatabase = AppDatabase.getInstance(requireContext())
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
        val logoutButton : TextView = rootView.findViewById(R.id.logoutbutton)
        logoutButton.setOnClickListener {
            sharedpreferences.clearPreferences()
            sharedpreferences.LoggedIn(false)
            val i = Intent(requireActivity(), MainActivity::class.java)
            startActivity(i)
        }
        showUserData(rootView)
        return rootView
    }
    private fun showUserData(rview: View){
        try {
            CoroutineScope(Dispatchers.Main).launch {
                val user: String = sharedpreferences.getUserLogged()
                val balance: Double = withContext(Dispatchers.IO) {
                    appDatabase.userDao().getBalanceByUsername(user)
                }
                val userView: TextView = rview.findViewById(R.id.showuser)
                val balanceView: TextView = rview.findViewById(R.id.showbalance)
                userView.text = user
                balanceView.text = "$ ${balance.toString()}"
            }
        } catch (e: Exception) {
            Log.d("Coroutine Error:", "Error with ${e.message}")
        }
    }
}
