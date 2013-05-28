/*---------------------------------------------------------------------------
    Copyright (c) 2012 Cilico Semiconductor, Inc.  All rights reserved.
---------------------------------------------------------------------------*/
package com.cilico.UHFlib;

import android.util.Log;

public class manager {

    public manager() {
    	Log.i("Cilico-manager","Starting...");
    	//this.C1G2 = new _C1G2();
    	//this.ISO15693 = new _ISO15693();
    }

    public native int MS_Connect(int type, int port, int baud_rate_id);
    public native int MS_Disconnect(int handle);
    public native int MS_Get_Version();
    public native int MS_Find_Tag();
    public native int MS_Read_Tag_Block();
    public native int MS_Write_Tag_Block();
    public native int MS_Set_Channel_Type(int handle, int chType, int chID);
    public native int MS_Set_C1G2_Tag_Speed(int handle, int BLF);
    public native int MS_Set_PA_Output(int handle, int pLevel);
    public native int MS_FW_Upgrage();

	/* this is used to load the 'cilicoUHFlib' library on application
	 * startup. The library has already been unpacked into
	 * /data/data/com.cilico.UHFapp/lib/libcilicoUHFlib.so at
	 * installation time by the package manager.
	 */
    static {
        try {
        	System.loadLibrary("cilicoUHFlib");
        }
        catch (UnsatisfiedLinkError e) {
        	Log.e("Cilico-manager", "cilicoUHFlib library not found!");
        }
    }

	/* Hopping Mechiaism */
	public String[] tableHopping = new String[]
	{
		"Hopping Channel",
		"Fixed Channel"
	};

	public static class Channel_Type_e_
	{
		public final static int	CH_TYPE_HOPPING = 0;
		public final static int	CH_TYPE_FIXED = 1;
	}

	/* China frequency band */
	public String[] tableFreq = new String[]
	{
		"920.875 MHz",
		"921.125 MHz",
		"921.375 MHz",
		"921.625 MHz",
		"921.875 MHz",
		"922.125 MHz",
		"922.375 MHz",
		"922.625 MHz",
		"922.875 MHz",
		"923.125 MHz",
		"923.375 MHz",
		"923.625 MHz",
		"923.875 MHz",
		"924.125 MHz"
	};

	/* BLF FM0 */
	public String[] tableBLFFM0 = new String[]
	{
		"640K bps",
		"320K bps",
		"200K bps",
		"160K bps",
		"80K bps"
	};

	/* BLF MILLER2 */
	public String[] tableBLFMiller2 = new String[]
	{
		"320K bps",
		"160K bps",
		"100K bps",
		"80K bps",
		"40K bps"
	};

	/* BLF MILLER4 */
	public String[] tableBLFMiller4 = new String[]
	{
		"160K bps",
		"80K bps",
		"50K bps",
		"40K bps",
		"20K bps"
	};

	/* BLF MILLER8 */
	public String[] tableBLFMiller8 = new String[]
	{
		"80K bps",
		"40K bps",
		"25K bps",
		"20K bps",
		"10K bps"
	};

	/* Modulation */
	public String[] tableModulation = new String[]
	{
		"FM0",
		"Miller 2",
		"Miller 4",
		"Miller 8"
	};

	/* Power Level */
	public String[] tablePowerLevel = new String[]
	{
		"Level 1",
		"Level 2",
		"Level 3",
		"Level 4",
		"Level 5",
		"Level 6",
		"Level 7",
		"Level 8"
	};

    /*************************************************************************
     *       Connect/Disconnect
     *************************************************************************/
    /* baud rate
     * 0:9600
     * 1:19200
     * 2:38400
     * 3:57600
     * 4:115200
     * 5:230400
     */
    public enum BaudRateID
    {
    	B9600,
    	B19200,
    	B38400,
    	B57600,
    	B115200,
    	B230400
    }

    /*************************************************************************
     *       Get Version
     *************************************************************************/
    public int Handle;                          //In, Handle ID
    public int	Major;                          //Out, Major Version Number
    public int	Minor;                          //Out, Minor Version Number
    public byte	Month;                          //Out, Month
    public byte	Day;                            //Out, Day
    public int	Year;                           //Out, Year
    public byte	Hour;                           //Out, Hour
    public byte	Minute;                         //Out, Minute
    public byte	Second;                         //Out, Second
    public byte[] Serial_Number = new byte[8];  //Out, Serial Number

    /*************************************************************************
     *       Find Tag Class
     *************************************************************************/
    public static class Tag_Type_et_ {
    	public final static int TAG_TYPE_ALL = 0;                   /**< All supported tags */
    	public final static int TAG_TYPE_ISO15693 = 1;              /**< ISO15693 type */
    	public final static int TAG_TYPE_C1G2 = 2;                  /**< C1G2 type     */
    	public final static int TAG_TYPE_ISO15693_CILICO = 3;        /**< ISO15693 Cilico proprietary */
    	public final static int TAG_TYPE_C1G2_CILICO = 4;            /**< C1G2 Cilico proprietary */
    	public final static int TAG_TYPE_ISO14443A_UltraLight = 5;  /**< ISO14443 UltraLight */
    	public final static int TAG_TYPE_ISO14443A_Topaz = 6;       /**< ISO14443 Topaz */
    	public final static int TAG_TYPE_MAX = 7;                    /**< Unknown media type */
    }

	public static class C1G2_Mem_Type_et_
	{
		public final static int C1G2_MEM_TYPE_RESERVED = 0;     	/**< Reserved */
		public final static int C1G2_MEM_TYPE_EPC = 1;				/**< EPC type  */
		public final static int C1G2_MEM_TYPE_TID = 2;				/**< TID type  */
		public final static int C1G2_MEM_TYPE_USER = 3; 			/**< USER type */
		public final static int C1G2_MEM_TYPE_MAX = 4;		      	/**< Unknown media type */
	}

	/**
	 *	@brief RFID Tag Response Speed
	 */
	public static class C1G2_BLF_et_
	{
		public final static int C1G2_BLF_640kbps = 0;
		public final static int C1G2_BLF_320kbps = 1;
		public final static int C1G2_BLF_200kbps = 2;
		public final static int C1G2_BLF_160kbps = 3;
		public final static int C1G2_BLF_80kbps = 4;
		public final static int C1G2_BLF_Max = 5;
	}

	/**
	 *	@brief RFID Modulation type
	 */
	public static class C1G2_Mod_Type_et_
	{
		public final static int C1G2_MOD_FM0 = 0;
		public final static int C1G2_MOD_MILLER2 = 1;
		public final static int C1G2_MOD_MILLER4 = 2;
		public final static int C1G2_MOD_MILLER8 = 3;
		public final static int C1G2_MOD_MAX = 4;
	}

	/**
	 *	@brief RFID C1G2 Inventory Mode
	 */
	public static class C1G2_Inventory_Mode_et_
	{
		public final static int C1G2_Inv_Normal = 0;
		public final static int C1G2_Inv_Turbo = 1;
		public final static int C1G2_Inv_Max = 2;
	}

    public static class HF693_Quiet_Mode_et_
    {
    	public final static int HF693_Not_Quiet = 0;
    	public final static int HF693_Quiet = 1;
    	public final static int HF693_Max = 2;
    }

 	public int          Tag_Type;       // In, RFID Tag Type

    /* C1G2 */
	public int		Mem_Bank; 	  // In, Bank Type of Mask
	public int		BLF; 		   // In, Tag response speed
	public int		Modulation;	  // In, Tag response modulation Type
	public int		Search_Mode;	// In, Normal mode available only now.
	public int		Pointer;		 // In, Memory bit address
	public int		Ant_Mask;		 // In, Specify Antenna
    public int      AccessPasswd;   // In, AccessPasswd
    public int      Read_Bank_Type; // In,  Type for read
    public int      Write_Bank_Type; // In,  Type for read
	public int		Q; 			 // In, Specify Q value
	public int		PilotTone; 	 // In, pilot tone on/off
	public int		InventoryTimes;

    /* ISO15693 */
 	public class _ISO15693
 	{
 	    public int     Quiet;          // In, Set tag into Quiet state or not
 	}
 	public _ISO15693          ISO15693;

 	public int     HF693_Quiet_Mode;          // In, Set tag into Quiet state or not

 	/* Common */
    public int          Mask_Len;       // In, Mask Length
    public byte[]       Mask;           // In, Mask Data
    //public byte       Resp_Buf[];       // Out, Pointer of rx buffer
    public byte[] Resp_Buf = new byte[1024];  //Out, Serial Number
    public int          Resp_Len;       // Out, Received Data Length
    public int          Tag_Count;      // Out, Tag Count

     /*************************************************************************
     *       Read Tag Class
     *************************************************************************/
    public char         Tag_Model;
    public char         Cus_Category;
    public char[]       PWD;

     /* Common */
    public int          Start_Block;    //In, Start block address
    //public byte[]       Read_Buf;       // Out, Pointer of rx buffer
    //public byte       Read_Buf[];       // Out, Pointer of rx buffer
    public byte         Read_Len;       // In, Target Length; Out, Got Length. Count in bytes

    /*************************************************************************
     *       Write Tag Class
     *************************************************************************/
    public byte[]       Write_Buf;      // In, Pointer of tx buffer
    public byte         Write_Len;      // In Count in bytes
}
