package

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chaapter4.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    var total = 0
    var started = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStart.setOnClickListener {
            start()
        }
        binding.buttonStop.setOnClickListener {
            stop()
        }
        binding.buttonEnd.setOnClickListener {
            end()
        }
    }

    fun start() {
        if (!started) {
            started = true
            thread(start=true) {
                while (started) {
                    Thread.sleep(1000)
                    total += 1
                    runOnUiThread {
                        binding.textTimer.text = formatTime(total)
                    }
                }
            }
        }
    }

    fun stop() {
        started = false
    }

    fun end() {
        started = false
        total = 0
        binding.textTimer.text = formatTime(total)
    }

    private fun formatTime(total: Int): String {
        val minutes = total / 60
        val seconds = total % 60
        return String.format("%02d : %02d", minutes, seconds)
    }

}