package gateway.sockettest.server.domain;

import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2010-7-1
 * Time: 9:04:42
 * To change this template use File | Settings | File Templates.
 */
public class User {
    public User(Socket socket) {
        this.socket = socket;
        this.ip = socket.getRemoteSocketAddress().toString().split(":")[0]
                .replaceAll("\\||/", "");
        this.port = Integer.valueOf(socket.getRemoteSocketAddress().toString()
                .split(":")[1]);
    }

    private String ip;
    private Integer port;
    private String username = "anonymous";
    private String nickName = null;
    private Socket socket;

    public boolean equals(Object obj) {
        if (null == obj)
            return false;
        User user2 = (User) obj;
        if (null == this.ip || null == this.port || null == user2.ip
                || null == user2.port)
            return false;
        return this.ip.equals(user2.ip) && this.port.equals(user2.port);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDisplayName() {
        if (null == this.getNickName()) {
            return this.getUsername();
        } else {
            return this.getNickName();
        }
    }

    public String getUserNameDetail() {
        String[] nums = this.getIp().split("\\.");
        return this.getDisplayName() + "*" + nums[2] + "." + nums[3] + ":" + this.getPort();
    }

    public String getUserNameDetailWithExcapeRegExp() {
        String[] nums = this.getIp().split("\\.");
        return this.getUsername() + "\\*" + nums[2] + "\\." + nums[3] + ":" + this.getPort();

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}