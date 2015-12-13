//**
     * �����ֵ�ָ�
     * @param pix
     * @param w
     * @param h
     * @return
     */
	public int bestThresh(int[] pix, int w, int h)
	{
		int i, j, thresh, newthresh,
			gmax, gmin;         //���,��С�Ҷ�ֵ
        double[] p = new double[256];
	    int[][] im = new int[w][h];
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
				
        for (i = 0; i < 256; i++)
            p[i] = 0;
        
        //1.ͳ�Ƹ��Ҷȼ����ֵĴ������Ҷ�������Сֵ
        gmax = 0;
        gmin =255;
        //����ͼ���������С�Ҷ�ֵ
        for (j = 0; j < h; j++){
            for (i = 0; i < w; i++){
            	int g = im[i][j];
                p[g]++;
                if(g > gmax) gmax = g;
                if(g < gmin) gmin = g;
            }
        }
        
        thresh = 0;
        //��ֵ��ʼֵ
        newthresh = (gmax+gmin)/2;
        
        int meangray1,meangray2;
        long p1, p2, s1, s2;
        //������ѵ���ֵ=Ŀ��Ҷ�ƽ��ֵ+�����Ҷ�ƽ��ֵ
        for(i = 0; (thresh!=newthresh)&&(i<100);i++){
        	thresh = newthresh;
        	p1 = 0; p2 = 0; s1 = 0; s2 = 0;
        	
        	//2. ����������ĻҶ�ƽ��ֵ
        	for(j = gmin; j < thresh;j++){//����С�ڳ�ʼ��ֵ�ĻҶ�ƽ��ֵ
        		p1 += p[j]*j;//�����ܵĻҶ�ֵ=����*�Ҷ�ֵ
        		s1 += p[j];  //�������      		
        	}
        	meangray1 = (int)(p1/s1);//Ŀ���ƽ���Ҷ�ֵ
        	
        	for(j = thresh+1; j < gmax; j++){//������ڳ�ʼ��ֵ��ƽ���Ҷ�ֵ��Ŀ��ƽ���Ҷ�ֵ
        		p2 += p[j]*j;
        		s2 += p[j];        		
        	}
        	meangray2 = (int)(p2/s2);//����ƽ���Ҷ�ֵ
        	//3. ��������ֵ
        	newthresh = (meangray1+meangray2)/2; 	
        }
        return newthresh;
	}