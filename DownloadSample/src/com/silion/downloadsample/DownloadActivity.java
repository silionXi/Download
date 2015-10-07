package com.silion.downloadsample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DownloadActivity extends Activity {
	
	private Button mDoc;
	private Button mMedia;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case 1:
				Toast.makeText(getBaseContext(), "下载成功", Toast.LENGTH_LONG).show();
				break;
			case 0:
				Toast.makeText(getBaseContext(), "下载失败", Toast.LENGTH_LONG).show();
			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		mDoc = (Button) findViewById(R.id.doc);
		mDoc.setOnClickListener(mDocListener);
		mMedia = (Button) findViewById(R.id.media);
		mMedia.setOnClickListener(mMediaListener);
	}
	
	OnClickListener mDocListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(mDownloadDoc).start();
		}	
	};
	
	OnClickListener mMediaListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			new Thread(mDownloadMedia).start();
		}
	};
	
	Runnable mDownloadDoc = new Runnable() {

		@Override
		public void run() {
			byte[] buffer = new byte[8*1024];
			try {
				URL url = new URL("http://www.baidu.com/");
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				InputStream is = urlConnection.getInputStream();
				is.read(buffer);
				String text = new String(buffer);
				android.util.Log.v("slong.liang", "text = " + text);
				Message msg = new Message();
				msg.arg1 = 1;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.arg1 = 0;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	};
	
	Runnable mDownloadMedia = new Runnable() {

		@Override
		public void run() {
			int count;
			byte[] buffer = new byte[8*1024];
			FileOutputStream fos = null;
			try {
				URL url = new URL("http://media.ringring.vn/ringtone/realtone/0/0/161/165346.mp3");
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				InputStream is = urlConnection.getInputStream();
				android.util.Log.v("slong.liang", "path = " + Environment.getExternalStorageDirectory().getPath());
				File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/silion");
				if(!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, "text.mp3");
				fos = new FileOutputStream(file);
				while((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				fos.flush();
				Message msg = new Message();
				msg.arg1 = 1;
				mHandler.sendMessage(msg);
			} catch (Exception e) {
				Message msg = new Message();
				msg.arg1 = 0;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
