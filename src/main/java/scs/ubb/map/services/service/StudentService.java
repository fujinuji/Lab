package scs.ubb.map.services.service;

import scs.ubb.map.domain.Student;
import scs.ubb.map.repository.CrudRepository;
import scs.ubb.map.utils.events.StudentChangeEvent;
import scs.ubb.map.utils.observers.Observable;
import scs.ubb.map.utils.observers.Observer;

import java.util.ArrayList;
import java.util.List;

public class StudentService extends Service<Long, Student> implements Observable<StudentChangeEvent> {
    private List<Observer<StudentChangeEvent>> observerList = new ArrayList<>();

    public StudentService(CrudRepository<Long, Student> repository) {
        super(repository);
    }

    public void addObserver(Observer<StudentChangeEvent> observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer<StudentChangeEvent> e) {

    }

    public void notifyObservers(StudentChangeEvent studentChangeEvent) {
        observerList.forEach(x -> x.update(studentChangeEvent));
    }

    @Override
    public Student save(Student student) {
        Student data = super.save(student);

        if(data == null) {
            notifyObservers(new StudentChangeEvent(null, student));
        }

        return data;
    }

    @Override
    public Student delete(Long id) {
        Student data = super.delete(id);

        if(data != null) {
            notifyObservers(new StudentChangeEvent(null, null));
        }

        return data;
    }

    @Override
    public Student update(Student student) {
        Student data = super.update(student);

        if(data == null) {
            notifyObservers(new StudentChangeEvent(null, null));
        }

        return data;
    }

}
