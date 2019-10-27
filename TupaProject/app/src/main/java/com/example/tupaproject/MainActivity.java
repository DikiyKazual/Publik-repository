package com.example.tupaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {




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



    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startButton = (Button) findViewById(R.id.StartButton);
        final Button сircleButton = (Button) findViewById(R.id.CircleButton);
        TextView textViewCurrentName = findViewById(R.id.textViewCurrentName);
        TextView textViewCurrentSplit = findViewById(R.id.textViewCurrentSplit);

        сircleButton.setEnabled(false);
        startButton.setEnabled(false);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                differenceInMillis = elapsedMillis-previousElapsedMillis;
            }
        });

        textViewCurrentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textViewCurrentSplit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                сircleButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void StartOnClick(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void PauseOnClick(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void ResetOnClick(View v) {
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
        running = false;
    }

    public void GoToFirstScreenButtonOnClick(View v) {
        if(!running) {
            Intent intent = new Intent(MainActivity.this, TupaFirstScreenActivity.class);
            startActivity(intent);
        }
    }

    public void GoToEditSplitsButtonOnClick(View v) {
        if(!running) {
            Intent intent = new Intent(MainActivity.this, EditSplitsActivity.class);
            startActivity(intent);
        }
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
                            if (chosenToLoadFileName != "") {
                                if((previousChosenToLoadFileName != "") && (previousChosenToLoadFileName != chosenToLoadFileName)){
                                    splitsArrayList = new ArrayList();
                                    quantityOfSplitsInChosenFile = 0;
                                }
                                FileInputStream inputFile = null;
                                try {
                                    inputFile = openFileInput(chosenToLoadFileName + ".txt");
                                    InputStreamReader isr = new InputStreamReader(inputFile);
                                    BufferedReader buffer = new BufferedReader(isr);
                                    StringBuffer strBuffer = new StringBuffer();
                                    String lines;
                                    while ((lines = buffer.readLine()) != null) {
                                        strBuffer.append(lines).append("\n");
                                        quantityOfSplitsInChosenFile++;
                                        splitsArrayList.add(lines);
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
                                        textViewCurrentName.setText(chosenToLoadFileName);
                                        textViewCurrentSplit.setText(splitsArrayList.get(0).toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        /*chosenToLoadFileName = "";*/
                        previousChosenToLoadFileName = chosenToLoadFileName;
                    }
                });
                builder.setCancelable(true);
                myAlertDialog = builder.create();
                return myAlertDialog;
            default:
                return null;
        }
    }


    public void LoadSplitsButtonOnClick(View v) { /*ЗАГРУЗИТЬ ЭТАПЫ*/
        if(!running) {
            final TextView textViewCurrentName = findViewById(R.id.textViewCurrentName);
            final TextView textViewCurrentSplit = findViewById(R.id.textViewCurrentSplit);
            /*splitsArrayList = new ArrayList();
            /*ЧИТАЕМ ИЗ ВЫБРАННОГО ФАЙЛА ЭТАПЫ*/
            /*if (chosenToLoadFileName != "") {
                FileInputStream inputFile = null;
                try {
                    inputFile = openFileInput(chosenToLoadFileName + ".txt");
                    InputStreamReader isr = new InputStreamReader(inputFile);
                    BufferedReader buffer = new BufferedReader(isr);
                    StringBuffer strBuffer = new StringBuffer();
                    String lines;
                    while ((lines = buffer.readLine()) != null) {
                        strBuffer.append(lines).append("\n");
                        quantityOfSplitsInChosenFile++;
                        splitsArrayList.add(lines);
                    }
                    loadedSplitsOfChosenFile = strBuffer.toString();

                    textViewCurrentName.setText(chosenToLoadFileName);
                    textViewCurrentSplit.setText(splitsArrayList.get(0).toString());
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
            }*/
            showDialog(1); /*выбор файла*/

        }
    }

    public void CircleOnClick(View v) {
        TextView textViewCurrentSplit = findViewById(R.id.textViewCurrentSplit);

        strForHelp = String.valueOf(differenceInMillis);
        strForHelp = "+" + strForHelp;
        currentTimeArrayList.add(strForHelp);
        previousElapsedMillis = elapsedMillis;
        numberOfACurrentSplit++;
        if (numberOfACurrentSplit < splitsArrayList.size()) {
            textViewCurrentSplit.setText(splitsArrayList.get(numberOfACurrentSplit).toString());
        }



        /*пробуем читать файл со временем, если его нет, создаем новый и пишем в него*/
        if (numberOfACurrentSplit  == quantityOfSplitsInChosenFile) { /*пишем в файл лишь в том случае, если пройден последний этап*/
            FileInputStream inputFile = null;
            FileOutputStream outputFile = null;
            try {
                inputFile = openFileInput(chosenToLoadFileName + "_time.txt");
                InputStreamReader isr = new InputStreamReader(inputFile);
                BufferedReader buffer = new BufferedReader(isr);
                StringBuffer strBuffer = new StringBuffer();
                String lines;
                while ((lines = buffer.readLine()) != null) {
                    resultTimeArrayList.add(lines);
                }
                int i = resultTimeArrayList.size();
                int indexx;
                for(indexx = resultTimeArrayList.size() - i; i>0; i--){
                    /*strForHelp2 = resultTimeArrayList.get(indexx)+currentTimeArrayList.get(indexx);
                    resultTimeArrayList.set(indexx, strForHelp2);*/
                    resultTimeString = resultTimeString + resultTimeArrayList.get(indexx) +currentTimeArrayList.get(indexx)+ "\n";
                }
                try {
                    outputFile = openFileOutput(chosenToLoadFileName + "_time.txt", MODE_PRIVATE);
                    outputFile.write(resultTimeString.getBytes());
                    Toast.makeText(MainActivity.this, "УСПЕШНО ЗАПИСАНО", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputFile != null) {
                        try {
                            outputFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileNotFoundException e1) { /*создаем новый файл со временем, если его не было*/
                int i = currentTimeArrayList.size();
                int indexx;
                for(indexx = currentTimeArrayList.size() - i; i>0; i--){
                    resultTimeString = resultTimeString + currentTimeArrayList.get(indexx) + "\n";
                }
                try {
                    outputFile = openFileOutput(chosenToLoadFileName + "_time.txt", MODE_PRIVATE);
                    outputFile.write(resultTimeString.getBytes());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (outputFile != null) {
                        try {
                            outputFile.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Toast.makeText(MainActivity.this, "УСПЕШНО ЗАПИСАНО", Toast.LENGTH_SHORT).show();
                e1.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            resultTimeString = "";
            numberOfACurrentSplit = 0;
            quantityOfSplitsInChosenFile = 0;
            previousElapsedMillis = 0;
            currentTimeArrayList = new ArrayList<>();
            resultTimeArrayList = new ArrayList<>();
            strForHelp2 = "";
            strForHelp = "";

            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            running = false;
        }
    }

    public  void StatOnClick(View v){
        if(!running) {
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            startActivity(intent);
        }
    }
}
 /* часть кода взята с сайта https://codinginflow.com/tutorials/android/chronometer */