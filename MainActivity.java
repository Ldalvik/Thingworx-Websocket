package tufts;

import android.content.*;
import android.content.res.*;
import android.net.wifi.*;
import android.os.*;
import android.support.v7.app.*;
import android.widget.*;
import fi.iki.elonen.*;
import java.io.*;
import thingworx.tufts.*;
import java.net.*;
import thingworx.*;

public class MainActivity extends AppCompatActivity {
	RelativeLayout canvas;
	String fileTxt;
	Websocket s;
	Creator c = null;
	TextView[] textviews = new TextView[100];
	ImageView[] images = new ImageView[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
	    canvas = (RelativeLayout) findViewById(R.id.canvas);
		c = new Creator(canvas, MainActivity.this);
		try {
			s = new Websocket(8080, new CallbackInterface(){
					@Override
					public void onRequestFinish(final String result){
						runOnUiThread(new Runnable(){
							@Override
								public void run(){
									Request r = new Request(result);
									switch(r.getFunction()){
										case "toast": Toast.makeText(MainActivity.this, result.split(":")[1] +" has connected.", Toast.LENGTH_SHORT).show(); break;
										case "addText": c.addText(r.getText(), r.getX(), r.getY(), r.getSize(), r.getColor(), r.getId()); break;
										case "deleteText": c.deleteText(r.getId()); break;
										case "updateText":
											switch(r.getAttribute()){
												case "text": c.updateText(r.getText(), r.getId()); break;
												case "x": c.updateX(r.getX(), r.getId()); break;
												case "y": c.updateY(r.getY(), r.getId()); break;
												case "size": c.updateSize(r.getSize(), r.getId()); break;
												case "color": c.updateColor(r.getColor(), r.getId()); break;
											} break;
										}
									}
							});
					}
					
				
			}, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		fileTxt = openFile();
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("Enable Web Server?");
		dialog.setMessage("This will deploy a WebServer at http://"+phoneIp()+":8080/");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				try {
					Server c = new Server(s);
					c.execute();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		});
		
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int id){
					System.exit(0);
				}
			});
			dialog.setCancelable(false);
			dialog.show();
    }
	
	public String phoneIp(){
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
		final String formattedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
		return formattedIpAddress;
	}
	
	public String openFile () {
		System.out.println("Starting openFile now");
		StringBuilder str = new StringBuilder();
		AssetManager am = getAssets();
		try {
			InputStream is = am.open("webserver.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				str.append(inputLine);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}
}

