package dadachen

import java.io.File
import javax.sound.sampled.AudioSystem


fun main() {
    val soundFile = "src/dadachen/fmcw-high.wav"
    val audioInputStream = AudioSystem.getAudioInputStream(File(soundFile))
    val listener = FrameListener { myStft, frAddr ->
        if (frAddr == -1L) {
            System.err.println("ana finished")
            myStft.stop()

        } else {
            val frame = myStft.getFrame(frAddr)

        }
    }
    val stft = STFT(audioInputStream, 4096, 10, 1024)
    stft.addFrameListener(listener)
    stft.start()
}