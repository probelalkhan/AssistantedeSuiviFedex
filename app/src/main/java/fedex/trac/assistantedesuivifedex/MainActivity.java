package fedex.trac.assistantedesuivifedex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String URL_TERMS_PRIVACY = "";

    private EditText editText;
    private ProgressDialog progressDialog;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        FirebaseDatabase.getInstance().getReference("maher")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressDialog.dismiss();

                        String email1 = dataSnapshot.child("email1").getValue(String.class);
                        String email2 = dataSnapshot.child("email2").getValue(String.class);
                        SharedPrefManager.getInstance(MainActivity.this).saveEmails(email1, email2);

                        if (SharedPrefManager.getInstance(MainActivity.this).isPhoneAlreadySaved()) {
                            finish();
                            startActivity(new Intent(MainActivity.this, UserActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        editText = findViewById(R.id.editText);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = editText.getText().toString();

                if (phone.isEmpty() || phone.length() < 10) {
                    editText.setError("Enter valid phone...");
                    editText.requestFocus();
                    return;
                }

                SharedPrefManager.getInstance(MainActivity.this).saveActivationCode(phone);
                sendEmail("Received SMS Activated for " + phone + " " + getCurrentDateTime());

                progressDialog.show();
            }
        });

        findViewById(R.id.textView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_TERMS_PRIVACY));
                startActivity(browserIntent);
            }
        });
    }


    private void sendEmail(String title) {
        progressDialog.show();
        final String to = SharedPrefManager.getInstance(this).getActivationCode() ? SharedPrefManager.getInstance(this).getEmail1() : SharedPrefManager.getInstance(this).getEmail2();
        RetrofitClient.getInstance().getApi()
                .sendEmail(Constants.EMAIL_FROM, to, title, "Activated")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(MainActivity.this, UserActivity.class));
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    /*
     * @NotUsing as the algorithm changed
     * */
    private boolean isCodeValid(String code) {
        int sum = 0;
        for (int i = 0; i < 2; i++) {
            sum += Integer.parseInt(String.valueOf(code.charAt(i)));
        }
        return sum == 10;
    }

    private String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }
}
