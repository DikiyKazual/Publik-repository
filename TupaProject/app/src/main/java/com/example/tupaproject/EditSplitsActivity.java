package com.example.tupaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

public class EditSplitsActivity extends AppCompatActivity {

    String fileNamesString = ""; /*строка со всеми прочитанными из файла этапами*/
    String chosenToEditFileName = "";
    String lastPressedButtonEditOrDelete = "";
    int numberOfNames = 0;
    public AlertDialog myAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_splits);

        FileInputStream inputFile = null;
        FileOutputStream outputFile = null;

            try {
                inputFile = openFileInput("file_with_filenames.txt");
            } catch (FileNotFoundException e1) {
                try {             /*ПИШЕМ ИМЯ ФАЙЛА В ФАЙЛ С ИМЕНАМИ ФАЙЛОВ*/
                    outputFile = openFileOutput("file_with_filenames.txt", MODE_PRIVATE);
                    outputFile.write("".getBytes());
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
                e1.printStackTrace();
            } finally {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

        final Button SaveCreatedSplitsButton = (Button) findViewById(R.id.SaveCreatedSplitsButton);
        EditText EditTextSplitsName = (EditText) findViewById(R.id.EditTextSplitsName);
        EditTextSplitsName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SaveCreatedSplitsButton.setEnabled(!s.toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void GoToSplitsButtonOnClick(View v) {
        Intent intent = new Intent(EditSplitsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void MakeSplitsButtonOnClick(View v) { /*ЧИТОБЫ МОЖНО БЫЛО НАПИСАТЬ ИМЯ И СОДЕРЖИМОЕ НОВОГО ФАЙЛА*/
        EditText EditTextSplits = (EditText) findViewById(R.id.EditTextSplits);
        EditText EditTextSplitsName = (EditText) findViewById(R.id.EditTextSplitsName);
        Button SaveCreatedSplitsButton = (Button) findViewById(R.id.SaveCreatedSplitsButton);
        EditTextSplits.setVisibility(View.VISIBLE);
        EditTextSplitsName.setVisibility(View.VISIBLE);
        EditTextSplitsName.setEnabled(true);
        SaveCreatedSplitsButton.setVisibility(View.VISIBLE);
        EditTextSplitsName.setText("");
        EditTextSplits.setText("");
    }
                                             /**/
                                        /*ПИШЕМ В ФАЙЛ*/
                                             /**/
    public void SaveCreatedSplitsButtonOnClick(View v) throws IOException {
        EditText EditTextSplits = (EditText) findViewById(R.id.EditTextSplits);
        EditText EditTextSplitsName = (EditText) findViewById(R.id.EditTextSplitsName);
        Button SaveCreatedSplitsButton = (Button) findViewById(R.id.SaveCreatedSplitsButton);
        String writtenName = EditTextSplitsName.getText().toString();
        String writtenSplits = EditTextSplits.getText().toString();
        FileOutputStream outputFile = null;


            /*ЧИТАЕМ ИМЕНА ФАЙЛОВ ИЗ ФАЙЛА С ИМЕНАМИ ФАЙЛОВ чтобы определить, есть ли уже файл с таким названием*/
            FileInputStream inputFile = null;
            try {
                inputFile = openFileInput("file_with_filenames.txt");
                InputStreamReader isr = new InputStreamReader(inputFile);
                BufferedReader buffer = new BufferedReader(isr);
                StringBuffer strBuffer = new StringBuffer();
                String lines;
                while ((lines = buffer.readLine()) != null) {
                    strBuffer.append(lines).append("\n");
                    /*namesArrayList.add(lines);*/
                }
                fileNamesString = strBuffer.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            /*ИФ ДЛЯ ЗАПИСИ НОВОГО ФАЙЛА*/
            if (fileNamesString.indexOf(EditTextSplitsName.getText().toString()) == -1) {

                try {             /*ПИШЕМ ИМЯ ФАЙЛА В ФАЙЛ С ИМЕНАМИ ФАЙЛОВ*/
                    outputFile = openFileOutput("file_with_filenames.txt", MODE_APPEND);
                    outputFile.write((writtenName + "\n").getBytes());

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

                try {              /*ПИШЕМ ЭТАПЫ В  НОВЫЙ ОТДЕЛЬНЫЙ ФАЙЛ*/
                    outputFile = openFileOutput(writtenName + ".txt", MODE_PRIVATE);
                    outputFile.write(writtenSplits.getBytes());

                    EditTextSplits.setText("");
                    EditTextSplitsName.setText("");
                    EditTextSplits.setVisibility(View.GONE);
                    EditTextSplitsName.setVisibility(View.GONE);
                    SaveCreatedSplitsButton.setVisibility(View.GONE);
                    Toast.makeText(EditSplitsActivity.this, "УСПЕШНО ЗАПИСАНО", Toast.LENGTH_LONG).show();
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
                fileNamesString = null;
            } else {             /*ЭЛС ДЛЯ СОХРАНЕНИЯ УЖЕ СУЩЕСТВУЮЩЕГО ФАЙЛА*/
                try {
                    outputFile = openFileOutput(writtenName + ".txt", MODE_PRIVATE);
                    outputFile.write(writtenSplits.getBytes());

                    EditTextSplits.setText("");
                    EditTextSplitsName.setText("");
                    EditTextSplits.setVisibility(View.GONE);
                    EditTextSplitsName.setVisibility(View.GONE);
                    SaveCreatedSplitsButton.setVisibility(View.GONE);
                    Toast.makeText(EditSplitsActivity.this, "УСПЕШНО ЗАПИСАНО", Toast.LENGTH_LONG).show();
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
                fileNamesString = null;
            }
    }

    @Override /*ДЛЯ ВЫБОРА ИЗ СПИСКА ФАЙЛОВ (ВСПЛЫВАЮЩЕЕ ОКНО)*/
    protected Dialog onCreateDialog(int id) {

        final EditText EditTextSplits = (EditText) findViewById(R.id.EditTextSplits);
        final EditText EditTextSplitsName = (EditText) findViewById(R.id.EditTextSplitsName);
        final Button SaveCreatedSplitsButton = (Button) findViewById(R.id.SaveCreatedSplitsButton);

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
                                        Toast.LENGTH_LONG).show();
                                chosenToEditFileName = fileNamesStringArray[item];
                                /*ДЛЯ СЛУЧАЯ РЕДАКТИРОВАНИЯ ФАЙЛА*/
                                if (lastPressedButtonEditOrDelete == "Edit button was pressed last") {
                                    EditTextSplits.setVisibility(View.VISIBLE);
                                    EditTextSplitsName.setVisibility(View.VISIBLE);
                                    SaveCreatedSplitsButton.setVisibility(View.VISIBLE);
                                    /*ЧИТАЕМ ИЗ ВЫБРАННОГО ФАЙЛА ЭТАПЫ*/
                                    if (chosenToEditFileName != "") {
                                        FileInputStream inputFile = null;
                                        try {
                                            inputFile = openFileInput(chosenToEditFileName + ".txt");
                                            InputStreamReader isr = new InputStreamReader(inputFile);
                                            BufferedReader buffer = new BufferedReader(isr);
                                            StringBuffer strBuffer = new StringBuffer();
                                            String lines;
                                            while ((lines = buffer.readLine()) != null) {
                                                strBuffer.append(lines).append("\n");
                                            }
                                            EditTextSplits.setVisibility(View.VISIBLE);
                                            EditTextSplitsName.setVisibility(View.VISIBLE);
                                            EditTextSplitsName.setEnabled(false);
                                            SaveCreatedSplitsButton.setVisibility(View.VISIBLE);
                                            EditTextSplitsName.setText(chosenToEditFileName);
                                            EditTextSplits.setText(strBuffer);
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
                                    }
                                } else if (lastPressedButtonEditOrDelete == "Delete button was pressed last") {
                                    /*ДЛЯ СЛУЧАЯ УДАЛЕНИЯ ФАЙЛА*/
                                    /*удаляем имя файла из файла с именами файлов*/
                                    FileInputStream inputFile = null;
                                    try {
                                        inputFile = openFileInput("file_with_filenames.txt");
                                        InputStreamReader isr = new InputStreamReader(inputFile);
                                        BufferedReader buffer = new BufferedReader(isr);
                                        StringBuffer strBuffer = new StringBuffer();
                                        String lines;
                                        while ((lines = buffer.readLine()) != null) {
                                            strBuffer.append(lines).append("\n");
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
                                    fileNamesString.replace(chosenToEditFileName + "\n", "");
                                    FileOutputStream outputFile = null;
                                    try {             /*ПИШЕМ ИМЯ ФАЙЛА В ФАЙЛ С ИМЕНАМИ ФАЙЛОВ*/
                                        outputFile = openFileOutput("file_with_filenames.txt", MODE_PRIVATE);
                                        outputFile.write(fileNamesString.replace(chosenToEditFileName + "\n", "").getBytes());
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
                                    /*УДАЛЯЕМ ФАЙЛ С ЭТАПАМИ*/
                                    File fileToDelete = new File(getFilesDir(), chosenToEditFileName + ".txt");
                                    fileToDelete.delete();
                                    fileToDelete = new File(getFilesDir(), chosenToEditFileName + "_time.txt");
                                    fileToDelete.delete();

                                    EditTextSplits.setText("");
                                    EditTextSplitsName.setText("");
                                    EditTextSplits.setVisibility(View.GONE);
                                    EditTextSplitsName.setVisibility(View.GONE);
                                    SaveCreatedSplitsButton.setVisibility(View.GONE);

                                    Toast.makeText(getApplicationContext(), "ФАЙЛ " + chosenToEditFileName + " УДАЛЕН", Toast.LENGTH_LONG).show();
                                }
                                chosenToEditFileName = "";
                            }
                        });

                builder.setCancelable(true);
                myAlertDialog = builder.create();
                return myAlertDialog;
            default:
                return null;
        }
    }

    public void EditSplitsButtonOnClick(View v) { /*КНОПКА РЕДАКТИРОВАНИЯ СОХРАНЕННЫХ ЭТАПОВ*/
        lastPressedButtonEditOrDelete = "Edit button was pressed last";

        /*ЧИТАЕМ ИМЕНА ФАЙЛОВ ИЗ ФАЙЛА С ИМЕНАМИ ФАЙЛОВ*/
        /*FileInputStream inputFile = null;
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
        }*/

        showDialog(1); /*выбор файла*/
        /*fileNamesString = "";*/

    }

    public void DeleteSplitButtonOnClick(View v){
        lastPressedButtonEditOrDelete = "Delete button was pressed last";

        /*ЧИТАЕМ ИМЕНА ФАЙЛОВ ИЗ ФАЙЛА С ИМЕНАМИ ФАЙЛОВ*/
        /*FileInputStream inputFile = null;
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
        }*/

        showDialog(1); /*выбор файла*/
        /*EditText EditTextSplits = (EditText) findViewById(R.id.EditTextSplits);
        EditText EditTextSplitsName = (EditText) findViewById(R.id.EditTextSplitsName);
        Button SaveCreatedSplitsButton = (Button) findViewById(R.id.SaveCreatedSplitsButton);
        try {
            FileOutputStream outputFile = openFileOutput("files.txt", MODE_PRIVATE);
            outputFile.write(("").getBytes());
            outputFile.close();

            EditTextSplits.setText("");
            EditTextSplitsName.setText("");
            EditTextSplits.setVisibility(View.GONE);
            EditTextSplitsName.setVisibility(View.GONE);
            SaveCreatedSplitsButton.setVisibility(View.GONE);
            Toast.makeText(EditSplitsActivity.this, "ВСЕ УСПЕШНО УДАЛЕНО", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}
