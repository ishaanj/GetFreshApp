package getfresh.com.getfreshapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import getfresh.com.getfreshapplication.data.Cart;
import getfresh.com.getfreshapplication.fragment.CartFragment;
import getfresh.com.getfreshapplication.fragment.MainActivityFragment;
import getfresh.com.getfreshapplication.fragment.NavigationDrawerFragment;
import getfresh.com.getfreshapplication.fragment.PromoFragment;
import getfresh.com.getfreshapplication.settings.SettingsActivity;


public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, MainActivityFragment.MainActivityFragmentListener{

    private Toolbar toolbar;
    private ArrayList<Cart> cartList;
    private MainActivityFragment mainActivityFragment;
    private CartFragment cartFragment;
    private PromoFragment promoFragment;
    private double cartTotal;

    private static final String TAG = "MainAcitivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cartList = new ArrayList<Cart>();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sp.getBoolean(LoginActivity.KEY_LOGIN_VISITED, false)) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }

        toolbar = (Toolbar) findViewById(R.id.drawer_toolbar);
        setSupportActionBar(toolbar);

        //This is how you change the colour
        //toolbar.setBackgroundColor(Color.parseColor("#33B5E5"));
        toolbar.setTitleTextColor(Color.WHITE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(position == 0) {
            if(mainActivityFragment == null) {
                mainActivityFragment = new MainActivityFragment();
            }
            else {
                fragmentManager.popBackStack();
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.container, mainActivityFragment)
                    .commit();
        }
        else if(position == 1){
            getCartList();

            cartTotal = 0;

            if(promoFragment == null || promoFragment.getCartTotal() == 0) {
                cartFragment = (cartFragment == null) ? new CartFragment() : cartFragment;
                cartFragment.setcList(cartList);

                for (Cart c : cartList)
                    if (c != null)
                        cartTotal += (double) c.getItemQuantity() * (Double.parseDouble(c.getItemPrice()));

                cartFragment.setTotalResult(cartTotal);
            }
            else {
                cartTotal = promoFragment.getCartTotal();
                cartFragment.setTotalResult(cartTotal);
            }
            getSupportFragmentManager().beginTransaction()
                    .addToBackStack("CartFragment")
                    .replace(R.id.container, cartFragment)
                    .commit();

        }
        else if(position == 2) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        else if(position == 3){
            promoFragment = (promoFragment == null)? new PromoFragment():promoFragment;


            promoFragment.setCartArrayList(cartList, cartTotal);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, promoFragment)
                    .commit();
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Senpai, use this to send data from MainActivityFragment -> MainActivity.
     * Currently, sends the cart data for the item that was currently clicked.
     */
    @Override
    public void onNewCartItemAddedListener(final Cart cart) {
        //TODO: Remove logs before deploy
        Log.d(TAG, "Item added : " + cart.toString());

        getCartList();

        Snackbar.make(this.toolbar, cart.getItemQuantity() + " " + cart.getItemName() + " Added To Cart", Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCartList();

                        if (cartList != null) {
                            cartList.remove(cart);
                            setCartList();
                        }

                        Snackbar.make(toolbar, cart.getItemName() + " Removed From Cart", Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
    }

    /**
     * Use this method to get the MainActivityFragment.cartList
     */
    private void getCartList() {
        if(mainActivityFragment != null)
            this.cartList = mainActivityFragment.getCartList();
    }

    /**
     * Use this method to set the MainActivityFragment.cartList
     */
    private void setCartList() {
        if(mainActivityFragment != null)
            mainActivityFragment.setCartList(cartList);
    }

    @Override
    protected void onPause() {
        if(cartFragment != null) {
            cartFragment.destroyProgressDialog();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(cartFragment != null) {
            cartFragment.destroyProgressDialog();
        }
        super.onStop();
    }
}
