package com.tonmoym2mx.handwrittendigit.classifire

import android.content.res.AssetManager
import android.graphics.Bitmap
import com.tonmoym2mx.handwrittendigit.classifire.data_class.Result
import java.nio.MappedByteBuffer

interface TFLiteClassifier {
    fun predict(bitmap: Bitmap):Result
    fun convertBitmapToByteBuffer(bitmap: Bitmap)
    fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer?
}