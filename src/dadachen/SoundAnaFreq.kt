package dadachen

import java.io.File
import javax.sound.sampled.AudioSystem


fun main() {
    val soundFile = "src/dadachen/wav/fmcw_output.wav"
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
    println(audioInputStream.frameLength)
    val index = mutableListOf<Int>()
    val listener = FrameListener { myStft, frAddr ->
        if (frAddr == -1L) {
            System.err.println("ana finished")
            myStft.stop()
            var count = 0
            index.forEachIndexed { id, i ->
                count++
                if(id>0){
                    if(index[id-1]>i){
                        println("$i size:$count and pre: ${index[id-1]}")
                        count=0
                    }
                }else{
                    print(i)
                    count=0
                }
            }
            println("size: ${index.size}")
        } else {
            val frame = myStft.getFrame(frAddr).filterIndexed { index, d -> index<100 }
            val element = frame.indexOf(frame.max() ?: 0.0)
            println(element)
//            index.add(element)
        }
    }
    val stft = STFT(audioInputStream, 4096, 4096/6, 1)
    stft.addFrameListener(listener)
    stft.start()
}