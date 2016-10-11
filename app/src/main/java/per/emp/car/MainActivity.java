package per.emp.car;


import org.json.JSONException;
import org.json.JSONObject;

import per.emp.beam.BeamActivity;
import per.emp.net.SocketConnect;
import per.emp.net.WifiAdmin;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zxing.activity.CaptureActivity;

public class MainActivity extends Activity {

	private SocketConnect socketConn;
	private MyApplication myApp;
	static String scanResult;
	private DrawerLayout mDrawerLayout;
	private TextView showarea;
	private String[] mPlanetTitles;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ����Activity��ӵ��б���
		CloseActivity.getInstance().addActivity(this);

		// ʵ��������
		socketConn = new SocketConnect();
		myApp = (MyApplication) getApplicationContext();
		showarea = (TextView) findViewById(R.id.showarea);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		initListView();

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// ActionBar����ģʽ����
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	/*
	 * ���ò������ĸ�ʽ�Ͱ�ť��ʾ
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Activity�����й��ڸ�����ť�����¼�
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.erweima:
			Intent openCameraIntent = new Intent(MainActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0);
			return true;
		case R.id.nfc:
			Intent intents = new Intent(MainActivity.this, BeamActivity.class);
			startActivity(intents);
			return true;
		case R.id.about:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * ���ض�ά��ɨ��Ľ�� (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ����ɨ�������ڽ�������ʾ��
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			scanResult = bundle.getString("result");
			ShowMes(scanResult);
			parseJson(scanResult);
		}
	}
	// ������ά��ɨ�践�ص�����
	private void parseJson(String strResult) {
		try {
			JSONObject jsonObj = new JSONObject(strResult);
			
			ShowMes(jsonObj.toString());
			
			/*
			String KeyA = jsonObj.getString("KeyA");
			String KeyB = jsonObj.getString("KeyB");
			int WifiNameID = jsonObj.getInt("WifiName");
			int WifiPassID = jsonObj.getInt("WifiPass");
			String message = "KeyA:" + KeyA + "KeyB:" + KeyB + "WifiName:"
					+ WifiNameID + "WifiPass:" + WifiPassID;
					*/
			String Wifi=jsonObj.getString("Wifi");
			String Pass=jsonObj.getString("Pass");
			String UID=jsonObj.getString("UID");
			int IP=jsonObj.getInt("IP");
			String message=Wifi+Pass+UID+IP;
			ShowMes(message);
			myApp.setWifiName(Wifi);
			myApp.setWifiPass(Pass);
			//myApp.setKeyA(KeyA);
			//myApp.setKeyB(KeyB);
			//myApp.setWifiNameID(WifiNameID);
			//myApp.setWifiPassID(WifiPassID);
		} catch (JSONException e) {
			System.out.println("Json parse error");
			e.printStackTrace();
		}
	}

	/*
	 * ������Ĵ����д���������back���ؼ��˳�����Ĺ���
	 */
	private static final String TAG = MainActivity.class.getSimpleName();
	// ����һ������������ʶ�Ƿ��˳�
	private static boolean isExit = false;
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	// ����������
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// ʵ������������η��ؼ��˳�
	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "�ٰ�һ�κ��˼��˳�����",
					Toast.LENGTH_SHORT).show();
			// ����handler�ӳٷ��͸���״̬��Ϣ
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Log.e(TAG, "exit application");
			CloseActivity.getInstance().exit();
		}
	}

	// ʵ��WiFi���������Զ�����
	public void WifiConnect() {
		ShowMes(myApp.getWifiName()+myApp.getWifiPass());
		WifiAdmin wifiAdmin = new WifiAdmin(this);
		wifiAdmin.openWifi();
		wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(myApp.getWifiName(),
				myApp.getWifiPass(), 2));
	}
	
	/*
	 * ��ʼ���������룬��Ӷ����ʾ�б�Ϊÿһ��Items���������¼�����ʵ�ֲ�ͬ�Ĺ���
	 */
	private void initListView(){
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mDrawerList.setItemChecked(position, true);
				switch (position) {
				case 0:
					Intent intent1 = new Intent(MainActivity.this,
							BeamActivity.class);
					startActivity(intent1);
					break;
				case 1:
					Intent intent2 = new Intent(MainActivity.this,
							CaptureActivity.class);
					startActivity(intent2);
					break;
				case 2:
					WifiConnect();
					break;
				case 5:
					myApp.setControlStr("G");
					ShowMes("Gǰ��");
					break;
				case 6:
					myApp.setControlStr("L");
					break;
				case 7:
					myApp.setControlStr("R");
					break;
				case 8:myApp.setControlStr("B");
					break;
				case 9:
					myApp.setControlStr("X");
					ShowMes("X�Զ�Ѱ��");
					break;
				case 10:
					myApp.setControlStr("P");
					break;
				default:
					break;
				}
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});
	}

	/*
	 * ��������ס��ʾ������б���ʾ��
	 * @see android.app.Activity#onPrepareOptionsMenu(android.view.Menu)
	 */
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	/*
	 * Ϊ���ݽ���ͳһ����ʾ����
	 */
	public void ShowMes(String mes){
		showarea.setText(showarea.getText()+"\n"+mes);
		
	}
}