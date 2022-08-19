package com.ingenieriajhr.radarjhr

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.*
import kotlin.math.*

/**
 * Desarrollador por ingenieria JHR JOSE HIDALGO RODRIGUEZ.
 */

class RadarJhr (context:Context?, attrs: AttributeSet?):View(context,attrs){

    private var hEsc = 0f
    private var wEsc = 0f
    private var radioEsc = 0f
    //espacio de inicio y fin
    private val espEnd = 2f
    private var radio4 = 0f

    private var startAnimation = false

    private  var gradosRadar = 0

    private var obstaculos = floatArrayOf(0f,0f,0f,0f,0f)

    private var maxDist = 0f

    private lateinit var paint:Paint

    private var anguloObj = ArrayList<Int>()

    private var distanciaObj = ArrayList<Float>()

    private var velocidadAnimacion = 70L

    private var tamLetras = 0f

    private var obj1Visible = true

    private var radioObs = 0.03f

    private var colorCircl = Color.GREEN

    private var colorRadar = Color.GREEN

    private var colorLine = Color.GREEN

    private var countAlpha = 0

    var maxAnimationAlpha = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        hEsc = h.toFloat()
        wEsc = w.toFloat()
        radioEsc = if(wEsc>hEsc)hEsc/2 else wEsc/2
        radio4 = radioEsc/4
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint = Paint()
        semiCircleInit(canvas)
        if (anguloObj.size>0 && startAnimation)obstaculosFun(canvas)
        animation(canvas)
    }

    private fun obstaculosFun(canvas: Canvas?) {
        if (startAnimation) {
        for (contador in 0..anguloObj.size-1) {
            val angle2 = (((anguloObj[contador] + 90) * Math.PI) / 180)
                if (obj1Visible) {
                    val distanciaObjeto = (radioEsc * distanciaObj[contador]) / maxDist
                    var x2 = (wEsc / 2 + distanciaObjeto * (Math.sin(angle2)))
                    var y2 = (hEsc / 2 + distanciaObjeto * (Math.cos(angle2)))

                    //paint.alpha = 255
                    paint.color = Color.GREEN
                    paint.style = Paint.Style.FILL_AND_STROKE

                    val radioObj = radioEsc * radioObs
                    paint.color = colorCircl

                    if (maxAnimationAlpha!=0) {

                        countAlpha++

                        if (countAlpha > maxAnimationAlpha * 2) countAlpha = 0

                        if (countAlpha > (maxAnimationAlpha - maxAnimationAlpha / 2)) {
                            paint.alpha = 255
                        } else {
                            if (countAlpha > 0)
                                paint.alpha = 0
                        }
                    }
                    canvas!!.drawCircle(x2.toFloat(), y2.toFloat(), radioObj, paint)


                    paint.textSize = tamLetras
                    canvas.drawText(
                        "(${anguloObj[contador]}°, ${distanciaObj[contador]} cm)",
                        radioObj + 2 + x2.toFloat(),
                        radioObj + 2 + y2.toFloat(),
                        paint
                    )
                    paint.style = Paint.Style.STROKE
                    paint.color = Color.GREEN
                }
            }
        }
    }


    fun maxDistancia(distanciaCm: Float){
        maxDist = distanciaCm
    }

    fun detecto(angulo:Int,distancia: Float){
        anguloObj.add(angulo)
        distanciaObj.add(distancia)
        invalidate()
    }

    fun resetPoints(){
        anguloObj.clear()
        distanciaObj.clear()
    }

    private fun animation(canvas: Canvas?){
        if (startAnimation){
            paint.color = colorLine
            var angle = ((gradosRadar*Math.PI)/180)
            var x = (wEsc/2+radioEsc*(sin(angle)))
            var y = (hEsc/2+radioEsc*(cos(angle)))
            canvas!!.drawLine(wEsc/2,hEsc/2,x.toFloat(), y.toFloat(),paint)
            for(i in 1..80){
                paint.alpha = 255-i*3
                lineas(canvas,gradosRadar-i)
            }
            gradosRadar +=3
            Thread.sleep(velocidadAnimacion)
            invalidate()
        }
    }

    private fun lineas(canvas: Canvas?,grados:Int){
        val angle1 = ((grados*Math.PI)/180)
        val x1 = (wEsc/2+radioEsc*(sin(angle1)))
        val y1 = (hEsc/2+radioEsc*(cos(angle1)))
        canvas!!.drawLine(wEsc/2,hEsc/2,x1.toFloat(), y1.toFloat(),paint)
    }

    fun initAnimation(boolean: Boolean){
        startAnimation = boolean
        invalidate()
    }

    fun stopAnimatio(boolean: Boolean){
        startAnimation = boolean
        invalidate()
    }

    private fun semiCircleInit(canvas: Canvas?) {
        paint.alpha = 255
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = colorRadar
        canvas!!.drawCircle(wEsc/2,hEsc/2,(radioEsc-espEnd),paint)

        paint.strokeWidth = 2.0f

        canvas.drawCircle(wEsc/2,hEsc/2,(radio4),paint)
        canvas.drawCircle(wEsc/2,hEsc/2,(radio4*2),paint)
        canvas.drawCircle(wEsc/2,hEsc/2,(radio4*3),paint)
    }



    fun velAnimacion(time:Long){
        velocidadAnimacion = time
    }


    fun tamLetras(size:Float){
        tamLetras = size
    }


    fun obj1Visible(boolean: Boolean){
        obj1Visible = boolean
    }

    fun radioObj(float: Float){
        radioObs = float
    }

    fun colorCircle(r:Int,g:Int,b:Int){
        colorCircl = Color.rgb(r,g,b)
    }

    fun colorRadar(r:Int,g:Int,b:Int){
        colorRadar = Color.rgb(r,g,b)
    }

    fun colorLinea(r:Int,g:Int,b:Int){
        colorLine = Color.rgb(r,g,b)
    }

}