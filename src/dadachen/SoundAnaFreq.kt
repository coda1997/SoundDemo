package dadachen

import java.io.File
import javax.sound.sampled.AudioSystem


fun main() {
    val soundFile = "src/dadachen/wav/fmcw_high.wav"
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
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
            val frame = myStft.getFrame(frAddr)
            index.add(frame.indexOf(frame.max()?:0.0))
        }
    }
    val stft = STFT(audioInputStream, 4096, 10, 1)
    stft.addFrameListener(listener)
    stft.start()
}