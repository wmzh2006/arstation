/*******************************************************************************
 * Copyright (C) 1998-2009 puyu Inc.All Rights Reserved. FileName: Tools.java
 * Description： History： 版本号 作者 日期 简要介绍相关操作 1.0 sunny 2009-8-5 Create
 *******************************************************************************/
package  com.funoble.myarstation.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;

import com.funoble.myarstation.game.MyArStation;

/**
 * <p>
 * 工具类
 * </p>
 */
public class Util
{
    public static SimpleDateFormat dateTimeNoYearSdf = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * 时间的文字表述
     * 
     * @param date
     * @return
     */
    public static String getTimeDesc(Date date)
    {
        long diff = System.currentTimeMillis() - date.getTime();

        if(diff < 0)
            return "刚刚";

        int days = 0;
        if(diff > 86400000)
        {
            days = (int) diff / 86400000;
        }

        if(days > 10)
        {
            return dateTimeNoYearSdf.format(date);
        }
        else if(days > 0)
        {
            return days + "天前";
        }
        else if(days == 0)
        {
            int hours = (int) diff / 3600000;
            if(hours > 0)
            {
                return hours + "小时前";
            }
            else if(hours == 0)
            {
                int mins = (int) diff / 60000;
                if(mins > 0)
                {
                    return mins + "分钟前";
                }
                else
                {
                    return "刚刚";
                }
            }
        }
        return "刚刚";
    }

    /**
     * 把时间转换为字符串
     * @param time
     * @return 
     */
    public static String TimetoString(long seconds) {
    	Date date = new Date(seconds * 1000);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String StrTime = sdf.format(date);
    	return StrTime;
    }
    /**
     * 把时间转换为字符串
     * @param time
     * @return 
     */
    public static String TimetoString(long seconds, String format) {
    	Date date = new Date(seconds * 1000);
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	String StrTime = sdf.format(date);
    	return StrTime;
    }
    
    /**
     * 把long转换为unsigned int
     */
    public static int convert2Unsigned(long data)
    {
        int unsigned = (int) data;
        if(data < 4294967296L && data > 2147483647)
        {
            unsigned = (int) (data - 4294967296L);
        }
        return unsigned;
    }

    public static long convertUnsigned2Long(int data)
    {
        long l = (long) data;
        if(data < 0)
        {
            l = 4294967296L + data;
        }
        return l;
    }

    /**
     * 验证是否为QQ号
     * 
     * @param uin
     * @return 返回-1表示非法号码，否则返回该QQ号
     */
    public static long checkQQNum(long uin)
    {
        if(uin > 10000 && uin <= 4294967296L)
        {
            return uin;
        }
        else
        {
            return -1;
        }
    }

    /**
     * 验证是否为QQ号
     * 
     * @param uin
     * @return 返回-1表示非法号码，否则返回该QQ号
     */
    public static long checkQQNum(String strUin)
    {
        long uin = 0;
        try
        {
            uin = Long.parseLong(strUin);
        }
        catch(Exception e)
        {
            return -1L;
        }
        return checkQQNum(uin);
    }

    /**
     * 验证密码是否合法
     * 
     * @param uin
     * @return
     */
    public static boolean isQQPsw(String psw)
    {
        if(psw != null && psw.length() >= 0 && psw.length() <= 16)
            return true;
        else
            return false;
    }

    /**
     * 作用：
     * <p>
     * 返回当前时间戳值
     * </p>
     * 
     * @return
     */
    public static long getCurrentTimeStamp()
    {
        return new Date().getTime();
    }

    /**
     * 让线程睡觉 timeLong毫秒
     * 
     * @param timeLong
     */
    public static void threadSleep(int timeLong)
    {
        try
        {
            Thread.sleep(timeLong);
        }
        catch(InterruptedException ex)
        {
        }
    }

    /**
     * X轴镜像
     */
    public static final byte MIRROR_X = 1;
    /**
     * Y轴镜像
     */
    public static final byte MIRROR_Y = 2;

    /**
     * 镜像图片
     * 
     * @param pixels
     * @param width
     * @param height
     * @param type
     *            镜像方式：(MIRROR_X or MIRROR_Y)
     * @return
     */
    public static int[] effect_mirror(int[] pixels, int width, int height, byte type)
    {
        if(pixels == null || width <= 0 || height <= 0)
        {
            return null;
        }
        int len = pixels.length;
        int temp;
        if(type == MIRROR_X)
        {
            int half_len = len / 2;
            for(int i = 0; i < half_len; i++)/* 反转:右下 */
            {
                temp = pixels[i];
                pixels[i] = pixels[len - 1 - i];
                pixels[len - 1 - i] = temp;
            }
        }
        else
        {
            for(int i = 0; i < height; i++)
            {
                len = (i + 1) * width;
                for(int ii = 0; ii < width / 2; ii++)
                {
                    temp = pixels[i * width + ii];
                    pixels[i * width + ii] = pixels[len - 1 - ii];
                    pixels[len - 1 - ii] = temp;
                }
            }
        }

        return pixels;

    }

    /**
     * 获取过渡色列表------------------------------------------------------------------
     * -- ----------
     * 
     * @param nums
     *            : 过渡色填充的距离 (nums>=2)
     * @param baseColor
     *            ：基础色(16进制int)
     * @param armColor
     *            : 目标色(16进制int)
     * @param return：int[nums] 16进制int的数组如0xf9aa66。
     */
    public static int[] getTransColor(int nums, int baseColor, int armColor)
    {
        int initAlpha = baseColor >> 24;
        int initR = baseColor >> 16;
        int initG = (baseColor >> 8) & 0x00FF;
        int initB = baseColor & 0x0000FF;

        int toR = armColor >> 16;
        int toG = (armColor >> 8) & 0x00FF;
        int toB = armColor & 0x0000FF;

        int sR = ((toR - initR) * 100) / (nums - 2);
        /** 模拟两位浮点运算 */
        int sG = ((toG - initG) * 100) / (nums - 2);
        int sB = ((toB - initB) * 100) / (nums - 2);

        int[] tranC = new int[nums];
        tranC[0] = baseColor;
        tranC[nums - 1] = armColor;
        for(int i = 1; i < nums - 1; i++)
        {
            tranC[i] = (initAlpha << 24) | ((((initR + (i * sR) / 100) | 0x000000) << 16))
                    | (((initG + (i * sG) / 100) | 0x0000) << 8) | (initB + (i * sB) / 100);
        }
        return tranC;
    }

    /**
     * 作用：
     * <p>
     * 将srcArray数组的内容填充到destArray数组内
     * </p>
     * 
     * @param destArray
     *            要填充的数组
     * @param offset
     *            偏移位置
     * @param srcArray
     *            填充的内容
     * @param size
     *            填充内容的长度
     */
    public static void copyData(byte[] destArray, int offset, byte[] srcArray, int size)
    {
        for(int i = 0; i < size; i++)
        {
            destArray[offset + i] = srcArray[i];
        }
    }

    public static void getByteFromChars(byte[] to, int toIndex, char[] from, int iStart, int iLen)
    {
        for(int i = 0; i < iLen; i++)
        {
            word2Byte(to, i * 2 + toIndex, (short) from[i + iStart]);
        }
    }

    /**
     * 作用：
     * <p>
     * 将short数据【从高位到低位】填充到destArray数组内
     * </p>
     * 
     * @param destArray
     *            目标数组
     * @param offset
     *            偏移位
     * @param num
     *            待填充的字符型
     */
    public static void word2Byte(byte[] destArray, int offset, short num)
    {
        destArray[offset] = (byte) (num >> 8);
        destArray[offset + 1] = (byte) (num);
    }

    /**
     * 作用：
     * <p>
     * 将int数据【从高位到低位】填充到destArray数组内
     * </p>
     * 
     * @param destArray
     *            目标数组
     * @param offset
     *            偏移位
     * @param num
     *            待填充的整型
     */
    public static void word2Byte(byte[] destArray, int offset, int num)
    {
        destArray[offset] = (byte) (num >> 24);
        destArray[offset + 1] = (byte) (num >> 16);
        destArray[offset + 2] = (byte) (num >> 8);
        destArray[offset + 3] = (byte) (num);
    }

    /**
     * 作用：
     * <p>
     * 将long数据【从高位到低位】填充到destArray数组内
     * </p>
     * 
     * @param destArray
     *            目标数组
     * @param offset
     *            偏移位
     * @param num
     *            待填充的长整型
     */
    public static void word2Byte(byte[] destArray, int offset, long num)
    {
        destArray[offset] = (byte) (num >> 56);
        destArray[offset + 1] = (byte) (num >> 48);
        destArray[offset + 2] = (byte) (num >> 40);
        destArray[offset + 3] = (byte) (num >> 32);
        destArray[offset + 4] = (byte) (num >> 24);
        destArray[offset + 5] = (byte) (num >> 16);
        destArray[offset + 6] = (byte) (num >> 8);
        destArray[offset + 7] = (byte) (num);
    }

    /**
     * 在字节流中从fromIndex位置【按从高到低位】提取short类型数据
     * 
     * @param from
     * @param fromIndex
     * @return
     */
    public static short getShortData(byte[] from, int fromIndex)
    {
        long result = 0;
        long temp = 0;
        if(from[fromIndex] >= 0)
            temp = (long) from[fromIndex];
        else
            temp = (long) (256 + from[fromIndex]);

        result = temp;
        result = result << 8;

        if(from[fromIndex + 1] >= 0)
            temp = (long) from[fromIndex + 1];
        else
            temp = (long) (256 + from[fromIndex + 1]);
        result += temp;
        return (short) result;
    }

    public static int getIntData(byte[] from, int fromIndex)
    {
        long result;
        long temp = 0;

        if(from[fromIndex] >= 0)
            temp = (long) from[fromIndex];
        else
            temp = (long) (256 + from[fromIndex]);
        result = temp;
        result = result << 8;

        if(from[fromIndex + 1] >= 0)
            temp = (long) from[fromIndex + 1];
        else
            temp = (long) (256 + from[fromIndex + 1]);
        result += temp;
        result = result << 8;

        if(from[fromIndex + 2] >= 0)
            temp = (long) from[fromIndex + 2];
        else
            temp = (long) (256 + from[fromIndex + 2]);
        result += temp;
        result = result << 8;

        if(from[fromIndex + 3] >= 0)
            temp = (long) from[fromIndex + 3];
        else
            temp = (long) (256 + from[fromIndex + 3]);
        result += temp;

        return (int) result;
    }

    public static long getLongData(byte[] from, int fromIndex)
    {
        long result;
        long temp = 0;

        if(from[fromIndex] >= 0)
            temp = (long) from[fromIndex];
        else
            temp = (long) (256 + from[fromIndex]);
        result = temp;
        result = result << 8;

        if(from[fromIndex + 1] >= 0)
            temp = (long) from[fromIndex + 1];
        else
            temp = (long) (256 + from[fromIndex + 1]);
        result += temp;
        result = result << 8;

        if(from[fromIndex + 2] >= 0)
            temp = (long) from[fromIndex + 2];
        else
            temp = (long) (256 + from[fromIndex + 2]);
        result += temp;
        result = result << 8;

        if(from[fromIndex + 3] >= 0)
            temp = (long) from[fromIndex + 3];
        else
            temp = (long) (256 + from[fromIndex + 3]);
        result += temp;

        return result;
    }

    public static short getHShortData(byte[] from, int fromIndex)
    {
        long result = 0;
        long temp = 0;
        try
        {
            if(from[fromIndex + 1] >= 0)
            {
                temp = (long) from[fromIndex + 1];
            }
            else
            {
                temp = (long) (256 + from[fromIndex + 1]);

            }
            result = temp;
            result = result << 8;

            if(from[fromIndex] >= 0)
            {
                temp = (long) from[fromIndex];
            }
            else
            {
                temp = (long) (256 + from[fromIndex]);
            }
            result += temp;
            return (short) result;
        }
        catch(Exception e)
        {
            return 42;
        }
    }

    public static void getBytesData(byte[] from, int fromIndex, byte[] to, int size)
    {
        for(int i = 0; i < size; i++)
        {
            to[i] = from[fromIndex + i];
        }
    }

    /**
     * 作用：
     * <p>
     * 替换字符串
     * </p>
     * 
     * @param sb
     * @param srcStr
     * @param destStr
     * @return
     */
    public static StringBuffer replace(StringBuffer sb, String srcStr, String destStr)
    {
        if(sb == null || srcStr == null || destStr == null)
        {
            return sb;
        }
        int startIndex = sb.indexOf(srcStr);
        while(startIndex != -1)
        {
            sb.replace(startIndex, startIndex + srcStr.length(), destStr);
            startIndex = sb.indexOf(srcStr);
        }

        return sb;
    }


    /**
     * 从制定流内取得String串
     * 
     * @param dis
     * @param nameL
     * @return
     */
    public static String getString(DataInputStream dis, short nameL)
    {
        short tempLong = (short) (nameL);
        if(tempLong <= 0)
            return "";

        byte[] nameByte = new byte[tempLong];
        try
        {
            for(int i = 0; i < tempLong; i++)
            {
                nameByte[i] = dis.readByte();
            }
        }
        catch(IOException e)
        {

        }

        StringBuffer ss = new StringBuffer();
        char[] cc = new char[tempLong / 2];
        getNetCharsData(nameByte, 0, cc, nameByte.length / 2);

        for(int i = 0; i < cc.length; i++)
        {
            if(cc[i] != '\0')
                ss.append(cc[i]);
        }
        return ss.toString();
    }

    private static void getNetCharsData(byte[] from, int fromIndex, char[] to, int size)
    {
        try
        {
            short nSeq = Util.getShortData(from, fromIndex);
            if(nSeq == -1)
            { // net seq
                for(int i = 1; i < size; i++)
                {
                    to[i - 1] = (char) Util.getShortData(from, fromIndex + i * 2);
                }
            }
            else if(nSeq == -2)
            { // host seq
                for(int i = 1; i < size; i++)
                {
                    to[i - 1] = (char) Util.getHShortData(from, fromIndex + i * 2);
                }
            }
            else
            {
                for(int i = 0; i < size; i++)
                {
                    to[i] = (char) Util.getHShortData(from, fromIndex + i * 2);
                }
            }
            if(size > 1)
            {
                to[size - 1] = 0;
            }
        }
        catch(Exception e)
        {
            for(int i = 0; i < size; i++)
            {
                to[i] = '*';
            }
        }

    }

    /**
     * 从字符串数组中寻找指定的字符串的索引
     * 
     * @param value
     * @param array
     * @return
     */
    public static int findIndexInArray(String value, String array[])
    {
        if(array == null || value == null)
        {
            return -1;
        }
        for(int i = 0; i < array.length; i++)
        {
            if(array[i].equals(value))
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * 断行函数
     * 
     * @string ：源字符串
     * @nomOfCharacter : 屏幕宽度
     */
    public static String[] breakLine(String string, int nomOfCharacter)
    {
        if(string == null)
            return new String[]
            {
                ""
            };
        if(nomOfCharacter >= string.length())
            return new String[]
            {
                string
            };

        Vector<String> tempvec = new Vector<String>();
        int index = 0;
        do
        {
            if(index + nomOfCharacter >= string.length())
            {
                tempvec.add(string.substring(index, string.length()));
                break;
            }
            tempvec.add(string.substring(index, index + nomOfCharacter));
            index += nomOfCharacter;
        }
        while(index < string.length());
        String[] temp = new String[tempvec.size()];
        tempvec.copyInto(temp);
        return temp;
    }
    
    /**
     * 根据url获取文件名
     * @param url
     * @return
     */
    public static String getPicNameByUrlPath(String url)
    {
        if(url != null && url.length() > 0)
        {
            return url.substring(url.lastIndexOf("/")+1, url.length());
        }
        return null;
    }
    
    /**
     * 验证密码是否合法
     * 
     * @param pswLen
     * @return
     */
    public static boolean isQQPsw(int pswLen)
    {
      if(pswLen >= 0 && pswLen <= 16)
        return true;
      else
        return false;
    }
    
    /**
     * 检测字符串是否包含非法字符
     * @param key
     * @return
     */
    public static boolean isHaveInvalidedChars(String key)
    {
    	if (key != null && key.length() > 0)
    	{
    		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\b\f\n\r\t\\s*]";
    		Pattern p = Pattern.compile(regEx);
    		Matcher m = p.matcher(key);
    		if (m != null && m.find())
    		{
    			return true;
    		}    		
    	}
		
		return false;
    }
    
    public static boolean inputValidName(String name) {
    	String regEx = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{2,6}$";
		Pattern p = Pattern.compile(regEx);
		return p.matcher(name).find();
	}
    
    /**
     * 过滤非法字符
     * @param key
     * @return
     */
    public static String filterInvalidedChars(String key)
    {
    	if (key != null && key.length() > 0)
    	{
    		String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\b\f\n\r\t]";
    		Pattern p = Pattern.compile(regEx);
    		Matcher m = p.matcher(key);
    		key = m.replaceAll(""); 
    	}
		
		return key;
    }
       
    /**

  　　* Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。

  　　* @param src byte[] data

  　　* @return hex string

  　　*/
    public static String bytesToHexString(byte[] src) {
    	StringBuilder stringBuilder = new StringBuilder("");
    	if (src == null || src.length <= 0) {
    		return null;
    	}
    	for (int i = 0; i < src.length; i++) {
    		int v = src[i] & 0xFF;
    		String hv = Integer.toHexString(v);
    		if (hv.length() < 2) {
    			stringBuilder.append(0);
    		}
    		stringBuilder.append(hv);
    	}
    	return stringBuilder.toString();
    }
    
	public static String getPhoneStatus() {
		StringBuilder phoneStatus = new StringBuilder();
		DisplayMetrics dm = new DisplayMetrics();  
		dm = new DisplayMetrics();  
		MyArStation.mGameMain.getWindowManager().getDefaultDisplay().getMetrics(dm);  
		  
		int screenWidthDip = dm.widthPixels;       
		int screenHeightDip = dm.heightPixels;    
		phoneStatus.append("-" + Build.MODEL).append("-" +screenWidthDip + "-").append(screenHeightDip);
		Tools.debug(phoneStatus.toString());
		return phoneStatus.toString();
	}
	
	public static Long int2Long(int aSrc) {
		long dst = aSrc;
		if(dst < 0) {
			dst += Integer.MAX_VALUE;
		}
		return dst;
	}
	
	/** 
	   * 替换字符串 
	   * 
	   * @param from String 原始字符串 
	   * @param to String 目标字符串 
	   * @param source String 母字符串 
	   * @return String 替换后的字符串 
	   */  
	public static String replace(String from, String to, String source) {  
	    if (source == null || from == null || to == null)  
	      return null;  
	    StringBuffer bf = new StringBuffer("");  
	    int index = -1;  
	    while ((index = source.indexOf(from)) != -1) {  
	      bf.append(source.substring(0, index) + to);  
	      source = source.substring(index + from.length());  
	      index = source.indexOf(from);  
	    }  
	    bf.append(source);  
	    return bf.toString();  
	}  
	
	
	 /**
    * 根据 timestamp 生成各类时间状态串
    * 
    * @param timestamp 距1970 00:00:00 GMT的秒数
    * @return 时间状态串(如：刚刚5分钟前)
    */
   public static String getTimeState(String timestamp) {
       if (timestamp == null || "".equals(timestamp)) {
           return "";
       }

       try {
           long _timestamp = Long.parseLong(timestamp) * 1000;
           if (System.currentTimeMillis() - _timestamp < 1 * 60 * 1000) {
               return "刚刚";
           } else if (System.currentTimeMillis() - _timestamp < 30 * 60 * 1000) {
               return ((System.currentTimeMillis() - _timestamp) / 1000 / 60)
                       + "分钟前";
           } else {
               Calendar now = Calendar.getInstance();
               Calendar c = Calendar.getInstance();
               c.setTimeInMillis(_timestamp);
               if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                       && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                       && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
                   SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
                   return sdf.format(c.getTime());
               }
               if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                       && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                       && c.get(Calendar.DATE) == now.get(Calendar.DATE) - 1) {
                   SimpleDateFormat sdf = new SimpleDateFormat("昨天 HH:mm");
                   return sdf.format(c.getTime());
               } else if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                   SimpleDateFormat sdf = new SimpleDateFormat("M月d日 HH:mm:ss");
                   return sdf.format(c.getTime());
               } else {
                   SimpleDateFormat sdf = new SimpleDateFormat(
                           "yyyy年M月d日 HH:mm:ss");
                   return sdf.format(c.getTime());
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
           return "";
       }
   }
   
   public static String getFileNameNotExt(String fileName) {
   	String tempName = System.currentTimeMillis()+"";
   	if(fileName == null || fileName.length() <= 0) {
   		return tempName;
   	}
   	int index = fileName.indexOf(".");
   	if(index != -1) {
   		tempName = fileName.substring(0, index);
   	}
   	return tempName;
   }
   /**
    * 去空格回车tab
    * @param str
    * @return
    */
   public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}
   
   @SuppressWarnings("finally")
public static String PintHost() {
		String delay = new String();  
	    Process p;
	    try {
	    	p = Runtime.getRuntime().exec("ping -c 1 " + "120.132.132.101");
	    	BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));  
	    	String str = new String();  
	    	while((str=buf.readLine())!=null){  
//	                if(str.contains("packet loss")){  
//	                    int i= str.indexOf("received");  
//	                    int j= str.indexOf("%");  
//	                    Tools.debug("丢包率:"+str.substring(i+10, j+1));  
//	                }  
	    		if(str.contains("avg")){  
	    			int i=str.indexOf("/", 20);  
	    			int j=str.indexOf(".", i);  
	    			delay =str.substring(i+1, j);  
	    			delay = "延迟:" + delay + "ms";  
	    			System.out.println(delay);  
	    		}
	    	}  
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }  
	    finally {
	    	return delay;
	    }
   }
   
   /**
    * 异或的一个特点： a^b = c c^b = a
    * 所以简单点，这里的加解密都用一个函数就行了
    * @param src
    * @param dest
    * @throws Exception
    */
   public static String xorEn(String src) {
       // 文件不存在或为文件夹就不判断了
	   byte XOR_CONST = 0X12;
       byte[] bs = src.getBytes();
       for (int i = 0; i < bs.length; i++) {
    	   bs[i] ^= XOR_CONST;
       }
       return new String(bs);
   }

   private static String getUUIDForFile() {
	   String m_szUniqueID = null;
	   if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
		   String path = "/lyingdice/.cache/.tmp/";
		   File sdCard = android.os.Environment.getExternalStorageDirectory();
		   String dir = sdCard.getPath()+path;
		   File IMEI = new File(dir, "idata");
		   if(IMEI != null) {
			   BufferedReader reader = null;
			   try {
				   Tools.debug("Util::getUUIDForFile::以行为单位读取文件内容，一次读一整行：");
				   reader = new BufferedReader(new FileReader(IMEI));
				   String String1= null;
				   String String2= null;
				   String tempString;
				   int line = 1;
				   // 一次读入一行，直到读入null为文件结束
				   while ((tempString = reader.readLine()) != null) {
					   // 显示行号
					   if(line == 1) {
						   String1 = tempString;
					   }
					   else if(line == 2) {
						   String2 = tempString;
						   break;
					   }
					   line++;
				   }
				   if(String1 != null && String2 != null) {
					   if(String1.equals(xorEn(String2))) {
						   m_szUniqueID = String1;
					   }
				   }
				   reader.close();
			   } catch (IOException e) {
				   e.printStackTrace();
				   Tools.debug("Util::getUUIDForFile::1");
			   } finally {
				   Tools.debug("Util::getUUIDForFile::2");
				   if (reader != null) {
					   Tools.debug("Util::getUUIDForFile::3");
					   try {
						   Tools.debug("Util::getUUIDForFile::4");
						   reader.close();
						   Tools.debug("Util::getUUIDForFile::5");
					   } catch (IOException e1) {
						   Tools.debug("Util::getUUIDForFile::6");
					   }
				   }
				   Tools.debug("Util::getUUIDForFile::7");
			   }
		   }
	   }
	   Tools.debug("Util::getUUIDForFile::8");
	   return m_szUniqueID;
   }
   
   public static String getUUID(Context context) {
	   String m_szUniqueID = null;
	   if((m_szUniqueID = getUUIDForFile()) != null) {
		   return m_szUniqueID;
	   }
	   String m_szDevIDShort = "35" + //we make this look like a valid IMEI 
			   Build.BOARD.length()%10+
			   Build.BRAND.length()%10+ 
			   Build.CPU_ABI.length()%10+
			   Build.DEVICE.length()%10+ 
			   Build.DISPLAY.length()%10+
			   Build.HOST.length()%10+ 
			   Build.ID.length()%10+ 
			   Build.MANUFACTURER.length()%10+
			   Build.MODEL.length()%10+ 
			   Build.PRODUCT.length()%10+ 
			   Build.TAGS.length()%10+ 
			   Build.TYPE.length()%10+ 
			   Build.USER.length()%10 ; //13 digits  
	   Tools.debug("Util::getUUID()::" + m_szDevIDShort);
	   
	   String szImei = "0";
	   try {
		   TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE); 
		   szImei = TelephonyMgr.getDeviceId(); // Requires READ_PHONE_STATE
		   Tools.debug("Util::getUUID()::" + szImei);
	   } catch (Exception e1) {
		   e1.printStackTrace();
	   }
	   
	   String m_szWLANMAC = "00:11:22:33:44:55";
	   try {
		   WifiManager wm = (WifiManager)context.getSystemService(context.WIFI_SERVICE); 
		   m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
	   } catch (Exception e) {
//			e.printStackTrace();
	   }
	   Tools.debug("Util::getUUID()::m_szWLANMAC:" + m_szWLANMAC);
	   
	   String m_szAndroidID = "9774d56d682e549c";
	   try {
		   m_szAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	   } catch (Exception e) {
//			e.printStackTrace();
	   }
	   Tools.debug("Util::getUUID()::m_szAndroidID:" + m_szAndroidID);
	   
	   String m_szLongID = szImei + m_szDevIDShort 
			   + m_szAndroidID+ m_szWLANMAC;      
	   // compute md5     
	   // get md5 bytes   
	   MD5 md5 = new MD5();
	   m_szUniqueID =md5.getMD5ofStr(m_szLongID);
	   m_szLongID = xorEn(m_szUniqueID);
	   md5 = null;
	   Tools.debug("Util::getUUID()::1");
	   if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
		   Tools.debug("Util::getUUID()::2");
		   String path = "/lyingdice/.cache/.tmp/";
		   File sdCard = android.os.Environment.getExternalStorageDirectory();
		   String dir = sdCard.getPath()+path;
		   File pahtFile = new File(dir);
		   Tools.debug("Util::getUUID()::3");
		   if(!pahtFile.exists()) {
			   pahtFile.mkdirs();
		   }
		   Tools.debug("Util::getUUID()::4");
		   File IMEI = new File(dir+"idata");
		   FileOutputStream fw;
		   Tools.debug("Util::getUUID()::5");
		   try {
			   fw = new FileOutputStream(IMEI);// 初始化输出流
			   fw.write(m_szUniqueID.getBytes());
			   fw.write("\n".getBytes());
			   fw.write(m_szLongID.getBytes());
			   fw.flush();
			   fw.close();
			   Tools.debug("Util::getUUID()::6");
		   } catch (Exception e) {
			   e.printStackTrace();
			   Tools.debug("Util::getUUID()::7");
		   }
	   }
	   Tools.debug("Util::getUUID()::8");
	   return m_szUniqueID;
   }
   
   public static SpannableString parseSecret(String input) {
	    Pattern SECRET_PATTERN = Pattern.compile("(【.+】|<<.+>>|\\[.+\\]|《.+》)");
		SpannableString mSpannableString = new SpannableString(input);
		Matcher mAtMatcher = SECRET_PATTERN.matcher(input);
		while(mAtMatcher.find()){
			mSpannableString.setSpan(new ForegroundColorSpan(0xffff9933), mAtMatcher.start(), mAtMatcher.end(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return mSpannableString;
	}
}
