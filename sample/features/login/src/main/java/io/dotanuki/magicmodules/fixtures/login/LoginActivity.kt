package io.dotanuki.magicmodules.fixtures.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.dotanuki.magicmodules.fixtures.core.Destination
import io.dotanuki.magicmodules.fixtures.core.FeaturesNavigator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginButton.setOnClickListener {
            FeaturesNavigator(this).navigate(Destination.HOME)
        }
    }
}