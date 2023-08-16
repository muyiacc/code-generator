package cc.seektao.utils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionUtils {
    public static void close(ResultSet rs,  PreparedStatement ps, Connection conn){
        if (rs  != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps  != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn  != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(ResultSet rs,  PreparedStatement ps){
        if (rs  != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps  != null){
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void close(BufferedWriter bw, OutputStreamWriter osw, OutputStream os){
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
    }
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
