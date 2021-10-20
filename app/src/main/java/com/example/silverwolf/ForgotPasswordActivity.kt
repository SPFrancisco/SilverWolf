package com.example.silverwolf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.silverwolf.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding

    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener{
            email = binding.forgotPasswordEmailEdit.text.toString().trim()
            if(TextUtils.isEmpty(binding.forgotPasswordEmailEdit.text.toString())){
                binding.forgotPasswordEmailEdit.error = "This field is required"
            }
            else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.forgotPasswordEmailEdit.error = "Invalid email formal"
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnSuccessListener {

                        Toast.makeText(this,"Reset password email send to: $email", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this,LoginActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener{ e->

                        Toast.makeText(this, "Can't send the reset password email due to ${e.message}", Toast.LENGTH_SHORT).show()

                    }
            }
        }
    }
}