package dadachen

import java.io.File
import javax.sound.sampled.AudioSystem

fun printFreqDomain(path: String) {
    val soundFile = path
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
    val indices = mutableListOf<Pair<Int, Int>>()
    val lowIndexRange = 1620 until 1820
    val highIndexRange = 1850 until 2049
    val listener = FrameListener { myStft, frAddr ->
        if (frAddr == -1L) {
            myStft.stop()
            var count = 0
            var hcount = 0
            var isone = true
            var indexOne = 0
            var indexSec = 0
            var indexHigh = 0
            var T = 882
            indices.forEachIndexed { index, i ->
                if (index > 0) {
                    count++
                    hcount++
                    if (count > 600 && indices[index - 1].first > i.first) {
//                        println("low : from ${i.first} to ${indices[index - 1].first}, count: $count, time in index:${index}")
                        if(isone){
                            indexOne = index
                        }else{
                            indexSec = index
                            val interval = ((indexSec-indexHigh+indexOne-indexHigh)+T)/2.0
                            println("low: ($indexOne, $indexSec), high: $indexHigh, interval: $interval, time: ${1.0/4410*interval}")
                        }
                        isone = isone.toggle()
                        count = 0
                    }

                    if (hcount > 600 && indices[index - 1].second > i.second) {
//                        println("high: from ${i.second} to ${indices[index - 1].second}, time in index:${index}")
                        indexHigh = index
                        hcount = 0
                    }

                } else {
                    println("the total size is ${indices.size}")
                    println("the row num is $i")

                    count++
                }
            }


        } else {
            val frame = myStft.getFrame(frAddr)
            val highFrame = frame.filterIndexed { index, _ ->
                index in highIndexRange
            }
            val lowFrame = frame.filterIndexed { index, _ ->
                index in lowIndexRange
            }
            val lowIndex = lowFrame.indexOf(lowFrame.max() ?: 0.0) + 1620
            val highIndex = highFrame.indexOf(highFrame.max() ?: 0.0) + 1850
            val element = Pair(lowIndex, highIndex)
            indices.add(element)
        }
    }
    val stft = STFT(audioInputStream, 4096, 10, 900)
    stft.addFrameListener(listener)
    stft.start()
}

fun Boolean.toggle():Boolean = !this

fun main() {
    val isLow = false
    val path = "src/dadachen/wav/fmcw_${if (isLow) "low" else "high"}.wav"
    val testPath = "src/dadachen/wav/test.wav"
    printFreqDomain(testPath)
}