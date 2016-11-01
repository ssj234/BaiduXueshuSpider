package com.joup.client;


import java.io.Console;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.joup.one.GetLink;
import com.joup.pre.ReadTitle;
import com.joup.three.GetAbstract;
import com.joup.two.GetLongsign;
import com.sail.db.step.one.TitleDao;




/**
 * �༭�Ի���Ļ���
 * @author ssj234
 *
 */
public class CommonDialog extends TitleAreaDialog{
	private String topTitle;

	private int Width, Height;

	
	Text text;
	
	public CommonDialog(Shell arg0) {
		super(arg0);
	}

	/**
	 * ���öԻ���ı��� ��С
	 * 
	 * @param topTitle
	 * @param bottomTitle
	 * @param width
	 * @param height
	 */
	public void setStyle(String topTitle, int width,
			int height) {
		this.topTitle = topTitle;
		
		this.Width = width;
		this.Height = height;
	}

	/**createContents
	 * ��дTitleAreaDialog��createDialogArea()����
	 */
	protected Control createDialogArea(Composite parent) {// ����
       setTitle(topTitle);
		GridLayout gl = new GridLayout();
		gl.marginWidth = 20;
		parent.setLayout(gl);

		parent.addListener(SWT.Traverse, new Listener() {
			public void handleEvent(Event e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE) {
					e.doit = false;
				}
			}
		});
		
		Composite topComp = new Composite(parent, SWT.NONE);
		topComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		FillLayout fill = new FillLayout();
		fill.marginHeight = 20;
		fill.marginWidth = 20;
		topComp.setLayout(fill);
		
		Composite main = new Composite(topComp, SWT.NONE);
		main.setLayout(new GridLayout(1,false));
		Group group=new Group(main,SWT.NONE);
		group.setText("1.导入文件");
		group.setLayout(new GridLayout(3,false));
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1,2));
		final Text file=new Text(group, SWT.BORDER|SWT.READ_ONLY);
		file.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2,1));
		Button btn=getButton(group,1);
		btn.setText("open");
		final Combo scope=getScope(group,0);
		final Button execute=getButton(group,2);
		execute.setEnabled(false);
		execute.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false, 1,1));
		btn.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				FileDialog fd=new FileDialog(getShell());
				String name=fd.open();
				if(name!=null)
				{
					file.setText(name);
					execute.setEnabled(true);
				}
			}
		});
		
		
		
		Group link=new Group(main,SWT.NONE);
		link.setText("2.获取Link");
		link.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1,2));
		link.setLayout(new GridLayout(3,false));
		final Combo list=getScope(link,0);
		Button linkBtn=getButton(link,3);
		
		Group sign=new Group(main,SWT.NONE);
		sign.setText("3.获取Sign");
		sign.setLayout(new GridLayout(3,false));
		sign.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1,2));
		Combo listsign=getScope(sign,2);
		Button signBtn=getButton(sign,4);
		
		Group abs=new Group(main,SWT.NONE);
		abs.setText("4.获取参考文献");
		abs.setLayout(new GridLayout(3,false));
		abs.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1,2));
		Combo listabs=getScope(abs,4);
		Button absBtn=getButton(abs,5);
		
		Group console=new Group(main,SWT.NONE);
		console.setText("console");
		console.setLayout(new GridLayout(2,false));
		console.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1,4));
		text=new Text(console, SWT.BORDER|SWT.V_SCROLL|SWT.WRAP);
		text.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1,1));
		
		
		
		//导入
		execute.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				final String cnt=file.getText();
				final int scopeIndex=scope.getSelectionIndex()+1;
				if(cnt==null){
					MessageDialog.openInformation(getShell(), "错误", "文件名为空");
					return;
				}
				boolean flag=MessageDialog.openConfirm(getShell(), "提示", "确认导入"+cnt+"吗? 领域为"+scopeIndex);
				if(flag){
					//ReadTitle rt=new ReadTitle();
					//rt.read(file.getText(), );
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							ReadTitle rt=new ReadTitle();
							rt.read(cnt,scopeIndex,new ProcessMClient(text));
						}
					}).start();
				}
				
			}
		});
		//
		linkBtn.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				final int scopeIndex=list.getSelectionIndex()+1;
				boolean flag=MessageDialog.openConfirm(getShell(), "提示", "确认查找领域为"+scopeIndex+"的link吗?");
				if(flag){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							GetLink rt=new GetLink();
							rt.beginParse(scopeIndex,new ProcessMClient(text));
						}
					}).start();
				}
				
			}
		});
		
		signBtn.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				final int scopeIndex=list.getSelectionIndex()+1;
				boolean flag=MessageDialog.openConfirm(getShell(), "提示", "确认查找领域为"+scopeIndex+"的Sign吗?");
				if(flag){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							GetLongsign rt=new GetLongsign();
							rt.beginParse(scopeIndex,new ProcessMClient(text));
						}
					}).start();
				}
				
			}
		});
		
		absBtn.addListener(SWT.MouseDown, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				final int scopeIndex=list.getSelectionIndex()+1;
				boolean flag=MessageDialog.openConfirm(getShell(), "提示", "确认查找领域为"+scopeIndex+"的参考文献吗?");
				if(flag){
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							GetAbstract rt=new GetAbstract();
							rt.beginParse(scopeIndex,new ProcessMClient(text));
						}
					}).start();
				}
				
			}
		});
		return topComp;
	}
	
	public Combo getScope(Composite parent,final int status){
		final Combo list=new Combo(parent, SWT.READ_ONLY);
		for(int i=1;i<20;i++){
			list.add("scope"+i);
		}
		list.select(0);
		list.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 1,1));
		
		final Label label=new Label(parent,SWT.NONE);
		label.setText("共计:#####");
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false, 1,1));
		
		list.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				long count=new TitleDao().countTitle(list.getSelectionIndex()+1, status);
				label.setText("共计:"+count);
			}
		});
		return list;
	}


	public Button getButton(Composite parent,int i ){
		Button btn=new Button(parent, SWT.READ_ONLY);
		btn.setText("click here for STEP-"+i);
		btn.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false, 1,1));
		return btn;
	}
	/*************************************************************************/
	/**
	 * ɾ���ʱ��Ҫ������
	 * @param obj
	 */
	public Object beforeDelete(Object obj){
		return obj;
	}
   public void afterDelete(Object obj){
		
	}
   /**
	 * �޸ĵ�ʱ��Ҫ������
	 * @param obj
	 */
   public void beforeUpdate(Object obj){
		
	}
  public void afterUpdate(Object obj){
		
	}
  /**
	 * ��ӵ�ʱ��Ҫ������
	 * @param obj
	 */
  public void beforeSave(Object obj){
		
	}
 public void afterSave(Object obj){
		
	}
	
/********************************������ʽ*****************************************/
	protected Point getInitialSize() {// �Ի����С
		Rectangle rtg = this.getShell().getMonitor().getClientArea();
		int width = rtg.width;
		int height = rtg.height;
		return new Point(width * Width / 100, height * Height / 100);
	}

	protected void configureShell(Shell newShell) {// ����

		super.configureShell(newShell);

		newShell.setText(topTitle);

	}

	protected Point getInitialLocation(Point arg0) {// λ��
		Rectangle rtg = getShell().getMonitor().getClientArea();
		int width = rtg.width;
		int height = rtg.height;

		int x = getInitialSize().x;
		int y = getInitialSize().y;

		Point p = new Point((width - x) / 2, (height - y) / 2);
		return p;
	}

	protected Button createButton(Composite comp, int id, String label,
			boolean defaultButton) {// ȡ��OK��cancel��ť

		return null;
	}

	public boolean close() {

		return super.close();
	}


	@Override
	protected int getShellStyle() {
		// TODO Auto-generated method stub
		return super.getShellStyle()|SWT.RESIZE;
	}

}
