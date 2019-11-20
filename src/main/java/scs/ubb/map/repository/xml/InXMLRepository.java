package scs.ubb.map.repository.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import scs.ubb.map.domain.Entity;
import scs.ubb.map.repository.InMemoryRepository;
import scs.ubb.map.validators.Validator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class InXMLRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String fileName;
    private Element rootElement;

    protected Document document;

    public InXMLRepository(Validator<E> validator, String fileName) throws ParserConfigurationException, IOException, SAXException {
        super(validator);
        this.fileName = fileName;

        document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(this.fileName);
        if (document.getElementsByTagName(getRootName()) == null) {
            rootElement = document.createElement(getRootName());
            document.appendChild(rootElement);
        }

        rootElement = document.getDocumentElement();
        loadData();
    }

    @Override
    public E save(E entity) {
        E exitEntity = super.save(entity);
        if (exitEntity == null) {
            rootElement.appendChild(getElement(entity));

            try {
                this.writeFile();
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }

        return exitEntity;
    }

    @Override
    public E delete(ID id) {
        E exitEntity = super.delete(id);

        if(exitEntity != null) {
            NodeList nodeList = rootElement.getElementsByTagName(getElementTagName());

            for(int i = 0; i < nodeList.getLength(); ++i) {
                Element element = (Element) nodeList.item(i);
                if(String.valueOf(id).equals(element.getAttribute("id"))) {
                    rootElement.removeChild(element);

                    try {
                        writeFile();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return exitEntity;
    }

    @Override
    public E update(E entity) {
        E exitEntity = super.update(entity);

        if(exitEntity == null) {
            NodeList nodeList = rootElement.getElementsByTagName(getElementTagName());

            for(int i = 0; i < nodeList.getLength(); ++i) {
                Element element = (Element) nodeList.item(i);
                if(String.valueOf(entity.getId()).equals(element.getAttribute("id"))) {
                    rootElement.removeChild(element);
                    rootElement.appendChild(getElement(entity));

                    try {
                        writeFile();
                    } catch (TransformerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return exitEntity;
    }

    private void loadData() {
        NodeList nodeList = document.getElementsByTagName(this.getElementTagName());

        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                super.save(this.getObject(node));
            }
        }
    }

    private void writeFile() throws TransformerException {
        document.normalize();
        StreamResult file = new StreamResult(new File(this.fileName));
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer writer = new StringWriter();
        transformer.transform(new DOMSource(document), file);
    }

    public abstract String getRootName();

    public abstract String getElementTagName();

    public abstract Element getElement(E entity);

    public abstract E getObject(Node node);
}
