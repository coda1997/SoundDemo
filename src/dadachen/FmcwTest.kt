package dadachen

import dadachen.wavutils.WavFile
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.spi.AudioFileWriter


fun calculateShift() {

    val path1 = "src/dadachen/wav/fmcw_test1.wav"
    val path2 = "src/dadachen/wav/fmcw_test2.wav"
    val s1 = getInputFileByShortArray(path1)
    val s2 = getInputFileByShortArray(path2)
    val outputData = s1 * s2
//    println(s1.size)
//    println(outputData.size)
    val s = "src/dadachen/wav/fmcw_output.wav"
    val wav = WavFile.newWavFile(File(s),1,outputData.size.toLong(),16,48000)
    wav.writeFrames(outputData,0, outputData.size)
    wav.close()
}

val getInputFileByShortArray = { path: String ->
    val f = File(path)
    val input = f.readBytes()
    byteArrayToShortArray(input)
}

fun ShortArray.toByteArray() = ByteArray(this.size).apply {
    this@toByteArray.forEachIndexed { index, sh ->
        this[index] = sh.toByte()
    }
}


operator fun ShortArray.times(op2: ShortArray): IntArray {
    val res = IntArray(this.size)
    for (i in this.indices) {
        res[i] = (this[i] * op2[i])
    }
    return res
}

fun byteArrayToShortArray(data: ByteArray): ShortArray {
    val shortArray = ShortArray(data.size / 2)
    for (i in 0 until data.size / 2) {
        shortArray[i] = bytesToShort(data[2 * i], data[2 * i + 1])
    }
    return shortArray
}

fun bytesToShort(b1: Byte, b2: Byte): Short {
    val bb: ByteBuffer = ByteBuffer.allocate(2)
    bb.order(ByteOrder.LITTLE_ENDIAN)
    bb.put(b1)
    bb.put(b2)
    return bb.getShort(0)
}

fun main() {
    calculateShift()
}