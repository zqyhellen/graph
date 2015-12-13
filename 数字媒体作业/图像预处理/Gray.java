 

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import process.algorithms.Elements;
import process.param.*;
import process.common.Common;

public class Gray extends JFrame implements ActionListener
{
        
    Image iImage, iImage2, oImage;
    BufferedImage bImage;  
    int   iw, ih;
    int[] pixels;          
             
    boolean loadflag  = false,
            runflag   = false;    
            
  
    JButton okButton;
	JDialog dialog;  
     
    Common common;
    Elements elements;
    
    public Gray()
    {    
        setTitle("图像预处理");
        this.setBackground(Color.lightGray);        
              
      
        setMenu();
        
        common = new Common();
        elements = new Elements();
        
        closeWin();
            
        setSize(530, 330);
        setLocation(700, 10);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent evt)
    {
    	Graphics graph = getGraphics();
    	MediaTracker tracker = new MediaTracker(this);
    	      	  
        if (evt.getSource() == openItem) 
        {
            JFileChooser chooser = new JFileChooser();
            common.chooseFile(chooser, "./images", 0);
            int r = chooser.showOpenDialog(null);                    
            
            if(r == JFileChooser.APPROVE_OPTION) 
            {  
                String name = chooser.getSelectedFile().getAbsolutePath();
                
                if(runflag)
                { 
                    loadflag  = false;
                    runflag   = false;
                }   
			    if(!loadflag)
			    {
				    iImage = common.openImage(name, tracker);    
				    iw = iImage.getWidth(null);
				    ih = iImage.getHeight(null);				    
				    repaint();
			    }
			    else if(loadflag && (!runflag))
			    {			        
				    iImage2 = common.openImage(name, tracker);    
				    common.draw(graph, iImage, "pic 1", iImage2, "pic 2");
				    repaint();			    	
			    }				               
            }
            loadflag = true;
        }
        else if (evt.getSource() == grayItem)
        {
        	setTitle("图像预处理");
        	if(loadflag)        	
        	{ 
        	    pixels = common.grabber(iImage, iw, ih);
				
				pixels = elements.toGray(pixels, iw, ih);
				
				showPix(graph, pixels, "gray pic");
				repaint();				
			}
        }
        else if (evt.getSource() == contrastItem)
        {
            setTitle("图像预处理");
            if(loadflag)
            {	
            	pixels = common.grabber(iImage, iw, ih);
            	int con = common.getParam("type in contrast level(-100~100)","0");
                double contrast = (double)con/100.0;
                pixels = doContrast(pixels, iw, ih, contrast);
                showPix(graph, pixels, "contrast pic");
                repaint();
            }
        }
        else if (evt.getSource() == exitItem) 
            System.exit(0);       
    }
    public int[] doContrast(int[] pix, int iw, int ih, double contrast)
    {    
		ColorModel cm = ColorModel.getRGBdefault();
		int r, g, b;
		for(int i = 0; i < iw*ih; i++)	
		{			
			r = (int) (contrast>=0?cm.getRed(pix[i]) + (255-cm.getRed(pix[i]))*contrast:
				cm.getRed(pix[i]) + cm.getRed(pix[i])*contrast);
			g = (int) (contrast>=0?cm.getGreen(pix[i]) + (255-cm.getGreen(pix[i]))*contrast:
				cm.getGreen(pix[i]) + cm.getGreen(pix[i])*contrast);
			b = (int) (contrast>=0?cm.getBlue(pix[i]) + (255-cm.getBlue(pix[i]))*contrast:
				cm.getBlue(pix[i]) + cm.getBlue(pix[i])*contrast);
	
			pix[i] = 255 << 24|r << 16|g << 8|b;
			
		}
		System.out.println("contrast is: " + contrast);
		return pix;
	}	
    int y = Color.yellow.getRGB();
    int b = Color.black.getRGB();
    int w = Color.white.getRGB();
    
    private final int pixs[] =
	{
		w, w, w, w, y, y, y, y, y, y, y, y, w, w, w, w,
		w, w, w, y, y, y, y, y, y, y, y, y, y, w, w, w,
		w, w, y, y, y, y, y, y, y, y, y, y, y, y, w, w,
		w, y, y, y, b, b, y, y, y, y, b, b, y, y, y, w,
		y, y, y, y, b, b, y, y, y, y, b, b, y, y, y, y,
		y, y, y, y, y, y, y, y, y, y, y, y, y, y, y, y,
		y, y, y, y, y, y, y, y, y, y, y, y, y, y, y, y,
		y, y, y, y, y, y, y, y, y, y, y, y, y, y, y, y,
		y, y, y, y, y, y, y, y, y, y, y, y, y, y, y, y,
		y, y, y, b, y, y, y, y, y, y, y, y, b, y, y, y,
		y, y, y, y, b, y, y, y, y, y, y, b, y, y, y, y,
		y, y, y, y, y, b, b, y, y, b, b, y, y, y, y, y,
		w, y, y, y, y, y, y, b, b, y, y, y, y, y, y, w,
		w, w, y, y, y, y, y, y, y, y, y, y, y, y, w, w,
		w, w, w, y, y, y, y, y, y, y, y, y, y, w, w, w,
	    w, w, w, w, y, y, y, y, y, y, y, y, w, w, w, w
	};
	
    public void paint(Graphics g) 
    {    	  
        if (iImage != null)
        {
        	g.clearRect(0, 0, 260, 350);        	
            g.drawImage(iImage, 5, 50, null);
            g.drawString("pic1", 120, 320);
        }
              
    }
    
    public void showPix(Graphics graph, int[] pixels, String str)
    {    
		ImageProducer ip = new MemoryImageSource(iw, ih, pixels, 0, iw);
		Image oImage = createImage(ip);
		common.draw(graph, iImage, "pic1", oImage, str);
		runflag = true;
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
    
    public void setPanel(Parameters pp, String s)
    {
    	JPanel buttonsPanel = new JPanel();  
	    okButton     = new JButton("确定");				
        okButton.addActionListener(this);
        
    	dialog = new JDialog(this, s+ " 参数设置", true);     
        
        Container contentPane = getContentPane();
		Container dialogContentPane = dialog.getContentPane();

		dialogContentPane.add(pp, BorderLayout.CENTER);
		dialogContentPane.add(buttonsPanel, BorderLayout.SOUTH);
		dialog.pack();		        
        buttonsPanel.add(okButton);			
       	dialog.setLocation(50,330);
    	dialog.show();
    }
        
    public static void main(String[] args) 
    {  
        new Gray();        
    }
    
    public void setMenu()
    {
        Menu fileMenu = new Menu("File");
        openItem = new MenuItem("Open");
        openItem.addActionListener(this);
        fileMenu.add(openItem);

        exitItem = new MenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);        
        
        Menu processMenu = new Menu("PictureTreatment");
        grayItem = new MenuItem("gray level treatment");
        grayItem.addActionListener(this);
        processMenu.add(grayItem);
        
        processMenu.addSeparator();        
        contrastItem = new MenuItem("contrast level treatment");
        contrastItem.addActionListener(this);
        processMenu.add(contrastItem);        
            
        MenuBar menuBar = new MenuBar();
        menuBar.add(fileMenu);
        menuBar.add(processMenu);
        setMenuBar(menuBar);      
    }
    
    MenuItem openItem;
    MenuItem exitItem;
    MenuItem grayItem;
    MenuItem contrastItem;
}
