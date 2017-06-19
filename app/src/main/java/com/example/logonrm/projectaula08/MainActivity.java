package com.example.logonrm.projectaula08;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {

    String METHOD_NAME = "soma";
    String SOAP_ACTION = "";

    String NAMESPACE = "http://andrecouto.com.br/";
    String SOAP_URL = "http://10.3.1.40:8080/Calculadora/Calculadora";

    private EditText tilN1;
    private EditText tilN2;
    private TextView txtResult;

    SoapObject request;
    SoapPrimitive calcular;

    ProgressDialog pdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tilN1 = (EditText) findViewById(R.id.n1);
        tilN2 = (EditText) findViewById(R.id.n2);
        txtResult = (TextView) findViewById(R.id.txtResult);
    }

    private class CalcularAsync extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {

            request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("a", params[0]);
            request.addProperty("b", params[1]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport = new HttpTransportSE(SOAP_URL);
            try {
                httpTransport.call(SOAP_ACTION, envelope);
                calcular = (SoapPrimitive) envelope.getResponse();
            } catch (Exception e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Resultado: " + calcular.toString(), Toast.LENGTH_SHORT).show();
            txtResult.setText(calcular.toString());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MainActivity.this);
            pdialog.setMessage("Converting...");
            pdialog.show();
        }
    }

    public void soma(View view) {
        CalcularAsync calcularAsync = new CalcularAsync();
        calcularAsync.execute(Integer.parseInt(tilN1.getText().toString()), Integer.parseInt(tilN2.getText().toString()));
    }
}
