package com.badminton.club.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 檔案處理工具
 */
public class FileTool {

	private static final Logger log = LoggerFactory.getLogger(FileTool.class);

	/**
	 * classpath下的前置路徑
	 * 
	 * @throws FileNotFoundException
	 */
	public static String resource_prefix(String path) throws FileNotFoundException {

		// ======================classes底下==================
		 return ResourceUtils.getURL("classpath:").getPath()+"static" +path;
//		 return "C:/Users/GameToGo/Desktop/test/club/static" +path;
//		 return "C:/Users/GameToGo/Desktop/test/club/static" +path;
//        return "/home/j43343/jenkins_home/club/static" +path;

	}

	/**
	 * 新增資料夾
	 */
	public static void uploadDir(String dirPath) throws Exception {
		File dir = new File(resource_prefix(dirPath));
		if (!dir.exists()) {
			dir.mkdir();
		}
	}

	/**
	 * 上傳圖片
	 */
	public static void upload(MultipartFile file, String dirPath) throws Exception {
		File dir = new File(resource_prefix(dirPath));
		if (!dir.exists()) {
			dir.mkdir();
		}
		File theFile = new File(resource_prefix(dirPath), file.getOriginalFilename());
		if (theFile.exists()) {
			System.out.println(resource_prefix(dirPath) + "：已經刪除" + theFile.delete());
		}
//		BufferedInputStream brin = new BufferedInputStream(file.getInputStream(), 1024);
		
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(theFile)); // 上傳檔案位置
//		byte input[] = new byte[1024];
//		int count;
//        while ((count=brin.read(input))!= -1) {
//        	outputStream.write(input,0,count);
//        }
		outputStream.write(file.getBytes());
		outputStream.flush();
		outputStream.close();
		
		log.info("----------待上傳圖檔:{},若上傳後:{}", file.getOriginalFilename()  , theFile.getName());


		//圖檔大於1M，等比例壓縮圖檔
		if(file.getSize()>1*1024*1024){
			if("/images/activity/".equals(dirPath)){				
				ImageZipUtil.zipWidthHeightImageFile(theFile, theFile, 1200, 800, 1f);
			}else if("/images/member/".equals(dirPath) || "/images/".equals(dirPath)){
				ImageZipUtil.zipWidthHeightImageFile(theFile, theFile, 1000, 1000, 1f);
			}
			
			log.info("----------圖檔大於1M:{},壓縮成功圖檔:{}", file.getSize(),theFile.getName() );
		}

	}

	/**
	 * 修改圖片(刪除再上傳)
	 */
	public static void modifyAndUpload(MultipartFile file, String dirPath, String filePath) throws Exception {
		File oldFile = new File(resource_prefix(filePath));
		if (oldFile.exists()) {
			log.info("檔案:{},已經刪除:{}", resource_prefix(filePath), oldFile.delete());

		} else {
			File dir = new File(resource_prefix(dirPath));// "/images/member/"
			if (!dir.exists()) {
				dir.mkdir();
			}
		}
//		BufferedInputStream brin = new BufferedInputStream(file.getInputStream(), 1024);
		
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(oldFile)); // 上傳檔案位置
//		byte input[] = new byte[1024];
//		int count;
//        while ((count=brin.read(input))!= -1) {
//        	outputStream.write(input,0,count);
//        }
		outputStream.write(file.getBytes());
		outputStream.flush();
		outputStream.close();
		
		log.info("----------待上傳圖檔:{},若上傳後:{}", file.getOriginalFilename()  , oldFile.getName());
				
		//圖檔大於1M，等比例壓縮圖檔
		if(file.getSize()>1*1024*1024){
			if("/images/activity/".equals(dirPath)){				
				ImageZipUtil.zipWidthHeightImageFile(oldFile, oldFile, 1200, 800, 1f);
			}else if("/images/member/".equals(dirPath) || "/images/".equals(dirPath)){
				ImageZipUtil.zipWidthHeightImageFile(oldFile, oldFile, 1000, 1000, 1f);
			}
			
			log.info("----------圖檔大於1M:{},壓縮成功圖檔:{}", file.getSize(),oldFile.getName() );
		}

	}

	/**
	 * 刪除圖片
	 */
	public static void deleteFile(String path) {
		try {
			File file = new File(resource_prefix(path));
			if (file.exists()) {
				log.info("檔案:{},已經刪除:{}", resource_prefix(path), file.delete());
			}
		} catch (FileNotFoundException e) {
			log.debug(e.getMessage());
		}
	}

	/**
	 * 刪除資料夾
	 */
	public static void deleteDirectory(String path) {
		try {
			File dir = new File(resource_prefix(path));
			if (dir.isDirectory()) {
				File TrxFiles[] = dir.listFiles();
				if (TrxFiles == null || TrxFiles.length == 0) {
					log.info("資料夾:{},已經刪除:{}", resource_prefix(path), dir.delete());
				}
			}
		} catch (FileNotFoundException e) {
			log.debug(e.getMessage());
		}
	}

	/**
	 * 瀏覽器下載檔案(CSV)
	 */
	public static void downLoadFile(HttpServletRequest request, //
			HttpServletResponse response, String csvDirPath, String name) throws Exception {

		// 獲取指定目錄下的第一個檔案
		File scFileDir = new File(csvDirPath);// E://music_eg
		File TrxFiles[] = scFileDir.listFiles();
		log.info("檔名:{}", TrxFiles[0]);
		String fileName = name; // 下載的檔名

		// 如果檔名不為空，則進行下載
		if (fileName != null) {
			// 設定檔案路徑
			String realPath = csvDirPath + "/";// E://music_eg/
			File file = new File(realPath, fileName);

			// 如果檔名存在，則進行下載
			if (file.exists()) {

				// 配置檔案下載
				response.setHeader("content-type", "application/octet-stream");
				response.setContentType("application/octet-stream");
				// 下載檔案能正常顯示中文
				response.setHeader("Content-Disposition",
						"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

				// 實現檔案下載
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					os.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }); // 避免excel中文亂碼(加上BOM信息)
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					os.close();

					log.info("Download the csv successfully!");
				} catch (Exception e) {
					log.info("Download the csv failed!:{}", e.getMessage());
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

	}

}
