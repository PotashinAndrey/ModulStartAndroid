package com.example.potashin

//import android.R
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.potashin.data.Api
import com.example.potashin.data.model.Credentials
import com.example.potashin.data.model.User
import com.example.potashin.data.model.Wallet
import com.example.potashin.ui.login.LoginActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Wallets : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = intent.extras
        val user: User = arguments!!["user"] as User

        setContentView(R.layout.activity_wallets)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action ${user.name}", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
//        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        appBarConfiguration = AppBarConfiguration(setOf(
//            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        val navDrawerHeader: LinearLayout = navView.findViewById(R.id.user_info_main);
        val hView: View = navView.getHeaderView(0)
//        val hView: View = navView.inflateHeaderView(R.layout.nav_header_wallets);
        val userInfoName: TextView = hView.findViewById(R.id.user_info_name)
        userInfoName.text = user.name
        val userInfoEmail: TextView = hView.findViewById(R.id.user_info_email)
        userInfoEmail.text = user.email

//        navView.setNavigationItemSelectedListener(this);
        val api = Api()
        GlobalScope.launch {
            val result = api.sendGetRequest("wallets/all/${user.id}", user.token);
            println("RESULT $result")

            val itemType = object : TypeToken<List<Wallet>>() {}.type
            val wallets = Gson().fromJson<List<Wallet>>(result, itemType)
            println("Wallet [0] ID ${wallets[0].number}")

//            wallets.putExtra("user", user);

            val menu = navView.menu
            val submenu: Menu = menu.addSubMenu("Ваши счета")
            var index = 0;

            wallets.forEach {
                submenu.add(it.name)
                val wallet = it

                val item: MenuItem = submenu.getItem(index);
                item.setOnMenuItemClickListener {
                    println(wallet.number)
                    val id = wallet.number
                    val navController = findNavController(R.id.nav_host_fragment)

                    val bundle = bundleOf("wallet" to id.toString(), "token" to user.token, "user" to user.id.toString())
                    navController.navigate(R.id.nav_gallery, bundle)

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return@setOnMenuItemClickListener true
                }
                index += 1;
            }
        }

        navView.invalidate()
        navView.setNavigationItemSelectedListener(this)
    }

    override  fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        println(item.contentDescription)
        when (item.itemId) {
//            R.id.nav_profile -> {
//                Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_messages -> {
//                Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_friends -> {
//                Toast.makeText(this, "Friends clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_update -> {
//                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
//            }
//            R.id.nav_logout -> {
//                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
//            }
            0 -> println("jlhkjh")
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.wallets, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_exit -> {
                val login = Intent(this, LoginActivity::class.java);
                startActivity(login);
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
