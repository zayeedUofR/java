/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javabrains.courseapi.topics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author zayee
 */
@Service
public class TopicService {
    
    private final List<Topic> topics = new ArrayList<>(Arrays.asList(
            new Topic("java", "Java", "Java Description"),
            new Topic("spring", "Spring", "Spring Description"),
            new Topic("javaScript", "JavaScript", "JavaScript Description")
    ));
    
    public List<Topic> getAllTopics(){
        return this.topics;
    }
    
    public Topic getTopic(String id){
        return  topics.stream().filter(t -> t.getId().equals(id)).findFirst().get();
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
    }
    
    void updateTopic(Topic topic, String id) {
        for(int i = 0; i < topics.size(); i++){
            Topic t  = topics.get(i);
            if (t.getId().equals(id)){
                topics.set(i, topic);
                return;
            }
        }
    }
    
    public void deleteTopic(String id) {
        topics.removeIf(t -> t.getId().equals(id));
    }
    
}
