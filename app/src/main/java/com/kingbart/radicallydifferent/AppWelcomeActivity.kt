package com.kingbart.radicallydifferent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class AppWelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_welcome)

        var introduction = findViewById(R.id.intro) as TextView
        var library = findViewById(R.id.library) as TextView

        introduction.setOnClickListener{
            val intent = Intent(this, AppIntroActivity::class.java)
            startActivity(intent)
        }

        library.setOnClickListener{
            val intent = Intent(this, RecyclerViewActivity::class.java)
            startActivity(intent)
        }

    }
}
