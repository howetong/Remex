package cn.tonghao.remex.common.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 获得本地在局域网的ipv4地址
 */
public class NetworkUtils {

    public static String getIPv4Address() {
        String localIpAddress = "127.0.0.1";
        try {
            Enumeration<NetworkInterface> interfaceList = NetworkInterface.getNetworkInterfaces();
            if (Objects.nonNull(interfaceList)) {
                while (interfaceList.hasMoreElements()) {
                    NetworkInterface iface = interfaceList.nextElement();
                    Enumeration<InetAddress> addrList = iface.getInetAddresses();
                    while (addrList.hasMoreElements()) {
                        InetAddress address = addrList.nextElement();
                        if (Objects.nonNull(address) && address instanceof Inet4Address && !"127.0.0.1".equals(address.getHostAddress())) {
                            localIpAddress = address.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localIpAddress;
    }


}
