package per.emp.net;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnect {
	// 服务器地址
	private final String SERVER_IP = "192.168.0.12";
	// 服务器端口
	private final int SERVER_PORT = 5000;
	// 摄像机端口
	private final int CAMERA_PORT = 9000;
	// 编译原理

	public void connecttoserver(String socketData) throws UnknownHostException,
			IOException {
		int port;
		if(socketData.equals("P"))
		{
			port=CAMERA_PORT;
		}else
		{
			port=SERVER_PORT;
		}
		Socket socket =new Socket(SERVER_IP, port);
		SendMsg(socket, socketData);
	}
	private void SendMsg(Socket socket, String msg) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		if (socket != null) {
			try {
				dataOutputStream.writeBytes(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unused")
	private void ReceiveMsg(final Socket socket) throws IOException {
		final DataInputStream dataInputStream = new DataInputStream(
				socket.getInputStream());
		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (socket != null && !socket.isClosed()) {
					try {
						dataInputStream.read();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	// 接收图片信息数组，并返回byte 数组的方法
	@SuppressWarnings("unused")
	private byte[] getData(DataInputStream inputStream) throws IOException{
		ServerSocket serverSocket = new ServerSocket(CAMERA_PORT);
		Socket socket = serverSocket.accept();
		DataInputStream dataInputStream = new DataInputStream(
				socket.getInputStream());
		byte[] inputByte = new byte[1024];
		int length = -1;
		int count = 0;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			while ((length = inputStream.read(inputByte)) != -1) {
				outputStream.write(inputByte, 0, length);
				count += length;
				if (count == 320 * 240 * 5) {
					break;
				}
			}
			byte[] data = outputStream.toByteArray();
			outputStream.flush();
			outputStream.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
