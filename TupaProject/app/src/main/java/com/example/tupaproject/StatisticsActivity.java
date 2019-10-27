package com.example.tupaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    ArrayList splitsArrayList = new ArrayList();
    String splitsMain; /*строка со всеми прочитанными из файла этапами*/
    String chosenToLoadFileName = "";
    String previousChosenToLoadFileName = "";
    public AlertDialog myAlertDialog;
    String fileNamesString = ""; /*строка со всеми прочитанными из файла именами файлов*/
    int numberOfNames = 0;
    int quantityOfSplitsInChosenFile = 0;
    int numberOfACurrentSplit = 0;
    String loadedSplitsOfChosenFile;
    String resultTimeString = "";
    String strForHelp;
    String strForHelp2;
    ArrayList<String> currentTimeArrayList = new ArrayList<>();
    ArrayList<String> resultTimeArrayList = new ArrayList<>();
    long elapsedMillis;
    long previousElapsedMillis = 0;
    long differenceInMillis = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }

    @Override /*ДЛЯ ВЫБОРА ИЗ СПИСКА ФАЙЛОВ (ВСПЛЫВАЮЩЕЕ ОКНОООООООООООО)*/
    protected Dialog onCreateDialog(int id) {
        final Button сircleButton = (Button) findViewById(R.id.CircleButton);
        final TextView textViewCurrentName = findViewById(R.id.textViewCurrentName);
        final TextView textViewCurrentSplit = findViewById(R.id.textViewCurrentSplit);

        /*ЧИТАЕМ ИМЕНА ФАЙЛОВ ИЗ ФАЙЛА С ИМЕНАМИ ФАЙЛОВ*/
        FileInputStream inputFile = null;
        try {
            inputFile = openFileInput("file_with_filenames.txt");
            InputStreamReader isr = new InputStreamReader(inputFile);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffer.readLine()) != null){
                strBuffer.append(lines).append("\n");
                numberOfNames++;
            }
            fileNamesString = strBuffer.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputFile != null;
                inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final String[] fileNamesStringArray = new String[numberOfNames];
        numberOfNames = 0;

        Reader reader = new StringReader(fileNamesString);
        BufferedReader buffer = new BufferedReader(reader);
        String lines1;

        int indexFileNames = 0;
        try{
            while((lines1 = buffer.readLine()) != null){
                fileNamesStringArray[indexFileNames] = lines1;
                indexFileNames++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }


        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ВЫБЕРИ ФАЙЛ"); // заголовок для диалога

                builder.setItems(fileNamesStringArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),
                                "выбран файл " + fileNamesStringArray[item],
                                Toast.LENGTH_SHORT).show();
                        chosenToLoadFileName = fileNamesStringArray[item];
                        /*ЧИТАЕМ ИЗ ВЫБРАННОГО ФАЙЛА ЭТАПЫ*/
                        final TextView stats = findViewById(R.id.Stats);
                        FileInputStream inputFile = null;
                        try {
                            inputFile = openFileInput(chosenToLoadFileName + "_time.txt");
                            InputStreamReader isr = new InputStreamReader(inputFile);
                            BufferedReader buffer = new BufferedReader(isr);
                            StringBuffer strBuffer = new StringBuffer();
                            String lines;
                            while ((lines = buffer.readLine()) != null) {
                                strBuffer.append(lines).append("\n");
                            }
                            loadedSplitsOfChosenFile = strBuffer.toString();


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                assert inputFile != null;
                                inputFile.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        stats.setText(loadedSplitsOfChosenFile);

                        /*chosenToLoadFileName = "";*/
                    }
                });
                builder.setCancelable(true);
                myAlertDialog = builder.create();
                return myAlertDialog;
            default:
                return null;
        }
    }

    public void ShowStatOnClick(View v){
        final TextView stats = findViewById(R.id.Stats);
        showDialog(1); /*выбор файла*/
        /*stats.setText();*/
        FileInputStream inputFile = null;
        /*try {
            inputFile = openFileInput(chosenToLoadFileName + "_time.txt");
            InputStreamReader isr = new InputStreamReader(inputFile);
            BufferedReader buffer = new BufferedReader(isr);
            StringBuffer strBuffer = new StringBuffer();
            String lines;
            while ((lines = buffer.readLine()) != null) {
                strBuffer.append(lines).append("\n");
            }
            loadedSplitsOfChosenFile = strBuffer.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert inputFile != null;
                inputFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    }
}
