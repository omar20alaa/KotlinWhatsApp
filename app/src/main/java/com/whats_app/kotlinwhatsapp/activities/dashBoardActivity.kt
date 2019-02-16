package com.whats_app.kotlinwhatsapp.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.whats_app.kotlinwhatsapp.R
import com.whats_app.kotlinwhatsapp.adapters.SectionPagerAdapter
import kotlinx.android.synthetic.main.activity_dash_board.*

class dashBoardActivity : AppCompatActivity() {

    // variables
    var sectionAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
        getIntentData()
        initViewPager()
    }


    private fun getIntentData() {
        if (intent.extras != null) {
            var username = intent.extras.get("name")

            Toast.makeText(this, username.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun initViewPager() {

        supportActionBar!!.title = "Dashboard"
        sectionAdapter = SectionPagerAdapter(supportFragmentManager)
        dash_view_pager.adapter = sectionAdapter
        main_tabs.setupWithViewPager(dash_view_pager)
        main_tabs.setTabTextColors(Color.WHITE, Color.GREEN)
        main_tabs.setSelectedTabIndicatorColor(Color.GREEN)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        super.onOptionsItemSelected(item)

        if (item != null) {
            if (item.itemId == R.id.logout_id) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            if (item.itemId == R.id.settings_id) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }

        }
        return true
    }
}
