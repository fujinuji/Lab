package scs.ubb.map.repository.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.validators.Validator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class HomeworkXMLRepository extends InXMLRepository<Integer, Homework> {
    public HomeworkXMLRepository(Validator<Homework> validator, String fileName) throws ParserConfigurationException, IOException, SAXException {
        super(validator, fileName);
    }

    @Override
    public String getRootName() {
        return "homeworks";
    }

    @Override
    public String getElementTagName() {
        return "homework";
    }

    @Override
    public Element getElement(Homework entity) {
        Element element = super.document.createElement(getElementTagName());
        element.setAttribute("id","" + entity.getId());

        Element description = super.document.createElement("description");
        description.setTextContent(entity.getDescription());
        element.appendChild(description);

        Element startWeek = super.document.createElement("startWeek");
        startWeek.setTextContent("" + entity.getStartWeek());
        element.appendChild(startWeek);

        Element endWeek = super.document.createElement("deadlineWeek");
        endWeek.setTextContent("" + entity.getDeadlineWeek());
        element.appendChild(endWeek);

        return element;
    }

    @Override
    public Homework getObject(Node node) {
        Element element = (Element) node;

        Integer id = Integer.parseInt(element.getAttribute("id"));
        String description = element.getElementsByTagName("description").item(0).getTextContent();
        Integer startWeek = Integer.parseInt(element.getElementsByTagName("startWeek").item(0).getTextContent());
        Integer deadlineWeek = Integer.parseInt(element.getElementsByTagName("deadlineWeek").item(0).getTextContent());

        Homework homework = new Homework(description, startWeek, deadlineWeek);
        homework.setId(id);
        return homework;
    }
}
