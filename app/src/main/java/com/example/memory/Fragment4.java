package com.example.memory;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.example.memory.MainActivity.BROADCAST_ACTION_FRAG_1;
import static com.example.memory.MainActivity.MESSAGEOUTPUTFR1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment4 extends Fragment implements MediaPlayer.OnCompletionListener {
    // Store instance variables
    String LOG_TAG="Fragment4";

    Context ctx;
    String satz;
    String translate ="1122.3344.5566";
    String row;
    int rowint;
    String filename_satz;
    String filename_translate;
    String fileNameDeutschText;
    String fileNameOurText;

    BroadcastReceiver br1;
//    String MESSAGE="messageforfragment_1";

    dbHelper dbHelper;
    private List<View> allEds;
    private MediaPlayer mediaPlayer;
    private String path;
    boolean playenable;
    int count;
    int counter;
    int counter2;

    // newInstance constructor for creating fragment with arguments
    public static Fragment4 newInstance(int page, String title) {
        Fragment4 fragment4 = new Fragment4();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment4.setArguments(args);
        return fragment4;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = Environment.getExternalStorageDirectory() + "/Podcasts/Memory/";
        Log.d(LOG_TAG, "===fileName=== = " + path);

        File rootPath = new File(Environment.getExternalStorageDirectory(), "Podcasts/Memory");

        if(!rootPath.exists()) {
            // Toast toast = Toast.makeText(getApplicationContext(),"Create Direktory ", Toast.LENGTH_SHORT);
            // toast.show();
            Log.d(LOG_TAG, "Create Direktory");
            rootPath.mkdirs();
            Log.d(LOG_TAG, "Create Direktory2");
        }


        br1 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String message1 = intent.getStringExtra(MESSAGEOUTPUTFR1);
                Log.d(LOG_TAG, "BR onReceive: message1 = " + message1);
                //          assert message1 != null;

            }
        };

        Context ctx = (Context)Fragment4.this.getActivity();

        // create filter for BroadcastReceiver
        //   String BROADCAST_ACTION="mass";
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_FRAG_1);
        // registration (On) BroadcastReceiver
        ctx.registerReceiver(br1, intFilt);
        //==========================================================================




    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_4, container, false);
        // TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
        //  tvLabel.setText(page + " -- " + title);

        allEds = new ArrayList<View>();
        Context ctx = (Context)Fragment4.this.getActivity();


        Button button_filling = (Button) view.findViewById(R.id.button_get_data);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear);
        button_filling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "button_filling");
                allEds.clear();
                linear.removeAllViews();
                Processing();
            }
        });

        return view;
    }

    void Processing(){

        Context ctx = (Context)Fragment4.this.getActivity();
        Log.d(LOG_TAG, "ctx== "+ ctx.toString());
        dbHelper = new dbHelper(ctx);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String allData[] = new String[5];
        count=0;
        Cursor cursor2 = database.query("satz",
                null,
                null,
                null,
                null,
                null,
                null);
        //    Log.d(LOG_TAG, "---cursor2---");

        cursor2.moveToFirst();
        do{
            int deutschtextIndex = cursor2.getColumnIndex(com.example.memory.dbHelper.KEY_DEUTSCHTEXT);
            int ourtextIndex = cursor2.getColumnIndex(com.example.memory.dbHelper.KEY_OURTEXT);
            int idIndex = cursor2.getColumnIndex(com.example.memory.dbHelper.KEY_ID);
            allData[0]= cursor2.getString(deutschtextIndex);
            allData[2]= cursor2.getString(ourtextIndex);
            allData[1]= cursor2.getString(idIndex);
            createfield(allData);
            count++;
        } while (cursor2.moveToNext());
        Log.d(LOG_TAG, "count = )" + count);
        dbHelper.close();
    }

    void createfield(String[] textfield){
        Context ctx = (Context)Fragment4.this.getActivity();
        LinearLayout linear = (LinearLayout) this.getActivity().findViewById(R.id.linear);
        //берем наш кастомный лейаут находим через него все наши кнопки и едит тексты, задаем нужные данные
        final View view = getLayoutInflater().inflate(R.layout.custom_edittext_layout, null);

        TextView text = (TextView) view.findViewById(R.id.editText);
        text.setText(textfield[0]);

        TextView text2 = (TextView) view.findViewById(R.id.editText2);
        text2.setText(textfield[2]);

        Button buttongo = (Button) view.findViewById(R.id.button_go);
        buttongo.setText(textfield[1]);

//------------------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------
        buttongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "onClick: ");
                if(!playenable) {
                    playenable = true;
                    satz = ((TextView)view.findViewWithTag("id1")).getText().toString();
                    translate = ((TextView)view.findViewWithTag("id2")).getText().toString();
                    row = ((TextView)view.findViewWithTag("id3")).getText().toString();
                    try{
                        rowint = Integer.parseInt(row);
                    }
                    catch (NumberFormatException ex){
                        ex.printStackTrace();
                    }
                    getFileName(rowint);
                    counter = 0;
                    counter2 = 0;

                    playStart(filename_satz+".mp3");
                }
                else{
                    playStop();
                    playenable = false;
                }
            }
        });

        buttongo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(LOG_TAG, "onLongClick: ");

                playStop();
                playenable = false;

                row = ((TextView)view.findViewWithTag("id3")).getText().toString();
                satz = ((TextView)view.findViewWithTag("id1")).getText().toString();
                translate = ((TextView)view.findViewWithTag("id2")).getText().toString();

                Intent intent = new Intent();
                intent.putExtra("row", row);
                intent.putExtra("satz", satz);
                intent.putExtra("translate", translate);
                intent.setClass(ctx, Control_panel.class);
                startActivity(intent);

                return false;
            }
        });

        //добавляем все что создаем в массив
        allEds.add(view);
        //добавляем елементы в linearlayout
        linear.addView(view);
    }
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onStop");

    }

    void getFileName(int rowint){
        Context ctx = (Context)Fragment4.this.getActivity();
        Log.d(LOG_TAG, "ctx== "+ ctx.toString());
        dbHelper = new dbHelper(ctx);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String rows = Integer.toString(rowint);
        Cursor cursor = database.query(
                "satz",
                null,
                "_id = ?",
                new String[] {rows},
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            int fileNameDeutschTextIndex_int = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_DEUTSCHTEXT);
            int fileNameOurTextIndex_int = cursor.getColumnIndex(com.example.memory.dbHelper.KEY_OURTEXT);
            fileNameDeutschText = cursor.getString(fileNameDeutschTextIndex_int);
            fileNameOurText = cursor.getString(fileNameOurTextIndex_int);
            filename_satz = fileNameDeutschText.trim().replaceAll("\\p{Punct}","_");
            filename_translate = fileNameOurText.trim().replaceAll("\\p{Punct}","_");
            //Log.d(LOG_TAG, "===fileNameDeutschText=== = " + fileNameDeutschText);
        }
        dbHelper.close();
    }

    private void playStop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void playStart(String fileName) {
        Log.d(LOG_TAG, "===fileName=== = " + path + fileName);
        try {
            releasePlayer();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(path+fileName);
            mediaPlayer.prepare();
            Log.d(LOG_TAG,"====Player start====");
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer MP) {
        Log.d(LOG_TAG,"====Player stopped====");

        //Counter for play translate
        if(counter < 4) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playenable) playStart(filename_satz + ".mp3");
                }
            }, 4000);
            counter++;

            //Counter2 for spring to next row
            if(counter2 >= 3){
                rowint++;
                counter2 = 0;
                getFileName(rowint);
            }
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (playenable) playStart(filename_translate + ".mp3");
                }
            }, 4000);
            counter = 0;
            counter2 ++;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}