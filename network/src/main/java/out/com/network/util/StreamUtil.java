package out.com.network.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流相关操作
 * Created by dengjinwen on 2018/3/14.
 */

public class StreamUtil {

    /**
     * 字符串  转  InputStream
     * @param s
     * @return
     */
    public static InputStream convertStringToInputStream(String s){
        InputStream is=new ByteArrayInputStream(s.getBytes());
        return is;
    }

    /**
     *
     * @param is  InputStream  转  String
     * @return
     * @throws IOException
     */
    public static String convertInputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int i=-1;
        while ((i=is.read())!=-1){
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     *  InputStream  转  String
     * @param is
     * @return
     */
    public static String convertInputStreamToStringByBR(InputStream is){
        String buf;
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(is,"utf-8"));
            StringBuilder sb=new StringBuilder();
            String line="";
            while ((line=reader.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            buf=sb.toString();
            return buf;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * InputStream 转  byte[]
     * @param inputStream
     * @return
     */
    public static byte[] convertInputStreamToByte(InputStream inputStream) throws  IOException{
        ByteArrayOutputStream swapStream=new ByteArrayOutputStream();
        byte[] buff=new byte[100];
        int rc=0;
        while ((rc=inputStream.read(buff,0,100))>0){
            swapStream.write(buff,0,rc);
        }
        byte[] in2b=swapStream.toByteArray();
        return in2b;
    }
}
