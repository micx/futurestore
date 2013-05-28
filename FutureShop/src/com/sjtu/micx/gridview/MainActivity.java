/**
* 	 this is the test func of GridView
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.gridview;

import com.sjtu.micx.data.DataCenter;
import com.sjtu.micx.futureshop.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {  
    private GridView gridView;   
     
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
          
        gridView = (GridView) findViewById(R.id.gridview);   
        GridItemAdapter adapter = new GridItemAdapter(DataCenter.titles, DataCenter.images,DataCenter.description,this);   
        gridView.setAdapter(adapter);   
   
        gridView.setOnItemClickListener(new OnItemClickListener()   
            {   
                @Override  
                public void onItemClick(AdapterView<?> parent, View v, int position, long id)   
                {   
                    Toast.makeText(MainActivity.this, "item" + (position+1), Toast.LENGTH_SHORT).show();   
                }  
            });   
    }  
}  