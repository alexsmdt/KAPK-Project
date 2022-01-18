package oth.wit.kapk_project.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.airbnb.lottie.LottieAnimationView
import oth.wit.kapk_project.R


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var appname : TextView
    lateinit var lottie : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        appname = findViewById(R.id.appname)
        lottie = findViewById(R.id.lottie)

        appname.animate().translationY(-1700F).setDuration(2700).startDelay = 0
        //lottie.animate().translationX(2000F).setDuration(2000).startDelay = 2900

        Handler(Looper.getMainLooper()).postDelayed({
            val launcherIntent = Intent(this, MainMenuActivity::class.java)
            refreshIntentLauncher.launch(launcherIntent)
        }, 5000)

        registerRefreshCallback()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { }
    }
}