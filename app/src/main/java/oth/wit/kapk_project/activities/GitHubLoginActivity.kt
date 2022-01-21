package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import oth.wit.kapk_project.R
import oth.wit.kapk_project.databinding.ActivityGitHubLoginBinding
import oth.wit.kapk_project.databinding.ActivityGoogleLoginBinding
import com.google.firebase.auth.OAuthProvider
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.auth.AuthResult

import com.google.android.gms.tasks.OnSuccessListener

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FireFoodStore
import timber.log.Timber.i
import java.util.*


class GitHubLoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    lateinit var inputEmail : EditText
    lateinit var btnLogin : Button
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
            var email = inputEmail.text.toString()
            if (!email.matches(emailPattern.toRegex()))
                i("ALEX no matching pattern")
            else {
                i("ALEX matching pattern")
                val provider = OAuthProvider.newBuilder("github.com")
                // Target specific email with login hint.
                provider.addCustomParameter("login", email)

                val scopes: ArrayList<String?> = object : ArrayList<String?>() {
                    init {
                        i("ALEX init")
                        add("user:email")
                    }
                }

                i("ALEX after init")
                //provider.scopes = scopes
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
                            i("ALEX mauth.addOnSuccessListener")
                            openNextActivity()
                        }
                        .addOnFailureListener {
                            // Handle failure.
                            i("ALEX mauth.addFailuresListener")
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
        //app.consumedFoods = FireFoodStore(applicationContext, uid.toString(), day)
        //app.foods = FireFoodStore(applicationContext, "foods", null)

        Thread(Runnable {
            try {
                app.consumedFoods = FireFoodStore(applicationContext, uid.toString(), day)
                app.foods = FireFoodStore(applicationContext, "foods", null)

                Tasks.await((app.consumedFoods as FireFoodStore).getQueryTask())
                Tasks.await((app.foods as FireFoodStore).getQueryTask())

                i("ALEX GoogleLoginActivity go to MainMenuActivity")
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }catch(e: InterruptedException){}
        }).start()

        /*Handler(Looper.getMainLooper()).postDelayed({
            i("ALEX GitHubLoginActivitygo to MainMenuActivity")
            val intent = Intent(this, MainMenuActivity::class.java)

            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }, 1000)*/
    }
}