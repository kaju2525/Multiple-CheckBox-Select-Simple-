package com.demo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private LinearLayout linearMain;
    private Button btn_ok;
    private CheckBox checkBox;
    private static String url = "https://raw.githubusercontent.com/pune02525/API_Test/master/.gitignore/Fruits.json";
    private LinkedHashMap<String, String> alphabet;
    private ProgressDialog progressDialog;
    private List<JSONObject> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearMain = (LinearLayout) findViewById(R.id.linearMain);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        alphabet = new LinkedHashMap<>();
        list=new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        initJsonParse();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAGS", "Final Data :" + list.toString());
                Toast.makeText(getApplicationContext(),""+list.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initJsonParse() {
        progressDialog.show();
        AndroidNetworking.get(url).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.d("TAGS", "response:" + response);

                try {
                    JSONArray jsonArray = response.getJSONArray("fruits");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        alphabet.put(object.getString("id"), object.getString("name"));

                    }
                    loadData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(ANError anError) {
                progressDialog.dismiss();
                Log.d("TAGS", "ANError:" + anError.toString());
            }
        });


    }

    private void loadData() {

        Set<?> set = alphabet.entrySet();
        Iterator<?> i = set.iterator();
        while (i.hasNext()) {
            @SuppressWarnings("rawtypes") Map.Entry me = (Map.Entry) i.next();

            System.out.print(me.getKey() + ": ");
            System.out.println(me.getValue());


            checkBox = new CheckBox(this);
            checkBox.setId(Integer.parseInt(me.getKey().toString()));
            checkBox.setText(me.getValue().toString());
            checkBox.setOnClickListener(getOnClickDoSomething(checkBox));
            linearMain.addView(checkBox);
        }
    }

    View.OnClickListener getOnClickDoSomething(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("TAGS", "ID:" + button.getId() + "," + button.getText().toString());
                JSONObject object = new JSONObject();
                try {
                    object.put("id",button.getId());
                    object.put("name",button.getText().toString());
                    list.add(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}

