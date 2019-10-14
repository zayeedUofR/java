package com.javabrains.courseapi.courses;

import com.javabrains.courseapi.topics.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zayeed
 */
@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
     
    public List<Topic> getAllCourses(){
        List<Topic> topics = new ArrayList<>();
        courseRepository.findAll().forEach(topics::add);
        return topics;
    }
    
    public Topic getCourse(String id){
        return courseRepository.findById(id).get();
    }

    public void addCourse(Topic topic) {
        courseRepository.save(topic);
    }
    
    void updateCourse(Topic topic){
        courseRepository.save(topic);
    }
    
    public void deleteCourse(String id) {
          courseRepository.delete(courseRepository.findById(id).get());
    }
    
}
