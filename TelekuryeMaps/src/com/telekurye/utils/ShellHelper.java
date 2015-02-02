package com.telekurye.utils;

import java.io.DataOutputStream;
import java.util.LinkedList;
import java.util.List;

import com.telekurye.mobileui.Login;
import com.telekurye.tools.Tools;

public class ShellHelper {

	public static void Reboot() {
		List<String> cmds = new LinkedList<String>();
		cmds.add("reboot");
		doCmds(cmds);
	}

	public static void doCmds(List<String> cmds) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(process.getOutputStream());

			for (String tmpCmd : cmds) {
				os.writeBytes(tmpCmd + "\n");
			}

			os.writeBytes("exit\n");
			os.flush();
			os.close();

			process.waitFor();
		}
		catch (Exception e) {
			Tools.saveErrors(e);

		}
	}
}
