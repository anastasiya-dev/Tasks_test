package myproject.extending;

import org.springframework.stereotype.Service;

@Service("taskService")
public class TaskService implements DoTask{
    @Override
    public void doMainJob() {
        System.out.println("Class doing main job");
    }
}
