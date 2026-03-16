package hos.srm.srmbusiness.utils;



import io.minio.ComposeSource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Test {

    public static void main(String[] args) {
        String folderPath = "D:\\123\\app"; // 替换为你的文件夹路径
        int totalLines = countLinesInFolder(new File(folderPath));
        System.out.println("总行数: " + totalLines);
    }

    /**
     * 递归统计文件夹中所有文件的行数
     * @param file 文件或文件夹
     * @return 行数
     */
    public static int countLinesInFolder(File file) {
        int totalLines = 0;
        if (file.isFile()) {
            // 如果是文件，统计其行数
            totalLines += countLinesInFile(file);
        } else if (file.isDirectory()) {
            // 如果是文件夹，递归处理所有子文件和子文件夹
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    totalLines += countLinesInFolder(f);
                }
            }
        }
        return totalLines;
    }

    /**
     * 统计单个文件的行数
     * @param file 文件
     * @return 行数
     */
    public static int countLinesInFile(File file) {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
