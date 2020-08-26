package com.example.newsx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FrontScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText searchEt;
    private ImageButton filterBtn;
    private RecyclerView sourcesRv;

    private ArrayList<ModeSourceList> sourceLists;
    private AdapterSourceList adapterSourceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_screen);

        progressBar = findViewById(R.id.progressBar);
        searchEt = findViewById(R.id.searchh2);
        filterBtn = findViewById(R.id.fillerBtn);
        sourcesRv = findViewById(R.id.sourcesRv);

        loadSources();

        //search
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //call as and when user type/remove letter
                try {
                    adapterSourceList.getFilter().filter(s);
                } catch (Exception e) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterBottomSheet();
            }
        });

    }

    //initialy selected items (by deafault show all)
    private String selectedCountry = "All", selectedCategory = "All", selectedLanguage = "All";
    private int selectedCountryPosition = 0, selectedCategoryPosition = 0, selectedLanguagePosition = 0;

    private void filterBottomSheet() {
        //lets design layout for bottom sheet
        View view = LayoutInflater.from(this).inflate(R.layout.filter_layout, null);
        Spinner countrySpinner = view.findViewById(R.id.countrySpinner);
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        Spinner languageSpinner = view.findViewById(R.id.languageSpinner);
        Button applyBtn = view.findViewById(R.id.applyBtn);

        //create an ArrayAdapter  using the string array and a default spinner layout
        ArrayAdapter<String> adapterCountries = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Contants.COUNTRIES);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Contants.CATEGORIES);
        ArrayAdapter<String> adapterLanguages = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Contants.LANGAUGES);

        //specify the layout to use when the list of choices appears
        adapterCountries.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterLanguages.setDropDownViewResource(android.R.layout.simple_spinner_item);

        //apply adapter to our spinners
        countrySpinner.setAdapter(adapterCountries);
        categorySpinner.setAdapter(adapterCategories);
        languageSpinner.setAdapter(adapterLanguages);

        //set last selected value
        countrySpinner.setSelection(selectedCountryPosition);
        categorySpinner.setSelection(selectedCategoryPosition);
        languageSpinner.setSelection(selectedLanguagePosition);

        //spinner item selected listener
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = Contants.COUNTRIES[position];
                selectedCountryPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = Contants.CATEGORIES[position];
                selectedCategoryPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = Contants.LANGAUGES[position];
                selectedLanguagePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //setup bottom sheet dialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        //add layout/view to bottom sheet
        bottomSheetDialog.setContentView(view);

        //show bottom sheet
        bottomSheetDialog.show();


        //apply filter on click
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss dialog
                bottomSheetDialog.dismiss();

                loadSources();
            }
        });


    }

    private void loadSources() {

        Log.d("FILTER_TAG", "Country: " + selectedCountry);
        Log.d("FILTER_TAG", "Category: " + selectedCategory);
        Log.d("FILTER_TAG", "Languages: " + selectedLanguage);

        //show selected option in actionbar
        getSupportActionBar().setSubtitle("Country: " + selectedCountry + " Category:" + selectedCategory + " Language: " + selectedLanguage);
        if (selectedCountry.equals("All")) {
            selectedCountry = "";
        }
        if (selectedCategory.equals("All")) {
            selectedCategory = "";
        }
        if (selectedLanguage.equals("All")) {
            selectedLanguage = "";
        }

        //init list
        sourceLists = new ArrayList<>();
        sourceLists.clear();

        progressBar.setVisibility(View.VISIBLE);

        String url = "https://newsapi.org/v2/sources?apiKey=" + Contants.API_KEY + "&country=" + selectedCountry + "&category" + selectedCategory + "&language" + selectedLanguage;
        //Request data
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //response is got as string
                try {
                    //convert string to JSON object
                    JSONObject jsonObject = new JSONObject(response);
                    //get sources array from that object
                    JSONArray jsonArray = jsonObject.getJSONArray("sources");

                    //get all data from that array using loop
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        //make sure to use same key and spellings as in response
                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        String description = jsonObject1.getString("description");
                        String url = jsonObject1.getString("url");
                        String country = jsonObject1.getString("country");
                        String category = jsonObject1.getString("category");
                        String language = jsonObject1.getString("language");

                        //set data to model
                        ModeSourceList model = new ModeSourceList(
                                "" + id, "" + name, "" + description, "" + url, "" + category, "" + language, "" + country
                        );
                        //add model to list

                        sourceLists.add(model);
                    }
                    progressBar.setVisibility(View.GONE);
                    // adapter
                    adapterSourceList = new AdapterSourceList(FrontScreenActivity.this, sourceLists);
                    //set adapter to recyclerview
                    sourcesRv.setAdapter(adapterSourceList);
                } catch (Exception e) {

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(FrontScreenActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(FrontScreenActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
