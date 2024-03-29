package com.grigorenko.yourchance.ui.investor.favorite_startups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grigorenko.yourchance.R
import com.grigorenko.yourchance.databinding.FavStartupItemBinding
import com.grigorenko.yourchance.domain.model.Startup
import com.squareup.picasso.Picasso

class FavoriteStartupsAdapter(
    private val startupClickListener: StartupClickListener
) : ListAdapter<Startup, FavoriteStartupsViewHolder>(StartupDiff) {
    companion object {
        private object StartupDiff : DiffUtil.ItemCallback<Startup>() {
            override fun areItemsTheSame(oldItem: Startup, newItem: Startup): Boolean {
                return oldItem.image == newItem.image
            }

            override fun areContentsTheSame(oldItem: Startup, newItem: Startup): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteStartupsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FavStartupItemBinding.inflate(inflater, parent, false)
        return FavoriteStartupsViewHolder(binding, startupClickListener)
    }

    override fun onBindViewHolder(holder: FavoriteStartupsViewHolder, position: Int) {
        val startup = getItem(position)
        holder.bindStartup(startup)
    }

    override fun submitList(list: List<Startup>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }
}

class FavoriteStartupsViewHolder(
    private val startupItemBinding: FavStartupItemBinding,
    private val startupClickListener: StartupClickListener
) : RecyclerView.ViewHolder(startupItemBinding.root) {

    fun bindStartup(startup: Startup) {
        Picasso.get()
            .load(startup.image.uri.toUri())
            .fit().centerCrop()
            .placeholder(R.drawable.ic_loading_image)
            .into(startupItemBinding.image)
        startupItemBinding.apply {
            name.text = startup.name
            category.text = startup.category
            iconCategory.setImageResource(getCategoryDrawableId(startup.category))
            wholeSumInvest.text = startup.moneyInvest.wholeSum.toString()
            collectedSumInvest.text = startup.moneyInvest.collectedSum.toString()
            val tLocation = startup.location.country + ", " + startup.location.city
            location.text = tLocation
            progressBarMoneyInvest.max = startup.moneyInvest.wholeSum.toInt()
            progressBarMoneyInvest.progress = startup.moneyInvest.collectedSum.toInt()
            amountOfViews.text = startup.views.toString()
            startupCardView.setOnClickListener {
                startupClickListener.onClick(startup, true)
            }
        }
    }

    private fun getCategoryDrawableId(category: String): Int {
        return when (category) {
            "Бизнес" -> R.drawable.ic_business_category_black_24dp
            "Еда" -> R.drawable.ic_food_category_black_24dp
            "Искусство" -> R.drawable.ic_art_category_black_24dp
            "Общество" -> R.drawable.ic_society_category_black_24dp
            "IT" -> R.drawable.ic_it_category_black_24dp
            "Технологии" -> R.drawable.ic_techn_category_black_24dp
            "Путешествия" -> R.drawable.ic_trip_category_black_24dp
            "Спорт" -> R.drawable.ic_sport_category_black_24dp
            else -> R.drawable.ic_business_category_black_24dp
        }
    }
}
