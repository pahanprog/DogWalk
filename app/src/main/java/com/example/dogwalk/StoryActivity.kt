package com.example.dogwalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_story.*
import kotlinx.android.synthetic.main.story_row.view.*

class StoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)


        supportActionBar?.title = "History"

        fetchStory()
    }


    private fun fetchStory() {
        val ref = FirebaseDatabase.getInstance().getReference("/story")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()

                p0.children.forEach {
                    Log.d("story", it.toString())
                    val story = it.getValue(MainMenuActivity.Story::class.java)
                    if (story != null ){
                    adapter.add(StoryItem(story))
                    }
                }

                story_view.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    class StoryItem(val story: MainMenuActivity.Story) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.story_text_view_name.text = story.name
            viewHolder.itemView.story_text_view_time.text = story.time
        }

        override fun getLayout(): Int {
            return R.layout.story_row
        }
    }
}
