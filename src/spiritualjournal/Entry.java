/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournal;

import java.util.List;

/******************************************************************************
 * @author mormon
 *****************************************************************************/
public class Entry {
    private String date;
    private String cont;
    private List<String> scrip;
    private List<String> topic;
    private List<String> entry;

    /**************************************************************************
     * CONSTRUCTOR
     * Not much going on here
     * @param date
     * @param content
     * @param scriptures
     * @param topic 
     *************************************************************************/
    Entry(String date, String content, List<String> scriptures, List<String> topic) {
        this.date = date;
        this.cont = content;
        this.scrip = scriptures;
        this.topic = topic;
    }
    
    /**************************************************************************
     * @param scriptures 
     *************************************************************************/
    public void setScripture(List<String> scriptures) {
        scrip = scriptures;
    }
    
    /*************************************************************************
     * @param dates 
     ************************************************************************/
    public void setDate(String dates) {
        date = dates;
    }
    
    /*************************************************************************
     * @param content 
     ************************************************************************/
    public void setContent(String content) {
        cont = content;
    }
    
    /*************************************************************************
     * @param top 
     ************************************************************************/
    public void setTopic(List<String> top){
        topic = top;
    }
    
    /*************************************************************************
     * @return 
     ************************************************************************/
    public String getDate() {
        return date;
    }
    
    /************************************************************************* 
     * @return 
     ************************************************************************/
    public String getContent() {
        return cont;
    }
    
    /*************************************************************************
     * @return 
     ************************************************************************/
    public List<String> getScriptures() {
        return scrip;
    }
   
    /*************************************************************************
     * @return 
     ************************************************************************/
    public List<String> getTopic(){
        return topic;
    }
    
    /*************************************************************************
     * @param scripture 
     ************************************************************************/
    public void addScrip(String scripture) {
        scrip.add(scripture);
    }
    
    /**************************************************************************
     * @param topics 
     *************************************************************************/
    public void addTopic(String topics) {
        topic.add(topics);
    }   
}