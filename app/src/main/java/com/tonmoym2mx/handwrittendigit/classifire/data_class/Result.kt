package com.tonmoym2mx.handwrittendigit.classifire.data_class

class Result(var outputLayer:Array<FloatArray>?= null) {
    val probability: Float?
        get() {
        val finalLayer = outputLayer?.get(0)
        return (finalLayer?.get(predictedNumber)?:0f) * 100f
    }
    val predictedNumber:Int get() {
        val finalLayer = outputLayer?.get(0)
        return finalLayer?.indices?.maxBy { finalLayer[it] } ?:-1
    }

}