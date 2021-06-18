package io.dotanuki.magicmodules.fixtures.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.magicmodules.fixtures.utils.GenerateRandomName
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        labelHome.text = "Favorite Actor :\n${GenerateRandomName()}"
    }
}