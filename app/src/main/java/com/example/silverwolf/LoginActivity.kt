package com.example.silverwolf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.silverwolf.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding : ActivityLoginBinding

    // ActionBar
    private lateinit var actionBar : ActionBar

    // ProgressDialog
    private lateinit var progressDialog : ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // Handle click, open forgot password activity
        binding.loginForgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        // Handle click, open register activity
        binding.noAccount.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Handle click, begin login
        binding.btnLogin.setOnClickListener{
            // Before logged in, validate data
            validateData()
        }
    }

    private fun validateData() {
        // get data
        email = binding.loginEmailEdit.text.toString().trim()
        password = binding.loginPasswordEdit.text.toString().trim()

        // validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // invalid email format
            binding.loginEmailEdit.error = "Invalid email formal"
        }
        else if(TextUtils.isEmpty(password)){
            // no password entered
            binding.loginPasswordEdit.error = "Please enter password"
        }
        else{
            // data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener{
                // login success
                progressDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"LoggedIn as $email", Toast.LENGTH_SHORT).show()

                // open profile
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // if user is already logged in go to profile activity
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            // user is already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}