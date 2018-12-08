package com.denta.xo;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class XOBoard extends AppCompatActivity implements View.OnClickListener {


    private static final int ENABLE_BLUETOOTH =1 ;
    Button btnReset,btnPlay;
    GridView gridView;
    ArrayList<String>arrayList =new ArrayList<>();
    ArrayAdapter<String>adapter;
    boolean isPlaying = false;

    BluetoothAdapter bluetooth ;

    enum GAME_STATE{SELF,PHONE_X,PHONE_O,FRIEND}
    GAME_STATE game_state = GAME_STATE.SELF;
    char c = 'X';
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xo_board);

        gridView = findViewById(R.id.grid_board);
        btnReset = findViewById(R.id.btnReset);
        btnPlay = findViewById(R.id.btnPlay);
        reset();
        adapter = new ArrayAdapter<>(this,R.layout.item,R.id.tv_item,arrayList);
        gridView.setAdapter(adapter);
        btnReset.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = arrayList.get(i);
                if(s.equals("X")||s.equals("O")) return;

                switch (game_state)
                {
                    case SELF:
                        playWithYourSelf(i);
                        break;
                    case PHONE_X:
                    case PHONE_O:
                        playWithYourPhone(i);
                        break;
                    case FRIEND:
                        playWithYourFriend(i);
                        break;
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        gridView.setEnabled(isPlaying);
    }

    private void reset() {

        arrayList.clear();
        gridView.setBackgroundColor(Color.WHITE);
        for (int i=0;i<9;i++)
        {
            arrayList.add(""+(i+1));
        }
        isPlaying=false;
    }

    private boolean check_success(char c) {
        String s = c+"";
        if(arrayList.get(0).equals(arrayList.get(1))&&
                arrayList.get(0).equals(arrayList.get(2))&&
                arrayList.get(0).equals(s))
            return true;

        if(arrayList.get(3).equals(arrayList.get(4))&&
                arrayList.get(3).equals(arrayList.get(5))&&
                arrayList.get(3).equals(s))
            return true;

        if(arrayList.get(6).equals(arrayList.get(7))&&
                arrayList.get(6).equals(arrayList.get(8))&&
                arrayList.get(6).equals(s))
            return true;

        if(arrayList.get(0).equals(arrayList.get(3))&&
                arrayList.get(0).equals(arrayList.get(6))&&
                arrayList.get(0).equals(s))
            return true;

        if(arrayList.get(1).equals(arrayList.get(4))&&
                arrayList.get(1).equals(arrayList.get(7))&&
                arrayList.get(1).equals(s))
            return true;

        if(arrayList.get(2).equals(arrayList.get(5))&&
                arrayList.get(2).equals(arrayList.get(8))&&
                arrayList.get(2).equals(s))
            return true;

        if(arrayList.get(0).equals(arrayList.get(4))&&
                arrayList.get(0).equals(arrayList.get(8))&&
                arrayList.get(0).equals(s))
            return true;

        if(arrayList.get(2).equals(arrayList.get(4))&&
                arrayList.get(2).equals(arrayList.get(6))&&
                arrayList.get(2).equals(s))
            return true;

        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnPlay:
                if(!isPlaying) {
                    isPlaying = true;
                    gridView.setEnabled(isPlaying);
                    Toast.makeText(XOBoard.this, "isPlaying = " + isPlaying, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnReset:
                reset();
                adapter.notifyDataSetChanged();
                gridView.setEnabled(isPlaying);
                Toast.makeText(XOBoard.this, "isPlaying = " + isPlaying, Toast.LENGTH_LONG).show();

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isPlaying) {
            Toast.makeText(XOBoard.this,"press reset firstly ",Toast.LENGTH_LONG).show();
            return false;
        }

        switch (item.getItemId())
        {
            case R.id.action_yourSelf:
                game_state = GAME_STATE.SELF;
                break;
            case R.id.action_yourPhoneX:
                game_state = GAME_STATE.PHONE_X;
                break;
            case R.id.action_yourPhoneO:
                game_state = GAME_STATE.PHONE_O;
                break;
            case R.id.action_yourFriend:
                game_state = GAME_STATE.FRIEND;
                break;
        }
        Toast.makeText(XOBoard.this,"I Play With "+game_state.toString(),Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }

    private void playWithYourFriend(int i) {

    }

    private void playWithYourPhone(int i) {
        boolean b ;
        switch (game_state)
        {
            case PHONE_X:
                c='O';
                arrayList.set(i,c+"");
                adapter.notifyDataSetChanged();
                b = check_success(c);
                c='X';
                if(b)
                { print_winner(); return;}

                break;

            case PHONE_O:
                arrayList.set(i,c+"");
                adapter.notifyDataSetChanged();
                b = check_success(c);
                c='X';
                if(b)
                {print_winner();return;}
                break;
        }


    }

    private void playWithYourSelf(int i) {

        boolean b;
        if(c == 'X' )
        {
            arrayList.set(i,c+"");
            b = check_success(c);
            c='O';

        }
        else
        {
            arrayList.set(i,c+"");
            b = check_success(c);
            c = 'X';

        }
        adapter.notifyDataSetChanged();
        if(b)
        {
            print_winner();
        }
    }

    private void print_winner() {
        Toast.makeText(XOBoard.this,c+" is Looser ... ",Toast.LENGTH_LONG).show();
        gridView.setEnabled(false);
        gridView.setBackgroundColor(Color.GREEN);
    }


    public void turnBluetooth(View view) {

    }
}
