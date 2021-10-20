package com.example.silverwolf

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.silverwolf.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    // viewBinding
    private lateinit var binding : ActivitySignupBinding

    // ActionBar
    private lateinit var actionBar: ActionBar

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FirebaseAuth
    private lateinit var firebaseAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    // Database
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        //enable back button
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // init database
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Handle click, begin signup
        binding.btnSignUp.setOnClickListener{
            // validate data
            validateData()
        }
    }

    private fun validateData() {
        // get data
        email = binding.signUpEmailEdit.text.toString().trim()
        password = binding.signUpPasswordEdit.text.toString().trim()

        // validate data
        if(TextUtils.isEmpty(binding.signUpNameEdit.text.toString())){
            binding.signUpNameEdit.error = "This field is required"
        }
        else if(TextUtils.isEmpty(binding.signUpLastNameEdit.text.toString())){
            binding.signUpLastNameEdit.error = "This field is required"
        }
        else if(TextUtils.isEmpty(binding.signUpPhoneNumberEdit.text.toString())){
            binding.signUpPhoneNumberEdit.error = "This field is required"
        }
        else if(TextUtils.isEmpty(binding.signUpEmailEdit.text.toString())){
            binding.signUpEmailEdit.error = "This field is required"
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                // invalid email format
                binding.signUpEmailEdit.error = "Invalid email formal"
            }
        }
        else if(TextUtils.isEmpty(password)){
            // password isn't entered
            binding.signUpPasswordEdit.error = "Please enter password"
        }
        else if(password.length < 6){
            // password length is less than 6
            binding.signUpPasswordEdit.error = "Password must at least have 6 characters long"
        }
        else if(binding.signUpPasswordEdit.text.toString().trim() != binding.signUpRePasswordEdit.text.toString().trim()){
            binding.signUpRePasswordEdit.error = "Passwords don't match, try again"
        }
        else{
            // data is valid, continue signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // show progress
        progressDialog.show()

        // add user to database
        val userId = database.push().key.toString()
        val user = UserDto(userId,
            binding.signUpNameEdit.text.toString(),
            binding.signUpLastNameEdit.text.toString(),
            binding.signUpPhoneNumberEdit.text.toString(),
            binding.signUpEmailEdit.text.toString(),
            binding.signUpGenderEdit.text.toString(),
            binding.signUpDateOfBirthEdit.text.toString(),
            binding.signUpCountryEdit.text.toString(),
            binding.signUpProvinceEdit.text.toString(),
            binding.signUpAddressEdit.text.toString(),
            binding.signUpPasswordEdit.text.toString()
        )

        database.child(userId).setValue(user).addOnSuccessListener{

            // create account
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    // signup success
                    progressDialog.dismiss()
                    // get current user
                    val firebaseUser = firebaseAuth.currentUser
                    val email = firebaseUser!!.email
                    Toast.makeText(this,"Account created with email $email", Toast.LENGTH_SHORT).show()

                    // open profile
                    startActivity(Intent(this,ProfileActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e->
                    // signup failed
                    progressDialog.dismiss()
                    Toast.makeText(this, "SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener { e->
            // database user add failed
            progressDialog.dismiss()
            Toast.makeText(this, "Database register failed due to ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when button of actionbar clicked
        return super.onSupportNavigateUp()
    }
}