package com.example.yunoi.mp3player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Rating;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class Fragment1 extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static ArrayList<MainData> list = new ArrayList<>();
    private ListAdapter listAdapter;
    private View view;
    private boolean isPlaying = false;
    private int pos;    // 재생 멈춘 시점 저장
    private EditText edtSinger, edtTitle;
    private Spinner spinner;
    private RatingBar ratingBar;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private String janre;
    public static int position;
    public static String selectedFile;
    public static final String MP3_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Music/";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment1, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);


        dbHelper = new DBHelper(getActivity().getApplicationContext());

//        File[] mp3List = new File(MP3_PATH).listFiles();
//        for (File file : mp3List) {
//            String fileName = file.getName();
//            if (fileName.length() >= 5) {
//                String extendName = fileName.substring(fileName.length() - 3);
//                if (extendName.equals("mp3") && !list.contains(fileName)) {
//                    list.add(new MainData(fileName));
//                }
//            }
//        }   // end of for


        inputMusic();
        listSetting();
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        listAdapter = new ListAdapter(R.layout.list_view_holder, list);
        recyclerView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        return view;
    }

    private void listSetting() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor1;
        cursor1 = db.rawQuery("SELECT title FROM favMusicTBL;", null);
        while (cursor1.moveToNext()) {
            list.add(new MainData(cursor1.getString(0)));
        }
        cursor1.close();
        db.close();
    }

    private void inputMusic() {
        String[] data = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,

        };

//        MediaStore.Audio.Media.DATA // 데이터 경로
        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                data, MediaStore.Audio.Media.DATA +" like ? ", new String[]{MP3_PATH+"%"}, null);

        //db열기
        db = dbHelper.getWritableDatabase();

        while (cursor.moveToNext()) {
            //음악데이터 가져오기
            String id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

            title = title.replace("'", "''");
            Log.d("Fragment1", "id = " + id);
            Log.d("Fragment1", "artist = " + artist);
            Log.d("Fragment1", "title = " + title);
            Log.d("Fragment1", "albumArt = " + albumArt);

            String query = "INSERT INTO favMusicTBL VALUES("
                    + "'" + id + "',"
                    + "'" + artist + "',"
                    + "'" + title + "',"
                    + "'" + albumArt +"');";
            db.execSQL(query);

        } // while
        cursor.close();
        db.close();

    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        selectedFile = list.get(position);
//        this.position = position;
//        playMusic(MP3_PATH + selectedFile);
//    }


    private void toastDisplay(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }


}


