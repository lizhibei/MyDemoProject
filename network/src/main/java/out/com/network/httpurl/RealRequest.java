package out.com.network.httpurl;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class RealRequest {

    private static final String BOUNDARY = java.util.UUID.randomUUID().toString();
    private static final String TWO_HYPHENS = "--";
    private static final String LINE_END = "\r\n";

    /**
     * get  请求
     * @param requestURL
     * @param headerMap
     * @return
     */
    public RealResponse getData(String requestURL, Map<String,String> headerMap){
        HttpURLConnection conn=null;
        try {
            conn=getHttpURLConnection(requestURL,"GET");
            conn.setDoInput(true);
            if(headerMap!=null){
                setHeader(conn,headerMap);
            }
            conn.connect();
            return getRealResponse(conn);
        } catch (IOException e) {
            return getExceptionResponse(conn,e);
        }
    }

    /**
     * post请求
     * @param requestURL
     * @param body
     * @param bodyType
     * @param headerMap
     * @return
     */
    public RealResponse postData(String requestURL,String body,String bodyType,Map<String,String> headerMap){
        HttpURLConnection conn=null;
        try {
            conn=getHttpURLConnection(requestURL,"POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            if(bodyType!=null&&!bodyType.isEmpty()){
                conn.setRequestProperty("Content-Type",bodyType);
            }
            if(headerMap!=null){
                setHeader(conn,headerMap);
            }
            conn.connect();
            if(body!=null&&!body.isEmpty()){
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(),"UTF-8"));
                writer.write(body);
                writer.close();
            }
            return getRealResponse(conn);
        } catch (IOException e) {
            return getExceptionResponse(conn,e);
        }
    }

    /**
     * 上传文件
     * @param requestURL
     * @param file
     * @param fileList
     * @param fileMap
     * @param fileKey
     * @param fileType
     * @param paramsMap
     * @param headerMap
     * @param callBack
     * @return
     */
    public RealResponse uploadFile(String requestURL, File file,List<File> fileList,
                                   Map<String,File> fileMap,String fileKey,String fileType,
                                   Map<String,String> paramsMap,Map<String, String> headerMap,
                                   CallBackUtil callBack){
        HttpURLConnection conn=null;
        try {
            conn=getHttpURLConnection(requestURL,"POST");
            setConnection(conn);
            if(headerMap!=null){
                setHeader(conn,headerMap);
            }
            conn.connect();
            DataOutputStream outputStream=new DataOutputStream(conn.getOutputStream());
            if(paramsMap!=null){
                outputStream.write(getParamsString(paramsMap).getBytes());
                outputStream.flush();
            }
            if(file!=null){
                writeFile(file,fileKey,fileType,outputStream,callBack);
            }else if(fileMap!=null){
                for(File f:fileList){
                    writeFile(f,fileKey,fileType,outputStream,null);
                }
            }else if(fileMap!=null){
                for(String key:fileMap.keySet()){
                    writeFile(fileMap.get(key),key,fileType,outputStream,null);
                }
            }
            byte[] endData = (LINE_END + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();//写结束标记位
            outputStream.write(endData);
            outputStream.flush();
            return getRealResponse(conn);
        } catch (IOException e) {
            return getExceptionResponse(conn,e);
        }
    }

    /**
     * 上传文件设置Connection参数
     * @param conn
     */
    private void setConnection(HttpURLConnection conn){
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection","Keep-Alive");
        conn.setRequestProperty("Charset","UTF-8");
        conn.setRequestProperty("Content-Type","multipart/form-data; BOUNDARY="+BOUNDARY);
    }

    /**
     * 上传文件时得到拼接的参数字符串
     * @param paramsMap
     */
    private String getParamsString(Map<String,String> paramsMap){
        StringBuffer strBuf=new StringBuffer();
        for(String key:paramsMap.keySet()){
            strBuf.append(TWO_HYPHENS);
            strBuf.append(BOUNDARY);
            strBuf.append(LINE_END);
            strBuf.append("Content-Disposition: form-data; name=\"" + key + "\"");
            strBuf.append(LINE_END);

            strBuf.append("Content-Type: " + "text/plain" );
            strBuf.append(LINE_END);
            strBuf.append("Content-Lenght: "+paramsMap.get(key).length());
            strBuf.append(LINE_END);
            strBuf.append(LINE_END);
            strBuf.append(paramsMap.get(key));
            strBuf.append(LINE_END);
        }
        return strBuf.toString();
    }

    /**
     * 上传文件时得到一定格式的拼接字符串
     * @param file
     * @param fileKey
     * @param fileType
     * @return
     */
    private String getFileParamsString(File file,String fileKey,String fileType){
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(LINE_END);
        strBuf.append(TWO_HYPHENS);
        strBuf.append(BOUNDARY);
        strBuf.append(LINE_END);
        strBuf.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"");
        strBuf.append(LINE_END);
        strBuf.append("Content-Type: " + fileType );
        strBuf.append(LINE_END);
        strBuf.append("Content-Lenght: "+file.length());
        strBuf.append(LINE_END);
        strBuf.append(LINE_END);
        return strBuf.toString();
    }

    /**
     * 上传文件时写文件
     * @param file
     * @param fileKey
     * @param fileType
     * @param outputStream
     * @param callBack
     * @throws IOException
     */
    private void writeFile(File file, String fileKey, String fileType, DataOutputStream outputStream,
                           final CallBackUtil callBack) throws  IOException{
        outputStream.write(getFileParamsString(file,fileKey,fileType).getBytes());
        outputStream.flush();

        FileInputStream inputStream=new FileInputStream(file);
        final long total=file.length();
        long sum=0;
        byte[] buffer=new byte[1024*2];
        int length=-1;
        while ((length=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,length);
            sum=sum+length;
            if(callBack!=null){
                final long finalsum=sum;
                CallBackUtil.mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onProgress(finalsum*100.0f/total,total);
                    }
                });
            }
        }
        outputStream.flush();
        inputStream.close();
    }

    /**
     * 获得HttpURLConnection 连接
     * @param requestURL
     * @param requestMethod
     * @return
     * @throws IOException
     */
    private HttpURLConnection getHttpURLConnection(String requestURL,String requestMethod) throws IOException {
        URL url=new URL(requestURL);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10*1000);
        conn.setReadTimeout(15*1000);
        conn.setRequestMethod(requestMethod);
        return  conn;
    }

    /**
     * 设置请求头
     * @param headerMap
     */
    private void setHeader(HttpURLConnection conn, Map<String,String> headerMap){
        if(headerMap!=null){
            for(String key:headerMap.keySet()){
                conn.setRequestProperty(key,headerMap.get(key));
            }
        }
    }

    /**
     * 得到Response对象
     * @param conn
     * @return
     * @throws IOException
     */
    private RealResponse getRealResponse(HttpURLConnection conn) throws IOException {
        RealResponse response=new RealResponse();
        response.code=conn.getResponseCode();
        response.contentLength=conn.getContentLength();
        response.inputStream=conn.getInputStream();
        response.errorStream=conn.getErrorStream();
        return  response;
    }

    /**
     * 当发生异常时，得到Response对象
     * @param conn
     * @param e
     * @return
     */
    private RealResponse getExceptionResponse(HttpURLConnection conn,Exception e){
        if(conn!=null){
            conn.disconnect();
        }
        e.printStackTrace();
        RealResponse response=new RealResponse();
        response.exception=e;
        return response;
    }
}
