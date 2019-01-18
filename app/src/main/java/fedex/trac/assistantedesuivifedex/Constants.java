package fedex.trac.assistantedesuivifedex;

import android.util.Base64;

public class Constants {

    public static final String BASE_URL = "https://api.mailgun.net/v3/sandboxe8a223b5554b4c12abff1fb2ded9b3c2.mailgun.org/";
    public static final String AUTH = "Basic " + Base64.encodeToString(("api:db3c517ceb26ffa8118c5576c9a63a27-0e6e8cad-2f2de2d9").getBytes(), Base64.NO_WRAP);

    public static final String EMAIL_FROM = "admin@receivedsms.com";

}

