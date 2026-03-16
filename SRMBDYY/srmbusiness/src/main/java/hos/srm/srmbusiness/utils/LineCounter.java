package hos.srm.srmbusiness.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class LineCounter {
    private static long totalLines = 0;

    public static void main(String[] args) {


        String folderPath = "D:\\统计\\app";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定的路径不存在或不是文件夹");
            return;
        }

        processFolder(folder);
        System.out.println("总行数: " + totalLines);
    }

    private static void processFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                processFolder(file);
            } else if (file.isFile()) {
                countFileLines(file);
            }
        }
    }

    private static void countFileLines(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            long fileLines = 0;
            while (reader.readLine() != null) {
                fileLines++;
            }
            totalLines += fileLines;
            System.out.println(file.getPath() + ": " + fileLines + " 行");
        } catch (IOException e) {
            System.err.println("读取文件 " + file.getPath() + " 时出错: " + e.getMessage());
        }
    }
}
