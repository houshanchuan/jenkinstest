package hos.srm.srmbusiness.ftp;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

public class SftpUserInfo implements UserInfo, UIKeyboardInteractive {
    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public boolean promptPassphrase(String message) {
        return false;
    }

    public boolean promptPassword(String message) {
        return false;
    }

    public boolean promptYesNo(String message) {
        return true;
    }

    public void showMessage(String message) {}

    public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
        return null;
    }
}
