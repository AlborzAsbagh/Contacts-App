package alborz.rad.mycontacts.Activities

import alborz.rad.mycontacts.R
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        var intent = Intent(this,MainActivity::class.java)
        var timer= Timer()
        timer.schedule(timerTask {
            startActivity(intent)
            finish()
        },2000)
    }
}
