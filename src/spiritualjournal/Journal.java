/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

/*************************************************************************
 * @author mormon
 ************************************************************************/
public class Journal {
    
    private List <Scripture> scripture;
    private List <Topic> topic;
    private List <Entry> entries;
    
    public FileService fs = new FileService();
    
    /*********************************************************
     * MAIN
     * Just calls the run function
     ********************************************************/
    public static void main(String[] args) throws Exception{
        new Journal().run();
    }
    
    /**********************************************************************
     * RUN
     * This will call all of the necessary functions to make this 
     * program work.
     * @throws Exception 
     *********************************************************************/
    public void run() throws Exception {
        FileService f = new FileService();
        String propertyFile = "/resources/journal.properties";
        String termFile = f.getProperties("terms", propertyFile);
        String bookFile = f.getProperties("books", propertyFile);
        String inFile = "c:/journal.txt";
        String outFile = "c:/journalOutput.txt";
        String xmlOut = "c:/xmlOutput.txt";
        
        
        Map<String, List<String>> bookList  = f.readBook(bookFile);
        Map<String, List<String>> topicList = f.readTopic(termFile);
        
        entries = f.fileProcess(inFile, bookList, topicList);
        
        try {
            f.buildXML(entries, xmlOut);
            f.writeFile(entries, outFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /************************************************************************
     * This is the display for Milestone 3A
     * @param entry 
     ***********************************************************************/
    void display(List<Entry> entry) { 
        for (Entry e : entry) {
            System.out.println("Entry: ");
            System.out.println("Date: " + e.getDate());
            System.out.println("Content: \n" + e.getContent() + "\n");
        }
    }
    
    /************************************************************************
     * This is the display for Milestone 3B
     * @param map
     * @param head 
     ***********************************************************************/
    public void displayInfo(Map<String, List<String>> map, String head) {
        System.out.println(head);
        
        for (String key : map.keySet()) {
            if (map.get(key) != null) {
                System.out.println(key + ":");
                List<String> date = map.get(key);
                for (String str : date) {
                    System.out.println("\t" + str);
                }
            }
        }
    }
    
    public void setEntries(List<Entry> entry) {
        entries = entry;
    }
    
    public List<Entry> getEntries() {
        return entries;
    }
    
    /*************************************************************************
     * WRITE TEXT TO FILE
     * This function will write the text from the file text area into a file.
     * @param content
     * @param date
     * @param file
     * @throws IOException 
     ************************************************************************/
    public void writeTextToFile(String content, String date, String file) throws IOException {
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        content = content.trim();
        content = content.replaceAll("\\t+", "\n");
        pw.println("----");
        pw.println(content);
        pw.println(date);
        pw.close();
    }
    
    /*************************************************************************
     * READ FILE
     * This file will read in a simple text file.
     * @param content
     * @param file
     * @throws FileNotFoundException
     * @throws IOException 
     ************************************************************************/
    public void readFile(String content, String file) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        
        while ((content = br.readLine()) != null) {
            content = content.trim();
            content = content.replaceAll("\\t+", "\n");
            System.out.println(content);
        }
    }
}