import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import algorithms.ImageSegment;
import algorithms.ImageEnhance;
import common.Common;

public class TuxiangFenge extends JFrame implements ActionListener
{
    Image iImage, iImage2, oImage;
    
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            loadflag2 = false,
            runflag   = false;    //图像处理执行标志 
     
    ImageEnhance enhance;       
    ImageSegment segment;
    Common common;
    
    public TuxiangFenge()
    {    
        setTitle("图像分割");
        this.setBackground(Color.lightGray);        
              
        //菜单界面
        setMenu();
        
        enhance = new ImageEnhance();
        segment = new ImageSegment();
        common  = new Common();
        
        //关闭窗口
        closeWin();
        
        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//文件选择对话框
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images/ch8", 0);//设置默认目录,过滤文件
            int r = chooser.showOpenDialog(null);
                        
            MediaTracker tracker = new MediaTracker(this);
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                 
                if(loadflag2||runflag)
                { 
                    loadflag  = false;
                    loadflag2 = false;
                    runflag   = false;
                }                   
			    if(!loadflag)
			    {
	                //装载图像
				    iImage = common.openImage(name, tracker);    
				    //取载入图像的宽和高
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }
			    else if(loadflag && (!runflag))
			    {			        
				    iImage2 = common.openImage(name, tracker);    
				    common.draw(graph, iImage, "原图", iImage2, "原图2");
				    loadflag2 = true;				    			    	
			    }			    			               
            }                      
        }
	    else if (evt.getSource() == thresh1Item)
        {
        	setTitle("图像分割 阈值分割");
        	if(loadflag)//6: 1维阈值分割
        	    show(graph, "1维阈值分割");				
	    }
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    
    public void paint(Graphics g) 
    {    	  
        if (loadflag)
        {
        	g.clearRect(0,0,530, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("原图", 120, 320);
        }        
    }
      
    /*************************************************
     * graph - 图像参数
     * name - 输出图像标题字符串
     *************************************************/    
    public void show(Graphics graph, String name)
    {  
        int th;
    	pixels = common.grabber(iImage, iw, ih);
		
		//1维阈值分割	            
	    th = segment.segment(pixels, iw, ih);	           
	    pixels = common.thSegment(pixels, iw, ih, th);
			    
	    //将数组中的象素产生一个图像
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "原图", oImage, name);
		runflag = true;		
    }
    
    public static void main(String[] args) 
    {  
        new TuxiangFenge();        
    } 
    
    private void closeWin()
    {
    	addWindowListener(new WindowAdapter()
        {  
            public void windowClosing(WindowEvent e) 
            {  
                System.exit(0);
            }
        });
    }
    
    //菜单界面
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("文件");
        openItem = new MenuItem("打开");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("退出");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
                
        Menu imSegMenu = new Menu("图像分割");
        thresh1Item = new MenuItem("1维阈值分割");
        thresh1Item.addActionListener(this);
        imSegMenu.add(thresh1Item);
    
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(imSegMenu);
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem thresh1Item;       //一维阈值分割  
  
}
