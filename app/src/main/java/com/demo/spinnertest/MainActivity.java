package com.demo.spinnertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.selva.widget.PopupActionSpinner;
import com.selva.widget.PopupTouchInterceptor;

public class MainActivity extends AppCompatActivity implements PopupTouchInterceptor,AdapterView.OnItemSelectedListener{

    private PopupActionSpinner spinner1 , spinner2, spinner3,spinner4;
    private PopupActionSpinner[] popupActionSpinnersArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner1 =(PopupActionSpinner)findViewById(R.id.spinner1);
        spinner2 = (PopupActionSpinner)findViewById(R.id.spinner2);
        spinner3 = (PopupActionSpinner)findViewById(R.id.spinner3);
        spinner4 = (PopupActionSpinner)findViewById(R.id.spinner4);

        List<String> listSpinner1 = new ArrayList<String>();
        listSpinner1.add("Spinner 1 Element 1");
        listSpinner1.add("Spinner 1 Element 2");
        listSpinner1.add("Spinner 1 Element 3");

        ArrayAdapter<String> dataAdapterSpinner1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner1);
        dataAdapterSpinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> listSpinner2 = new ArrayList<String>();
        listSpinner2.add("Spinner 2 Element 1");
        listSpinner2.add("Spinner 2 Element 2");
        listSpinner2.add("Spinner 2 Element 3");

        ArrayAdapter<String> dataAdapterSpinner2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner2);
        dataAdapterSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> listSpinner3 = new ArrayList<String>();
        listSpinner3.add("Spinner 3 Element 1");
        listSpinner3.add("Spinner 3 Element 2");
        listSpinner3.add("Spinner 3 Element 3");

        ArrayAdapter<String> dataAdapterSpinner3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner3);
        dataAdapterSpinner3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        List<String> listSpinner4 = new ArrayList<String>();
        listSpinner4.add("Spinner 4 Element 1");
        listSpinner4.add("Spinner 4 Element 2");
        listSpinner4.add("Spinner 4 Element 3");

        ArrayAdapter<String> dataAdapterSpinner4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listSpinner4);
        dataAdapterSpinner4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinner1.setAdapter(dataAdapterSpinner1);
        spinner2.setAdapter(dataAdapterSpinner2);
        spinner3.setAdapter(dataAdapterSpinner3);
        spinner4.setAdapter(dataAdapterSpinner4);

        popupActionSpinnersArr = new PopupActionSpinner[4];
        popupActionSpinnersArr[0] = spinner1;
        popupActionSpinnersArr[1] = spinner2;
        popupActionSpinnersArr[2] = spinner3;
        popupActionSpinnersArr[3] = spinner4;

        spinner1.setPopupTouchInterceptor(this,popupActionSpinnersArr);
        spinner2.setPopupTouchInterceptor(this,popupActionSpinnersArr);
        spinner3.setPopupTouchInterceptor(this,popupActionSpinnersArr);
        spinner4.setPopupTouchInterceptor(this,popupActionSpinnersArr);

        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        spinner3.setOnItemSelectedListener(this);
        spinner4.setOnItemSelectedListener(this);

    }

    @Override
    public void onTouchIntercepted(View viewClicked) {
        Log.i("CLicked",viewClicked.getId()+"");
        ((PopupActionSpinner)viewClicked).performClick();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("Selected",(String) adapterView.getItemAtPosition(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
