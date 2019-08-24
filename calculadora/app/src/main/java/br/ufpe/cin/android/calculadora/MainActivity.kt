package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {

    var resultado = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_7.setOnClickListener {
            resultado = resultado + "7"
            text_calc.setText(resultado)
            //text_cal é Editable e resultado é String, necessidade do setText
            //atribuicao direta nao funciona
        }

        btn_8.setOnClickListener {
            resultado = resultado + "8"
            text_calc.setText(resultado)
        }

        btn_9.setOnClickListener {
            resultado = resultado + "9"
            text_calc.setText(resultado)
        }

        btn_Divide.setOnClickListener {
            resultado = resultado + "/"
            text_calc.setText(resultado)
        }

        btn_4.setOnClickListener {
            resultado = resultado + "4"
            text_calc.setText(resultado)
        }

        btn_5.setOnClickListener {
            resultado = resultado + "5"
            text_calc.setText(resultado)
        }

        btn_6.setOnClickListener {
            resultado = resultado + "6"
            text_calc.setText(resultado)
        }

        btn_Multiply.setOnClickListener {
            resultado = resultado + "*"
            text_calc.setText(resultado)
        }

        btn_1.setOnClickListener {
            resultado = resultado + "1"
            text_calc.setText(resultado)
        }

        btn_2.setOnClickListener {
            resultado = resultado + "2"
            text_calc.setText(resultado)
        }

        btn_3.setOnClickListener {
            resultado = resultado + "3"
            text_calc.setText(resultado)
        }

        btn_Subtract.setOnClickListener {
            resultado = resultado + "-"
            text_calc.setText(resultado)
        }

        btn_Dot.setOnClickListener {
            resultado = resultado + "."
            text_calc.setText(resultado)
        }

        btn_0.setOnClickListener {
            resultado = resultado + "0"
            text_calc.setText(resultado)
        }
        /*
        Resultado é convertido para string e armazenado em text_info
        Quando aparece resultado inválido, vai ativar o Toast com a mensagem (msg)
         */
        btn_Equal.setOnClickListener {
            try {
                resultado = eval(resultado).toString()
                text_info.text = resultado
                //atribuicao direta funciona, text_info e resultado são strings
            }

            catch(e: RuntimeException) {
                val msg = "Expressão inválida. Tente novamente."
                Toast.makeText(
                    this,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        btn_Add.setOnClickListener {
            resultado = resultado + "+"
            text_calc.setText(resultado)
        }

        btn_LParen.setOnClickListener {
            resultado = resultado + "("
            text_calc.setText(resultado)
        }

        btn_RParen.setOnClickListener {
            resultado = resultado + ")"
            text_calc.setText(resultado)
        }

        btn_Power.setOnClickListener {
            resultado = resultado + "^"
            text_calc.setText(resultado)
        }

        btn_Clear.setOnClickListener {
            resultado = ""
            text_calc.setText(resultado)
        }
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }


}
