package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private  lateinit var binding : ActivityMainBinding
    var lastNumeric= false
    var stateError = false
    var lastDot = false
    private lateinit var expression: Expression

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root   )
    }

    fun onAllClearClick(view: View) {
        binding.dataTv.text=""
        binding.result.text=""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.result.visibility=View.GONE
    }


    fun onEqualClick(view: View) {
        OnEqual()
        binding.dataTv.text=binding.result.text.toString().drop(1)
    }


    fun onDigitClick(view: View) {
        if (stateError){
            binding.dataTv.text= (view as Button).text
            stateError= false

        }else{
            binding.dataTv.append((view as Button).text)
        }
        lastNumeric = true
        OnEqual()
    }


    fun onOperatorClick(view: View) {
        if (!stateError  && lastNumeric){
            binding.dataTv.append((view as Button).text)
            lastDot =false
            lastNumeric = false
            OnEqual()
        }
    }


    fun onBackClick(view: View) {
        binding.dataTv.text=binding.dataTv.text.toString().drop(1)
        try {
            val lastchar = binding.dataTv.text.toString().last()
            if (lastchar.isDigit()){
                OnEqual()
            }
        }catch (e:Exception){
            binding.result.text=""
            binding.result.visibility=View.GONE
            Log.e("last char error",e.toString())
        }
    }


    fun onClearClick(view: View) {
        binding.dataTv.text=""
        lastNumeric=false
    }

    fun OnEqual(){
        if (lastNumeric && !stateError){
            val txt = binding.dataTv.text.toString()
            expression = ExpressionBuilder(txt).build()

            try {
                val result = expression.evaluate()
                binding.result.visibility= View.VISIBLE
                binding.result.text= "="+result.toString()
            }catch (ex :ArithmeticException){
                Log.e("evaluate error",ex.toString())
                binding.result.text= "Error"
                stateError = true
                lastNumeric =  false
            }
        }
    }
}