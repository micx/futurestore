package com.sjtu.micx.db;


import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {
	
	private static final String TAG = "Book";
	private DBAdapter dbAdapter = null;
	List<String> mList = new ArrayList<String>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		
		
		/*
		 * create and open DB
		 */
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		/*
		 * insert books to DB
		 */
		insertBook();
		
		
		/*get all book list*/
		loadAllList();
		
		/*
		 * display all book as a list
		 */
		setListAdapter(new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, mList));//mStrings)); //use List or String[]
		
		//setListAdapter(new ArrayAdapter<String>(this,R.layout.file_row, mList));
		
		getListView().setTextFilterEnabled(true);
	}
	
	/*
	 * insert titls to DB
	 */
	public void insertBook()
	{
		long id;
		id = dbAdapter.insertTitle(
		"0470285818",
		"C# 2008 Programmer's Reference",
		"Wrox");
		id = dbAdapter.insertTitle(
		"047017661X",
		"Professional Windows Vista",
		"Wrox");
		id = dbAdapter.insertTitle("0470285818",
		"C Programming",
		"Wrox");
		id = dbAdapter.insertTitle("0470285818",
				"D Programming",
				"Wrox");
		Log.i(TAG, "id = "+ id);
	}

	@Override
	protected void onDestroy() {
	// TODO Auto-generated method stub
	
	super.onDestroy();
	dbAdapter.close();
	}

	/*
	 * load all book list from DB
	 */
	public void loadAllList()
	{
		mList.add("ALL BOOK LIST");
		Cursor c = dbAdapter.getAllTitles();
		if (c.moveToFirst())
		{
			 do{
				 int nameColumn = c.getColumnIndex(DBAdapter.KEY_TITLE);
				 int isbnColumn = c.getColumnIndex(DBAdapter.KEY_ISBN);
				 int publishColumn = c.getColumnIndex(DBAdapter.KEY_PUBLISHER);
				 int idColumn = c.getColumnIndex(DBAdapter.KEY_ROWID);
				 mList.add(c.getString(idColumn)+"    "+c.getString(isbnColumn)+"    "+c.getString(nameColumn)+"    "+c.getString(publishColumn));
			 } while (c.moveToNext());
		}
	}

}

