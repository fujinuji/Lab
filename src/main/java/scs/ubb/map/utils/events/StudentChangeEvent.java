package scs.ubb.map.utils.events;

import scs.ubb.map.domain.Student;

public class StudentChangeEvent implements Event{
    private ChangeEventType eventType;
    private Student data;

    public StudentChangeEvent(ChangeEventType eventType, Student data) {
        this.eventType = eventType;
        this.data = data;
    }
}
