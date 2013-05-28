/*---------------------------------------------------------------------------
    Copyright (c) 2012 Cilico Semiconductor, Inc.  All rights reserved.
---------------------------------------------------------------------------*/
package com.sjtu.micx.UHFReader;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Message;
import android.util.Log;

import com.cilico.UHFlib.manager;


public class Scanner  {

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
	
	public final static int 	DELAY_CLICK = 500;
	public static boolean 	isConnected = false;
	public boolean 			isContinusScan = false;
	
	public static String[]	strFoundEPCs = new String[200];
	public static manager 	op = new manager();
	public static byte[] 		accessEPC = new byte[12];
	public static byte[][]	aryFoundEPC = new byte[200][12];
	public static int 		gScanCnt = 0;
    public static String[]	gEPClist = new String[200];
    public static int 		gresult = -1; 
    public static String EPCID="";
    public static String ConnAck="";
    public static int connresult=0;
    
    //private Timer timer = new Timer();
    private Timer timer = null;
    private TimerTask  ScanTask= null;
	
	protected void onStart() {
		// TODO Auto-generated method stub
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


    public static void mDelay(int dly)
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
    public void keyConnect(){
//    	new threadConnect().execute();
    }
	


	public static void keyScan() {		
			int i;
			for(i=0;i<100;i++)
				gEPClist[i] = "";
			gScanCnt = 0;
			gresult = 0;
//			new threadScan().execute();	
	}
	
	


		public void handleMessage(int what) {
			String str = new String("");

			if(what == 0){
				
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
//				showEPCList(gEPClist);
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

//			Status.setText(str);
			Log.i(TAG, str);

			
			}
		};
	class  timerTask extends TimerTask{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Log.i(TAG, "doScanNext!");
			//doScanNext();
			int what=10000;
			if(gresult == 2)
			{

				gresult = 0;
				doScanNext();
				Message msg = new Message();
				what =0;
				handleMessage(what);				
				
			}
			else if(gresult == 3)
			{
				gresult = 0;
				doScanNext();	
				Message msg = new Message();
				what =0;
				handleMessage(what);					
			}

		}
		
	};
	public void keyContinusScan() {
		
			int i;
			if(isContinusScan == true)
			{
				ScanTask.cancel();
				timer.cancel();
				isContinusScan = false;
//				btnContinusScan.setText("Continus Scan");
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
//				btnContinusScan.setText("Scaning!!!press to stop");
			}
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


	
	/*************************************************************************
	 *						The thread for openning/closing.
	 ************************************************************************/
	public static void openning(){
		int result = 0;
		String str = new String("");
		isConnected = true;
		//if ((gintAntHandle = op.MS_Connect(1, 1, 5)) >= 0) //1:UART, 2:I2C
		if ((gintAntHandle = op.MS_Connect(2, 1, 5)) >= 0) //1:UART, 2:I2C
		{
			op.Handle = gintAntHandle;

			result = op.MS_Get_Version();
		}
		
		
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

//					version.setText(str);
//					Status.setText("Open port/Get Version Success.");
//					btnScan.setEnabled(true);
//					btnContinusScan.setEnabled(true);
				}
				else
				{
					str = "Get version is failed : " + result;
					Log.i(TAG, str);
//					Status.setText(str);
				}
				gintAntHandle = gintAntHandle;

			}
			else
			{
				str = "Open port is failed : " + gintAntHandle;
//				Status.setText(str);
//				btnOpen.setText("Open");
				isConnected = false;
			}

			//mDelay(DELAY_CLICK);
//			btnOpen.setEnabled(true);
		}
		else
		{
			//mDelay(DELAY_CLICK);
//			btnOpen.setText("Open");
//			version.setText("");
			//selectEPC.setText("");
			str = "";
			gintAntHandle = -1;

//			btnOpen.setEnabled(true);
//			btnScan.setEnabled(false);
//			btnContinusScan.setEnabled(false);
		
//			Status.setText("Close Port.");
		}
		ConnAck = str;
		connresult= result;
	}
		
	public static void closing(){
		isConnected = false;
		op.MS_Disconnect(gintAntHandle);
	}

	/*************************************************************************
	 *						The thread for inventory.
	 ************************************************************************/
	public static void inventory(){
		
		keyScan();
		
		
		int result;
		byte[] mask = new byte[8];
		isSelectEPC = false;
		isFoundEPC = false;

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
				EPCID = str;
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
//			showEPCList(gEPClist);
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

	}


}

