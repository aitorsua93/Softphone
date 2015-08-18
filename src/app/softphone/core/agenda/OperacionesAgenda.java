package app.softphone.core.agenda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import app.softphone.core.cuentas.Cuenta;
import app.softphone.core.cuentas.EstadoCuenta.Estado;


public class OperacionesAgenda {
	
	public static void main(String[] args) {
		OperacionesAgenda opAgenda = new OperacionesAgenda();
		/*Contacto c1 = new Contacto("Administracion","8002");
		Contacto c2 = new Contacto("Direccion","8003");
		
		opAgenda.crear(c1);
		opAgenda.crear(c2);*/
		
		List<Contacto> lc = new ArrayList<Contacto>();
		
		lc = opAgenda.buscarCuentas();
	
		for(int i=0;i<lc.size();i++) {
			System.out.println(lc.get(i).getNombre());
			System.out.println(lc.get(i).getNumero() + "\n");
		}
	}
	
	
	public OperacionesAgenda() {
		
	}
	
	public void crear(Contacto contacto) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/agenda.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos el nodo padre "agenda"
			Node nodoRaiz = doc.getDocumentElement();
			//agregamos una nueva etiqueta al documento
			Element nuevoContacto = doc.createElement("contacto");
			//agregamos etiquetas hijas
			Element nuevoNombre = doc.createElement("nombre");
			nuevoNombre.setTextContent(contacto.getNombre());
			
			Element nuevoNumero = doc.createElement("numero");
			nuevoNumero.setTextContent(contacto.getNumero());
			
			//agregar etiquetas al nuevo contacto
			nuevoContacto.appendChild(nuevoNombre);
			nuevoContacto.appendChild(nuevoNumero);
			
			//relacionar el nuevo contacto con la etiqueta "agenda"
			nodoRaiz.appendChild(nuevoContacto);
			
			//transformamos esta estructura de datos en un archivo XML
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("xmlsrc/agenda.xml"));
			transformer.transform(source, result);
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		} catch(TransformerConfigurationException transE) {
			System.out.println(transE.getMessage());
		} catch(TransformerException transformE) {
			System.out.println(transformE.getMessage());
		}
	}
	
	public static String obtenerNodoValor(String strTag, Element eCuenta) {
		Node nValor = (Node)eCuenta.getElementsByTagName(strTag).item(0).getFirstChild();
		return nValor.getNodeValue();
	}
	
	public List<Contacto> buscarCuentas() {
		List<Contacto> agenda = new ArrayList<Contacto>();
		
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/agenda.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "agenda"
			NodeList nodosAgenda = doc.getElementsByTagName("contacto");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosAgenda.getLength();i++) {
				Node contacto = nodosAgenda.item(i);
				if (contacto.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) contacto;
					
					Contacto objContacto = new Contacto();
					objContacto.setNombre(obtenerNodoValor("nombre",unElemento));
					objContacto.setNumero(obtenerNodoValor("numero",unElemento));
				
					agenda.add(objContacto);
				}
			}
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		}
		
		return agenda;
	}
	
	

}
