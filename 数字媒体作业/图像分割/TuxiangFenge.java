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
            runflag   = false;    //ͼ����ִ�б�־ 
     
    ImageEnhance enhance;       
    ImageSegment segment;
    Common common;
    
    public TuxiangFenge()
    {    
        setTitle("ͼ��ָ�");
        this.setBackground(Color.lightGray);        
              
        //�˵�����
        setMenu();
        
        enhance = new ImageEnhance();
        segment = new ImageSegment();
        common  = new Common();
        
        //�رմ���
        closeWin();
        
        setSize(530, 330);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	      	  
        if (evt.getSource() == openItem) 
        {
        	//�ļ�ѡ��Ի���
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images/ch8", 0);//����Ĭ��Ŀ¼,�����ļ�
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
	                //װ��ͼ��
				    iImage = common.openImage(name, tracker);    
				    //ȡ����ͼ��Ŀ�͸�
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
				    loadflag = true;
			    }
			    else if(loadflag && (!runflag))
			    {			        
				    iImage2 = common.openImage(name, tracker);    
				    common.draw(graph, iImage, "ԭͼ", iImage2, "ԭͼ2");
				    loadflag2 = true;				    			    	
			    }			    			               
            }                      
        }
	    else if (evt.getSource() == thresh1Item)
        {
        	setTitle("ͼ��ָ� ��ֵ�ָ�");
        	if(loadflag)//6: 1ά��ֵ�ָ�
        	    show(graph, "1ά��ֵ�ָ�");				
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
            g.drawString("ԭͼ", 120, 320);
        }        
    }
      
    /*************************************************
     * graph - ͼ�����
     * name - ���ͼ������ַ���
     *************************************************/    
    public void show(Graphics graph, String name)
    {  
        int th;
    	pixels = common.grabber(iImage, iw, ih);
		
		//1ά��ֵ�ָ�	            
	    th = segment.segment(pixels, iw, ih);	           
	    pixels = common.thSegment(pixels, iw, ih, th);
			    
	    //�������е����ز���һ��ͼ��
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		oImage = createImage(ip);
		common.draw(graph, iImage, "ԭͼ", oImage, name);
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
    
    //�˵�����
    public void setMenu()
    {    	
        Menu fileMenu = new Menu("�ļ�");
        openItem = new MenuItem("��");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("�˳�");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);
    
                
        Menu imSegMenu = new Menu("ͼ��ָ�");
        thresh1Item = new MenuItem("1ά��ֵ�ָ�");
        thresh1Item.addActionListener(this);
        imSegMenu.add(thresh1Item);
    
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(imSegMenu);
        setMenuBar(menuBar);
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem thresh1Item;       //һά��ֵ�ָ�  
  
}
