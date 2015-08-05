package app.softphone.core.cuentas;

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

import app.softphone.core.cuentas.EstadoCuenta.Estado;

public class OperacionesCuenta {

	
	public static void main(String[] args) {
		Cuenta cuenta1 = new Cuenta("1111","192.168.1.100","123","Administracion",Estado.NO_REGISTRADO);
		Cuenta cuenta2 = new Cuenta("2222","192.168.1.100","456","Secretaria",Estado.REGISTRADO);
		Cuenta cuenta3 = new Cuenta("2222","192.168.1.102","456","Secretaria",Estado.REGISTRADO);
		OperacionesCuenta op = new OperacionesCuenta();
		//op.crear(cuenta1);
		//op.crear(cuenta2);
		List<Cuenta> c = new ArrayList<Cuenta>();
		Cuenta b;
		//op.borrar("Santi");
		//op.crear(cuenta1);
		//op.crear(cuenta2);
		op.actualizar(cuenta3, cuenta2.getNombre());
		c = op.buscarCuentas();
		//b = op.buscarCuenta("Secretaria");
		//System.out.println(b.getEstado().getDescr());
		System.out.println(c.get(0).getEstado().getDescr());
		System.out.println(c.get(1).getServidor());
	}
	
	
	public OperacionesCuenta() {
		
	}
	
	public void crear(Cuenta cuenta) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos el nodo padre "cuentas"
			Node nodoRaiz = doc.getDocumentElement();
			//agregamos una nueva etiqueta al documento
			Element nuevaCuenta = doc.createElement("cuenta");
			//agregamos etiquetas hijas
			Element nuevoUsuario = doc.createElement("usuario");
			nuevoUsuario.setTextContent(cuenta.getUsuario());
			
			Element nuevoServidor = doc.createElement("servidor");
			nuevoServidor.setTextContent(cuenta.getServidor());
			
			Element nuevoPassword = doc.createElement("password");
			nuevoPassword.setTextContent(cuenta.getPassword());
			
			Element nuevoNombre = doc.createElement("nombre");
			nuevoNombre.setTextContent(cuenta.getNombre());
			
			Element nuevoEstado = doc.createElement("estado");
			nuevoEstado.setTextContent(cuenta.getEstado().getDescr());
			
			//agregar etiquetas a la nueva cuenta
			nuevaCuenta.appendChild(nuevoUsuario);
			nuevaCuenta.appendChild(nuevoServidor);
			nuevaCuenta.appendChild(nuevoPassword);
			nuevaCuenta.appendChild(nuevoNombre);
			nuevaCuenta.appendChild(nuevoEstado);
			
			//relacionar la nueva cuenta con la etiqueta "cuentas"
			nodoRaiz.appendChild(nuevaCuenta);
			
			//transformamos esta estructura de datos en un archivo XML
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("xmlsrc/cuentas.xml"));
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
	
	public List<Cuenta> buscarCuentas() {
		List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
		
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosCuentas = doc.getElementsByTagName("cuenta");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosCuentas.getLength();i++) {
				Node cuenta = nodosCuentas.item(i);
				if (cuenta.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) cuenta;
					
					Cuenta objCuenta = new Cuenta();
					objCuenta.setUsuario(obtenerNodoValor("usuario",unElemento));
					objCuenta.setServidor(obtenerNodoValor("servidor",unElemento));
					objCuenta.setPassword(obtenerNodoValor("password",unElemento));
					objCuenta.setNombre(obtenerNodoValor("nombre",unElemento));
					String e = obtenerNodoValor("estado",unElemento);
					if (e.equals(Estado.REGISTRADO.getDescr())) {
						objCuenta.setEstado(Estado.REGISTRADO);
					} else {
						objCuenta.setEstado(Estado.NO_REGISTRADO);
					}
				
					listaCuentas.add(objCuenta);
				}
			}
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		}
		
		return listaCuentas;
	}
	
	public Cuenta buscarCuenta(String nombre) {
		Cuenta objCuenta = new Cuenta();
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosCuentas = doc.getElementsByTagName("cuenta");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosCuentas.getLength();i++) {
				Node cuenta = nodosCuentas.item(i);
				if (cuenta.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) cuenta;
					String n = obtenerNodoValor("nombre",unElemento);
					if (n.equals(nombre)) {
						objCuenta.setUsuario(obtenerNodoValor("usuario",unElemento));
						objCuenta.setServidor(obtenerNodoValor("servidor",unElemento));
						objCuenta.setPassword(obtenerNodoValor("password",unElemento));
						objCuenta.setNombre(obtenerNodoValor("nombre",unElemento));
						String e = obtenerNodoValor("estado",unElemento);
						if (e.equals(Estado.REGISTRADO.getDescr())) {
							objCuenta.setEstado(Estado.REGISTRADO);
						} else {
							objCuenta.setEstado(Estado.NO_REGISTRADO);
						}
					};
				}
			}
			
		} catch(ParserConfigurationException parseE) {
			System.out.println(parseE.getMessage());
		} catch(SAXException saxE) {
			System.out.println(saxE.getMessage());
		} catch(IOException ioE) {
			System.out.println(ioE.getMessage());
		} 
		
		return objCuenta;
	}
	
	public Boolean existeCuenta(Cuenta compCuenta, Cuenta editar) {
		Boolean existe = false;
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosCuentas = doc.getElementsByTagName("cuenta");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosCuentas.getLength();i++) {
				Node cuenta = nodosCuentas.item(i);
				if (cuenta.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) cuenta;
					String u = obtenerNodoValor("usuario",unElemento);
					String s = obtenerNodoValor("servidor",unElemento);
					String n = obtenerNodoValor("nombre",unElemento);
					if (editar == null) {
						if (u.equals(compCuenta.getUsuario()) && s.equals(compCuenta.getServidor())) {
							existe = true;
						};
						if (n.equals(compCuenta.getNombre())) {
							existe = true; 
						}
					} else {
						if (u.equals(editar.getUsuario()) && s.equals(editar.getServidor()) && n.equals(editar.getNombre())) {
							continue;
						} 
						if (u.equals(compCuenta.getUsuario()) && s.equals(compCuenta.getServidor())) {
							existe = true;
						};
						if (n.equals(compCuenta.getNombre())) {
							existe = true; 
						}
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
		
		return existe;
	}
	
	public void actualizar(Cuenta cuentaAct, String nombreAnt) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosCuentas = doc.getElementsByTagName("cuenta");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosCuentas.getLength();i++) {
				Node cuenta = nodosCuentas.item(i);
				if (cuenta.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) cuenta;
					String n = obtenerNodoValor("nombre",unElemento);
					if (n.equals(nombreAnt)) {
						//agregamos una nueva etiqueta al documento
						Element nuevaCuenta = doc.createElement("cuenta");
						//agregamos etiquetas hijas
						Element nuevoUsuario = doc.createElement("usuario");
						nuevoUsuario.setTextContent(cuentaAct.getUsuario());
						
						Element nuevoServidor = doc.createElement("servidor");
						nuevoServidor.setTextContent(cuentaAct.getServidor());
						
						Element nuevoPassword = doc.createElement("password");
						nuevoPassword.setTextContent(cuentaAct.getPassword());
						
						Element nuevoNombre = doc.createElement("nombre");
						nuevoNombre.setTextContent(cuentaAct.getNombre());
						
						Element nuevoEstado = doc.createElement("estado");
						nuevoEstado.setTextContent(cuentaAct.getEstado().getDescr());
						
						
						//agregar etiquetas a la nueva cuenta
						nuevaCuenta.appendChild(nuevoUsuario);
						nuevaCuenta.appendChild(nuevoServidor);
						nuevaCuenta.appendChild(nuevoPassword);
						nuevaCuenta.appendChild(nuevoNombre);
						nuevaCuenta.appendChild(nuevoEstado);
						
						unElemento.getParentNode().replaceChild(nuevaCuenta, unElemento);
					};
				}
			}
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("xmlsrc/cuentas.xml"));
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

	public void borrar(String nombre) {
		try {
			//clases necesarias para leer XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("xmlsrc/cuentas.xml"));
			
			//preparamos el archivo XML para leer los datos
			doc.getDocumentElement().normalize();
			//obtenemos todos los nodos de la etiqueta "cuentas"
			NodeList nodosCuentas = doc.getElementsByTagName("cuenta");
			//por cada nodo se obtienen los datosy se guardan en un objeto
			for (int i=0;i<nodosCuentas.getLength();i++) {
				Node cuenta = nodosCuentas.item(i);
				if (cuenta.getNodeType() == Node.ELEMENT_NODE) {
					Element unElemento = (Element) cuenta;
					String n = obtenerNodoValor("nombre",unElemento);
					if (n.equals(nombre)) {
						unElemento.getParentNode().removeChild(unElemento);
					};
				}
			}
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("xmlsrc/cuentas.xml"));
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
