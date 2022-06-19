package com.example.parallel

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val N = 2000000
    private val NTHREADS = 8
    private var gPi = 0.0
    var gPiList : ArrayList<Double> = arrayListOf()
    var timeList : ArrayList<Long> = arrayListOf()
    var timeAndPiList : ArrayList<Map<Long,Double>> = arrayListOf()
    var piAndTimeList : ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runBlocking {
            val k = N / NTHREADS
            for (i in 0 until N step k) {
                async {
                    operation(i, i + k)
                }
            }

        }

        findViewById<Button>(R.id.buttonPi).setOnClickListener {
            for(i in gPiList){
                gPi += i
            }
            println("Pi değeri :  $gPi ")
            findViewById<TextView>(R.id.tvPi).setText(gPi.toString())
            var multi = ""
            var time : Long = 0
            for(i in piAndTimeList){
                multi += i
            }
            for(i in timeList){
                time += i
            }
            findViewById<TextView>(R.id.tvMulti).setText(multi)
            findViewById<TextView>(R.id.tvTime).setText("Toplam zaman : $time ms")
        }

    }

    private fun operation(a : Int, b : Int) {
        var x : Double
        val step = 1.0/N
        var gPiL = 0.0
        val time = measureTimeMillis {
            for(i in a..b){
                x = (i + 0.5) * step
                gPiL += step * 4.0 / (1.0 + x * x)
            }
        }
        gPiList.add(gPiL)
        timeList.add(time)
        timeAndPiList.add(mapOf(Pair(time,gPiL)))
        piAndTimeList.add("Paralle işleyen bu kısmın süresi: $time ms \n Hesaplanan Pi parçasının" +
                " değeri : $gPiL \n\n")
        println("Parallel işleyen bu kısmın süresi: $time ms ve hesaplanan Pi parçası : $gPiL")
    }
}