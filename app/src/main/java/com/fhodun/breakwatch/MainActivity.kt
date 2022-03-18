package com.fhodun.breakwatch

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import java.time.LocalTime

val breaks = arrayOf<String>(
    "08:00:00",
    "08:45:00",
    "08:50:00",
    "09:35:00",
    "09:40:00",
    "10:25:00",
    "10:45:00",
    "11:30:00",
    "11:40:00",
    "12:25:00",
    "12:30:00",
    "13:15:00",
    "13:20:00",
    "14:05:00",
    "14:10:00",
    "14:55:00",
//    "21:37:00"
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val breakDuration = findViewById<TextView>(R.id.breakDuration)

//        while (true) {
        var nearestBreak: Int = 0
        var nearestBreakString: String = "00:00:00"

        for (brk in breaks) {
            val brkDistance: Int =
                LocalTime.parse(brk).toSecondOfDay() - LocalTime.now().toSecondOfDay()
            if (brkDistance <= 0) {
                continue
            }
            if (brkDistance > nearestBreak) {
                nearestBreak = brkDistance
                nearestBreakString = brk
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Android alert!")
        builder.setMessage("Found nearest break at $nearestBreakString and time to it is $nearestBreak")
        builder.show()

        object : CountDownTimer((nearestBreak * 1000).toLong(), 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                breakDuration.text =
                    LocalTime.ofSecondOfDay(millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                breakDuration.text = "wypad z klasy!"
            }
        }.start()
//        }
    }
}