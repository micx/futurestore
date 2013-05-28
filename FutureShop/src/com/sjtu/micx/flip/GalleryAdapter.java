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
 * Gallery���������࣬��Ҫ���ڼ���ͼƬ
 * @author lyc
 *
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;
	/*ͼƬ�ز�*/
	
	
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
		//ÿ���ƶ���ȡͼƬ�����¼��أ���ͼƬ�ܶ�ʱ���Թ��캯���Ͱ�bitmap���벢����list���У�
		//Ȼ����getview��������ȡ��ֱ����
		Log.i("IMG___", images[position]);
		Bitmap bmp = BitmapFactory.decodeFile(images[position]);
//		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), images[position]);
		MyImageView view = new MyImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

}
