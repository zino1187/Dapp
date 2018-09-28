package com.example.zino.dapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthMining;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView txt;
    Thread thread;
    Handler handler;
    String TAG=this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt=(TextView)findViewById(R.id.txt);

        thread = new Thread(){
            public void run() {
                Web3j web3j= Web3jFactory.build(new HttpService("http://172.30.1.4:8545"));
                try {
                    EthAccounts acc=web3j.ethAccounts().send();
                    //System.out.println("계정수 :  "+acc.getAccounts().size());

                    EthMining ethMining=web3j.ethMining().send();
                    //System.out.println("현재 채굴여부 : "+ethMining.isMining());

                    Log.d("계정수는 ", ""+acc.getAccounts().size());
                    Log.d("채굴상태는 ", ""+ethMining.isMining());

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt("count", acc.getAccounts().size());
                    bundle.putBoolean("mining", ethMining.isMining());
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        handler = new Handler(){
            public void handleMessage(Message message) {
                Bundle bundle=message.getData();
                int count=bundle.getInt("count");
                boolean mining=bundle.getBoolean("mining");

                txt.append("계정수는 "+count+"\n");
                txt.append("채굴상태 "+mining+"\n");

            }
        };

        thread.start();
    }

}
