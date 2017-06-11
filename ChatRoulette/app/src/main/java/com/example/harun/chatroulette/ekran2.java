package com.example.harun.chatroulette;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.method.MultiTapKeyListener;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.List;

/**
 * Created by Harun on 14.05.2017.
 */

public class ekran2 extends Activity {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://msevinc-001-site1.ctempurl.com/dd/Service1.svc"; //location
    private String TAG = "Harun", Searchuser, selecteduser, knmuser, TESTER;
    private static String username;
    private static String Cevap;
    public LocationManager locationManager;
    public LocationListener locationListener;
    double latitute, longitude;
    String lat, lon;
    String aray[] = new String[0];
    String erey[] = new String[0];
    TextView takipedilecelview, testview;
    konumbilgileri currentUser = new konumbilgileri();
    Button kabulbtn, retbtn, takipgndrbn, haritadagosterbtn, logoutbtn, testbtn;
    Spinner spinner, spinner2;
    int REQ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ekran_2);
        final SharedPreferences preferences;
        preferences = getSharedPreferences("Veriler", MODE_PRIVATE);
        logoutbtn = (Button) findViewById(R.id.button8);
        takipgndrbn = (Button) findViewById(R.id.button4);
        takipedilecelview = (TextView) findViewById(R.id.editText3);
        kabulbtn = (Button) findViewById(R.id.button6);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        retbtn=(Button) findViewById(R.id.button5);
        haritadagosterbtn = (Button) findViewById(R.id.button7);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("KA");
                editor.remove("PA");
                editor.remove("SCC");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(intent);
            }
        });
        takipgndrbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takipedilecelview.getText().length() != 0 && takipedilecelview.getText().toString() != "") {
                    Searchuser = takipedilecelview.getText().toString();
                    tkb task = new tkb();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Takipci Adını Hatalı Girdiniz", Toast.LENGTH_LONG).show();
                }
            }
        });
        kabulbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null) {
                    selecteduser = spinner.getSelectedItem().toString();
                    tkbonay task = new tkbonay();
                    task.execute();
                } else
                    Toast.makeText(getApplicationContext(), "Takipci Seç !!!", Toast.LENGTH_SHORT).show();
            }
        });
        haritadagosterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner2.getSelectedItem() != null) {
                    knmuser = spinner2.getSelectedItem().toString();
                    knm t = new knm();
                    t.execute();
                }
            }
        });
        retbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem() != null) {
                    selecteduser = spinner.getSelectedItem().toString();
                    red task = new red();
                    task.execute();
                } else
                    Toast.makeText(getApplicationContext(), "Takipci Seç !!!", Toast.LENGTH_SHORT).show();

            }
        });
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
            }, 10);
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitute = location.getLatitude();
                longitude = location.getLongitude();
                String konum = "Konum " + location.getLatitude() + " , " + location.getLongitude();
                Toast.makeText(getApplicationContext(), konum, Toast.LENGTH_LONG).show();
                cha task = new cha();
                task.execute();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (preferences.getBoolean("SCC", false)) {
            username = preferences.getString("KA", "");

        }
        ony task3 = new ony();
        task3.execute();
        gbt task2 = new gbt();
        task2.execute();
        cha task = new cha();
        task.execute();
    }

    private void configurebutton() {
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(intent);
    }

    private class cha extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            latitute = location.getLatitude();
            longitude = location.getLongitude();
            int ret = konumKayıt();
            if (ret == 1) {
                Cevap = "Başarı ile Kayıt Edildi";
            } else
                Cevap = "Hatalı" + ret;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }  //konumu kaydet

    private class tkb extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int res = takipIstegiGonder();
            if (res == 0) {
                Cevap = "Hatalı Takip isteği";
            } else {
                Cevap = "Takip İsteği Gönderildi";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), Cevap, Toast.LENGTH_SHORT).show();
        }
    } //takip isteği gönder

    private class tkbonay extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int ret = takiponayla();
            if (ret == 0) {
                Cevap = "Takip onaylama sorunu";
            } else {
                Cevap = "Takip Onaylandı";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), Cevap, Toast.LENGTH_SHORT).show();
        }
    } //takibi onayla

    private class ony extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int ret = onaylanmayantakipcilerigoster();
            if (ret == 0) {
                Cevap = "Haiç Takip İsteğiniz Yok";
            } else {
                Cevap = ret + "  Tane Takip isteğiniz var !";
                REQ = ret;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), Cevap, Toast.LENGTH_SHORT).show();
            if (aray.length != 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, aray);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }
    } //Onaylanmayan takipcileri spinnere ekle

    private class gbt extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int ret = onaylanatakipcilerigoster();
            if (ret == 0) {
                Cevap = "Takipcileri Getirmede Hata";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (erey.length != 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, erey);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter);
            }
        }
    }

    private class knm extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            int ret = konumBilgiAl();
            if (ret == 0) {
                Cevap = "Konumbilgi Alma Hatası";
            } else {
                Cevap = "Geldi";
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("X", currentUser.x);
                intent.putExtra("Y", currentUser.y);
                intent.putExtra("UN", currentUser.username);
                intent.putExtra("T", currentUser.time);
                startActivity(intent);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), Cevap, Toast.LENGTH_SHORT).show();
        }
    }

    private class red extends AsyncTask<String,Void,Void>{
        @Override
        protected Void doInBackground(String... params) {
            int ret = takipret();
            if(ret==0)
            {
                Cevap="Takipçi Ret edilemedi HATA!";
            }
            else{
                Cevap="Takipçi Reddedildi";
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(),Cevap,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configurebutton();
                return;
        }
    }

    public int konumKayıt() {
        SoapObject request = new SoapObject(NAMESPACE, "konumkaydet");
        PropertyInfo x = new PropertyInfo();
        x.setName("x");
        lat = String.valueOf(latitute);
        x.setValue(lat);
        x.setType(String.class);
        request.addProperty(x);
        PropertyInfo y = new PropertyInfo();
        y.setName("y");
        lon = String.valueOf(longitude);
        y.setValue(lon);
        y.setType(String.class);
        request.addProperty(y);
        PropertyInfo adres = new PropertyInfo();
        adres.setName("adres");
        adres.setType(String.class);
        adres.setValue("konum");
        request.addProperty(adres);
        PropertyInfo time = new PropertyInfo();
        time.setName("time");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String zaman = day + "." + month + "." + year + " " + hour + ":" + minute + ":" + second;
        time.setValue(zaman);
        time.setType(String.class);
        request.addProperty(time);
        PropertyInfo ack = new PropertyInfo();
        ack.setType(String.class);
        ack.setName("aciklama");
        ack.setValue("Aciklama");
        request.addProperty(ack);
        PropertyInfo userpı = new PropertyInfo();
        userpı.setValue(username);
        userpı.setName("username");
        userpı.setType(String.class);
        request.addProperty(userpı);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/konumkaydet", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Res = Integer.parseInt(response.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int takipIstegiGonder() {
        SoapObject request = new SoapObject(NAMESPACE, "takipet");
        PropertyInfo takipciusernamePI = new PropertyInfo();
        takipciusernamePI.setName("takipciusername");
        takipciusernamePI.setType(String.class);
        takipciusernamePI.setValue(username);
        request.addProperty(takipciusernamePI);
        PropertyInfo takipedilecekPI = new PropertyInfo();
        takipedilecekPI.setName("takipedilcekusername");
        takipedilecekPI.setType(String.class);
        takipedilecekPI.setValue(Searchuser);
        request.addProperty(takipedilecekPI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/takipet", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Res = Integer.parseInt(response.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int konumBilgiAl() {
        SoapObject request = new SoapObject(NAMESPACE, "konumbilgial");
        PropertyInfo konumbulPI = new PropertyInfo();
        konumbulPI.setName("username");
        konumbulPI.setValue(knmuser);
        konumbulPI.setType(String.class);
        request.addProperty(konumbulPI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/konumbilgial", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            currentUser.x = response.getProperty("x").toString();
            currentUser.y = response.getProperty("y").toString();
            currentUser.adres = response.getProperty("adres").toString();
            currentUser.time = response.getProperty("time").toString();
            currentUser.aciklama = response.getProperty("aciklama").toString();
            currentUser.username = response.getProperty("username").toString();
            Res = 1;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int takiponayla() {
        SoapObject request = new SoapObject(NAMESPACE, "takiponay");
        PropertyInfo takipciusernamePI = new PropertyInfo();
        takipciusernamePI.setName("takipciusername");
        takipciusernamePI.setType(String.class);
        takipciusernamePI.setValue(selecteduser);
        request.addProperty(takipciusernamePI);
        PropertyInfo takipedilecekPI = new PropertyInfo();
        takipedilecekPI.setName("takipedilcekusername");
        takipedilecekPI.setType(String.class);
        takipedilecekPI.setValue(username);
        request.addProperty(takipedilecekPI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/takiponay", envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Res = Integer.parseInt(response.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int onaylanmayantakipcilerigoster() {
        SoapObject request = new SoapObject(NAMESPACE, "onaylanmayantakipciler");
        PropertyInfo takipciusernamePI = new PropertyInfo();
        takipciusernamePI.setName("username");
        takipciusernamePI.setType(String.class);
        takipciusernamePI.setValue(username);
        request.addProperty(takipciusernamePI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            int j = 0;
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/onaylanmayantakipciler", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            if (response.getPropertyCount() != 0 && response.getPropertyCount() < 15) {
                aray = new String[response.getPropertyCount()];
                for (j = 0; j < response.getPropertyCount(); j++) {
                    TESTER = response.getProperty(j).toString();
                    aray[j] = TESTER;
                }
            }
            Res = j;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int onaylanatakipcilerigoster() {
        SoapObject request = new SoapObject(NAMESPACE, "takipedilenler");
        PropertyInfo takipciusernamePI = new PropertyInfo();
        takipciusernamePI.setName("username");
        takipciusernamePI.setType(String.class);
        takipciusernamePI.setValue(username);
        request.addProperty(takipciusernamePI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            int j = 0;
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/takipedilenler", envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            if (response.getPropertyCount() != 0 && response.getPropertyCount() < 15) {
                erey = new String[response.getPropertyCount()];
                for (j = 0; j < response.getPropertyCount(); j++) {
                    TESTER = response.getProperty(j).toString();
                    erey[j] = TESTER;
                }
            }
            Res = j;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Res;
    }

    public int takipret(){
        SoapObject request = new SoapObject(NAMESPACE, "takipred");
        PropertyInfo takipedilecekPI = new PropertyInfo();
        takipedilecekPI.setName("username");
        takipedilecekPI.setType(String.class);
        takipedilecekPI.setValue(username);
        request.addProperty(takipedilecekPI);
        PropertyInfo takipciusernamePI = new PropertyInfo();
        takipciusernamePI.setName("redusername");
        takipciusernamePI.setType(String.class);
        takipciusernamePI.setValue(selecteduser);
        request.addProperty(takipciusernamePI);
        MarshalBase64 mabase = new MarshalBase64();
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        HttpTransportSE Trans = new HttpTransportSE(URL);
        int Res = 0;
        try {
            mabase.register(envelope);
            Trans.call("http://tempuri.org/IService1/takipred", envelope);
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
