package com.ds.assigthree.clientapp.service;

import java.io.IOException;
import java.io.PrintWriter;

public class FileService {

    public void createFile(String filename, String content) throws IOException{

        PrintWriter writer = new PrintWriter(filename, "UTF-8");
        writer.println(content);
        writer.close();

    }
}