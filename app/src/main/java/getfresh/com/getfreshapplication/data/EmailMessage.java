package getfresh.com.getfreshapplication.data;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Yue on 6/19/2015.
 */
public class EmailMessage {

    //Set your uncles email address here
    private static final String emailAddress = "abc@gmail.com";

    private String subject;
    private String message;
    private String phoneNumber;

    private EmailMessage() { }

    public Intent createEmailIntent() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        return Intent.createChooser(emailIntent, "Send email...");
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static class Builder {

        private Address location;

        private ArrayList<Cart> cartList;
        private String totalPrice;
        private String name;
        private String subject;
        private String phoneNumber;
        private String addressLine;
        private String featureName;
        private String locality;
        private String postalCode;
        private String subLocality;
        private String lat, lon;

        public Builder(Address location) {
            this.location = location;
        }

        public EmailMessage build() {
            EmailMessage email = new EmailMessage();

            StringBuilder sb = new StringBuilder();

            if(!TextUtils.isEmpty(name)) {
                sb.append("Name : " + name + "\n");
            }

            if(!TextUtils.isEmpty(subject))
                email.subject = subject;

            if(!TextUtils.isEmpty(phoneNumber)) {
                sb.append("Phone No. : " + phoneNumber + "\n");
            }

            if(!TextUtils.isEmpty(lat) || !TextUtils.isEmpty(lon)) {
                sb.append("\nLatitude/Longitude : " + "(" + lat + ", " + lon + ") \n " + "http://maps.google.com/?q=" + lat + "," + lon + "\n\n");
            }

            if(!TextUtils.isEmpty(addressLine)) {
                sb.append("Address Line : " + addressLine + "\n");
            }

            if(!TextUtils.isEmpty(featureName)) {
                sb.append("Feature Name : " + featureName + "\n");
            }

            if(!TextUtils.isEmpty(locality)) {
                sb.append("Locality : " + locality + "\n");
            }

            if(!TextUtils.isEmpty(subLocality)) {
                sb.append("Sub Locality : " + subLocality + "\n");
            }

            if(!TextUtils.isEmpty(postalCode)) {
                sb.append("Postal Code : " + postalCode + "\n");
            }

            if(cartList != null) {
                for(int i = 0; i < cartList.size(); i++) {
                    sb.append("\n" + cartList.get(i).toString());
                }
            }

            if(!TextUtils.isEmpty(totalPrice)) {
                sb.append("\n\n Total Price : " + totalPrice);
            }

            email.message = sb.toString();

            return email;
        }

        public Builder setCartList(ArrayList<Cart> cartList) {
            this.cartList = cartList;
            return this;
        }

        public Builder setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSubject(String tag) {
            /*Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int min = c.get(Calendar.MINUTE);
            int sec = c.get(Calendar.SECOND);

            int date = c.get(Calendar.DATE);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);*/
            DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS");
            String stamp = d.format(Calendar.getInstance().getTime());

            this.subject = this.name + " " + stamp;
            return this;
        }

        public Builder setPhoneNumber(String phone) {
            this.phoneNumber = phone;
            return this;
        }

        public Builder setAddressLine() {
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < location.getMaxAddressLineIndex(); i++) {
                sb.append(location.getAddressLine(i));
            }
            addressLine = sb.toString();
            return this;
        }

        public Builder setFeatureName() {
            this.featureName = location.getFeatureName();
            return this;
        }

        public Builder setLocality() {
            this.locality = location.getLocality();
            return this;
        }

        public Builder setPostalCode() {
            this.postalCode = location.getPostalCode();
            return this;
        }

        public Builder setSubLocality() {
            this.subLocality = location.getSubLocality();
            return this;
        }

        public Builder setLat(String lat) {
            this.lat = lat;
            return this;
        }

        public Builder setLon(String lon) {
            this.lon = lon;
            return this;
        }
    }
}
