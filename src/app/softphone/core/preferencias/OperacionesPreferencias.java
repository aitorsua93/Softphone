package app.softphone.core.preferencias;

import java.io.File;
import java.io.IOException;

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



public class OperacionesPreferencias {
	
	public static void main(String[] args) {
		Configuracion p = new Configuracion();
		OperacionesPreferencias opPre = new OperacionesPreferencias();
		p = opPre.obtenerPreferencias();
		System.out.println(p.getDesvioSiempre());
		System.out.println(p.getDesvioTimeout());
		System.out.println(p.getURIdesvio());
		System.out.println(p.getTimeoutDesvio());
		System.out.println(p.getCapturaOpcion());
		System.out.println(p.getSecCaptura());
		Configuracion p1 = new Configuracion("Si","No","8003@10.10.0.100","20","Si","#75#");
		opPre.actualizar(p1);
		p = opPre.obtenerPreferencias();
		System.out.println(p.getDesvioSiempre());
		System.out.println(p.getDesvioTimeout());
		System.out.println(p.getURIdesvio());
		System.out.println(p.getTimeoutDesvio());
		System.out.println(p.getCapturaOpcion());
		System.out.println(p.getSecCaptura());
	}
	
	public OperacionesPreferencias() {
		
	}
	
	public static String obtenerNodoValor(String strTag, Element eCuenta) {
		Node nValor = (Node)eCuenta.getElementsByTagName(strTag).item(0).getFirstChild();
		return nValor.getNodeValue();
	}
	
	public Configuracion obtenerPreferencias() {
		Configuracion objPreferencias = new Configuracion();
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/preferencias.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "configuracion"
			NodeList nodosPreferencias = doc.getElementsByTagName("configuracion");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			Node preferencias = nodosPreferencias.item(0);
			if (preferencias.getNodeType() == Node.ELEMENT_NODE) {
				Element unElemento = (Element) preferencias;
				objPreferencias.setDesvioSiempre(obtenerNodoValor("desvioSiempre",unElemento));
				objPreferencias.setDesvioTimeout(obtenerNodoValor("desvioTimeout",unElemento));
				objPreferencias.setURIdesvio(obtenerNodoValor("URIdesvio",unElemento));
				objPreferencias.setTimeoutDesvio(obtenerNodoValor("timeoutDesvio",unElemento));
				objPreferencias.setCapturaOpcion(obtenerNodoValor("capturaOpcion",unElemento));
				objPreferencias.setSecCaptura(obtenerNodoValor("secCaptura",unElemento));
			}	
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		} 
		
		return objPreferencias;
	}
	
	public void actualizar(Configuracion p) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/preferencias.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "configuracion"
			NodeList nodosCuentas = doc.getElementsByTagName("configuracion");
			//por cada nodo se obtienen los datos y se guardan en un objeto
				Node preferencias = nodosCuentas.item(0);
				if (preferencias.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) preferencias;
					//agregamos una nueva etiqueta al documento
					Element nuevaConfiguracion = doc.createElement("configuracion");
					//agregamos etiquetas hijas
					Element nuevoDesvioSiempre = doc.createElement("desvioSiempre");
					nuevoDesvioSiempre.setTextContent(p.getDesvioSiempre());
						
					Element nuevoDesvioTimeout = doc.createElement("desvioTimeout");
					nuevoDesvioTimeout.setTextContent(p.getDesvioTimeout());
						
					Element nuevoURIdesvio = doc.createElement("URIdesvio");
					nuevoURIdesvio.setTextContent(p.getURIdesvio());
						
					Element nuevoTimeoutDesvio = doc.createElement("timeoutDesvio");
					nuevoTimeoutDesvio.setTextContent(p.getTimeoutDesvio());
						
					Element nuevoCapturaOpcion = doc.createElement("capturaOpcion");
					nuevoCapturaOpcion.setTextContent(p.getCapturaOpcion());
					
					Element nuevoSecCaptura = doc.createElement("secCaptura");
					nuevoSecCaptura.setTextContent(p.getSecCaptura());
						
						
					//agregar etiquetas a la nueva cuenta
					nuevaConfiguracion.appendChild(nuevoDesvioSiempre);
					nuevaConfiguracion.appendChild(nuevoDesvioTimeout);
					nuevaConfiguracion.appendChild(nuevoURIdesvio);
					nuevaConfiguracion.appendChild(nuevoTimeoutDesvio);
					nuevaConfiguracion.appendChild(nuevoCapturaOpcion);
					nuevaConfiguracion.appendChild(nuevoSecCaptura);
						
					unElemento.getParentNode().replaceChild(nuevaConfiguracion, unElemento);
				}
				
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("xmlsrc/preferencias.xml"));
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
