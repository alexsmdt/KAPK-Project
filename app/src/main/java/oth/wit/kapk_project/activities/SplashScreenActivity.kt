package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.lottie.LottieAnimationView
import oth.wit.kapk_project.R
import timber.log.Timber.i


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var appName : TextView
    private lateinit var lottie : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        i("ALEX SplashScreenActivity.onCreate()")
        appName = findViewById(R.id.appname)
        lottie = findViewById(R.id.lottie)


        appName.animate().translationY(-1700F).setDuration(2700).startDelay = 0


        Handler(Looper.getMainLooper()).postDelayed({
            i("ALEX SplashScreenActivity go to GoogleLoginActivity")
            val launcherIntent = Intent(this, GoogleLoginActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
            finish()
        }, 5000)

        registerRefreshCallback()
    }

    private fun registerRefreshCallback() {
        i("ALEX SplashScreenActivity.registerRefreshCallback()")
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }

}