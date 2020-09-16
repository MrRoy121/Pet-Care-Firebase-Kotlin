
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.petcare.messageModel
import com.example.petcate.R

class messageDocAdapter(private val c: Context, private val listdata: List<messageModel>) :
    RecyclerView.Adapter<messageDocAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.chat_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chat.text = listdata[position].chat
        holder.box.setCardBackgroundColor(c.resources.getColor(R.color.fade))
        holder.user.visibility = View.VISIBLE
        if (!listdata[position].admin) {
            holder.box.setCardBackgroundColor(c.resources.getColor(R.color.txt))
            holder.user.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chat: TextView = itemView.findViewById(R.id.text)
        val box: CardView = itemView.findViewById(R.id.box)
        val user: View = itemView.findViewById(R.id.user)
    }
}