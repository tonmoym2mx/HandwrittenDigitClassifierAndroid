package com.tonmoym2mx.handwrittendigit.ui
import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tonmoym2mx.handwrittendigit.R
import com.tonmoym2mx.handwrittendigit.classifire.HandwrittenDigitClassifier
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
     companion object{
        private const val TAG = "MainActivity"
      }
    private lateinit var bitmapImage:Bitmap
    private lateinit var handwrittenDigitClassifier: HandwrittenDigitClassifier
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        handwrittenDigitClassifier = HandwrittenDigitClassifier(this)

        predict_button.setOnClickListener {
            displayResult()
        }

        clear_button.setOnClickListener {
            fingerview.clear()
            infotextview.text = "Clear"
            digit_textview.text = ""
        }

    }
    @SuppressLint("SetTextI18n")
    private fun displayResult(){
        bitmapImage = fingerview.exportToBitmap(handwrittenDigitClassifier.IMAGE_WIDTH, handwrittenDigitClassifier.IMAGE_HEIGHT)
        handwrittenDigitClassifier.predict(bitmapImage).apply {
            infotextview.text = "${String.format("%.2f",probability)} % Probability"
            digit_textview.text = "Predict : $predictedNumber"
        }
        fingerview.clear()
    }
}
