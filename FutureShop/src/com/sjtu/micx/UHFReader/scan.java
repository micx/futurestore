/*---------------------------------------------------------------------------
    Copyright (c) 2012 Cilico Semiconductor, Inc.  All rights reserved.
---------------------------------------------------------------------------*/
package com.sjtu.micx.UHFReader;

import java.util.Timer;
import java.util.TimerTask;

import com.cilico.UHFlib.manager;
import com.sjtu.micx.futureshop.R;



import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.AdapterView;


public class scan extends Activity {

	/** Called when the activity is first created. */
	private static final String TAG = "Cilico-scan";
	public static boolean	isFoundEPC = false;
	public static boolean	isSelectEPC = false;
	public static int		gintAntHandle = -1;
	public static int		gQvalue = 0;
	public static int		gBLF = manager.C1G2_BLF_et_.C1G2_BLF_80kbps;
	public static int		gModulation = manager.C1G2_Mod_Type_et_.C1G2_MOD_FM0;
	public static int		gFrequencyIndex = 0;
	public static int		gChannelType = manager.Channel_Type_e_.CH_TYPE_HOPPING;
	
	private final int 	DELAY_CLICK = 500;
	private boolean 	isConnected = false;
	private boolean 	isContinusScan = false;
	private Button		btnScan;
	private Button 		btnContinusScan;
	private Button  	btnOpen;
	private TextView    version;
	private TextView    selectEPC;
	private TextView	Status;
	private GridView	gridView_EPC;
	private String[]	strFoundEPCs = new String[200];
	private manager 	op = new manager();
	private byte[] 		accessEPC = new byte[12];
	private byte[][]	aryFoundEPC = new byte[200][12];
	private ListView	listView_EPC;
    private int 		gScanCnt = 0;
    private String[]	gEPClist = new String[200];
    private int 		gresult = -1; 
    
    //private Timer timer = new Timer();
    private Timer timer = null;
    private TimerTask  ScanTask= null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

        setContentView(R.layout.scan);

		/* get the resource */
		version = (TextView) findViewById(R.id.textView_Version);
		//version.setTextSize(20);

		//selectEPC = (TextView) findViewById(R.id.textView_EPCaccess);
		//selectEPC.setTextSize(20);

		Status = (TextView) findViewById(R.id.textView_Status);

		btnOpen = (Button)findViewById(R.id.btn_connect);
		btnOpen.setOnClickListener(keyConnect);

		btnScan = (Button)findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(keyScan);
		btnContinusScan = (Button)findViewById(R.id.btn_ContinusScan);
		btnContinusScan.setOnClickListener(keyContinusScan);
		btnOpen.setEnabled(true);
		btnScan.setEnabled(false);
		btnContinusScan.setEnabled(false);
		initEPCList();

    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "On Start .....");

		isFoundEPC = false;
		isSelectEPC = false;
		gintAntHandle = -1;
		gQvalue = 0;
		gBLF = manager.C1G2_BLF_et_.C1G2_BLF_80kbps;
		gModulation = manager.C1G2_Mod_Type_et_.C1G2_MOD_FM0;
		gFrequencyIndex = 0;
		gChannelType = manager.Channel_Type_e_.CH_TYPE_HOPPING;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "On Destroy .....");

		if (this.isConnected)
		{
			Log.i(TAG, "previous is connected and disconnect then exiting.");
			op.MS_Disconnect(gintAntHandle);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "On Pause .....");

		accessEPC = accessEPC;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "On Resume .....");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i(TAG, "On Restart .....");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "On Stop .....");
	}

    public void mDelay(int dly)
	{
		try
		{
			Thread.sleep(dly);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/*private SimpleAdapter createSimpleAdapter() {
			List<Map<String, String>> data = this.createData();
			return new SimpleAdapter(this, data, R.layout.list_item_b,
					new String[] {
							"txt1", "txt2"
					}, new int[] {
							R.id.txt1, R.id.txt2
					});
		}

	private List<Map<String, String>> createData() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		data.add(this.createMap("1", "Monday"));
		data.add(this.createMap("2", "Tuesday"));
		data.add(this.createMap("3", "Wednesday"));
		data.add(this.createMap("4", "Thursday"));
		data.add(this.createMap("5", "Friday"));
		return data;
	}

	private Map<String, String> createMap(String a, String b) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("txt1", a);
		map.put("txt2", b);
		return map;
	}
	//}*/

	private void showEPCList(String[] strEPC) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(scan.this, android.R.layout.simple_list_item_1, strEPC);
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(scan.this, R.layout.list_items, strEPC);
		gridView_EPC.setAdapter(adapter);
	}

	private void initEPCList() {

		/* List View */
		/*//TextView viewEPC = (TextView) findViewById(R.id.textView_EPC);
		//viewEPC.setTextSize(20);
		listView_EPC = (ListView) findViewById(R.id.listView_EPCs);
		//listView.setListAdapter(this.createSimpleAdapter());
		//listView.setAdapter(this.createSimpleAdapter());
		//listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings));
		final String[] mStrings = new String[]{"aaa","abc","bbb","bcd","123"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.list_items, mStrings);
		listView_EPC.setAdapter(adapter1);
		//listView.setTextFilterEnabled(true);*/

		/* Grid View */
		gridView_EPC = (GridView) findViewById(R.id.gridView_EPCs);
		final String[] list = new String[]{""};
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_items, list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
		//gridView_EPC.setNumColumns(1);
		//gridView_EPC.setStretchMode(1);
		//gridView_EPC.setHorizontalSpacing(1);
		//gridView_EPC.setVerticalSpacing(1);
		//gridView_EPC.setGravity(Gravity.CENTER);
		gridView_EPC.setAdapter(adapter);
		//gridView_EPC.setOnItemSelectedListener(gridViewListener_SelectEPC);

		gridView_EPC.setOnItemClickListener(new GridView.OnItemClickListener(){

			//@Override
			@Override
			public void onItemClick(AdapterView adapterView,View view,int position,long id) {
				String str = new String("Selected EPC : ");
				//str += Integer.toString(position);
				//str += " ";
				str += strFoundEPCs[position];
				str += " to access!";
				Log.i(TAG, str);
				//selectEPC.setText(strFoundEPCs[position]);
				for (int i=0; i<12; i++)
				{
					accessEPC[i] = aryFoundEPC[position][i];
				}

				isSelectEPC = true;

				Context context = getApplicationContext();
				CharSequence text = str;//"Hello toast!";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
	}

    private Button.OnClickListener keyConnect = new Button.OnClickListener() {
		@Override
		public void onClick(View v){
			new threadConnect().execute();
		}
	};

	private Button.OnClickListener keyScan = new Button.OnClickListener() {
		@Override
		public void onClick(View v){
			int i;
			for(i=0;i<100;i++)
				gEPClist[i] = "";
			gScanCnt = 0;
			gresult = 0;
			new threadScan().execute();
		}
	};
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			String str = new String("");

			if(msg.what == 0){
				
			if (gresult == 0)
			{
				String[]	EPClist = new String[op.Tag_Count];
				byte[]		EPC = new byte[12];
				boolean EPCfound = false;
				int cnt = 0;
				for (int j=0; j<op.Tag_Count; j++)
				{
					str = "";
					EPClist[j] = "";
					for(int i=0; i<12; i++)
					{
						EPC[i] = op.Resp_Buf[(6*(j+1))+(12*j)+i];
						str += toHex(EPC[i]);
						aryFoundEPC[j][i] = EPC[i];
					}
					for(int k=0;k<j+gScanCnt;k++)
					{
						if(gEPClist[k].equals(str))
						{
							EPCfound = true;
							break;
						}						
					}
					if(!EPCfound)
					{
						//EPClist[cnt] = str;
						gEPClist[cnt+gScanCnt] = str;
						strFoundEPCs[cnt+gScanCnt] = str;
						cnt++;
					}
					EPCfound = false;
					str = "";
				}
				//UID.setText(str);
				showEPCList(gEPClist);
				//showEPCList(EPClist);
				gScanCnt = gScanCnt + cnt;
				str = "Tag is found = " + gScanCnt;
				isFoundEPC = true;

				if (op.Tag_Count == 1)
				{
					//selectEPC.setText(strFoundEPCs[0]);
					for (int i=0; i<12; i++)
					{
						accessEPC[i] = aryFoundEPC[0][i];
					}

					isSelectEPC = true;
				}
				gresult = 2;
			}
			else
			{
				/*String[]	EPClist = new String[1];
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(scan.this, android.R.layout.simple_list_item_1, EPClist);
				EPClist[0] = "";
				gridView_EPC.setAdapter(adapter);*/
				
				//clear_grid();

				str = "Search tag is failed : " + gresult;
				isFoundEPC = false;
				gresult = 3;
			}

			Status.setText(str);
			Log.i(TAG, str);

			btnOpen.setEnabled(true);
			btnScan.setEnabled(true);
			btnScan.setClickable(true);
			
			}
		};
	};
	class  timerTask extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "doScanNext!");
			//doScanNext();
			if(gresult == 2)
			{

				gresult = 0;
				doScanNext();
				Message msg = new Message();
				msg.what =0;
				handler.sendMessage(msg);				
				
			}
			else if(gresult == 3)
			{
				gresult = 0;
				doScanNext();	
				Message msg = new Message();
				msg.what =0;
				handler.sendMessage(msg);				
			}

		}
		
	};
	private Button.OnClickListener keyContinusScan = new Button.OnClickListener() {
		@Override
		public void onClick(View v){
			int i;
			if(isContinusScan == true)
			{
				ScanTask.cancel();
				timer.cancel();
				isContinusScan = false;
				btnContinusScan.setText("Continus Scan");
			}
			else 
			{
				timer = new Timer();
				ScanTask = new timerTask();
				for(i=0;i<100;i++)
					gEPClist[i] = "";
				gScanCnt = 0;
				gresult = 2;
				timer.schedule(ScanTask,100, 200);
				isContinusScan = true;
				btnContinusScan.setText("Scaning!!!press to stop");
			}
		}
	};
	public void clear_grid()
	{
		String[]	EPClist = new String[1];
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(scan.this, android.R.layout.simple_list_item_1, EPClist);
		EPClist[0] = "";
		gridView_EPC.setAdapter(adapter);
	}

    protected int doScanNext() {
		// TODO Auto-generated method stub
    	byte[] mask = new byte[8];
		isSelectEPC = false;
		isFoundEPC = false;
		//Status.setText("scanning...");
		op.Tag_Type = manager.Tag_Type_et_.TAG_TYPE_C1G2;
		op.Mask_Len = 0;
		op.Mask = mask;
		op.Resp_Len = 0;
		op.Tag_Count = 0;
		op.InventoryTimes = 1;
		op.Handle = gintAntHandle;

		if( op.Tag_Type == manager.Tag_Type_et_.TAG_TYPE_C1G2 )
		{
			op.Mem_Bank = manager.C1G2_Mem_Type_et_.C1G2_MEM_TYPE_EPC;
			op.BLF = gBLF;//manager.C1G2_BLF_et_.C1G2_BLF_80kbps;
			op.Modulation = gModulation;//manager.C1G2_Mod_Type_et_.C1G2_MOD_FM0;
			op.Search_Mode = manager.C1G2_Inventory_Mode_et_.C1G2_Inv_Normal;
			op.Pointer = 0;
			op.Ant_Mask = 1;
			op.Q = gQvalue;
			op.PilotTone = 1;
		}
		else if( op.Tag_Type == manager.Tag_Type_et_.TAG_TYPE_ISO15693 )
		{
			op.HF693_Quiet_Mode = manager.HF693_Quiet_Mode_et_.HF693_Not_Quiet;
		}

		gresult = op.MS_Find_Tag();
		//mDelay(DELAY_CLICK);

		return 0;		
	}

	public static String toHex(byte b)
	{
    	return (""+"0123456789ABCDEF".charAt(0xf&b>>4)+"0123456789ABCDEF".charAt(b&0xf));
    }


	/*
	 * Create thread for each key. (mantis#242805)
	 */
	/*************************************************************************
	 *						The thread for openning/closing.
	 ************************************************************************/
	class threadConnect extends AsyncTask<Integer,String,Integer> {
		int result;
		String str = new String("");

		@Override
		protected Integer doInBackground(Integer... count) {

			if (isConnected)
			{
				isConnected = false;

				op.MS_Disconnect(gintAntHandle);
			}
			else
			{
				isConnected = true;
				//if ((gintAntHandle = op.MS_Connect(1, 1, 5)) >= 0) //1:UART, 2:I2C
				if ((gintAntHandle = op.MS_Connect(2, 1, 5)) >= 0) //1:UART, 2:I2C
				{
					op.Handle = gintAntHandle;

					result = op.MS_Get_Version();
				}
			}
			//mDelay(DELAY_CLICK);

			return 0;
		}

		@Override
		protected void onPreExecute() {

			if (isConnected)
			{
			}
			else
			{
				Status.setText("Connecting...");
				btnOpen.setText("Close");
			}
		}

		@Override
		protected void onProgressUpdate(String... progress) {
		}

		@Override
		protected void onPostExecute(Integer counts) {

			if (isConnected)
			{
				if (gintAntHandle >= 0)
				{
					if (result == 0)
					{
						Log.i(TAG, "Get version is successful!");
						str = "Version : " + op.Major + "." + op.Minor + " - ";
						str += op.Month + "/" + op.Day + "/" + op.Year + " " +
							   op.Hour + ":" + op.Minute + ":" + op.Second ;

						version.setText(str);
						Status.setText("Open port/Get Version Success.");
						btnScan.setEnabled(true);
						btnContinusScan.setEnabled(true);
					}
					else
					{
						str = "Get version is failed : " + result;
						Log.i(TAG, str);
						Status.setText(str);
					}
					gintAntHandle = gintAntHandle;

				}
				else
				{
					str = "Open port is failed : " + gintAntHandle;
					Status.setText(str);
					btnOpen.setText("Open");
					isConnected = false;
				}

				//mDelay(DELAY_CLICK);
				btnOpen.setEnabled(true);
			}
			else
			{
				//mDelay(DELAY_CLICK);
				btnOpen.setText("Open");
				version.setText("");
				//selectEPC.setText("");
				str = "";
				gintAntHandle = -1;

				btnOpen.setEnabled(true);
				btnScan.setEnabled(false);
				btnContinusScan.setEnabled(false);
				clear_grid();
				Status.setText("Close Port.");
			}

		}
	}

	/*************************************************************************
	 *						The thread for inventory.
	 ************************************************************************/
	class threadScan extends AsyncTask<Integer,String,Integer> {
		int result;

		@Override
		protected Integer doInBackground(Integer... count) {
			byte[] mask = new byte[8];

			op.Tag_Type = manager.Tag_Type_et_.TAG_TYPE_C1G2;
			op.Mask_Len = 0;
			op.Mask = mask;
			op.Resp_Len = 0;
			op.Tag_Count = 0;
			op.InventoryTimes = 1;
			op.Handle = gintAntHandle;

			if( op.Tag_Type == manager.Tag_Type_et_.TAG_TYPE_C1G2 )
			{
				op.Mem_Bank = manager.C1G2_Mem_Type_et_.C1G2_MEM_TYPE_EPC;
				op.BLF = gBLF;//manager.C1G2_BLF_et_.C1G2_BLF_80kbps;
				op.Modulation = gModulation;//manager.C1G2_Mod_Type_et_.C1G2_MOD_FM0;
				op.Search_Mode = manager.C1G2_Inventory_Mode_et_.C1G2_Inv_Normal;
				op.Pointer = 0;
				op.Ant_Mask = 1;
				op.Q = gQvalue;
				op.PilotTone = 1;
			}
			else if( op.Tag_Type == manager.Tag_Type_et_.TAG_TYPE_ISO15693 )
			{
				op.HF693_Quiet_Mode = manager.HF693_Quiet_Mode_et_.HF693_Not_Quiet;
			}

			result = op.MS_Find_Tag();
			gresult = result;
			mDelay(DELAY_CLICK);

			return 0;
		}

		@Override
		protected void onPreExecute() {

			clear_grid();
			//selectEPC.setText("");
			btnOpen.setEnabled(false);
			btnScan.setEnabled(false);
			btnScan.setClickable(false);
			Status.setText("scanning...");
			isSelectEPC = false;
			isFoundEPC = false;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
		}

		@Override
		protected void onPostExecute(Integer counts) {

			String str = new String("");

			if (result == 0)
			{
				String[]	EPClist = new String[op.Tag_Count];
				byte[]		EPC = new byte[12];
				boolean EPCfound = false;
				int cnt = 0;
				for (int j=0; j<op.Tag_Count; j++)
				{
					str = "";
					EPClist[j] = "";
					for(int i=0; i<12; i++)
					{
						EPC[i] = op.Resp_Buf[(6*(j+1))+(12*j)+i];
						str += toHex(EPC[i]);
						aryFoundEPC[j][i] = EPC[i];
					}
					for(int k=0;k<j+gScanCnt;k++)
					{
						if(gEPClist[k].equals(str))
						{
							EPCfound = true;
							break;
						}						
					}
					if(!EPCfound)
					{
						//EPClist[cnt] = str;
						gEPClist[cnt+gScanCnt] = str;
						strFoundEPCs[cnt+gScanCnt] = str;
						cnt++;
					}
					EPCfound = false;
					str = "";
				}
				//UID.setText(str);
				showEPCList(gEPClist);
				//showEPCList(EPClist);
				gScanCnt = gScanCnt + cnt;
				str = "Tag is found = " + gScanCnt;
				isFoundEPC = true;

				if (op.Tag_Count == 1)
				{
					//selectEPC.setText(strFoundEPCs[0]);
					for (int i=0; i<12; i++)
					{
						accessEPC[i] = aryFoundEPC[0][i];
					}

					isSelectEPC = true;
				}

			}
			else
			{
				/*String[]	EPClist = new String[1];
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(scan.this, android.R.layout.simple_list_item_1, EPClist);
				EPClist[0] = "";
				gridView_EPC.setAdapter(adapter);*/
				
				//clear_grid();

				str = "Search tag is failed : " + result;
				isFoundEPC = false;
			}

			Status.setText(str);
			Log.i(TAG, str);

			btnOpen.setEnabled(true);
			btnScan.setEnabled(true);
			btnScan.setClickable(true);
		}
	}

	/*************************************************************************
	 *						gridViewListener_SelectEPC
	 ************************************************************************/
	/*gridView.setOnItemClickListener(new GridView.OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView adapterView,View view,int position,long id) {
		Toast.makeText(MainActivity.this, "您選擇的是"+list[position], Toast.LENGTH_SHORT).show();
		}
	});*/

	/*GridView.OnItemSelectedListener gridViewListener_SelectEPC = new GridView.OnItemSelectedListener(){
		public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
			String str = new String("Selected EPC : ");
			//str += adapterView.getSelectedItem().toString();
			Log.i(TAG, str);
		}

		public void onNothingSelected(AdapterView arg0) {
		}
	};*/

}

