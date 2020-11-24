package com.anon.calculator

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlin.math.pow

class MainActivity : Activity() {
    var resNum:Double = 0.0
    var operator = ""
    var inputNumIntPart = mutableListOf<Int>()
    var inputNumDecPart = mutableListOf<Int>()
    var hasInputDot = false
    var inputState = "start" // start,num1,operator,num2

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun numClick(view: View){
        //获取选中数字
        val clickedNum = when (view.id) {
            R.id.btn1 -> 1
            R.id.btn2 -> 2
            R.id.btn3 -> 3
            R.id.btn4 -> 4
            R.id.btn5 -> 5
            R.id.btn6 -> 6
            R.id.btn7 -> 7
            R.id.btn8 -> 8
            R.id.btn9 -> 9
            R.id.btn0 -> 0
            else -> -1
        }
        //将选中的数字添加至数组
        if(hasInputDot)
            inputNumDecPart.add(clickedNum)
        else
            inputNumIntPart.add(clickedNum)
        //更新文本标签的显示
        if(inputState == "start" || inputState == "num1"){
            val nowNum1Text = findViewById<TextView>(R.id.nowNum1Text)
            nowNum1Text.text = "${nowNum1Text.text}${clickedNum.toString()}"
            inputState = "num1"
        } else if(inputState == "operator" || inputState == "num2"){
            val nowNum2Text = findViewById<TextView>(R.id.nowNum2Text)
            nowNum2Text.text = "${nowNum2Text.text}${clickedNum.toString()}"
            inputState = "num2"
        }
    }
    fun dotClick(view: View){
        if(inputState == "start" || inputState == "operator" || hasInputDot)
            return
        hasInputDot = true
        if(inputState == "num1"){
            val nowNum1Text = findViewById<TextView>(R.id.nowNum1Text)
            nowNum1Text.text = "${nowNum1Text.text}."
        } else if(inputState == "num2"){
            val nowNum2Text = findViewById<TextView>(R.id.nowNum2Text)
            nowNum2Text.text = "${nowNum2Text.text}."
        }
    }
    fun oprClick(view: View){
        when(inputState){
            "start" -> return
            "num1" -> {
                operator = when (view.id){
                    R.id.btnPlus -> "+"
                    R.id.btnMinus -> "-"
                    R.id.btnMultiply -> "*"
                    R.id.btnDivide -> "/"
                    else -> ""
                }
                resNum = getNowNum()
            }
            "operator" -> {
                operator = when (view.id){
                    R.id.btnPlus -> "+"
                    R.id.btnMinus -> "-"
                    R.id.btnMultiply -> "*"
                    R.id.btnDivide -> "/"
                    else -> ""
                }
            }
            "num2" -> {
                val histNum1Text = findViewById<TextView>(R.id.histNum1Text)
                val histOprText = findViewById<TextView>(R.id.histOprText)
                val histNum2Text = findViewById<TextView>(R.id.histNum2Text)
                val histEqlText = findViewById<TextView>(R.id.histEqlText)
                val histResText = findViewById<TextView>(R.id.histResText)
                val nowNum1Text = findViewById<TextView>(R.id.nowNum1Text)
                val nowNum2Text = findViewById<TextView>(R.id.nowNum2Text)
                val nowNum = getNowNum()
                val newResNum:Double = when(operator){
                    "+" -> resNum + nowNum
                    "-" -> resNum - nowNum
                    "*" -> resNum * nowNum
                    "/" -> resNum / nowNum
                    else -> 0.0
                }
                histNum1Text.text = getDecStr(resNum)
                histOprText.text = operator
                histNum2Text.text = getDecStr(nowNum)
                histEqlText.text = "="
                histResText.text = getDecStr(newResNum)
                nowNum1Text.text = getDecStr(newResNum)
                nowNum2Text.text = ""

                resNum = newResNum
                operator = when (view.id){
                    R.id.btnPlus -> "+"
                    R.id.btnMinus -> "-"
                    R.id.btnMultiply -> "*"
                    R.id.btnDivide -> "/"
                    else -> ""
                }
            }
        }
        inputNumIntPart = mutableListOf<Int>()
        inputNumDecPart = mutableListOf<Int>()
        val nowOprText = findViewById<TextView>(R.id.nowOprText)
        nowOprText.text = operator
        inputState = "operator"
        hasInputDot = false
    }
    fun eqlClick(view: View) {
        val histNum1Text = findViewById<TextView>(R.id.histNum1Text)
        val histOprText = findViewById<TextView>(R.id.histOprText)
        val histNum2Text = findViewById<TextView>(R.id.histNum2Text)
        val histEqlText = findViewById<TextView>(R.id.histEqlText)
        val histResText = findViewById<TextView>(R.id.histResText)
        val nowNum1Text = findViewById<TextView>(R.id.nowNum1Text)
        val nowOprText = findViewById<TextView>(R.id.nowOprText)
        val nowNum2Text = findViewById<TextView>(R.id.nowNum2Text)
        val nowNum = getNowNum()
        when (inputState){
            "start" -> return
            "num1","operator" -> {
                histNum1Text.text = ""
                histOprText.text = ""
                histNum2Text.text = getDecStr(nowNum)
                histResText.text = getDecStr(nowNum)
                histEqlText.text = "="
            }
            "num2" -> {
                val newResNum: String = getDecStr(
                    when (operator) {
                        "+" -> resNum + nowNum
                        "-" -> resNum - nowNum
                        "*" -> resNum * nowNum
                        "/" -> resNum / nowNum
                        else -> 0.0
                    }
                )
                histNum1Text.text = getDecStr(resNum)
                histOprText.text = operator
                histNum2Text.text = getDecStr(nowNum)
                histEqlText.text = "="
                histResText.text = newResNum
            }
        }
        nowNum1Text.text = ""
        nowOprText.text = ""
        nowNum2Text.text = ""
        hasInputDot = false
        inputState = "start"
        inputNumIntPart = mutableListOf<Int>()
        inputNumDecPart = mutableListOf<Int>()
    }
    private fun getDecStr(dec:Double):String{
        val decStr = dec.toString()
        val len = decStr.length
        return if(decStr[len-2] == '.' && decStr[len-1] == '0')
            decStr.substring(0,len-2) else decStr
    }
    private fun getNowNum():Double{
        var intPart:Double = 0.0
        var decPart:Double = 0.0
        for(i in inputNumIntPart.indices)
            intPart += inputNumIntPart[i] * 10.0.pow((inputNumIntPart.size - 1 - i).toDouble())
        for(i in inputNumDecPart.indices)
            decPart += inputNumDecPart[i] * 0.1.pow((1 + i).toDouble())
        return intPart + decPart
    }
}
