package oth.wit.kapk_project.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import oth.wit.kapk_project.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import oth.wit.kapk_project.databinding.ActivityGoogleLoginBinding
import oth.wit.kapk_project.main.MainApp
import oth.wit.kapk_project.models.FireFoodStore
import timber.log.Timber.i
import java.util.*
import kotlin.concurrent.thread


class GoogleLoginActivity : AppCompatActivity() {

    companion object{
        private const val RC_SIGN_IN = 999
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityGoogleLoginBinding
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        i("ALEX GoogleLoginActivity.onCreate()")
        binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        app = application as MainApp

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_auth_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        mAuth = FirebaseAuth.getInstance()

        binding.googleSignInButton.setOnClickListener{
            signIn()
        }

        binding.githubSignInButton.setOnClickListener {
            i("ALEX GoogleLoginActivity go to GitHubLoginActivity")
            val intent = Intent(this, GitHubLoginActivity::class.java)
            startActivity(intent)
        }
        i("ALEX end of GoogleLoginActivity.onCreate()")

    }

    private fun signIn() {

        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()

        i("ALEX GoogleLoginActivity.onStart()")
        app = application as MainApp

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
        i("ALEX end of GoogleLoginActivity.onStart()")
    }

    private fun updateUI(user: FirebaseUser?) {
        i("ALEX GoogleLoginActivity.updateUI()")
        if(user == null){
            i("ALEX user is null")
            return
        }

        val currentUser = mAuth.currentUser
        val uid = currentUser?.uid

        val day = String.format("%tF", GregorianCalendar())


        thread(start = true) {
            try {
                i("ALEX GoogleLoginActivity.thread.run()")
                app.consumedFoods = FireFoodStore(applicationContext, uid.toString(), day)
                app.foods = FireFoodStore(applicationContext, "foods", null)

                Tasks.await((app.consumedFoods as FireFoodStore).getQueryTask())
                Tasks.await((app.foods as FireFoodStore).getQueryTask())

                i("ALEX GoogleLoginActivity go to MainMenuActivity")
                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
                i("ALEX end of GoogleLoginActivity.thread.run()")
            }catch(e: Exception){
                i("ALEX got exception ${e.message}")
            }
        }


        i("ALEX end of GoogleLoginActivity.updateUI()")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        i("ALEX GoogleLoginActivity.onActivityResult()")

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if(task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    i("firebaseAuthWithGoogle:%s", account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    i("Google sign in failed")
                }
            }else{
                i("ALEX task not successful")
            }
        }
        i("ALEX end of GoogleLoginActivity.onActivityResult()")
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        i("ALEX GoogleLoginActivity.firebaseAuthWithGoogle()")
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    i("signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    i("signInWithCredential:failure")

                    updateUI(null)
                }
            }
        i("ALEX end of GoogleLoginActivity.firebaseAuthWithGoogle()")
    }
}


