package capps.learning.firebase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import capps.learning.firebase.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var emailRegex: Regex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")

        binding.signUp.setOnClickListener {
            signUp(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
        }
    }

    private fun signUp(email: String, password: String) {
        if (!emailRegex.matches(email)) {
            binding.emailEt.error = "invalid email"
        }

        if (password.length < 8) {
            binding.passwordEt.error = "password too short"
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                //Take them back to login.
                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_LONG).show()
                auth.currentUser?.sendEmailVerification()
                onBackPressed()
            }else{
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}