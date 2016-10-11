package per.emp.beam;

import per.emp.car.MyApplication;
import per.emp.car.R;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.widget.TextView;


public class BeamActivity extends Activity {
	NfcAdapter nfcAdapter;
	TextView promt;
	private MyApplication myApp;
	@SuppressWarnings("unused")
	private String WifiSSID, WifiPassword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		promt = (TextView) findViewById(R.id.promt);
		myApp = (MyApplication) getApplicationContext();

		// 获取默认的NFC控制器
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			promt.setText("设备不支持NFC！");
			finish();
			return;
		}
		if (!nfcAdapter.isEnabled()) {
			promt.setText("请在系统设置中先启用NFC功能！");
			finish();
			return;
		}
	}

	protected void onResume() {
		super.onResume();
		// 得到是否检测到ACTION_TECH_DISCOVERED触发
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			// 处理该intent
			processIntent(getIntent());
		}
	}

	// 字符序列转换为16进制字符串
	@SuppressWarnings("unused")
	private String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("0x");
		if (src == null || src.length <= 0) {
			return null;
		}
		char[] buffer = new char[2];
		for (int i = 0; i < src.length; i++) {
			buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
			buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
			System.out.println(buffer);
			stringBuilder.append(buffer);
		}
		return stringBuilder.toString();
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	@SuppressWarnings("unused")
	private void processIntent(Intent intent) {
		// 取出封装在intent中的TAG
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		for (String tech : tagFromIntent.getTechList()) {
			System.out.println(tech);
		}
		boolean auth = false;
		// 读取TAG
		MifareClassic mfc = MifareClassic.get(tagFromIntent);
		try {
			String metaInfo = "";
			mfc.connect();
			int type = mfc.getType();// 获取TAG的类型
			int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
			String typeS = "";
			switch (type) {
			case MifareClassic.TYPE_CLASSIC:
				typeS = "TYPE_CLASSIC";
				break;
			case MifareClassic.TYPE_PLUS:
				typeS = "TYPE_PLUS";
				break;
			case MifareClassic.TYPE_PRO:
				typeS = "TYPE_PRO";
				break;
			case MifareClassic.TYPE_UNKNOWN:
				typeS = "TYPE_UNKNOWN";
				break;
			}
			metaInfo += "卡片类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
					+ mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize()
					+ "B\n";
			for (int j = 0; j < sectorCount; j++) {
				auth = mfc.authenticateSectorWithKeyA(j, myApp.getKeyA()
						.getBytes());
				// auth = mfc.authenticateSectorWithKeyB(j,
				// myApp.getKeyB().getBytes());
				// auth =
				// mfc.authenticateSectorWithKeyA(j,MifareClassic.KEY_DEFAULT);
				int bCount;
				int bIndex;
				if (auth) {
					metaInfo += "Sector " + j + ":验证成功\n";
					// 读取扇区中的块
					// bCount = mfc.getBlockCountInSector(j);
					// bIndex = mfc.sectorToBlock(j);
					myApp.setWifiName(mfc.readBlock(myApp.getWifiNameID())
							.toString());
					myApp.setWifiPass(mfc.readBlock(myApp.getWifiPassID())
							.toString());
					/*
					 * for (int i = 0; i < bCount; i++) { byte[] data =
					 * mfc.readBlock(bIndex); metaInfo += "Block " + bIndex +
					 * " : "+ bytesToHexString(data) + "\n"; bIndex++; }
					 */
				} else {
					metaInfo += "Sector " + j + ":验证失败\n";
				}
			}
			promt.setText(metaInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}