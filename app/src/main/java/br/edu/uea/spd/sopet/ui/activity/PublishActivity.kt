package br.edu.uea.spd.sopet.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.spd.sopet.R

class PublishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
        title = R.string.publish.toString()
    }

}