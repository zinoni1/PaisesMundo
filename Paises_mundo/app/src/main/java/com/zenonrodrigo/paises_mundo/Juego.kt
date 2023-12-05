package com.zenonrodrigo.paises_mundo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.content.SharedPreferences

class Juego : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var bandera: TextView

    private var currentCountry: Country? = null
    private var correctAnswer: String = ""
    private var consecutiveCorrectAnswers = 0
    private var highestScore = 0
    private var score = 0
    private lateinit var scoreTextView: TextView
    private lateinit var recordTextView: TextView
    private var record = 0

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var filteredCountries: List<Country>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.juego)
        initializeViews()
        filteredCountries = intent.getSerializableExtra("filtered_countries") as? List<Country> ?: emptyList()

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        record = sharedPreferences.getInt("record", 0)

        loadNewQuestion()

        option1Button.setOnClickListener { checkAnswer(option1Button.text.toString()) }
        option2Button.setOnClickListener { checkAnswer(option2Button.text.toString()) }
        option3Button.setOnClickListener { checkAnswer(option3Button.text.toString()) }
        option4Button.setOnClickListener { checkAnswer(option4Button.text.toString()) }
    }
    private fun getRandomCountry(): Country {
        return filteredCountries.random()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btnJuego -> {
                onBackPressed()
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun initializeViews() {
        questionTextView = findViewById(R.id.tvPregunta)
        option1Button = findViewById(R.id.button)
        option2Button = findViewById(R.id.button2)
        option3Button = findViewById(R.id.button3)
        option4Button = findViewById(R.id.button4)
        bandera = findViewById(R.id.tvBanderaJoc)
        scoreTextView = findViewById(R.id.tvPuntuacion)
        recordTextView = findViewById(R.id.tvRecord)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }


    private fun loadNewQuestion() {
        currentCountry = getRandomCountry()
        correctAnswer = currentCountry?.capitalEs ?: ""
        updateQuestionViews()
        updateScoreTextView()
    }

    private fun updateScoreTextView() {
        scoreTextView.text = "Puntuación: $score\n"
        recordTextView.text = "Record: $record"
    }

    private fun updateQuestionViews() {
        currentCountry?.let {
            questionTextView.text = "¿Cuál es la capital de ${it.nameEs}?"
            bandera.text = it.emoji
                    val options = mutableListOf<String>()
                    options.add(correctAnswer)

                    while (options.size < 4) {
                        val randomCountry = getRandomCountry()
                        val randomOption = randomCountry.capitalEs
                        if (randomOption != correctAnswer && !options.contains(randomOption)) {
                            options.add(randomOption)
                        }
                    }

                    options.shuffle()

                    option1Button.text = options[0]
                    option2Button.text = options[1]
                    option3Button.text = options[2]
                    option4Button.text = options[3]
                }
            }

            private fun checkAnswer(selectedAnswer: String) {
        if (selectedAnswer == correctAnswer) {
            consecutiveCorrectAnswers++
            score++
            if (consecutiveCorrectAnswers > highestScore) {
                highestScore = consecutiveCorrectAnswers

                if (highestScore > record) {
                    record = highestScore

                    val editor = sharedPreferences.edit()
                    editor.putInt("Record", record)
                    editor.apply()
                }
            }
            loadNewQuestion()
            updateScoreTextView()
        } else {
            consecutiveCorrectAnswers = 0
            showCorrectAnswer()
        }
    }

    private fun showCorrectAnswer() {
        Toast.makeText(
            this,
            "Respuesta incorrecta. La respuesta es $correctAnswer",
            Toast.LENGTH_SHORT
        ).show()
        onBackPressed()
        finish()
    }
}