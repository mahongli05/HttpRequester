package com.test.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class FileUtil {

    public static void ensureFile(File file) {
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void ensureDirectory(File directory) {
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String readFile(String path) {
        File file = new File(path);
        return readFile(file);
    }

    public static String readFile(File file) {
        InputStream inputStream = null;
        InputStreamReader streamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(file);
            streamReader = new InputStreamReader(inputStream, "UTF-8");
            bufferedReader = new BufferedReader(streamReader);
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            Utils.close(inputStream);
            Utils.close(streamReader);
            Utils.close(bufferedReader);
        }
        return null;
    }

    public static void writeFile(String path, String content) {
        File file = new File(path);
        writeFile(file, content);
    }

    public static void writeFile(File file, String content) {
        OutputStream outputStream = null;
        OutputStreamWriter streamWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            outputStream = new FileOutputStream(file);
            streamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(streamWriter);
            bufferedWriter.write(content);
            bufferedWriter.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
             e.printStackTrace();
        } finally {
            Utils.close(outputStream);
            Utils.close(streamWriter);
            Utils.close(bufferedWriter);
        }
    }
}
