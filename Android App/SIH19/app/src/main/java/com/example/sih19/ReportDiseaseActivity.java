package com.example.sih19;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportDiseaseActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText age;
    private EditText pincode;
    private EditText number;
    private EditText address;
    private EditText dengueBed;
    private EditText malariaBed;
    private EditText tuberculosisBed;
    private EditText owner;
    private EditText govt_id;
    private EditText adhaar;
    private EditText docRegNo;
    private EditText disease;
    private EditText hosName;
    private EditText HosReg;
    private EditText accreditation;
    private Button report_upload;
    private Button certi_upload;
    private Button report_disease;
    private Spinner gender;
    private Spinner type_user;
    private RadioGroup rg;
    private LinearLayout freeBeds;
    private LinearLayout labEssentials;
    private LinearLayout hospital;
    private String sfirstName;
    private String slastName;
    private String sage;
    private String spincode;
    private String snumber;
    private String saddress;
    private String sdengueBed;
    private String smalariaBed;
    private String stuberculosisBed;
    private String sowner;
    private String sgovt_id;
    private String sadhaar;
    private String sdocRegNo;
    private String sdisease;
    private String shosName;
    private String sHosReg;
    private String saccreditation;
    private static final int report = 1888;
    private static final int certi = 2888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Bitmap report_pic;
    private Bitmap certi_pic;
    private LinearLayout parentLL;
    private String TAG;
    private String uploadURL = "https://80fae82c.ngrok.io/db/report2/";
    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_disease);

        firstName = (EditText)findViewById(R.id.editTextFirstName);
        lastName = (EditText)findViewById(R.id.editTextLastName);
        age = (EditText)findViewById(R.id.editTextAge);
        pincode = (EditText)findViewById(R.id.editTextPincode);
        number = (EditText)findViewById(R.id.editTextNumber);
        address = (EditText)findViewById(R.id.editTextAddress);
        dengueBed = (EditText)findViewById(R.id.editTextDengue);
        malariaBed = (EditText)findViewById(R.id.editTextMalaria);
        tuberculosisBed = (EditText)findViewById(R.id.editTextTuberculosis);
        owner = (EditText)findViewById(R.id.editTextOwner);
        govt_id = (EditText)findViewById(R.id.editTextGovtId);
        adhaar = (EditText)findViewById(R.id.editTextAdhaar);
        docRegNo = (EditText)findViewById(R.id.editTextDocReg);
        disease = (EditText)findViewById(R.id.editTextDisease);
        hosName = (EditText)findViewById(R.id.editTexthospitalName);
        accreditation = (EditText)findViewById(R.id.editTextAccreditation);
        HosReg = (EditText)findViewById(R.id.editTexthosRegNo);
        report_upload = (Button)findViewById(R.id.upload_report);
        certi_upload = (Button)findViewById(R.id.upload_certificate);
        report_disease = (Button)findViewById(R.id.report_disease);
        gender = (Spinner)findViewById(R.id.type_spinner_gender);
        type_user = (Spinner)findViewById(R.id.type_spinner_user_type);
        rg = (RadioGroup)findViewById(R.id.rg);
        freeBeds = (LinearLayout)findViewById(R.id.freeBeds);
        labEssentials = (LinearLayout)findViewById(R.id.labEssentials);
        parentLL = (LinearLayout)findViewById(R.id.parentLL);
        hospital = (LinearLayout)findViewById(R.id.hospital);

        hospital.setVisibility(View.INVISIBLE);
        labEssentials.setVisibility(View.INVISIBLE);

        type_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1)
                {
                    hospital.setVisibility(View.INVISIBLE);
                    labEssentials.setVisibility(View.INVISIBLE);
                }
                else if (position==2)
                {
                    hospital.setVisibility(View.VISIBLE);
                    labEssentials.setVisibility(View.INVISIBLE);
                }
                else if (position==3)
                {
                    hospital.setVisibility(View.INVISIBLE);
                    labEssentials.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        report_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, report);
                }
            }
        });

        certi_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, certi);
                }
            }
        });

        report_disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfirstName = firstName.getText().toString();
                slastName = lastName.getText().toString();
                sage = age.getText().toString();
                spincode = pincode.getText().toString();
                snumber = number.getText().toString();
                saddress = address.getText().toString();
                sdengueBed = dengueBed.getText().toString();
                smalariaBed = malariaBed.getText().toString();
                stuberculosisBed = tuberculosisBed.getText().toString();
                sowner = owner.getText().toString();
                sgovt_id = govt_id.getText().toString();
                sadhaar = adhaar.getText().toString();
                sdocRegNo = docRegNo.getText().toString();
                sdisease = disease.getText().toString();
                shosName = hosName.getText().toString();
                saccreditation = accreditation.getText().toString();
                sHosReg = HosReg.getText().toString();
                upload();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == report && resultCode == Activity.RESULT_OK) {
            report_pic = (Bitmap) data.getExtras().get("data");
            makeReportImageView();
        }
        else if (requestCode == certi && resultCode == Activity.RESULT_OK) {
            certi_pic = (Bitmap) data.getExtras().get("data");
            makeCertiImageView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, report);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void makeReportImageView()
    {
        parentLL.removeView(report_upload);
        ImageView iv = new ImageView(getApplicationContext());
        // Set an image for ImageView
        iv.setImageBitmap(report_pic);
        // Create layout parameters for ImageView
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 400);
        lp.gravity= Gravity.CENTER;
        // Add layout parameters to ImageView
        iv.setLayoutParams(lp);
        // Finally, add the ImageView to layout
        parentLL.addView(iv,9);
    }

    private void makeCertiImageView()
    {
        labEssentials.removeView(certi_upload);
        ImageView iv = new ImageView(getApplicationContext());
        // Set an image for ImageView
        iv.setImageBitmap(certi_pic);
        // Create layout parameters for ImageView
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300, 400);
        lp.gravity= Gravity.CENTER;
        // Add layout parameters to ImageView
        iv.setLayoutParams(lp);
        // Finally, add the ImageView to layout
        labEssentials.addView(iv,0);
    }

    private void upload()
    {
        String encodedReport=null;
        String encodedCerti=null;
        if (report_pic != null)
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            report_pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            encodedReport = Base64.encodeToString(byteArray, Base64.DEFAULT).trim().replace(" ","").replace("\n","");
        }

        if (certi_pic != null)
        {
            ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
            certi_pic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream1);
            byte[] byteArray1 = byteArrayOutputStream1 .toByteArray();
            encodedCerti = Base64.encodeToString(byteArray1, Base64.DEFAULT).trim().replace(" ","").replace("\n","");
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject();
            String imgnamereport = String.valueOf(Calendar.getInstance().getTimeInMillis())+"report";
            String imgnamecerti = String.valueOf(Calendar.getInstance().getTimeInMillis())+"certi";

            /*if (encodedReport!=null)
            {
                jsonObject.put("report_upload",encodedReport);
            }

            if (encodedCerti!=null)
            {
                jsonObject.put("certi_upload",encodedCerti);
            }*/
            jsonObject.put("firstName",sfirstName);
            jsonObject.put("lastName",slastName);
            jsonObject.put("age",sage);
            jsonObject.put("pincode",spincode);
//            jsonObject.put("number",snumber);
//            jsonObject.put("address",saddress);
//            jsonObject.put("dengueBed",sdengueBed);
//            jsonObject.put("malariaBed",smalariaBed);
//            jsonObject.put("tuberculosisBed",stuberculosisBed);
//            jsonObject.put("owner",sowner);
//            jsonObject.put("govt_id",sgovt_id);
//            jsonObject.put("adhaar",sadhaar);
//            jsonObject.put("docRegNo",sdocRegNo);
//            jsonObject.put("disease",sdisease);
//            jsonObject.put("hosName",shosName);
//            jsonObject.put("HosReg",sHosReg);
//            jsonObject.put("accreditation",saccreditation);
//            jsonObject.put("gender",gender.getSelectedItem());
//            jsonObject.put("type_user",type_user.getSelectedItem());
//            jsonObject.put("rg",rg.getCheckedRadioButtonId());

        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                uploadURL, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Toast.makeText(getApplicationContext(),"Disease Reported Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Uploading Failed,try again",Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
