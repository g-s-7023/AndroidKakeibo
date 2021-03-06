package g_s_org.androidapp.com.androidkakeibo.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import g_s_org.androidapp.com.androidkakeibo.R
import g_s_org.androidapp.com.androidkakeibo.model.FragmentToActivityInterection

class MainActivity : AppCompatActivity(), FragmentToActivityInterection {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set view
        setContentView(R.layout.activity_main)
        // set fragment
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, KakeiboAddFragment())
                .commit()
    }

    override fun changeFragment(to: Fragment) {
        // replace current fragment for "to"
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, to).addToBackStack(null).commit()
    }

    override fun backFragment() {
        // remove current fragment and pop backstack
        val manager = supportFragmentManager
        manager.popBackStack()
    }


}
