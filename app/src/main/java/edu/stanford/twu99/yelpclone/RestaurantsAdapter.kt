package edu.stanford.twu99.yelpclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsAdapter(val context: Context, val restaurants: List<YelpRestaurant>) :
    RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_restaurant,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = restaurants.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = restaurants[position]
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val restaurant = restaurants[position]
            itemView.tvName.text = "${position + 1}. ${restaurant.name}"
            itemView.tvNumReviews.text = "${restaurant.numReviews} Reviews"
            itemView.tvAddress.text = restaurant.location.address
            itemView.tvCategory.text = restaurant.categories[0].title
            itemView.tvDistance.text = restaurant.displayDistance()
            itemView.tvPrice.text = restaurant.price

            val rating =
                if (restaurant.rating % 1.0 == 0.0) "stars_small_${restaurant.rating.toInt()}"
                else "stars_small_${(restaurant.rating - 0.5).toInt()}_half"
            itemView.ivRating.setImageResource(
                context.resources.getIdentifier(
                    rating,
                    "drawable",
                    context.packageName
                )
            )

            Glide.with(context).load(restaurant.imageUrl).apply(
                RequestOptions().transforms(
                    CenterCrop(), RoundedCorners(20)
                )
            ).into(itemView.imageView)
        }

    }

}
