package com.sjtu.micx.flip;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
/**
 * Gallery的适配器类，主要用于加载图片
 * @author lyc
 *
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;
	/*图片素材*/
	
	
	public String[] images;
	public int len;

	public GalleryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return len;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//每次移动获取图片并重新加载，当图片很多时可以构造函数就把bitmap引入并放入list当中，
		//然后在getview方法当中取来直接用
		Log.i("IMG___", images[position]);
		Bitmap bmp = BitmapFactory.decodeFile(images[position]);
//		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), images[position]);
		MyImageView view = new MyImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

}
