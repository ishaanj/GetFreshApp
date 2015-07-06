package getfresh.com.getfreshapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import getfresh.com.getfreshapplication.fragment.LoginFragment;
import getfresh.com.getfreshapplication.settings.SettingsActivity;

/**
 * @author Somshubra
 */
public class LoginActivity extends AppCompatActivity {

    public static final String KEY_LOGIN_VISITED = "LOGIN_VISITED";
    public static final String KEY_PASS = SettingsActivity.GetFreshPreferenceFragment.KEY_PASS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame, new LoginFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
