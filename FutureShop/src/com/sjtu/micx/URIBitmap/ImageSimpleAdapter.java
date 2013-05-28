package com.sjtu.micx.URIBitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sjtu.micx.futureshop.R;
import com.sjtu.micx.futureshop.ViewLayoutC;

public class ImageSimpleAdapter extends SimpleAdapter{
	private int[] mTo;  
    private String[] mFrom;  
    private ViewBinder mViewBinder;  
    private List<? extends Map<String, ?>> mData;  
    private Map<String,?> mMap;
    private int mResource;  
    private int mDropDownResource;  
    private LayoutInflater mInflater;  
    private Context mContext; 
    public ImageSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {  
        super(context, data, resource, from, to);  
        mContext = context;
        mTo = to;  
        mFrom = from;  
        mData = data;  
        mResource = mDropDownResource = resource;  
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
    }  
    
    class ViewHolder {  
        ImageView imgview;  
        TextView price;  
        TextView num;
        TextView title;
        Button plus;
        Button mines;
    }  
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {  
 
    	convertView = mInflater.inflate(mResource, parent, false);
    	ImageButton plus = (ImageButton) convertView.findViewById(R.id.plusbtn);
    	ImageButton mines = (ImageButton) convertView.findViewById(R.id.minesbtn);
    	ImageButton delete = (ImageButton) convertView.findViewById(R.id.delete);
    	//final EditText txtview = (EditText) convertView.findViewById(R.id.num);
    	final TextView txtview = (TextView) convertView.findViewById(R.id.num);
        final int  p = position;  
        HashMap<String, Object> mMap = new HashMap<String, Object>();
        mMap = (HashMap<String, Object>) mData.get(p);
        if(plus!=null){
	       // txtview.setInputType(InputType.TYPE_CLASS_NUMBER);
	        plus.setOnLongClickListener(new Button.OnLongClickListener() {
	
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					return false;
				}           
	        });  
	        plus.setOnClickListener(new Button.OnClickListener() {  
	            @Override
				public void onClick(View view) {              	
	            	HashMap<String, Object> mMap ;//= new HashMap<String, Object>();
	                mMap = (HashMap<String, Object>) mData.get(p);
	                //int i =  Integer.parseInt(mMap.get("num").toString());
	                int i =  Integer.parseInt(txtview.getText().toString());
	                i++;
	                mMap.put("num", i+"");
	              //  System.out.println("position:" + p+"   "+mMap.get("num"));  
	               // txtview.setText(i+"");
	            	ViewLayoutC.UpdateCartList();
	            	//notifyDataSetInvalidated();	
	            }  
	        });  
	        mines.setOnClickListener(new Button.OnClickListener() {  
	            @Override
				public void onClick(View view) {  
	            	
	            	HashMap<String, Object> mMap ;//= new HashMap<String, Object>();
	                mMap = (HashMap<String, Object>) mData.get(p);
	                //int i =  Integer.parseInt(mMap.get("num").toString());
	                int i =  Integer.parseInt(txtview.getText().toString());
	                i--;
	                if(i<0)i=0;
	                mMap.put("num", i+"");
	               // txtview.setText(i+"");
	            	ViewLayoutC.UpdateCartList();
	            	//notifyDataSetInvalidated();	
	            }  
	        });  
	        delete.setOnClickListener(new Button.OnClickListener() {  
	            @Override
				public void onClick(View view) {  
	            dialog(p);
	            	
	            	
	            }  
	        });
        }
        return createViewFromResource(position, convertView, parent, mResource);   
    }  
    protected void dialog(int position) {
    	  final int p = position;
    	  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    	  builder.setMessage("确认删除商品："+mData.get(p).get("title"));
    	  builder.setTitle("提示");
    	  builder.setPositiveButton("确认", new AlertDialog.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				mData.remove(p);
				//DataCenter.Orders.remove(p);
            	ViewLayoutC.UpdateCartList();
				//notifyDataSetInvalidated();	
			}  
    	  });
    	  builder.setNegativeButton("取消", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
    	   
    	  });
    	  builder.create().show();
    	 }
    @Override
	public void notifyDataSetInvalidated() {
		// TODO Auto-generated method stub
		super.notifyDataSetInvalidated();
	} 
    
    @Override  
    public View getDropDownView(int position, View convertView, ViewGroup parent) {  
        return createViewFromResource(position, convertView, parent, mDropDownResource);  
    }  
    private View createViewFromResource(int position, View convertView,  
            ViewGroup parent, int resource) {  
        View v;  
        if (convertView == null) {  
            v = mInflater.inflate(resource, parent, false);  
        } else {  
            v = convertView;  
        }  
  
        bindView(position, v);  
  
        return v;  
    }  
    private void bindView(int position, View view) {  
        final Map dataSet = mData.get(position);  
        if (dataSet == null) {  
            return;  
        }  
  
        final ViewBinder binder = mViewBinder;  
        final String[] from = mFrom;  
        final int[] to = mTo;  
        final int count = to.length;  
  
        for (int i = 0; i < count; i++) {  
            final View v = view.findViewById(to[i]);  
            if (v != null) {  
                final Object data = dataSet.get(from[i]);  
                String text = data == null ? "" : data.toString();  
                if (text == null) {  
                    text = "";  
                }  
  
                boolean bound = false;  
                if (binder != null) {  
                    bound = binder.setViewValue(v, data, text);  
                }  
  
                if (!bound) {  
                    if (v instanceof Checkable) {  
                        if (data instanceof Boolean) {  
                            ((Checkable) v).setChecked((Boolean) data);  
                        } else if (v instanceof TextView) {  
                            // Note: keep the instanceof TextView check at the bottom of these  
                            // ifs since a lot of views are TextViews (e.g. CheckBoxes).  
                            setViewText((TextView) v, text);  
                        } else {  
                            throw new IllegalStateException(v.getClass().getName() +  
                                    " should be bound to a Boolean, not a " +  
                                    (data == null ? "<unknown type>" : data.getClass()));  
                        }  
                    } else if (v instanceof TextView) {  
                        // Note: keep the instanceof TextView check at the bottom of these  
                        // ifs since a lot of views are TextViews (e.g. CheckBoxes).  
                        setViewText((TextView) v, text);  
                    } else if (v instanceof ImageView) {  
                        if (data instanceof Integer) {  
                            setViewImage((ImageView) v, (Integer) data);  
                        } else if (data instanceof Bitmap) {//仅仅添加这一步  
                            setViewImage((ImageView) v, (Bitmap)data);  
                        } else if (data instanceof String) {//仅仅添加这一步  
                            setViewImage((ImageView) v, (String)data);  
                        } else {  
                            setViewImage((ImageView) v, text);  
                        }  
                    } else {  
                        throw new IllegalStateException(v.getClass().getName()  
                                + " is not a view that can be bounds by this SimpleAdapter");  
                    }  
                }  
            }  
        }  
    }  
    /** 
     * 添加这个方法来处理Bitmap类型参数 
     * @param v 
     * @param bitmap 
     */  
    public void setViewImage(ImageView v, Bitmap bitmap) {  
        v.setImageBitmap(bitmap);  
    }  
    @Override
	public void setViewImage(ImageView v, String str) {  
    	Bitmap bitmap = BitmapFact.getBitmapOpts(str, 100*100);
    	v.setImageBitmap(bitmap);       
    } 
    
}
