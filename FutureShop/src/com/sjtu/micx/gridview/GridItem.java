/**
* 
*    @author    micx
*    @version    0.1,    9/21/2012
*    Copyright (c) 2012 SJTU RFID LAB, Inc.  All rights reserved.
*/
package com.sjtu.micx.gridview;

class GridItem   
{   
    private String title;   
    private String imageId;   
    private String description;  
      
    public GridItem()   
    {   
        super();   
    }   
   
    public GridItem(String title, String imageId,String time)   
    {   
        super();   
        this.title = title;   
        this.imageId = imageId;   
        this.description = time;  
    }   
   
    public String getTime( )  
    {  
        return description;  
    }  

    public String getTitle()   
    {   
        return title;   
    }   
   
    public String getImageId()   
    {   
        return imageId;   
    }   
}   

