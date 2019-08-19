package com.drore.cloud.tdp.common.util;

import com.alibaba.fastjson.JSONObject;
import com.drore.cloud.sdk.common.resp.RestMessage;
import com.drore.cloud.tdp.common.util.weixin.StringUtil;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

/**
 * JAVA Linux FTP下载 助手类
 * <p/>
 * com.jcraft.jsch-0.1.53 jar包
 *
 * @author : zhangz
 */
public class FileManagerByFtp {

    private static final Logger logger = Logger.getLogger(FileManagerByFtp.class);

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            if (logger.isDebugEnabled()) {
                logger.debug("Session created.");
            }
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            if (logger.isDebugEnabled()) {
                logger.debug("Session connected.");
                logger.debug("Opening Channel.");
            }
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            if (logger.isDebugEnabled()) {
                logger.debug("已经连接到 " + host + "。");
            }
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Connected to " + e.getMessage());
            }
        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @param sftp
     */
    public static void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp)
            throws FileNotFoundException, SftpException {
        sftp.cd(directory);
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory, ChannelSftp sftp) throws SftpException {
        return sftp.ls(directory);
    }

    public static void main(String[] args) throws FileNotFoundException, SftpException {
        /*FileManagerByFtp sf = new FileManagerByFtp();
		String host = "192.168.11.110";
		int port = 22;
		String username = "root";
		String password = "drore123456";
		String directory = "/home/tomcat/usp-tdp/webapps/ROOT/WEB-INF/lib/";
		String uploadFile = "F:/tempfiles/2_201083103325.png";
		String downloadFile = "cloud-tdp-sms-ytx-v1.0-0.0.1-SNAPSHOT.jar";
		String saveFile = "F:/cloud-tdp-sms-ytx-v1.0-0.0.1-SNAPSHOT.jar";
		String deleteFile = "delete.txt";
		ChannelSftp sftp = sf.connect(host, port, username, password);
		// sf.upload(directory, uploadFile, sftp);
		sf.download(directory, downloadFile, saveFile, sftp);
		// sf.delete(directory, deleteFile, sftp);
		try {
			sftp.cd(directory);
			sftp.mkdir("ss");
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		}*/

        FileManagerByFtp sf = new FileManagerByFtp();
        sf.fileUploadByFtp(null, null, "zz", "C:\\Users\\admin\\Desktop\\led\\zz.vsn", "172.16.10.187", "vsn", "", "zz");
        sf.fileUploadByFtp(null, null, "Wildlife", "C:\\Users\\admin\\Desktop\\led\\files\\Wildlife.wmv", "172.16.10.187", "x", ".wmv", "zz");

        JSONObject responce = JSONObject.parseObject(HttpsUtils.httpsPut("http://172.16.10.187/api/vsns/sources/lan/vsns/zz.vsn/activated", null));
    }

    /**
     * @param host         主机
     * @param port         端口
     * @param username     用户名
     * @param password     密码
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     */
    public static boolean uploadJar(String host, int port, String username, String password, String directory,
                                    String downloadFile, String saveFile) {
        boolean flag = false;
        try {
            ChannelSftp sftp = connect(host, port, username, password);// 创建连接
            download(directory, downloadFile, saveFile, sftp);
            // sftp.cd(directory);
            // sftp.mkdir("ss");
            if (logger.isDebugEnabled()) {
                logger.debug("Upload to finished");
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getProjectPath() {

        java.net.URL url = FileManagerByFtp.class.getProtectionDomain().getCodeSource().getLocation();
        String filePath = null;
        try {
            filePath = java.net.URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath.endsWith(".jar"))
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        java.io.File file = new java.io.File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }

    /**
     * FTP上传单个文件测试，根据路径上传文件
     * 卡莱特LED专用
     */
    public static void fileUploadByFtp(String username, String pwd, String filename, String filepath, String ip, String filetype, String suffix, String jmname) {
        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            if (StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(pwd)) {
                ftpClient.connect(ip);
                ftpClient.login(username, pwd);
            } else {
                ftpClient.connect(ip);
            }

            File srcFile = new File(filepath);
            fis = new FileInputStream(srcFile);
            if (filetype.equals("vsn")) {
                ftpClient.changeWorkingDirectory("/program/");
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //命名
                ftpClient.storeFile(filename + ".vsn", fis);
            } else {
                //创建文件目录
                // 设置上传文件目录
                ftpClient.makeDirectory("/program/" + jmname + ".files/");
                ftpClient.changeWorkingDirectory("/program/" + jmname + ".files/");
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //命名
                ftpClient.storeFile(filename + suffix, fis);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
    }

    /**
     * FTP上传单个文件,根据传过来的流上传
     * 卡莱特LED专用
     * params:
     * username:ftp用户名
     * pwd：ftp密码
     * filename：文件名称
     * ip：上传文件所在服务器的ip地址
     * suffix：文件后缀
     * fis：文件流
     * jmname：文件夹名称(vsn文件名称)
     */
    public static RestMessage fileUploadByFtp(String username, String pwd, String filename, String ip, String suffix, InputStream fis, String jmname) {
        FTPClient ftpClient = new FTPClient();
        RestMessage message = new RestMessage();

        try {
            if (StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(pwd)) {
                ftpClient.connect(ip);
                ftpClient.login(username, pwd);
            } else {
                ftpClient.connect(ip);
            }
            //File srcFile = new File(filepath);
            //fis = new FileInputStream(srcFile);
            if (".vsn".equals(suffix)) {
                ftpClient.changeWorkingDirectory("/program/");
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //命名
                ftpClient.storeFile(filename + suffix, fis);
            } else {
                //创建文件目录
                ftpClient.makeDirectory("/program/" + jmname + ".files/");
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("GBK");
                // 设置上传文件目录
                ftpClient.changeWorkingDirectory("/program/" + jmname + ".files/");
                // 设置文件类型（二进制）
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                //命名
                ftpClient.storeFile(filename + suffix, fis);
            }

        } catch (IOException e) {
            e.printStackTrace();
            message.setMessage("FTP客户端出错或连接超时！");
            message.setSuccess(false);
            return message;
            //throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                //throw new RuntimeException("关闭FTP连接发生异常！", e);
                message.setMessage("FTP客户端出错！");
                message.setSuccess(false);
                return message;
            }
        }
        message.setSuccess(true);
        return message;
    }

    /**
     * FTP下载单个文件测试
     */
    public static boolean fileDownloadByFtp(String ip, String username, String password, String directory,
                                            String newdirectory) {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;
        boolean flag = false;
        try {
            ftpClient.connect(ip);
            ftpClient.login(username, password);
            fos = new FileOutputStream(newdirectory);
            ftpClient.setBufferSize(1024);
            // 设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(directory, fos);
            flag = true;
        } catch (IOException e) {
            flag = false;
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }
}
