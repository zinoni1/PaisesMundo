package com.zenonrodrigo.paises_mundo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class focus_card : AppCompatActivity() {
    private lateinit var Nombre: TextView
    private lateinit var Continente: TextView
    private lateinit var Capital: TextView
    private lateinit var Km: TextView
    private lateinit var bandera: TextView
    private lateinit var DialCode: TextView
    private lateinit var Code2: TextView
    private lateinit var Code3: TextView
    private lateinit var tld: TextView
    var modificar = false

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_paises_card)

        try {
            Nombre = findViewById(R.id.tvNombreRes)
            Continente = findViewById(R.id.tvContinenteRes)
            Capital = findViewById(R.id.tvCapitalRes)
            Km = findViewById(R.id.tvKilometrosRes)
            bandera = findViewById(R.id.tvBandera)
            DialCode = findViewById(R.id.tvdial_codeRes)
            Code2 = findViewById(R.id.tvCode_2Res)
            Code3 = findViewById(R.id.tvCode_3Res)
            tld = findViewById(R.id.tvTLDRes)

            val country = intent.getSerializableExtra("pais") as Country
            val favorito = intent.getBooleanExtra("favorito", country.favorito)

            val favColor: ImageView = findViewById(R.id.favoritos_0)
            val favColor2: ImageView = findViewById(R.id.favoritos_1)

            if (favorito) {
                favColor.visibility = View.VISIBLE
                favColor2.visibility = View.GONE
                modificar = true
            } else {
                favColor.visibility = View.GONE
                favColor2.visibility = View.VISIBLE
                modificar = false
            }

            favColor.setOnClickListener {
                if (favColor.visibility == View.VISIBLE) {
                    favColor.visibility = View.GONE
                    favColor2.visibility = View.VISIBLE
                    country.favorito = false
                    modificar = false
                }
            }
            favColor2.setOnClickListener {
                if (favColor2.visibility == View.VISIBLE) {
                    favColor2.visibility = View.GONE
                    favColor.visibility = View.VISIBLE
                    country.favorito = true
                    modificar = true
                }
            }

            val btnVolver: Button = findViewById(R.id.volver)
            btnVolver.setOnClickListener {
                val intent = Intent()
                intent.putExtra("pais", country)
                intent.putExtra("favorito", country.favorito)
                setResult(RESULT_OK, intent)
                onBackPressed()
                finish()
            }

            Nombre.text = country.nameEs
            Continente.text = country.continentEs
            Capital.text = country.capitalEs
            Km.text = country.km2.toString()
            bandera.text = country.emoji
            DialCode.text = country.dialCode
            Code2.text = country.code2
            Code3.text = country.code3
            tld.text = country.tld
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}