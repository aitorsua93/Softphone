package app.softphone.core.agenda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
		/*Contacto c1 = new Contacto("Pepe","Rodriguez","8002","Direccion");
		Contacto c2 = new Contacto("Manuel","Diaz","8003","Administracion");
		
		opAgenda.crear(c1);
		opAgenda.crear(c2);
		
		List<Contacto> lc = new ArrayList<Contacto>();
		
		lc = opAgenda.buscarCuentas("nombre");
	
		for(int i=0;i<lc.size();i++) {
			System.out.println(lc.get(i).getNombre());
			System.out.println(lc.get(i).getApellido());
			System.out.println(lc.get(i).getNumero());
			System.out.println(lc.get(i).getDepartamento() + "\n");
		}
		
		Contacto c = opAgenda.buscarContacto("Pepe", "Rodriguez");
		System.out.println(c.getNombre());
		System.out.println(c.getApellido());
		System.out.println(c.getNumero());
		System.out.println(c.getDepartamento() + "\n");
		
		Contacto c3 = new Contacto("Rodrigo","Martinez","8003","Administracion");
		opAgenda.actualizar(c3, "Manuel", "Diaz");
		
		lc = opAgenda.buscarCuentas("nombre");
		
		System.out.println("-----------------------------------------------------");
		for(int i=0;i<lc.size();i++) {
			System.out.println(lc.get(i).getNombre());
			System.out.println(lc.get(i).getApellido());
			System.out.println(lc.get(i).getNumero());
			System.out.println(lc.get(i).getDepartamento() + "\n");
		}*/
		
		opAgenda.borrar("Pepe", "Rodriguez");
		
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
			
			Element nuevoApellido = doc.createElement("apellido");
			nuevoApellido.setTextContent(contacto.getApellido());
			
			Element nuevoNumero = doc.createElement("numero");
			nuevoNumero.setTextContent(contacto.getNumero());
			
			Element nuevoDepartamento = doc.createElement("departamento");
			nuevoDepartamento.setTextContent(contacto.getDepartamento());
			
			//agregar etiquetas al nuevo contacto
			nuevoContacto.appendChild(nuevoNombre);
			nuevoContacto.appendChild(nuevoApellido);
			nuevoContacto.appendChild(nuevoNumero);
			nuevoContacto.appendChild(nuevoDepartamento);
			
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
	
	public List<Contacto> buscarCuentas(String ord) {
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
					objContacto.setApellido(obtenerNodoValor("apellido",unElemento));
					objContacto.setNumero(obtenerNodoValor("numero",unElemento));
					objContacto.setDepartamento(obtenerNodoValor("departamento",unElemento));
				
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
		
		if (ord.equals("nombre")) {
			Collections.sort(agenda, new NombreContactoComparator());
		} else if (ord.equals("apellido")) {
			Collections.sort(agenda, new ApellidoContactoComparator());
		} else if (ord.equals("departamento")) {
			Collections.sort(agenda, new DepartamentoContactoComparator());
		}
		
		return agenda;
	}
	
	public Contacto buscarContacto(String nombre, String apellido) {
		Contacto objContacto = new Contacto();
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/agenda.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosContacto = doc.getElementsByTagName("contacto");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosContacto.getLength();i++) {
				Node contacto = nodosContacto.item(i);
				if (contacto.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) contacto;
					String n = obtenerNodoValor("nombre",unElemento);
					String a = obtenerNodoValor("apellido",unElemento);
					if (n.equals(nombre) && a.equals(apellido)) {
						objContacto.setNombre(obtenerNodoValor("nombre",unElemento));
						objContacto.setApellido(obtenerNodoValor("apellido",unElemento));
						objContacto.setNumero(obtenerNodoValor("numero",unElemento));
						objContacto.setDepartamento(obtenerNodoValor("departamento",unElemento));
					}
				}
			}
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		} 
		
		return objContacto;
	}
	
	public void actualizar(Contacto contactoAct, String nombre, String apellido) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/agenda.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosContacto = doc.getElementsByTagName("contacto");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosContacto.getLength();i++) {
				Node contacto = nodosContacto.item(i);
				if (contacto.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) contacto;
					String n = obtenerNodoValor("nombre",unElemento);
					String a = obtenerNodoValor("apellido",unElemento);
					if (n.equals(nombre) && a.equals(apellido)) {
						//agregamos una nueva etiqueta al documento
						Element nuevoContacto = doc.createElement("contacto");
						//agregamos etiquetas hijas
						Element nuevoNombre = doc.createElement("nombre");
						nuevoNombre.setTextContent(contactoAct.getNombre());
						
						Element nuevoApellido = doc.createElement("apellido");
						nuevoApellido.setTextContent(contactoAct.getApellido());
						
						Element nuevoNumero = doc.createElement("numero");
						nuevoNumero.setTextContent(contactoAct.getNumero());
						
						Element nuevoDepartamento = doc.createElement("departamento");
						nuevoDepartamento.setTextContent(contactoAct.getDepartamento());
			
						//agregar etiquetas a la nueva cuenta
						nuevoContacto.appendChild(nuevoNombre);
						nuevoContacto.appendChild(nuevoApellido);
						nuevoContacto.appendChild(nuevoNumero);
						nuevoContacto.appendChild(nuevoDepartamento);
	
						unElemento.getParentNode().replaceChild(nuevoContacto, unElemento);
					};
				}
			}
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
	
	public void borrar(String nombre, String apellido) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/agenda.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosContacto = doc.getElementsByTagName("contacto");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosContacto.getLength();i++) {
				Node contacto = nodosContacto.item(i);
				if (contacto.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) contacto;
					String n = obtenerNodoValor("nombre",unElemento);
					String a = obtenerNodoValor("apellido",unElemento);
					if (n.equals(nombre) && a.equals(apellido)) {
						unElemento.getParentNode().removeChild(unElemento);
					};
				}
			}
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

}
