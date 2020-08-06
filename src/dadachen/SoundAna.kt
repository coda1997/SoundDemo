package dadachen

import java.io.File
import javax.sound.sampled.AudioSystem


fun main() {
    val soundFile = "src/dadachen/fmcw-high.wav"
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
    val indices = mutableListOf<Int>()
    val listener = FrameListener { myStft, frAddr ->
        if (frAddr == -1L) {
            System.err.println("ana finished")
            myStft.stop()
            var count = 0
            indices.forEachIndexed { index, i ->
                if(index>0){
                    count++
                    if(indices[index-1]>i){
                        println("the row num is $i, and the size is $count")
                        count = 0
                    }
                }else{
                    println("the total size is ${indices.size}")
                    count++
                }
            }


        } else {
            val frame = myStft.getFrame(frAddr)
            val index = frame.indexOf(frame.max() ?: 0.0)
            indices.add(index)
//            println(index)
//            println(Arrays.toString(frame))
        }
    }
    val stft = STFT(audioInputStream, 4096, 10, 1024)
    stft.addFrameListener(listener)
    stft.start()
}