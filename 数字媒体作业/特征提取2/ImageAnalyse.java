
package algorithms;

public class ImageAnalyse
{ 
    /*----------------------------------------------------
     * 轮廓跟踪和规范链码编码
     * 输入:
     * bw[]      --二值图像矩阵
     * iw, ih    --二值图像宽,高
     * 输出:
     * int[]     --二值图像序列,链码和规范链码起始点坐标
     *----------------------------------------------------*/
    
	
	//将ARGB图像序列pixels变为2值图像序列im
	public byte[] toBinary(int[] pix, int iw, int ih,int th )
	{
		byte[] im = new byte[iw*ih];
		for(int i = 0; i < iw*ih; i++)							
			if((pix[i]&0xff) > th) 
				im[i] = 0;   //背景色为0
			else
			    im[i] = 1;   //前景色为1			
		return im;
	}
	
    //边界提取
    public byte[] Bound(byte bw[], int iw, int ih)
    {	
		int p, r;
		
		byte[] tem = new byte[iw*ih];
		for(int j = 0;j < ih; j++)
		    for(int i = 0; i < iw; i++)		    
		        tem[i+j*iw] = bw[i+j*iw];
				
		for(int j = 1; j < ih - 1; j++)
		{
			for (int i = 1; i < iw - 1; i++)
			{	
				p = bw[i+j*iw];
						
				if(p == 0)//如果当前象素是白色, 进入下一个循环
					continue;
				else//当前象素是黑色
				{
					// 检查周边的8-连通域
					r = 1;
					for(int k =-1;k<2;k++)
					{
					    for(int l=-1;l<2;l++)
					    {					    
					        if(bw[i+k+(j+l)*iw] == 0)
					        {					        
					            r = 0;
					            k = 2; l = 2;//跳出2重循环
					        }
					    }
					}
										
					//如果都是黑点,判定为内部点,改变颜色
					if(r == 1)
						tem[i+j*iw] = 0;					
				}
			}
		}
		return tem;				
	}
    
    //将2值图像序列bw变为RGB图像序列pix
	public int[] bin2Rgb(byte[] bw, int iw, int ih)
	{
		int r, g, b;
		int[] pix = new int[iw * ih];
		
	    for(int i = 0; i < iw*ih; i++) 
		{		
			if(bw[i] == 0)	    {r = 255; g= 255;b = 255;}
			else if(bw[i] == 1)	{r = 0;   g = 0; b = 0;}
			else				{r = 255; g = 0; b = 0;}				
			pix[i] = (255<<24)|(r<<16)|(g<<8)|b;			
		}
		return pix;	
	}	
	//计算7个不变矩
    public double[] Moment7(byte bw[], int iw, int ih)
    {
    	int i,j;
		double ic, jc;
		double m00, m10, m01, m20, m11, m02, m30, m21, m12, m03;
		
		double[] invmom = new double[7];
		 	
		m00 = 0; m10 = 0; m01 = 0; m20 = 0; m11 = 0; 
		m02 = 0; m30 = 0; m21 = 0; m12 = 0; m03 = 0;
		
		//计算0-3阶矩
		for(j = 0; j < ih; j++)
		{
			for (i = 0; i < iw; i++)
	        {
	        	if(bw[i+j*iw]==0) continue;
	        	
	        	//计算0阶矩
				m00 += bw[i+j*iw];
				
				//计算1阶矩	
				m10 += i*bw[i+j*iw];
				m01 += j*bw[i+j*iw];
				
				//计算2阶矩
				m20 += i*i*bw[i+j*iw];
				m11 += i*j*bw[i+j*iw];
				m02 += j*j*bw[i+j*iw];
				
				//计算3阶矩
				m30 += i*i*i*bw[i+j*iw];
				m21 += i*i*j*bw[i+j*iw];
				m12 += i*j*j*bw[i+j*iw];
				m03 += j*j*j*bw[i+j*iw];
			}
		}
				
		//计算形心坐标
		ic = m10/m00;
		jc = m01/m00;
		
		//计算mu
		double mu11 = m11-jc*m10;
		double mu20 = m20-ic*m10;
		double mu02 = m02-jc*m01;
		double mu30 = m30-3*ic*m20+2*ic*ic*m10;
		double mu21 = m21-2*ic*m11-jc*m20+2*ic*ic*m01;
		double mu12 = m12-2*jc*m11-ic*m02+2*jc*jc*m10;
		double mu03 = m03-3*jc*m02+2*jc*jc*m01;
		
		//计算eta
		double sqrtm00 = Math.sqrt(m00);
		double eta20 = mu20/(m00*m00);
		double eta11 = mu11/(m00*m00);
		double eta02 = mu02/(m00*m00);
		double eta30 = mu30/(m00*m00*sqrtm00);
		double eta21 = mu21/(m00*m00*sqrtm00);
		double eta12 = mu12/(m00*m00*sqrtm00);
		double eta03 = mu03/(m00*m00*sqrtm00);
		
		//计算7个不变矩
		invmom[0] = eta20+eta02;
		invmom[1] = (eta20-eta02)*(eta20-eta02)
		            + 4*eta11 *eta11;
		invmom[2] = (eta30-3*eta12) *(eta30-3*eta12)
		            + (3*eta21-eta03) *(3*eta21-eta03);
		invmom[3] = (eta30+eta12)     *(eta30+eta12)
		            + (eta21+eta03)   *(eta21+eta03);
		invmom[4] = (eta30-3*eta12)   *(eta30+eta12) 
		            * ((eta30  +eta12)*(eta30+eta12) 
		            - 3*(eta21 +eta03)*(eta21+eta03))
		            + (3*eta21 -eta03)*(eta21+eta03) 
		            * (3*(eta30+eta12)*(eta30+eta12) 
		            - (eta21   +eta03)*(eta21+eta03));	
		invmom[5] = (eta20-eta02)     *((eta30+eta12)*(eta30+eta12)
		            - (eta21 +eta03)  *(eta21+eta03))
		            + 4*eta11*(eta30+eta12)*(eta21+eta03);
		invmom[6] = (3*eta21   -eta03)*(eta30+eta12)
		            * ((eta30  +eta12)*(eta30+eta12)
		            - 3*(eta21 +eta03)*(eta21+eta03))
		            + (3*eta12 -eta30)*(eta21+eta03)
		            * (3*(eta30+eta12)*(eta30+eta12)
		            - (eta21+eta03)   *(eta21+eta03)); 
		return invmom;
    }
}             