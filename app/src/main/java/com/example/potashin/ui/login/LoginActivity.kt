package com.example.potashin.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.example.potashin.R
import com.example.potashin.Wallets
import com.example.potashin.data.Api
import com.example.potashin.data.model.Credentials
import com.example.potashin.data.model.User

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//            )
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE

            val api = Api()
            GlobalScope.launch {
                val credentials = Credentials(username.text.toString(), password.text.toString());
                val result = api.sendPostRequest("user/login", credentials);
                println("RESULT $result")

                val  user = Gson().fromJson(result, User::class.java)
                println("USER ID ${user.id}")

                val wallets = Intent(applicationContext, Wallets::class.java);
                wallets.putExtra("user", user);
                startActivity(wallets);
            }
        }
    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        Toast.makeText(
                applicationContext,
                "$welcome",
                Toast.LENGTH_LONG
        ).show()

        val wallets = Intent(this, Wallets::class.java);
        startActivity(wallets);

        val api = Api()
        GlobalScope.launch {
          val result = api.sendGetRequest("user/c117deea-2e81-43a1-94f0-f815a3cff6f3");
            println("RESULT $result")

            val gson = Gson()
            val  user = gson.fromJson(result, User::class.java)
            println("USER ID ${user.id}")

        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
