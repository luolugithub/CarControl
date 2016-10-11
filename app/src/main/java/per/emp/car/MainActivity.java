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

		// 将此Activity添加到列表中
		CloseActivity.getInstance().addActivity(this);

		// 实例化定义
		socketConn = new SocketConnect();
		myApp = (MyApplication) getApplicationContext();
		showarea = (TextView) findViewById(R.id.showarea);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		initListView();

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// ActionBar操作模式开启
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
	 * 设置操作栏的格式和按钮显示
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * Activity界面中关于各个按钮处理事件
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
	 * 返回二维码扫描的结果 (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			scanResult = bundle.getString("result");
			ShowMes(scanResult);
			parseJson(scanResult);
		}
	}
	// 解析二维码扫描返回的数据
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
	 * 在下面的代码中处理点击两次back返回键退出程序的功能
	 */
	private static final String TAG = MainActivity.class.getSimpleName();
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	// 建立监听键
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 实现连续点击两次返回键退出
	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
					Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			Log.e(TAG, "exit application");
			CloseActivity.getInstance().exit();
		}
	}

	// 实现WiFi无线网络自动连接
	public void WifiConnect() {
		ShowMes(myApp.getWifiName()+myApp.getWifiPass());
		WifiAdmin wifiAdmin = new WifiAdmin(this);
		wifiAdmin.openWifi();
		wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(myApp.getWifiName(),
				myApp.getWifiPass(), 2));
	}
	
	/*
	 * 初始化导航抽屉，添加多个显示列表，为每一个Items建立监听事件，并实现不同的功能
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
					ShowMes("G前进");
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
					ShowMes("X自动寻迹");
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
	 * 建立覆盖住显示界面的列表显示框
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
	 * 为数据建立统一的显示区域
	 */
	public void ShowMes(String mes){
		showarea.setText(showarea.getText()+"\n"+mes);
		
	}
}