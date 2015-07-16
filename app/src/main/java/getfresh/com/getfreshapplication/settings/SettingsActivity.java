package getfresh.com.getfreshapplication.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import getfresh.com.getfreshapplication.R;

/**
 * @author Somshubra
 */
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
        public static final String KEY_ADDRESS_STREET = "KEY_ADDRESS_STREET";
        public static final String KEY_ADDRESS_BUILDING = "KEY_ADDRESS_BUILDING";
        public static final String KEY_ALT_NAME = "KEY_ALT_NAME";
        public static final String KEY_ALT_ADDRESS_STREET = "KEY_ALT_ADDRESS_STREET";
        public static final String KEY_ALT_ADDRESS_BUILDING = "KEY_ALT_ADDRESS_BUILDING";
        public static final String KEY_ALT_ADDRESS = "KEY_ALT_ADDRESS";
        public static final String KEY_PASS = "KEY_PASS";
        public static final String KEY_INSTRUCTIONS_MAIN = "KEY_INST_MAIN";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            addPreferencesFromResource(R.xml.pref_general);

            Preference inst = findPreference(KEY_INSTRUCTIONS_MAIN);
            inst.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(KEY_INSTRUCTIONS_MAIN, false).apply();
                    Toast.makeText(getActivity(), "Tutorial Reset", Toast.LENGTH_LONG).show();
                    return true;
                }
            });
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
            else if(key.equals(KEY_ADDRESS_BUILDING)) {
                EditTextPreference p = (EditTextPreference) findPreference(key);
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Building cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_ADDRESS_STREET)) {
                EditTextPreference p = (EditTextPreference) findPreference(key);
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Street cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_ALT_NAME)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_ALT_NAME");
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
            else if(key.equals(KEY_ALT_ADDRESS)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_ALT_ADDRESS");
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Address cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_ALT_ADDRESS_BUILDING)) {
                EditTextPreference p = (EditTextPreference) findPreference(key);
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Building cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_ALT_ADDRESS_STREET)) {
                EditTextPreference p = (EditTextPreference) findPreference(key);
                String address = p.getEditText().getText().toString();

                if(TextUtils.isEmpty(address)) {
                    Toast.makeText(getActivity(), "Street cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            else if(key.equals(KEY_PASS)) {
                EditTextPreference p = (EditTextPreference) findPreference("KEY_PASS");
                String pass = p.getEditText().getText().toString();

                if(pass.length() != 4 || !TextUtils.isDigitsOnly(pass)) {
                    sp.edit().putString(KEY_PASS, "").commit();
                    Toast.makeText(getActivity(), "Passcode must be 4 digits", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Passcode saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
