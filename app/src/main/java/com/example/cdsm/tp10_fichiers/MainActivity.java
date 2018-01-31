package com.example.cdsm.tp10_fichiers;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radiogroup;
    private RadioButton internalRadio;
    private Button buttonSave;
    private Button buttonRead;
    private Button buttonDelete;
    private EditText fileContent;
    private TextView fileContentView;


    private String fileName;
    private String internalPath;
    private String externalPath;
    private File curentFile;
    private int MY_PERMISSION_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initapp();
        appListeners();

    }

    private void appListeners() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                fileName = (i == R.id.radioInterne) ? internalPath + "/test.txt" : externalPath + "/test.txt";
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String text = fileContent.getText().toString();
                    if (text.isEmpty()) {
                        showMessage("WRITE - Aborted - File content will be empty ! ");

                    } else {
                        writeFile(text);
                        resetView();
                        showMessage("WRITE - Success - "+curentFile.getAbsolutePath()+curentFile.getName());
                    }
                }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder text = new StringBuilder();

                if (curentFile == null) {
                    showMessage("READ - Aborted - File does not exist ! ");

                } else {
                    showMessage("READ - Success - "+curentFile.getAbsolutePath()+curentFile.getName());
                    readFile(text);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (curentFile.exists()) {
                    boolean b = curentFile.delete();
                    resetView();
                    showMessage("DELETE - Success - "+curentFile.getAbsolutePath()+curentFile.getName());
                    curentFile = null;

                    if (!b) {
                        showMessage("DELETE - Aborted - An error occured ! ");
                    }
                } else {
                    showMessage("DELETE - Aborted - File does not exist ! ");
                }
            }
        });
    }

    private void initapp() {
        MY_PERMISSION_CODE = 1234;

        externalPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
        System.out.println(externalPath);
        internalPath = getApplication().getFilesDir().toString();


        buttonSave = ((Button) findViewById(R.id.button1));
        buttonRead = ((Button) findViewById(R.id.button2));
        buttonDelete = ((Button) findViewById(R.id.button3));
        fileContent = ((EditText) findViewById(R.id.editText));
        fileContentView = ((TextView) findViewById(R.id.textView));
        radiogroup = ((RadioGroup) findViewById(R.id.radiogroup));


        radiogroup.check(R.id.radioInterne);
        fileName = internalPath + "/test.txt";

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(MainActivity.this, permissions, MY_PERMISSION_CODE);
    }

    private void resetView() {
        fileContent.setText("");
        fileContentView.setText("");
    }

    private void readFile(StringBuilder text) {
        try {
            Scanner scaner = new Scanner(curentFile);

            while (scaner.hasNextLine()) {
                text.append(scaner.nextLine());
            }

            fileContentView.setText(text.toString());

        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException in MainActivity onSaveClick() call : " + fnfe.getMessage());
        }
    }


    private void writeFile(String text) {
        try {
            curentFile = new File(fileName);

            OutputStream os = new FileOutputStream(curentFile);
            os.write(text.getBytes());
            os.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println("FileNotFoundException in MainActivity onSaveClick() call : " + fnfe.getMessage());
        } catch (IOException ie) {
            System.err.println("IOException in MainActivity onSaveClick() call : " + ie.getMessage());
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
