package com.example.contentprovider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CustomAdapter(
    private val context: Context,
    private val contactModelList: MutableList<ContactModel>
): BaseAdapter() {

    override fun getCount(): Int {
        return contactModelList.size
    }

    override fun getItem(p0: Int): Any {
        return contactModelList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()

            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            convertView = inflater.inflate(R.layout.list_item, null,true)
            holder.nameTV = convertView!!.findViewById(R.id.nameTV) as TextView
            holder.phoneTV = convertView.findViewById(R.id.phoneTV) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.nameTV!!.text = contactModelList[p0].name
        holder.phoneTV!!.text = contactModelList[p0].phone
        return convertView

    }

    private inner class ViewHolder {
        var nameTV: TextView? = null
        var phoneTV: TextView? = null
    }
}