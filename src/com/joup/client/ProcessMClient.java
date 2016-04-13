package com.joup.client;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

public class ProcessMClient implements ProcessM {
	Text text;
	int count=0;
	Display display;
	public ProcessMClient(Text text){
		this.text=text;
//		this.display=display;
	}
	@Override
	public void show(final String cnt) {
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				text.append(cnt+"\r\n");
				count++;
				if(count>100){
					text.setText("");
					count=0;
				}
			}
		});
			
	}

	

}
