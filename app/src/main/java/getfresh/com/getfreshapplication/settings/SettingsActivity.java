package getfresh.com.getfreshapplication.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import getfresh.com.getfreshapplication.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.settings_frame, new GetFreshPreferenceFragment()).commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    public static class GetFreshPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        public static final String KEY_NAME = "KEY_USERNAME";
        public static final String KEY_PHONE = "KEY_PHONE";
        public static final String KEY_ADDRESS = "KEY_ADDRESS";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            addPreferencesFromResource(R.xml.pref_general);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sp, String key) {
            if(key.equals(KEY_NAME)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_USERNAME");
                String name = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(name)) {
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_PHONE)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_PHONE");
                String phone = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Phone Number cannot be empty", Toast.LENGTH_SHORT).show();
                }

                if(!TextUtils.isDigitsOnly(phone) && !TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Phone Number must have only digits", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_ADDRESS)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_ADDRESS");
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

}
