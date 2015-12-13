//**
     * 最佳阈值分割
     * @param pix
     * @param w
     * @param h
     * @return
     */
	public int bestThresh(int[] pix, int w, int h)
	{
		int i, j, thresh, newthresh,
			gmax, gmin;         //最大,最小灰度值
        double[] p = new double[256];
	    int[][] im = new int[w][h];
		for(j = 0; j < h; j++)
		    for(i = 0; i < w; i++)		    
				im[i][j] = pix[i+j*w]&0xff;
				
        for (i = 0; i < 256; i++)
            p[i] = 0;
        
        //1.统计各灰度级出现的次数、灰度最大和最小值
        gmax = 0;
        gmin =255;
        //计算图像的最大和最小灰度值
        for (j = 0; j < h; j++){
            for (i = 0; i < w; i++){
            	int g = im[i][j];
                p[g]++;
                if(g > gmax) gmax = g;
                if(g < gmin) gmin = g;
            }
        }
        
        thresh = 0;
        //阈值初始值
        newthresh = (gmax+gmin)/2;
        
        int meangray1,meangray2;
        long p1, p2, s1, s2;
        //计算最佳的阈值=目标灰度平均值+背景灰度平均值
        for(i = 0; (thresh!=newthresh)&&(i<100);i++){
        	thresh = newthresh;
        	p1 = 0; p2 = 0; s1 = 0; s2 = 0;
        	
        	//2. 求两个区域的灰度平均值
        	for(j = gmin; j < thresh;j++){//计算小于初始阈值的灰度平均值
        		p1 += p[j]*j;//计算总的灰度值=个数*灰度值
        		s1 += p[j];  //计算个数      		
        	}
        	meangray1 = (int)(p1/s1);//目标的平均灰度值
        	
        	for(j = thresh+1; j < gmax; j++){//计算大于初始阈值的平均灰度值，目标平均灰度值
        		p2 += p[j]*j;
        		s2 += p[j];        		
        	}
        	meangray2 = (int)(p2/s2);//背景平均灰度值
        	//3. 计算新阈值
        	newthresh = (meangray1+meangray2)/2; 	
        }
        return newthresh;
	}