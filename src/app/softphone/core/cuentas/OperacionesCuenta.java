package app.softphone.core.cuentas;

import java.io.File;
import java.io.IOException;
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
import org.xml.sax.SAXException;

public class OperacionesCuenta {

	
	public static void main(String[] args) {
		Cuenta cuenta1 = new Cuenta("1111","192.168.1.100","123","Administracion");
		//Cuenta cuenta2 = new Cuenta("2222","192.168.1.100","456","Secretaria");
		OperacionesCuenta op = new OperacionesCuenta();
		op.crear(cuenta1);
		//op.crear(cuenta2);
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
			
			//agregar etiquetas a la nueva cuenta
			nuevaCuenta.appendChild(nuevoUsuario);
			nuevaCuenta.appendChild(nuevoServidor);
			nuevaCuenta.appendChild(nuevoPassword);
			nuevaCuenta.appendChild(nuevoNombre);
			
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


	public void actualizar(Cuenta cuenta) {
	}

	public List<Cuenta> BuscarCuentas() {
		return null;
	}


	public Cuenta BuscarCuenta(String nombre) {
		return null;
	}

	public void Borrar(String nombre) {
		
	}
	
	

}
