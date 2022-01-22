package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityGitHubLoginBinding
import com.google.firebase.auth.OAuthProvider
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FireFoodStore
import timber.log.Timber.i
import java.util.*
import kotlin.concurrent.thread


class GitHubLoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var inputEmail : EditText
    private lateinit var btnLogin : Button
    private lateinit var binding: ActivityGitHubLoginBinding

    lateinit var app: MainApp

    private var emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z0-9._-]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX GitHubLoginActivity.onCreate()")
        binding = ActivityGitHubLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputEmail = findViewById(R.id.inputEmail)
        btnLogin = findViewById(R.id.btnLogin)
        mAuth = FirebaseAuth.getInstance()

        app = application as MainApp

        btnLogin.setOnClickListener{
            i("ALEX button click")
            val email = inputEmail.text.toString()
            if (!email.matches(emailPattern.toRegex()))
                i("ALEX no matching pattern")
            else {
                i("ALEX matching pattern")
                val provider = OAuthProvider.newBuilder("github.com")
                // Target specific email with login hint.
                provider.addCustomParameter("login", email)


                i("ALEX after init")
                val pendingResultTask = mAuth.pendingAuthResult
                i("ALEX $pendingResultTask")
                if (pendingResultTask != null) {
                    pendingResultTask.addOnSuccessListener {
                        i("ALEX pendingResultTask?.addOnSuccessListener")
                        // User is signed in.
                        // IdP data available in
                        // authResult.getAdditionalUserInfo().getProfile().
                        // The OAuth access token can also be retrieved:
                        // authResult.getCredential().getAccessToken().
                    }.addOnFailureListener {
                        i("ALEX pendingResultTask?.addOnFailureListener")
                    }

                }else {
                    i("ALEX resultTask = null")
                    mAuth.startActivityForSignInWithProvider( /* activity= */this, provider.build())
                        .addOnSuccessListener {
                            i("ALEX mAuth.addOnSuccessListener")
                            openNextActivity()
                        }
                        .addOnFailureListener {
                            // Handle failure.
                            i("ALEX mAuth.addFailuresListener")
                        }
                }
            }

        }

    }

    private fun openNextActivity() {
        i("ALEX GitHubLoginActivity.openNextActivity()")

        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid


        val day = String.format("%tF", GregorianCalendar())

        thread(start = true) {
            try {
                app.consumedFoods = FireFoodStore(applicationContext, uid.toString(), day)
                app.foods = FireFoodStore(applicationContext, "foods", null)

                Tasks.await((app.consumedFoods as FireFoodStore).getQueryTask())
                Tasks.await((app.foods as FireFoodStore).getQueryTask())

                i("ALEX GoogleLoginActivity go to MainMenuActivity")
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            }catch(e: Exception){
                i("ALEX got exception ${e.message}")
            }
        }

    }
}