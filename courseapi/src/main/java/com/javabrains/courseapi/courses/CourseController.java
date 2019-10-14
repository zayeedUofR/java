package com.javabrains.courseapi.courses;

import com.javabrains.courseapi.topics.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author zayeed
 */

@RestController
public class CourseController {
    
    @Autowired
    private CourseService courseService;

    @RequestMapping("/courses")
    public List<Topic> getAllCourses(){
        return courseService.getAllCourses();
    }
    @RequestMapping("/courses/{id}")
    public Topic getCourse(@PathVariable String id){
        return courseService.getCourse(id);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "/courses")
    @ResponseStatus(HttpStatus.OK)
    public void addCourse(@RequestBody Topic topic){
        courseService.addCourse(topic);
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "/courses/{id}")
    public void updateCourse(@RequestBody Topic topic, @PathVariable String id){
        courseService.updateCourse(topic);
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/courses/{id}")
    public void deleteCourse(@PathVariable String id){
        courseService.deleteCourse(id);
    }
}
