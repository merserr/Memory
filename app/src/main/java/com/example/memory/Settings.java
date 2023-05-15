package com.example.memory;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class Settings extends Activity {

    private static final String LOG_TAG = "==Setting==";
    private static final String FILENAME = "satz_database.txt";
    private static final String DIR_SD = "/Podcasts/Memory/";

    dbHelper dbHelper;

    String dataForSaving="";
    String readFromFile="";
    SeekBar seekBar;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Log.d(LOG_TAG, "===action_database===");
        dbHelper = new dbHelper(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);



        Button button_load = (Button) findViewById(R.id.button_load);
        button_load.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "===onClick button_load===");
                readFile();
            }
        });

        Button button_save = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "===onClick button_save===");
                Prepare_DB_for_saving();
                writeFile();
            }
        });

        Button button_load_from_file = (Button) findViewById(R.id.button_load_from_file);
        button_load_from_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "===onClick button_load_from_file===");
                read_file_from_SD();
            }
        });

        Button button_save_to_file = (Button) findViewById(R.id.button_save_to_file);
        button_save_to_file.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "===onClick button_save_to_file===");

                Prepare_DB_for_saving();
                write_DB_to_SD();

            }
        });


    }


    void Prepare_DB_for_saving() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("netarp", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_ID);
            int ipIndex = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_LESSON);
            int macIndex = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_OURTEXT);
            int factoryIndex = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_DEUTSCHTEXT);
            int nameIndex = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_DEUTSCHSOUND);
            do {
                Log.d(LOG_TAG, "ID = " + cursor.getInt(idIndex) +
                        ", ipaddress = " + cursor.getString(ipIndex) +
                        ", macaddress = " + cursor.getString(macIndex) +
                        ", factory = " + cursor.getString(factoryIndex) +
                        ", name = " + cursor.getString(nameIndex));

                String dataForSavingPrepare = cursor.getInt(idIndex) + ";" +
                        cursor.getString(ipIndex) + ";" +
                        cursor.getString(macIndex) + ";" +
                        cursor.getString(factoryIndex) + ";" +
                        cursor.getString(nameIndex) + "\n";
                dataForSaving = dataForSaving + dataForSavingPrepare;

            } while (cursor.moveToNext());
            Log.d(LOG_TAG,"dataForSaving = " + dataForSaving);
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }


    void read_file_from_SD() {
        readFromFile="";
        Log.d(LOG_TAG, "read_DB_from_SD");
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();
        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);
        //  формируем объект File, который содержит путь к файлу
        Log.d(LOG_TAG, "Read from: " + String.valueOf(sdPath));
        File sdFile = new File(sdPath, FILENAME);
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                    Log.d(LOG_TAG, str);
                readFromFile=readFromFile+str+"&";
            }
            Log.d(LOG_TAG, "readFromFile = " + readFromFile);
                Toast.makeText(Settings.this, readFromFile, Toast.LENGTH_LONG).show();
            ParseFile(readFromFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void write_DB_to_SD() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(LOG_TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            return;
        }
        // получаем путь к SD
        File sdPath = new File(String.valueOf(Environment.getExternalStoragePublicDirectory("Download")));
        //  формируем объект File, который содержит путь к файлу
        Log.d(LOG_TAG, String.valueOf(sdPath));
        File sdFile = new File(sdPath, FILENAME);
        Log.d(LOG_TAG, "sdFile: "+sdFile.toString());
        try {
            sdFile.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(sdFile);
            Writer w = new BufferedWriter(new OutputStreamWriter(fos));

            try {
                w.write(dataForSaving);
                w.flush();
                fos.getFD().sync();
            } finally {
                w.close();
                Toast.makeText(Settings.this, "Saved!", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Toast.makeText(Settings.this, "Error", Toast.LENGTH_LONG).show();
        }
    }


    void readFile() {
// Read file from internal storage
        readFromFile="";
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    openFileInput(FILENAME)));
            String str = "";
            // читаем содержимое
            //Log.d(LOG_TAG, "readFile");
            while ((str = br.readLine()) != null) {
                // Log.d(LOG_TAG, str);
                readFromFile=readFromFile+str+"&";
                Log.d(LOG_TAG, "read File: "+str);
            }
            ParseFile(readFromFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void writeFile() {
        // write file to internal storage
        try {
            Log.d(LOG_TAG, "writeFile");
            // отрываем поток для записи
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    openFileOutput(FILENAME, MODE_PRIVATE)));
            // пишем данные
            bw.write(dataForSaving);
            // закрываем поток
            bw.close();
            Log.d(LOG_TAG, "Файл записан");
            Toast.makeText(Settings.this, "Saved!", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void ParseFile(String inputMassage){

        Log.d(LOG_TAG, "==dbHelper==");

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //    Toast.makeText(MainActivity.this, inputMassage, Toast.LENGTH_LONG).show();
        Log.d(LOG_TAG, "input Processing Massage1: "+ inputMassage);
        //   if (!inputMassage.matches("\\{\\{.*\\}\\}")) {}
        // if (!inputMassage.matches("\\[(\\[\"-?\\d{10}\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\",\"-?\\d\\d?\\d?\\.\\d\"\\],?)+\\]")) { }

        //  if (str.matches("[0-9A-Fa-f]{2}[-:][0-9A-Fa-f]{2}[-:][0-9A-Fa-f]{2}[-:][0-9A-Fa-f]{2}[-:][0-9A-Fa-f]{2}[-:][0-9A-Fa-f]{2}.*")) {}
        //inputMassage = inputMassage.trim().replaceAll(" +", " ");

        if (inputMassage.matches("(.*\\u0009.*\\u0009.*\\u0009.*\\u0009.*\\u0009.*&)")) {
            inputMassage = inputMassage.trim().replaceAll(" +", " ");
            Log.d(LOG_TAG,"begin");
            String line[] = inputMassage.split("&");  // зазделяем по записи "&"
            int count=0;

            database.delete(
                    com.example.memory.dbHelper.TABLE_NAME,
                    null,
                    null);

            while (count < line.length){
                String subline[] = line[count].split("\\u0009");

                   Log.d(LOG_TAG,"----------------------------------------------------");
                   Log.d(LOG_TAG, subline[0]+"   "+subline[1]+"   "+subline[2]+"   "+subline[3]+"   "+subline[4]+"   "+subline[5]);

                contentValues.put(com.example.memory.dbHelper.KEY_LESSON, subline[1]);
                contentValues.put(com.example.memory.dbHelper.KEY_OURTEXT, subline[2]);
                contentValues.put(com.example.memory.dbHelper.KEY_DEUTSCHTEXT, subline[3]);
                contentValues.put(com.example.memory.dbHelper.KEY_OURSOUND, subline[4]);
                contentValues.put(com.example.memory.dbHelper.KEY_DEUTSCHSOUND, subline[5]);

                database.insert(
                        "satz",
                        null,
                        contentValues);

                count++;
                //  Log.d(LOG_TAG, "Position = " + String.valueOf(cursor2.getPosition()));
            }
            Toast.makeText(Settings.this, "Database changed!", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(Settings.this, "File format is wrong!", Toast.LENGTH_LONG).show();
        }
        dbHelper.close();
    }


}