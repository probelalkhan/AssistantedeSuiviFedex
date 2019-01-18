package fedex.trac.assistantedesuivifedex;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class UserActivity extends AppCompatActivity {

    private SmsVerifyCatcher smsVerifyCatcher;
    private TextView textViewNumber, textViewDate, textViewCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (!SharedPrefManager.getInstance(this).isPhoneAlreadySaved()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            return;
        }

        startDetectingSMS();

        textViewNumber = findViewById(R.id.textViewNumber);
        textViewDate = findViewById(R.id.textViewDate);
        textViewCondition = findViewById(R.id.textViewCondition);

        textViewNumber.setText(SharedPrefManager.getInstance(this).getPhone());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(new Date());
        textViewDate.setText(strDate);

        textViewCondition.setText("Colis en préparation\npour la livraison");
    }

    private void startDetectingSMS() {
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
            }
        });
        smsVerifyCatcher.onStart();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

