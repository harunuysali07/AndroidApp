package com.example.harun.chatroulette;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;

public class MainMenu extends AppCompatActivity {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://msevinc-001-site1.ctempurl.com/dd/Service1.svc"; //location
    private String TAG = "Harun";
    private static String username;
    private static String password;
    private static String Cevap;

    Button buton, kayıt;

    TextView text;
    EditText user, pass;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        buton = (Button) findViewById(R.id.button);
        kayıt = (Button) findViewById(R.id.button2);
        text = (TextView) findViewById(R.id.textView);
        user = (EditText) findViewById(R.id.editText);
        pass = (EditText) findViewById(R.id.editText2);
        preferences = getSharedPreferences("Veriler" , MODE_PRIVATE);
        boolean scc = preferences.getBoolean("SCC", false);
        if (scc){
            //AUTO LOGİN
            username = preferences.getString("KA", "");
            password = preferences.getString("PA", "");
            log task = new log();
            task.execute();
        }
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().length() != 0 && user.getText().toString() != "" && pass.getText().length() != 0 && pass.getText().toString() != "") {
                    username = user.getText().toString();
                    password = pass.getText().toString();
                    log task = new log();
                    task.execute();
                } else {
                    text.setText("Kullanıcı Adı Ve Şifrenizi Giriniz !!!");
                }
            }
        });
        kayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().length() != 0 && user.getText().toString() != "" && pass.getText().length() != 0 && pass.getText().toString() != "") {
                    username = user.getText().toString();
                    password = pass.getText().toString();
                    reg task = new reg();
                    task.execute();
                } else {
                    text.setText("Kullanıcı Adı Ve Şifrenizi Giriniz !!!");
                }
            }
        });
    }
    private class log extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //arkaplanda yapılacaklar
            int Res = Login(username, password);
            if (Res == 0) {
                Cevap = "Hatalı kullanıcı Adı şifre";
            } else {
                Cevap = "Hoşgeldiniz " + username;
                kayıtEt(username, password);
            }
            Log.i(TAG, "Do in background");
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            //bitince yapılacaklar
            super.onPostExecute(aVoid);
            text.setText("" + Cevap);
        }
        @Override
        protected void onPreExecute() {
            //başlarken yapılacaklar
            super.onPreExecute();
            Log.i(TAG, "on Pre Hesaplanıyor");
            text.setText("Giriş Yapılıyor");
        }
    }

    private class reg extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int Res = KayıtOl(username, password);
            if (Res == 0) {
                Cevap = "Kayıt Olurken Hata Oluştu";
            }else if (Res == -1)
            {
                Cevap = "Bu kullanıcı Adı Kullanılmış";
            }
            else {
                Cevap = "Başarı İle Kayıt Oldunuz Giriş Yapın ";
            }
            Log.i(TAG, "Do in background");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //bitince yapılacaklar
            text.setText("" + Cevap);
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            //başlarken yapılacaklar
            super.onPreExecute();
            Log.i(TAG, "on Pre Hesaplanıyor");
            text.setText("Kayıt Yapılıyor");
        }
    }

    public void kayıtEt(String username, String password) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("KA", username);
        editor.putString("PA", password);
        editor.putBoolean("SCC", true);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),ekran2.class);
        startActivity(intent);
    }

    public int Login(String username, String password) {
        SoapObject request = new SoapObject(NAMESPACE, "login");
        PropertyInfo userPI = new PropertyInfo();
        userPI.setName("username");
        userPI.setValue(username);
        userPI.setType(String.class);
        request.addProperty(userPI);
        PropertyInfo passPI = new PropertyInfo();
        passPI.setName("password");
        passPI.setValue(password);
        passPI.setType(String.class);
        request.addProperty(passPI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/login", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Res = Integer.parseInt(response.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int KayıtOl(String username, String password) {
        SoapObject request = new SoapObject(NAMESPACE, "kayitol");
        PropertyInfo userPI = new PropertyInfo();
        userPI.setName("username");
        userPI.setValue(username);
        userPI.setType(String.class);
        request.addProperty(userPI);
        PropertyInfo passPI = new PropertyInfo();
        passPI.setName("password");
        passPI.setValue(password);
        passPI.setType(String.class);
        request.addProperty(passPI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/kayitol", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Res = Integer.parseInt(response.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }
}
