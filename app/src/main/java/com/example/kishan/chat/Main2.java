package com.example.kishan.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2 extends AppCompatActivity implements View.OnClickListener {
    String ipadress;
    Intent intent;
    Socket client;
    int port = 9009;
    String msg;
    EditText message,received;
    Button send;
    OutputStream outtoserver;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String you="Android";
     BufferedReader br;
    InputStream is;
    InputStreamReader isr;
    ListView mainListView ;
    ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        intent = getIntent();
        ipadress = intent.getStringExtra("IP_AD");
        message = (EditText) findViewById(R.id.message);
        //received = (EditText) findViewById(R.id.receive);
        send = (Button) findViewById(R.id.send);
        mainListView = (ListView) findViewById( R.id.history );
        String[] planets = new String[] { "Test"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        /*listAdapter.add( "Ceres" );
        listAdapter.add( "Pluto" );
        listAdapter.add( "Haumea" );
        listAdapter.add( "Makemake" );
        listAdapter.add( "Eris" );
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );*/

        send.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client=new Socket();
                    SocketAddress server = new InetSocketAddress(ipadress,port);
                    client.connect(server);
                    Main2.this.runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            if(client.isConnected()) Toast.makeText(Main2.this, "Connected", Toast.LENGTH_SHORT).show();

                        }
                    });
                    outtoserver=client.getOutputStream();
                    osw=new OutputStreamWriter(outtoserver);
                    bw=new BufferedWriter(osw);
                    bw.write("Hello from android\n");
                    bw.flush();
                    is = client.getInputStream();
                    isr=new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    try{
                        for(;;){

                            final String message =  br.readLine();
                            if(message.length()!=0){
                                Main2.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        //Do your UI operations like dialog opening or Toast here
                                        listAdapter.add(message);
                                        mainListView.setAdapter(listAdapter);
                                        mainListView.setSelection(listAdapter.getCount() - 1);
                                        //received.append("\r\n"+message+"\r\n");
                                        //Set in list activity

                                    }
                                });
                            }
                        }
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }


                }
                catch (UnknownHostException e2) {
                    e2.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    Log.d("Time out", "Time");
                }
            }
        }).start();
    }



        @Override
        public void onClick (View view){
            msg = message.getText().toString();
            try {
                outtoserver = client.getOutputStream();
                //write in output stream
                osw = new OutputStreamWriter(outtoserver);
                //now write in output buffer
                bw = new BufferedWriter(osw);
                bw.write(you + ":" + msg + "\n");
                bw.flush();
               // received.append("   "+msg);
                //now add to list activity
                listAdapter.add(msg);
                mainListView.setAdapter(listAdapter);
                mainListView.setSelection(listAdapter.getCount() - 1);
                message.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
