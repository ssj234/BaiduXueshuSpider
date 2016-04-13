package com.joup.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display dis=new Display();
		Shell shell=new Shell(dis);
		CommonDialog cd=new CommonDialog(shell);
		cd.setStyle("百度学术搜索-加油~", 600, 400);
		cd.open();
	}

}
