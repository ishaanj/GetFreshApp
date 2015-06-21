package getfresh.com.getfreshapplication.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import getfresh.com.getfreshapplication.LoginActivity;
import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.settings.SettingsActivity;

/**
 * @author Somshubra
 */
public class LoginFragment extends Fragment {

    private EditText name, phone, address;
    private Button submit;

    private boolean nameIsEmpty, phoneIsEmpty, phoneHasAlphabets, addressIsEmpty;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        name = (EditText) v.findViewById(R.id.edit_name);
        phone = (EditText) v.findViewById(R.id.edit_phone);
        address = (EditText) v.findViewById(R.id.edit_address);

        submit = (Button) v.findViewById(R.id.login_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String phoneText = phone.getText().toString();
                String addressText = address.getText().toString();

                if(TextUtils.isEmpty(nameText)) {
                    Snackbar.make(submit, "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
                    nameIsEmpty = true;
                }
                else {
                    nameIsEmpty = false;
                }

                if(TextUtils.isEmpty(phoneText)) {
                    Snackbar.make(submit, "Phone Number cannot be empty", Snackbar.LENGTH_SHORT).show();
                    phoneIsEmpty = true;
                }
                else {
                    phoneIsEmpty = false;
                }

                if(!TextUtils.isDigitsOnly(phoneText) && !TextUtils.isEmpty(phoneText)) {
                    Snackbar.make(submit, "Phone Number must have only digits", Snackbar.LENGTH_SHORT).show();
                    phoneHasAlphabets = true;
                }
                else {
                    phoneHasAlphabets = false;
                }

                if(TextUtils.isEmpty(addressText)) {
                    Snackbar.make(submit, "Address cannot be empty", Snackbar.LENGTH_SHORT).show();
                    addressIsEmpty = true;
                }
                else {
                    addressIsEmpty = false;
                }

                if(!nameIsEmpty &&  !phoneIsEmpty && !phoneHasAlphabets &&  !addressIsEmpty) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(LoginActivity.KEY_LOGIN_VISITED, true).commit();
                    sp.edit().putString(SettingsActivity.GetFreshPreferenceFragment.KEY_NAME, nameText)
                             .putString(SettingsActivity.GetFreshPreferenceFragment.KEY_PHONE, phoneText)
                             .putString(SettingsActivity.GetFreshPreferenceFragment.KEY_ADDRESS, addressText)
                             .apply();

                    getActivity().finish();
                }
            }
        });

        return v;
    }


}
