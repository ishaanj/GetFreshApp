package getfresh.com.getfreshapplication.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import getfresh.com.getfreshapplication.LoginActivity;
import getfresh.com.getfreshapplication.MainActivity;
import getfresh.com.getfreshapplication.R;
import getfresh.com.getfreshapplication.settings.SettingsActivity;

/**
 * @author Somshubra
 */
public class LoginFragment extends Fragment {

    private static final String PASS = SettingsActivity.GetFreshPreferenceFragment.KEY_PASS;

    private ImageSwitcher imgSwitcher;
    private TextView labelPhone;
    private EditText  phone, password;
    private Button submit;

    private boolean phoneIsEmpty, phoneHasAlphabets, passIsNotEmpty, passIsCorrect;

    private ImageSwitchTask task;

    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        labelPhone = (TextView) v.findViewById(R.id.label_phone);
        phone = (EditText) v.findViewById(R.id.edit_phone);
        password = (EditText) v.findViewById(R.id.edit_password);

        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String pass = sp.getString(PASS, "");

        if(!TextUtils.isEmpty(pass)) {
            passIsNotEmpty = true;
            password.setVisibility(View.VISIBLE);
            phone.setVisibility(View.INVISIBLE);
            labelPhone.setVisibility(View.INVISIBLE);
        }
        else {
            passIsNotEmpty = false;
            password.setVisibility(View.GONE);
            phone.setVisibility(View.VISIBLE);
            labelPhone.setVisibility(View.VISIBLE);
        }

        imgSwitcher = (ImageSwitcher) v.findViewById(R.id.login_switcher);
        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(getActivity());
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return iv;
            }
        });

        Animation fadein = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in);
        fadein.setDuration(1000);
        imgSwitcher.setInAnimation(fadein);
        Animation fadeout = AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out);
        fadeout.setDuration(1000);
        imgSwitcher.setOutAnimation(fadeout);

        task = new ImageSwitchTask(imgSwitcher);
        task.execute();

        submit = (Button) v.findViewById(R.id.login_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneText = phone.getText().toString();
                String passwordText = password.getText().toString();

                if(!passIsNotEmpty) {
                    if (TextUtils.isEmpty(phoneText)) {
                        Snackbar.make(submit, "Phone Number cannot be empty", Snackbar.LENGTH_SHORT).show();
                        phoneIsEmpty = true;
                    } else {
                        phoneIsEmpty = false;
                    }

                    if (!TextUtils.isDigitsOnly(phoneText) && !TextUtils.isEmpty(phoneText)) {
                        Snackbar.make(submit, "Phone Number must have only digits", Snackbar.LENGTH_SHORT).show();
                        phoneHasAlphabets = true;
                    } else {
                        phoneHasAlphabets = false;
                    }
                }

                if(passIsNotEmpty) {
                    if(!TextUtils.isEmpty(passwordText) && passwordText.length() == 4 && TextUtils.isDigitsOnly(passwordText)) {
                        if(passwordText.equals(pass)) {
                            passIsCorrect = true;
                        }
                        else {
                            Snackbar.make(submit, "Password is incorrect", Snackbar.LENGTH_SHORT).show();
                            passIsCorrect = false;
                        }
                    }
                    else {
                        Snackbar.make(submit, "Password must be 4 digits", Snackbar.LENGTH_SHORT).show();
                        passIsCorrect = false;
                    }
                }

                if(!passIsNotEmpty) {
                    if (!phoneIsEmpty && !phoneHasAlphabets) {
                        sp.edit().putBoolean(LoginActivity.KEY_LOGIN_VISITED, true).commit();
                        sp.edit().putString(SettingsActivity.GetFreshPreferenceFragment.KEY_PHONE, phoneText)
                                .apply();

                        if(task != null)
                            task.cancel(true);

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    }
                }
                else {
                    if (!phoneIsEmpty && !phoneHasAlphabets && passIsCorrect) {
                        sp.edit().putBoolean(LoginActivity.KEY_LOGIN_VISITED, true)
                                .putBoolean(MainActivity.KEY_LOGGED_IN, true)
                                .commit();

                        if(task != null)
                            task.cancel(true);

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    }
                }
            }
        });

        return v;
    }

    private class ImageSwitchTask extends AsyncTask<Void, Integer, Void> {

        private ImageSwitcher switcher;
        private int[] imageIds = new int[] {
                R.drawable.aaismasala_big,
                R.drawable.chillycheese_big,
                R.drawable.comint_big,
                R.drawable.creamyajwain_big,
                R.drawable.freshthai_big,
                R.drawable.jaffana_big,
                R.drawable.kasurimethi_big,
                R.drawable.kolkatacalling_big,
                R.drawable.kovalam_big,
                R.drawable.madrasmagic_big,
                R.drawable.nizamekhas_big,
                R.drawable.peppybbq_big,
                R.drawable.periperi_big,
                R.drawable.rechado_big,
                R.drawable.suriyani_big,
                R.drawable.tikkatwist_big
        };

        private long timeToSwitch = 5000;

        public ImageSwitchTask(ImageSwitcher switcher) {
            this.switcher = switcher;
        }

        public ImageSwitchTask(ImageSwitcher switcher, long timeToSwitch) {
            this.switcher = switcher;
            this.timeToSwitch = timeToSwitch;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            imgSwitcher.setImageResource(imageIds[values[0]]);
        }

        @Override
        protected Void doInBackground(Void... params) {
            int currentImgPosition = 0;
            while(!isCancelled()) {
                publishProgress(currentImgPosition);

                try {
                    currentImgPosition = (currentImgPosition + 1) % imageIds.length;
                    Thread.sleep(timeToSwitch);
                } catch (InterruptedException e) {

                }
            }

            return null;
        }
    }


}
