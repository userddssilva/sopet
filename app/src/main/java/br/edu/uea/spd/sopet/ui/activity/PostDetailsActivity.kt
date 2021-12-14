package br.edu.uea.spd.sopet.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.spd.sopet.R
import br.edu.uea.spd.sopet.adapter.CommentAdapter
import br.edu.uea.spd.sopet.data.model.Comment
import br.edu.uea.spd.sopet.data.Datasource

class PostDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        title = "Details"

        val recycleView: RecyclerView = findViewById(R.id.rc_details_comments)
        val dataset: List<Comment> = Datasource.loadComments()
        val adapter = CommentAdapter(dataset) { openDetailClassWhenClickItem(it) }
        recycleView.layoutManager = LinearLayoutManager(this)
        recycleView.adapter = adapter

    }

    private fun openDetailClassWhenClickItem(Comment: Comment) {
        startActivity(Intent(this, PostDetailsActivity::class.java))
    }
}