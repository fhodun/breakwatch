package com.fhodun.breakwatch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
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
    "23:59:59",
)

class MainActivity : AppCompatActivity() {
    private lateinit var breakDuration: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        breakDuration = findViewById<TextView>(R.id.breakDuration)
        runTimer()
    }

    private fun runTimer() {
        var nearestBreak: Int =
            LocalTime.parse(breaks[breaks.size - 1]).toSecondOfDay() - LocalTime.now()
                .toSecondOfDay()
        var nearestBreakString: String = breaks[breaks.size - 1]

        for (brk in breaks) {
            val brkDistance: Int =
                LocalTime.parse(brk).toSecondOfDay() - LocalTime.now().toSecondOfDay()
            if (brkDistance <= 0) continue
            if (brkDistance < nearestBreak) {
                nearestBreak = brkDistance
                nearestBreakString = brk
            }
        }

        val message = "Found nearest break at $nearestBreakString and time to it is ${
            LocalTime.ofSecondOfDay(
                nearestBreak.toLong()
            )
        }"
        val alert = Dialog(this)
        alert.setContentView(R.layout.break_dialog)
        alert.findViewById<TextView>(R.id.message_text).text = message
        alert.create()
        alert.show()
        Handler().postDelayed({
            alert.dismiss()
        }, 5_000)

        object : CountDownTimer((nearestBreak * 1000).toLong(), 1000) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                breakDuration.text =
                    LocalTime.ofSecondOfDay(millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                breakDuration.text = "wypad z klasy!"
                Handler().postDelayed({
                    runTimer()
                }, 10_000)
            }
        }.start()
    }
}