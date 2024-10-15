package capps.learning.firebase

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import capps.learning.firebase.databinding.ActivityMainBinding
import capps.learning.firebase.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        loadCurrentUserData()

        binding.save.setOnClickListener {
            saveCurrentUserInfo(
                binding.fNameEt.text.toString(),
                binding.lNameEt.text.toString(),
                binding.usernameEt.text.toString(),
            )
        }
    }

    private fun loadCurrentUserData() {
        val currentUserID = auth.currentUser!!.uid

        firestore.collection("users").document(currentUserID).get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (it.result.exists()) {
                    val user = it.result.toObject(User::class.java)
                    binding.apply {
                        fNameEt.setText(user?.fName)
                        lNameEt.setText(user?.lName)
                        usernameEt.setText(user?.username)
                    }
                } else {
                    Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCurrentUserInfo(fName: String, lName: String, username: String) {
        if (fName.isBlank() || fName.length < 2) {
            binding.fNameEt.error = "Your name cant be this short ooooh!"
            return
        }
        if (lName.isBlank() || lName.length < 2) {
            binding.lNameEt.error = "Ah. Fill it right na"
            return
        }
        if (username.isBlank() || username.length < 2) {
            binding.usernameEt.error = "You sef nawa oh!"
            return
        }

        //Its time to save
        val user = User(auth.currentUser!!.uid, fName.trim(), lName.trim(), username.trim())
        try {
            firestore.collection("users").document(auth.currentUser!!.uid).set(user)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Data has been set successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            Log.e(tag, "loginUser exception: $e")
        }
    }
}