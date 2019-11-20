package scs.ubb.map.repository.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import scs.ubb.map.domain.Entity;
import scs.ubb.map.domain.Student;
import scs.ubb.map.validators.Validator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class StudentXMLRepository extends InXMLRepository<Long, Student> {

    public StudentXMLRepository(Validator validator, String fileName) throws ParserConfigurationException, IOException, SAXException {
        super(validator, fileName);
    }

    @Override
    public String getRootName() {
        return "students";
    }

    @Override
    public String getElementTagName() {
        return "student";
    }

    @Override
    public Element getElement(Student entity) {
        Element element = super.document.createElement(getElementTagName());
        element.setAttribute("id","" + entity.getId());

        Element firstName = super.document.createElement("firstName");
        firstName.setTextContent(entity.getFirstName());
        element.appendChild(firstName);

        Element lastName = super.document.createElement("lastName");
        lastName.setTextContent(entity.getLastName());
        element.appendChild(lastName);

        Element group = super.document.createElement("group");
        group.setTextContent("" + entity.getGroup());
        element.appendChild(group);

        Element email = super.document.createElement("email");
        email.setTextContent(entity.getEmail());
        element.appendChild(email);

        return element;
    }

    @Override
    public Student getObject(Node node) {
        Long id = Long.parseLong(node.getAttributes().item(0).getNodeValue());
        String firstName = ((Element) node).getElementsByTagName("firstName").item(0).getTextContent();
        String lastName = ((Element) node).getElementsByTagName("lastName").item(0).getTextContent();
        String email = ((Element) node).getElementsByTagName("email").item(0).getTextContent();
        Integer group = Integer.parseInt(((Element) node).getElementsByTagName("group").item(0).getTextContent());

        Student student = new Student(firstName, lastName, email, group);
        student.setId(id);
        return student;
    }
}
