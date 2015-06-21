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
 * Created by Somshubra on 6/19/2015.
 * @author Somshubra
 */
public class EmailMessage {

    //Set your uncles email address here
    private static final String emailAddress = "ishjain321@gmail.com";

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
        private String isDiscounted;
        private String discountCode;

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
                sb.append("\nFeature Name : " + featureName + "\n");
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
                sb.append("\n\n Total Price : " + totalPrice + "\n");
            }

            if(!TextUtils.isEmpty(isDiscounted)) {
                sb.append("Is the price discounted : " + isDiscounted + "\n");
            }

            if(!TextUtils.isEmpty(discountCode) && isDiscounted.equalsIgnoreCase("true")) {
                sb.append("Discount Code : " + discountCode);
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

        public Builder setSubject() {
            DateFormat d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS");
            String stamp = d.format(Calendar.getInstance().getTime());

            this.subject = this.name + " " + stamp;
            return this;
        }

        public Builder setPhoneNumber(String phone) {
            this.phoneNumber = phone;
            return this;
        }

        public Builder setAddressLine(String address) {
            addressLine = address;
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

        public Builder setIsDiscounted(String isDiscounted) {
            this.isDiscounted = isDiscounted;
            return this;
        }

        public Builder setDiscountCode(String discountCode) {
            this.discountCode = discountCode;
            return this;
        }
    }
}
