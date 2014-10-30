/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournal;

/**
 * @author mormon
 */
public class Scripture {
    private String book;
    private int chapter;
    private int verse;
    
    
    /*************************************************************************
     * @param bookName 
     ************************************************************************/
    public void setBook(String bookName){
        book = bookName;
    }
    
    /************************************************************************* 
     * @param chapt 
     ************************************************************************/
    public void setChapter(int chapt){
        chapter = chapt;
    }
    
    /*************************************************************************
     * @param verseNum 
     ************************************************************************/
    public void setVerse(int verseNum){
        verse = verseNum;
    }
    
    /************************************************************************* 
     * @return 
     ************************************************************************/
    public String getBook(){
       return book; 
    }
    
    /*************************************************************************
     * @return 
     ************************************************************************/
    public int getChapter(){
       return chapter; 
    }
    
    /*************************************************************************
     * @return 
     ************************************************************************/
    public int getVerse(){
       return verse; 
    }
}