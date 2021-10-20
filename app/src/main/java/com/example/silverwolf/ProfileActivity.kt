package com.example.silverwolf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.example.silverwolf.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity() {

    // viewBinding
    private lateinit var binding: ActivityProfileBinding

    // ActionBar
    private lateinit var actionBar: ActionBar

    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    // Database
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        // init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // init database
        database = FirebaseDatabase.getInstance().getReference("Users")
        checkUser()

        // Handle click logout
        binding.btnLogout.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        // check user is logged in or nor
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            // user not null, user is logged in, get user info
            val email = firebaseUser.email.toString()
            // set to text view
//            binding.profileEmailTitle.text = email
            getUserData(email)
        }
        else{
            // user is null, user is nor logged in, goto login activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun getUserData(email: String) {

        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue<UserDto>() }.toList()
                val user = users.find { x -> x.email == email }
                if (user != null){
                    setUserData(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setUserData(user: UserDto) {

        binding.profileNameEdit.setText(user.name)
        binding.profileLastNameEdit.setText(user.lastName)
        binding.profilePhoneNumberEdit.setText(user.phoneNumber)
        binding.profileEmailEdit.setText(user.email)
        binding.profileGenderEdit.setText(user.gender)
        binding.profileDateOfBirthEdit.setText(user.dateOfBirth)
        binding.profileCountryEdit.setText(user.country)
        binding.profileProvinceEdit.setText(user.province)
        binding.profileAddressEdit.setText(user.address)
    }
}