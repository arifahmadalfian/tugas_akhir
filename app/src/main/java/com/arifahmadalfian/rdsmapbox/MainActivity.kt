package com.arifahmadalfian.rdsmapbox

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.cari)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                //getDataSearch(query)

                //kembali ke list kosong dan menjadalankan loading shimmer
                //getListShimmer()

                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {

                true
            }
            R.id.action_logout -> {
                getActionLogout()
                true
            }
            R.id.action_tambah -> {
                getActionTambahPelanggan()
                true
            }
            R.id.action_about -> {
                Toast.makeText(this@MainActivity, " Tentang Aplikasi", Toast.LENGTH_SHORT).show()
                getActionAbout()
                true
            }
            else -> {

                true
            }
        }
    }

    private fun getActionTambahPelanggan() {
        val intent = Intent(this@MainActivity, ActivityTambahPelanggan::class.java)
        startActivity(intent)
    }

    private fun getActionLogout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Toast.makeText(this@MainActivity, " Berhasil Logout", Toast.LENGTH_SHORT).show()
    }

    private fun getActionAbout() {
        val intent = Intent(this@MainActivity,AboutActivity::class.java)
        startActivity(intent)
    }
}