
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

class DigitRecog extends JFrame implements ActionListener
{
    int model,       //���ִ���  
        m_num = 0;
    int count;  
        
    int[][][] sample;
    double[][][] Dsample;
    boolean isChooseAlg, //�㷨ѡ���־
            isSaved;     //�����ļ��洢��־
	String  algName;
	
    public DigitRecog() 
    {
    	setTitle("ģʽʶ��");
	    
	    sample  = new int[10][2][25];
	    Dsample = new double[10][2][25];
	    count  = 0;
        model  = 0;
	   	isChooseAlg = false;
	   	
	   	setMenu(); 	  
	                  
	    addWindowListener(new WindowAdapter() 
	    {
		    public void windowClosing(WindowEvent e) 
		    {			   
		        if(isSaved == false)
		            if(check() == 0)	                  
	                    save();           
	            System.exit(0);
		    }
        });	
        
        setSize(368,430);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
	    if(e.getSource() == recognize)        //ʶ��
	    {	    		    	
	    	
	    	result.setText(" "+ recognize());
	    	
		    train.setEnabled(false);		    
            num.setEnabled(false);  	           
            inform.setText("ʹ�ð�ť����ɲ�������");            
		} 
		else if(e.getSource() == clear)
	        clearall();            
        else if(e.getSource() == train)       //ѵ��
        {
        	
        	int[] tem = character();
		        
		    for(int j = 0; j < 25; j++)
		    	sample[model][m_num][j] = tem[j];
		    inform.setText("���� "+model+" �ĵ� "+(m_num+1)+
	                       " �����������ѵ��.\n"+
	                       "ÿ������������������.");	        
		    m_num++;
		    m_num = m_num%2;        
		        
		    if(model == 9)
		        recognize.setEnabled(true);
		        clearall();          
        }
        else if(e.getSource() == news)        //�½�����
        {
        	if(isChooseAlg)
	    	{
		        inform.setText("�������������϶�, ���ұ����������д����,"+
		                       "\n"+"��������0 - 9��������");
		        recognize.setEnabled(false);
		        alg.setEditable(false);	        
		        train.setEnabled(true);
		        num.setEnabled(true);
		        clear.setEnabled(true);
		        isSaved = false;
	        }
            else
                JOptionPane.showMessageDialog(null, "����ѡ��ʶ���㷨!"); 
        }
        else if(e.getSource() == save)        //��������
           	save();         	
        else if(e.getSource() == load)        //��������
        {
        	if(isChooseAlg)
	    	{
	        	load();
	        	isSaved = true;
	        	alg.setEnabled(false);
	        	num.setEnabled(false);
	        	inform.setText("�������������϶�, ���ұ����������д����");
        	}
            else
                JOptionPane.showMessageDialog(null, "����ѡ��ʶ���㷨!");         
        }
        else if(e.getSource() == renew)      //�����㷨,��ʼ��
        {
        	count  = 0;
            model  = 0;
        	isChooseAlg = false;        
            alg.setEnabled(true);
            
            recognize.setEnabled(false);
		    clear.setEnabled(false);
		    train.setEnabled(false);
		    num.setEnabled(false);
		    
        }
        else if(e.getSource() == alg)         //�㷨ѡ���
        {   	
            algName = (String)alg.getSelectedItem();
            isChooseAlg = true; 
        }              
        else if(e.getSource() == num)         //ѵ��ѡ���
           	model = Integer.parseInt((String)num.getSelectedItem());	        	        	        	         
        else if(e.getSource() == quit)
	    {
            if(isSaved == false)
                if(check() == 0)
                   	save();                	
	               
            System.exit(0);
        }
    }
    
    //��׼��,��������������ŵ�5X5��׼��С
	int[] character()
	{
		int minx = 1000, miny = 1000, 
	        maxx = 0,    maxy = 0;
	    int ti, tj;
        int[] chr = new int[25];
        
        int[][] temp = can.getXY();       
        
        //�ֱ������С������x[i],y[i]
	    for(int i = 0; i < count; i++)
	    {
	        if(temp[0][i] == -1)  continue;
	        if(temp[0][i] < minx) minx = temp[0][i];
		    if(temp[1][i] < miny) miny = temp[1][i];
		    if(temp[0][i] > maxx) maxx = temp[0][i];
		    if(temp[1][i] > maxy) maxy = temp[1][i];		    
		}
	    int xlen = maxx - minx;
	    int ylen = maxy - miny;
	    	    
	    //���������ŵ���׼��С.������row[i],col[i] ��
	    for(int i = 0; i < count; i++)
	    {
	         if(temp[0][i] == -1) continue;
	         
	         ti = (temp[1][i]-miny)*5/ylen;
	         tj = (temp[0][i]-minx)*5/xlen;
	         if(ti == 5) ti = 4;
		     if(tj == 5) tj = 4;
		     
		     chr[ti*5+tj]++;		                      
		}		
	    return chr;	    
    }
    
    //��׼��,��������������ŵ�5X5��׼��С
	public double[] CHARACTER()
	{
		int minx = 1000, miny = 1000, 
	        maxx = 0,    maxy = 0;
	    int ti, tj;
        int[] chr  = new int[25];
        double[] t = new double[25];
        
        int[][] temp = can.getXY();       
        
        //�ֱ������С������x[i],y[i]
	    for(int i = 0; i < count; i++)
	    {
	        if(temp[0][i] == -1)  continue;
	        if(temp[0][i] < minx) minx = temp[0][i];
		    if(temp[1][i] < miny) miny = temp[1][i];
		    if(temp[0][i] > maxx) maxx = temp[0][i];
		    if(temp[1][i] > maxy) maxy = temp[1][i];		    
		}
	    int xlen = maxx - minx;
	    int ylen = maxy - miny;
	    	    
	    //���������ŵ���׼��С.������row[i],col[i] ��
	    for(int i = 0; i < count; i++)
	    {
	         if(temp[0][i] == -1) continue;
	         
	         ti = (temp[1][i]-miny)*5/ylen;
	         tj = (temp[0][i]-minx)*5/xlen;
	         if(ti == 5) ti = 4;
		     if(tj == 5) tj = 4;
		     
		     chr[ti*5+tj]++;		                      
		}
		int w = xlen/5;
		int h = ylen/5;
		for(int i = 0; i < 5; i++)
		    for(int j = 0; j < 5; j++)
		        t[i*5+j] = chr[i*5+j]*7.0/(w*h);		
	    return t;	    
    }
    //ģ��ƥ���㷨����
    int recognize()
    {
    	int[][] distance = new int[10][2];
    	int[]   tem = character();
        int n = 0;
        
        for(int k = 0; k < 10; k++)
        {
        	for(int i = 0; i < 2; i++)
        	{        	
        	    distance[k][i] = 0;
	            for(int j = 0; j < 25; j++)
	                distance[k][i] += Math.abs(sample[k][i][j]-tem[j]);	
	        }            
	    }
	     
	    //����distance[k][i]����С�ߺ���Ӧ�����k
	    for(int k = 1; k < 10; k++)
	      	for(int i = 0; i < 2; i++)
	            if(distance[k][i] < distance[n][i])
	                n = k;		        
	    return n;		     
    }
    
    
    //�洢����
    public void save()
    {
    	FileDialog fd = new FileDialog(this,"�����ļ�",FileDialog.SAVE);
    	fd.setVisible(true);
    	try
	    {
	    	if(fd.getFile() != null)
            {
			    RandomAccessFile rf = new RandomAccessFile(fd.getFile(), "rw");
				    for(int k = 0; k < 10; k++)
				        for(int i = 0; i < 2; i++)
				       	    for(int j = 0; j < 25; j++)
				       	        rf.writeInt(sample[k][i][j]);                                        
                
                rf.close();
                isSaved = true;    	
	            inform.setText("�ڵ�ǰĿ¼�洢�ļ�"+fd.getFile()+"�ɹ�!");	               
			}						
		}			
	    catch(FileNotFoundException e1)
	    {
	        System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
	        System.err.println(e2);
	    }	     
    }
    
    //��������
    public void load()
    {
    	FileDialog fd = new FileDialog(this, "���ļ�", FileDialog.LOAD);
    	fd.setVisible(true);
    	try
	    {
	    	if(fd.getFile() != null)
            {
            	FileInputStream fin = new FileInputStream(fd.getFile());			  	    
	  	        DataInputStream  in = new DataInputStream(fin);
				    for(int k = 0; k < 10; k++)        //10������
				        for(int i = 0; i < 2; i++)     //2������
				       	    for(int j = 0; j < 25; j++)
				       	        sample[k][i][j] = in.readInt();
				
				       	                                         
                in.close();
                recognize.setEnabled(true);
		        clear.setEnabled(true);
		        train.setEnabled(true);
		        num.setEnabled(true);	               
			}						
		}			
	    catch(FileNotFoundException e1)
	    {
	        System.err.println(e1);
	    }
	    catch(IOException e2)
	    {
	        System.err.println(e2);
	    }	        	
    }
    
    void clearall()
    {
        count = 0;
        can.repaint();
        result.setText(" ");
    }
    
    int check()
	{
		String s = "�Ƿ�Ҫ�������޸ĵ������������ļ�?";
        String t = "��ʾ";       	               
        int tem  = JOptionPane.showConfirmDialog(new JFrame(), s, t, 0);
        return tem;
	}	   
    
	class myCanvas extends Canvas implements MouseListener,MouseMotionListener
	{
		int[][] pos = new int[2][500];//pos[0][]=x[],pos[1][]=y[]	  
	    myCanvas()
	    {
	        addMouseListener(this);
	        addMouseMotionListener(this);
	        setBackground(Color.white);
	    }
	  
	    public void paint(Graphics g)
	    {
	        g.setColor(Color.red);
	        for(int i = 0; i < count-1; i++)
	            if(pos[0][i+1] == -1)
	            {
	                i++;
	                continue;
	            }
	            else
	                g.drawLine(pos[0][i], pos[1][i], pos[0][i+1], pos[1][i+1]);        
	    }
	    
	    public int[][] getXY()
	    {
	    	return pos;
	    }
	    
	    public void mouseClicked(MouseEvent me){}
	    
	    public void mousePressed(MouseEvent me)
	    {
	        pos[0][count] = me.getX();
	        pos[1][count] = me.getY();
	        count++;
	    }
	    public void mouseReleased(MouseEvent me)
	    {
	        pos[0][count] = -1;//��ǽ���
	        pos[1][count] = -1;
	        count++;        
	    }
	    public void mouseEntered(MouseEvent me){}
	    public void mouseExited(MouseEvent me){}
	    public void mouseDragged(MouseEvent me)
	    {
	        pos[0][count] = me.getX();
	        pos[1][count] = me.getY();
	        count++;
	        repaint();
	    }
	    public void mouseMoved(MouseEvent me){}
    }
   
    
    public static void main(String args[]) 
	{
		new DigitRecog();
	}
	
	public void setMenu() 	  
	{    
	    JMenuBar Bar = new JMenuBar();
        JMenu file  = new JMenu("�ļ�");
        Bar.add(file);
        setJMenuBar(Bar);
        
        load = new JMenuItem("��������");
        load.addActionListener(this);
        file.add(load);
                
        news = new JMenuItem("�½�����");
        news.addActionListener(this);
        file.add(news);
                
        save = new JMenuItem("��������");
        save.addActionListener(this);
        file.add(save);
        file.addSeparator();
        
        renew = new JMenuItem("�����㷨");
        renew.addActionListener(this);
        file.add(renew);
        file.addSeparator();
              
        quit = new JMenuItem("�˳�");
        quit.addActionListener(this);
        file.add(quit);
        
        can = new myCanvas();
	    Container con = getContentPane();
	    con.setLayout(null);
	    
	    JPanel canpan = new JPanel();
	    canpan.setLayout(null);
	    canpan.add(can);
	    can.setBounds(20, 30, 160, 200);
	    canpan.setBorder(BorderFactory.createTitledBorder("��д���������"));
	    canpan.setBounds(150, 20, 200, 250);
	    con.add(canpan);
	    
	    //ʶ�����
	    JPanel recogpan = new JPanel();
        con.add(recogpan);
        recogpan.setLayout(null);
        recogpan.setBounds(10, 20, 130, 120);
        recogpan.setBorder(BorderFactory.createTitledBorder("ʶ����ʾ"));
            	    
	    result = new JTextArea();           
	    result.setEditable(false);
	    result.setBounds(10, 25, 110, 25);
	    recogpan.add(result);
	  	
	   	String[] s = {"ģ��ƥ���㷨"};
	    alg = new JComboBox(s);
        alg.setBounds(10, 55, 110, 25);
        alg.setEnabled(true);
        alg.addActionListener(this);  
        recogpan.add(alg); 
	  	  	    
	    recognize = new JButton("ʶ ��");
	    recognize.setBounds(10, 85, 110, 25);
	    recognize.setEnabled(false);
        recognize.addActionListener(this);
	    recogpan.add(recognize);
	    	    
	    //ѵ�����
	    JPanel trainpan = new JPanel();
        con.add(trainpan);
        trainpan.setLayout(null);
        trainpan.setBounds(10, 180, 130, 90);
        trainpan.setBorder(BorderFactory.createTitledBorder("ѵ������"));
            	    
	    String[] st = {"0","1","2","3","4","5","6","7","8","9"};
	    num = new JComboBox(st);
        num.setBounds(10, 25, 110, 25);
        num.setEnabled(false);
        num.addActionListener(this);  
        trainpan.add(num);  
	    
	    train = new JButton("ѵ ��");
	    train.setBounds(10, 55, 110, 25);
	    train.setEnabled(false);
        train.addActionListener(this);
	    trainpan.add(train);
	            
        clear = new JButton("�� ��");
	    clear.setBounds(10, 148, 130, 25);
	    clear.setEnabled(false);
        clear.addActionListener(this);         
	    con.add(clear);
	    	   
	    JPanel inforpan = new JPanel();          
        inforpan.setBounds(10, 280, 340, 80);
        inforpan.setBorder(BorderFactory.createTitledBorder("��Ϣ��ʾ"));
        inform = new JTextArea("��ѡ��ʶ���㷨, Ȼ��򿪲˵�"
               + "[�ļ�]/[��������]��[�½�����]", 2, 28);
        inforpan.add(inform);
        con.add(inforpan);
    }
    
    JButton recognize;
    JButton train;
    JButton clear;
      
    JComboBox alg, num;
    myCanvas can;
    
    JMenuItem load;    
    JMenuItem news;  
    JMenuItem save;    
    JMenuItem renew;
    JMenuItem quit;
    
    JTextArea result, inform;	
}  	