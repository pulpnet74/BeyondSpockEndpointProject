/**
 * com.beyondspock.endpoint package contains the server and its support classes
 */
package com.beyondspock.endpoint;

import static com.beyondspock.endpoint.tool.RoddenberryFactory.DEFAULT_BACKLOG;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.DOMINION_PORT;
import static com.beyondspock.endpoint.tool.RoddenberryFactory.MAIN_CONTEXT;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.beyondspock.control.BeyondSpockController;
import com.beyondspock.endpoint.tool.BeyondSpockFilter;
import com.beyondspock.endpoint.tool.BeyondSpockHandler;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

// JMX support: uncomment the below imports to use JMX
//import static com.beyondspock.endpoint.tool.RoddenberryFactory.DEFAULT_NUMBER_OF_THREADS;
//import java.lang.management.ManagementFactory;
//import javax.management.InstanceAlreadyExistsException;
//import javax.management.JMException;
//import javax.management.MBeanRegistrationException;
//import javax.management.MBeanServer;
//import javax.management.MalformedObjectNameException;
//import javax.management.NotCompliantMBeanException;
//import javax.management.ObjectName;
//import com.beyondspock.jmx.TerraNova;

/**
 * @author Fabio Riberto
 * 
 *         This class implements the HTTP server itself
 *
 */
public class BeyondSpockServer {

	/**
	 * This method is the entry point of the server. When server.start() is
	 * called the server switches on and it is ready to accept requests
	 * 
	 * @param args
	 * @throws Exception
	 * 
	 */
	public static void main(String[] args) throws Exception {
		try {
			// Switch the server on
			HttpServer server = HttpServer.create(new InetSocketAddress(DOMINION_PORT), DEFAULT_BACKLOG);
			HttpContext context = server.createContext(MAIN_CONTEXT,
					new BeyondSpockHandler(BeyondSpockController.getInstance()));
			context.getFilters().add(new BeyondSpockFilter());
			server.setExecutor(Executors.newCachedThreadPool()); // Threads
																	// creation,
																	// reusing
																	// and
																	// optimization
			server.start();
			System.out.println("\\m/ >_< \\m/    BeyondSpockServer works for the Founders @ http://localhost:"
					+ server.getAddress().getPort() + "/" + "    \\m/ >_< \\m/");
			// JMX injection: uncomment and specialize below code to use JMX
//			 // Configure JMX
//			 MBeanServer mBeanServer =
//			 ManagementFactory.getPlatformMBeanServer(); // Instance
//			 // of
//			 // MBean
//			 // server
//			 TerraNova mBeanClient = new TerraNova(DEFAULT_NUMBER_OF_THREADS);
//			 ObjectName objName = new
//			 ObjectName("com.beyondspock.jmx:type=TerraNova");
//			 mBeanServer.registerMBean(mBeanClient, objName); // MBean client
//			 // registration
//			 do {
//			 // JMX tasks
//			 } while (mBeanClient.getThreadCount() != 0);
//			 } catch (JMException e) {
//			 System.err.println("JMX unexpected exception -"
//			 		+ "".concat(e.getMessage()));
		} catch (BindException e) {
			System.err.println("Expected BindException - ".concat(e.getMessage()));
		} catch (Exception e) {
			System.err.println("BeyondSpockServer reached the Great Barrier for the will of Founders");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
