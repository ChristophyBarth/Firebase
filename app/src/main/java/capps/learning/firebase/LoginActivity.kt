package capps.learning.firebase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import capps.learning.firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var emailRegex: Regex

    private val tag = "LoginActivity"

    override fun onStart() {
        super.onStart()

        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")

        binding.login.setOnClickListener {
            loginUser(binding.emailEt.text.toString(), binding.passwordEt.text.toString())
        }

        binding.signUpInstead.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        if (password.isBlank() || password.length < 8) {
            binding.passwordEt.error = "password too short"
            return
        }
        if (password.isBlank() || !emailRegex.matches(email)) {
            binding.emailEt.error = "input a valid email"
            return
        }

        //Email and password is valid.
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    Log.e(tag, "loginUser failed: ${it.exception?.message}")
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e(tag, "loginUser exception: $e")
        }
    }
}