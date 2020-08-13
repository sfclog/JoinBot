package bot.sfclog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadFileProxy {
	private final List<Proxy> proxys = new ArrayList<>();
    private final Map<Proxy, SocksType> sockTypes = new HashMap<>();
    private volatile AtomicInteger currentPosition = new AtomicInteger(0);

    public LoadFileProxy()
    {
        proxys.add(Proxy.NO_PROXY);

        BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("socks4_proxies.txt"))));
		} catch (FileNotFoundException e) {
		}

        reader.lines().forEach(str -> {
            String[] arr = str.split(":");
            String host = arr[0];
        	int port = Integer.parseInt(arr[1]);
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port));

            proxys.add(proxy);
            sockTypes.put(proxy, SocksType.SOCKS4);
        });
    }

    public List<Proxy> getProxys()
    {
        return this.proxys;
    }

    public synchronized int getCurrentPosition()
    {
        return this.currentPosition.get();
    }

    public synchronized Proxy nextProxy()
    {
        Proxy proxy = proxys.get(this.currentPosition.getAndIncrement());

        if (this.currentPosition.get() >= proxys.size()) this.currentPosition.set(0);

        return proxy;
    }

    public SocksType getSocksType(Proxy proxy)
    {
        return sockTypes.get(proxy);
    }

    public static String proxyToString(Proxy proxy)
    {
        return "Proxy[host=" + ((InetSocketAddress) proxy.address()).getAddress().getHostAddress() + ";port=" + ((InetSocketAddress) proxy.address()).getPort() + "]";
    }

    public enum SocksType
    {
        SOCKS4,
        SOCKS5
    }
}