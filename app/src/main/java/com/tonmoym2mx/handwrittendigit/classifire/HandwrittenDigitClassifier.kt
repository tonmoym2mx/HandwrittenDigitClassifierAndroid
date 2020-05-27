package com.tonmoym2mx.handwrittendigit.classifire
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.tonmoym2mx.handwrittendigit.classifire.data_class.Result
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class HandwrittenDigitClassifier(private val context: Context):TFLiteClassifier{
    companion object{
        private const val TAG = "DigitClassifier"
        private const val MODEL_NAME = "handwrittendigit.tflite"
        private const val FLOAT_TYPE_SIZE = 4
        private const val PIXEL_SIZE = 1
        private const val OUTPUT_CLASSES_COUNT = 10
    }
    private lateinit var imageData: ByteBuffer
    private lateinit var interpreter: Interpreter
    var MODEL_INPUT_SIZE:Int = 0
    var IMAGE_WIDTH = 0
    var IMAGE_HEIGHT = 0

    init {
        loadModelFile(context.assets, MODEL_NAME)?.let { Interpreter(it,Interpreter.Options()) }?.let {
            interpreter = it
        }
        val inputShape = interpreter.getInputTensor(0).shape()
        IMAGE_WIDTH = inputShape[1]
        IMAGE_HEIGHT = inputShape[2]
        MODEL_INPUT_SIZE = 4 * IMAGE_WIDTH * IMAGE_HEIGHT
    }
    override fun predict(bitmap: Bitmap): Result {
        convertBitmapToByteBuffer(bitmap)
        val result =  Array(1) { FloatArray(OUTPUT_CLASSES_COUNT) }
        interpreter.run(imageData,result)
        return Result(result)
    }

    override fun convertBitmapToByteBuffer(bitmap: Bitmap) {
        imageData = ByteBuffer.allocateDirect(MODEL_INPUT_SIZE)
        imageData.order(ByteOrder.nativeOrder())
        val pixels = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (pixelValue in pixels) {
            imageData.putFloat(convertPixel(pixelValue))
        }

    }
    private fun convertPixel(color: Int): Float {
        return (255 - ((color shr 16 and 0xFF) * 0.299f + (color shr 8 and 0xFF) * 0.587f + (color and 0xFF) * 0.114f)) / 255.0f
    }

    override fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer? {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream =
            FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            startOffset,
            declaredLength
        )
    }

}