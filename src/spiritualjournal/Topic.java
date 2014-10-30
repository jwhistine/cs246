/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spiritualjournal;

import java.util.List;

/*************************************************************************
 * @author mormon
 ************************************************************************/
public class Topic {
    private List<String> topic;
    
    /************************************************************************* 
     * @param top 
     ************************************************************************/
    public void setTopic(List<String> top){
        topic = top;
    }
    
    /*************************************************************************
     * @return 
     ************************************************************************/
    public List<String> getTopic(){
        return topic;
    }
}