package dadachen

import java.io.File
import java.io.RandomAccessFile
import java.util.*
import javax.sound.sampled.AudioSystem


fun main() {
    val file = File("data.csv")
    if (!file.exists()){
        file.createNewFile()
    }
    val raf = RandomAccessFile(file, "rwd")
    val soundFile = "src/dadachen/fmcw_low.wav"
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
    val index = mutableListOf<Int>()
    val lowIndexRange = 1485 until 1670
    val highIndexRange = 1700 until 1900
    val listener = FrameListener { myStft, frAddr ->
        if (frAddr == -1L) {
            System.err.println("ana finished")
            myStft.stop()
            println("size: ${index.size}")
            raf.close()
        } else {
            val frame = myStft.getFrame(frAddr)

            val index = frame.indexOf(frame.max()?:0.0)
            println(index)


            val highFrame = frame.filterIndexed { index, _ ->
                index in highIndexRange
            }
            val lowFrame = frame.filterIndexed { index, _ ->
                index in lowIndexRange
            }
            val lowIndex = lowFrame.indexOf(lowFrame.max() ?: 0.0) + 1485
            val highIndex = highFrame.indexOf(highFrame.max() ?: 0.0) + 1700
            val element = Pair(lowIndex, highIndex)
            val message = "${element.first}, ${element.second}\n"
//            println(message)
            raf.write(message.toByteArray())
        }
    }
    val stft = STFT(audioInputStream, 4096, 10, 1)
    stft.addFrameListener(listener)
    stft.start()
}