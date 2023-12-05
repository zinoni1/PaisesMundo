package com.zenonrodrigo.paises_mundo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private lateinit var paisesAdapter: PaisesAdapter
    private lateinit var countries: List<Country>
    private lateinit var recyclerView: RecyclerView
    private var PantallaFav = false
    private var selectedContinent: String? = null
    private var isAscendingOrder: Boolean = true
    private var selectedSorting: String = "CAPITAL"
    private lateinit var juego: Juego
    private var previousContinent: String? = null
    private var previousSorting: String = "CAPITAL"
    private var previousIsAscendingOrder: Boolean = true
    private var previousPantallaFav: Boolean = false

    private var filteredCountries: List<Country> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        loadSimple()
        juego = Juego()
    }

    override fun onResume() {
        super.onResume()
        if (focus_card().modificar) {
            focus_card().modificar = false
            updateAdapterData()
        } else {
            selectedContinent = previousContinent
            selectedSorting = previousSorting
            isAscendingOrder = previousIsAscendingOrder
            updateAdapterData()
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        paisesAdapter = PaisesAdapter(emptyList())
        recyclerView.adapter = paisesAdapter
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)
        return true
    }

    private fun loadSimple() {
        val json: String = this.assets.open("paises.json").bufferedReader().use { it.readText() }
        val paises: Paises = Gson().fromJson(json, Paises::class.java)
        val gson = Gson()
        val listType = object : TypeToken<Paises>() {}.type
        countries = gson.fromJson<Paises>(json, listType).countries
        filteredCountries = countries.toList()
        countries = paises.countries
        updateAdapterData()
    }

    private fun startGameActivity() {
        val displayedCountries = paisesAdapter.getDisplayedCountries()
        val intent = Intent(this, Juego::class.java)
        intent.putExtra("filtered_countries", displayedCountries as Serializable)
        startActivity(intent)
    }

    private fun favorite() {
        previousContinent = selectedContinent
        previousSorting = selectedSorting
        previousIsAscendingOrder = isAscendingOrder
        previousPantallaFav = PantallaFav

        PantallaFav = !PantallaFav
        updateAdapterData()
    }

    private fun updateAdapterData() {
        val filteredList = filterAndSortData()
        paisesAdapter = PaisesAdapter(filteredList)
        paisesAdapter.notifyDataSetChanged()
        recyclerView.adapter = paisesAdapter
    }

    private fun filterAndSortData(): List<Country> {
        val filteredByContinent = if (selectedContinent.isNullOrEmpty()) {
            countries
        } else {
            countries.filter { it.continentEs == selectedContinent }
        }
        val filteredByFavorite = if (PantallaFav) {
            filteredByContinent.filter { it.favorito }
        } else {
            filteredByContinent
        }

        return when (selectedSorting) {
            "PAIS (Ascendente)" -> filteredByFavorite.sortedBy { it.nameEs }
            "PAIS (Descendente)" -> filteredByFavorite.sortedByDescending { it.nameEs }
            "CAPITAL (Ascendente)" -> filteredByFavorite.sortedBy { it.capitalEs }
            "CAPITAL (Descendente)" -> filteredByFavorite.sortedByDescending { it.capitalEs }
            else -> filteredByFavorite
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnFiltrarFav -> {
                favorite()
                true
            }
            R.id.btnJuego -> {
                startGameActivity()
                    return true
            }
            R.id.asia, R.id.africa, R.id.americadelnorte, R.id.europa, R.id.oceania, R.id.americadelsud, R.id.antartida, R.id.todos -> {
                handleOrdenar(item.itemId)

                true
            }
            R.id.opciopais -> {
                handleOrdenar(item.itemId)
                selectedSorting = "PAIS"
                true
            }
            R.id.opciodescendente -> {
                if (selectedSorting == "PAIS") {
                    handleOrdenar(item.itemId)
                    isAscendingOrder = false
                } else if (selectedSorting == "CAPITAL") {
                    handleOrdenar(item.itemId)
                    isAscendingOrder = false
                }
                true
            }
            R.id.opcioascendente -> {
                if (selectedSorting == "PAIS") {
                    handleOrdenar(item.itemId)
                    isAscendingOrder = true
                } else if (selectedSorting == "CAPITAL") {
                    handleOrdenar(item.itemId)
                    isAscendingOrder = true
                }
                true
            }
            R.id.opciocapital -> {
                handleOrdenar(item.itemId)
                selectedSorting = "CAPITAL"
                true
            }

            R.id.europa, R.id.asia, R.id.oceania, R.id.americadelsud, R.id.americadelnorte, R.id.africa, R.id.antartida, R.id.todos -> {
                selectedContinent = when (item.itemId) {
                    R.id.europa -> "Europa"
                    R.id.asia -> "Asia"
                    R.id.oceania -> "Oceanía"
                    R.id.americadelsud -> "América del Sur"
                    R.id.americadelnorte -> "América del Norte"
                    R.id.africa -> "África"
                    R.id.antartida -> "Antártida"
                    R.id.todos -> null
                    else -> null
                }
                updateAdapterData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun handleOrdenar(orderItemId: Int) {
        when {
            orderItemId == R.id.opciopais -> {
                selectedSorting = "PAIS"
                val sortingComparator = getSortingComparator(isAscendingOrder) { it.nameEn }
                paisesAdapter.sortCountries(sortingComparator)
            }
            orderItemId == R.id.opciocapital -> {
                selectedSorting = "CAPITAL"
                val sortingComparator = getSortingComparator(isAscendingOrder) { it.capitalEn }
                paisesAdapter.sortCountries(sortingComparator)
            }
            orderItemId == R.id.opcioascendente -> {
                isAscendingOrder = true
                updateSorting()
            }
            orderItemId == R.id.opciodescendente -> {
                isAscendingOrder = false
                updateSorting()
            }
            orderItemId == R.id.europa ||
                    orderItemId == R.id.asia ||
                    orderItemId == R.id.oceania ||
                    orderItemId == R.id.americadelsud ||
                    orderItemId == R.id.americadelnorte ||
                    orderItemId == R.id.africa ||
                    orderItemId == R.id.antartida ||
                    orderItemId == R.id.todos -> {

                selectedContinent = when (orderItemId) {
                    R.id.europa -> "Europa"
                    R.id.asia -> "Asia"
                    R.id.oceania -> "Oceanía"
                    R.id.americadelsud -> "América del Sur"
                    R.id.americadelnorte -> "América del Norte"
                    R.id.africa -> "África"
                    R.id.antartida -> "Antártida"
                    R.id.todos -> null
                    else -> null
                }
                updateAdapterData()
            }
            else -> {
            }
        }
    }

    private fun updateSorting() {
        val sortingComparator = when (selectedSorting) {
            "PAIS" -> getSortingComparator(isAscendingOrder) { it.nameEn }
            "CAPITAL" -> getSortingComparator(isAscendingOrder) { it.capitalEn }
            else -> getSortingComparator(isAscendingOrder) { it.nameEn }
        }
        paisesAdapter.sortCountries(sortingComparator)
    }

    private fun getSortingComparator(ascending: Boolean, selector: (Country) -> Comparable<*>): Comparator<Country> {
        return if (ascending) {
            compareBy(selector)
        } else {
            compareByDescending(selector)
        }
    }
}