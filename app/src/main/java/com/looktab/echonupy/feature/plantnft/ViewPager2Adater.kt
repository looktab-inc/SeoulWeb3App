package com.looktab.echonupy.feature.plantnft

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.looktab.echonupy.DesignUtil.setImageUrl
import com.looktab.echonupy.DesignUtil.setVisibleGone
import com.looktab.echonupy.R
import com.looktab.echonupy.feature.plantnft.model.Plant

class ViewPager2Adater(var list: ArrayList<Plant>, var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_plant, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as viewHolder).image.setImageUrl(list[position].url)
        holder.btnGrow.setVisibleGone(list[position].visible)
        holder.name.text = (list[position].name)
        holder.description.text = (list[position].description)
    }

    inner class viewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.plantImageView)
        var btnGrow: ImageView = view.findViewById(R.id.btnGrow)
        var name: TextView = view.findViewById(R.id.name)
        var description: TextView = view.findViewById(R.id.description)
    }
}
