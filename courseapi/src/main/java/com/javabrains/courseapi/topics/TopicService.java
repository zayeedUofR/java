package com.javabrains.courseapi.topics;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zayeed
 */
@Service
public class TopicService {
    
    @Autowired
    private TopicRepository topicRepository;
     
    public List<Topic> getAllTopics(){
        List<Topic> topics = new ArrayList<>();
        topicRepository.findAll().forEach(topics::add);
        return topics;
    }
    
    public Topic getTopic(String id){
        return topicRepository.findById(id).get();
    }

    public void addTopic(Topic topic) {
        topicRepository.save(topic);
    }
    
    void updateTopic(Topic topic){
        topicRepository.save(topic);
    }
    
    public void deleteTopic(String id) {
          topicRepository.delete(topicRepository.findById(id).get());
    }
    
}
