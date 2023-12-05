package com.zenonrodrigo.paises_mundo

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class PaisesAdapter(private var countries: List<Country>) :
    RecyclerView.Adapter<PaisesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.card)
        val nombre: TextView = itemView.findViewById(R.id.tvNombreRes)
        val continente: TextView = itemView.findViewById(R.id.tvContinenteRes)
        val capital: TextView = itemView.findViewById(R.id.tvCapitalRes)
        val kilometros: TextView = itemView.findViewById(R.id.tvKilometrosRes)
        val bandera: TextView = itemView.findViewById(R.id.tvBandera)
        val favColor: ImageView = itemView.findViewById(R.id.favoritos_0)
        val favColor2: ImageView = itemView.findViewById(R.id.favoritos_1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.paises_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val paisActual = countries[position]
        holder.nombre.text = paisActual.nameEs
        holder.continente.text = paisActual.continentEs
        holder.capital.text = paisActual.capitalEs
        holder.kilometros.text = "${paisActual.km2} km²"
        holder.bandera.text = paisActual.emoji

        holder.bandera.setOnClickListener {
                val wikiUrl = "https://es.wikipedia.org/wiki/${paisActual.nameEs}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl))
                holder.itemView.context.startActivity(intent)
        }

        holder.cardView.setOnClickListener {
            val intent = Intent(it.context, focus_card::class.java)
            intent.putExtra("pais", paisActual)
            intent.putExtra("favorito", paisActual.favorito)
            it.context.startActivity(intent)
        }

        if(paisActual.favorito){
            holder.favColor.visibility = View.VISIBLE
            holder.favColor2.visibility = View.GONE}
        else if(!paisActual.favorito){
            holder.favColor.visibility = View.GONE
            holder.favColor2.visibility = View.VISIBLE
        }

        holder.favColor.setOnClickListener(){
            if(holder.favColor.visibility == View.VISIBLE){
                holder.favColor.visibility = View.GONE
                holder.favColor2.visibility = View.VISIBLE
            paisActual.favorito = false}
        }
        holder.favColor2.setOnClickListener(){
            if(holder.favColor2.visibility == View.VISIBLE){
                holder.favColor2.visibility = View.GONE
                holder.favColor.visibility = View.VISIBLE
            paisActual.favorito = true}
        }

        if (paisActual.km2 > 1000000) {
            holder.kilometros.setTypeface(null, Typeface.BOLD)
            holder.kilometros.text = paisActual.km2.toString()
        }else{
            holder.kilometros.setTypeface(null, Typeface.NORMAL)
            holder.kilometros.text = paisActual.km2.toString()
        }
        when (paisActual.continentEn.toLowerCase()) {
            "africa" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelRosa))
            "asia" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelBlau))
            "europe" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelLila))
            "oceania" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelVerd))
            "north america" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelGroc))
            "antarctica" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelTronja))
            "south america" -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.pastelVermell))
            else -> holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }
    }

     override fun getItemCount(): Int {
        return countries.size
    }
    fun setCountries(newCountries: List<Country>) {
        countries = newCountries
        notifyDataSetChanged()
    }

    fun sortCountries(comparator: Comparator<Country>) {
        val sortedList = countries.sortedWith(comparator)
        setCountries(sortedList)
    }

    fun getDisplayedCountries(): List<Country> {
        return countries
    }
}