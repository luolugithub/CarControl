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

		// ��ȡĬ�ϵ�NFC������
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (nfcAdapter == null) {
			promt.setText("�豸��֧��NFC��");
			finish();
			return;
		}
		if (!nfcAdapter.isEnabled()) {
			promt.setText("����ϵͳ������������NFC���ܣ�");
			finish();
			return;
		}
	}

	protected void onResume() {
		super.onResume();
		// �õ��Ƿ��⵽ACTION_TECH_DISCOVERED����
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
			// �����intent
			processIntent(getIntent());
		}
	}

	// �ַ�����ת��Ϊ16�����ַ���
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
		// ȡ����װ��intent�е�TAG
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		for (String tech : tagFromIntent.getTechList()) {
			System.out.println(tech);
		}
		boolean auth = false;
		// ��ȡTAG
		MifareClassic mfc = MifareClassic.get(tagFromIntent);
		try {
			String metaInfo = "";
			mfc.connect();
			int type = mfc.getType();// ��ȡTAG������
			int sectorCount = mfc.getSectorCount();// ��ȡTAG�а�����������
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
			metaInfo += "��Ƭ���ͣ�" + typeS + "\n��" + sectorCount + "������\n��"
					+ mfc.getBlockCount() + "����\n�洢�ռ�: " + mfc.getSize()
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
					metaInfo += "Sector " + j + ":��֤�ɹ�\n";
					// ��ȡ�����еĿ�
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
					metaInfo += "Sector " + j + ":��֤ʧ��\n";
				}
			}
			promt.setText(metaInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}