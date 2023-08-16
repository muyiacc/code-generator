package cc.seektao.utils;

import java.io.*;

public class ConnectionUtils {
    public static void close(BufferedReader br, InputStreamReader isr, InputStream is){
        if (br  != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isr  != null){
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (is  != null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(BufferedWriter bw, OutputStreamWriter osw, OutputStream os, BufferedReader br, InputStreamReader isr, InputStream is){
        if (bw  != null){
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (osw  != null){
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (os  != null){
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (br  != null){
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isr  != null){
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (is  != null){
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
