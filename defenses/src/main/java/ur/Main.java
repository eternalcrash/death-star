package ur;

import java.io.IOException;
import java.net.URI;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.ContextResolver;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

/**
 * Main class.
 *
 */
public class Main {
	public static final String app_root = "deathstar-defenses/";
	// Base URI the Grizzly HTTP server will listen on
	public static final URI BASE_URI = URI.create("http://0.0.0.0:8002/" + app_root);

	/**
	 * Starts Grizzly HTTP server exposing JAX-RS resources defined in this
	 * application.
	 * 
	 * @return Grizzly HTTP server.
	 */
	public static HttpServer startServer() {
		String resources = "ur";
		BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setBasePath("/" + app_root);
		beanConfig.setResourcePackage(resources);
		beanConfig.setScan(true);

		 final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
	     @SuppressWarnings("rawtypes")
		final ContextResolver jsonConfigResolver = moxyJsonConfig.resolver();
		
		final ResourceConfig resourceConfig = new ResourceConfig();
		resourceConfig.packages(resources);
		resourceConfig.register(io.swagger.jaxrs.listing.ApiListingResource.class);
		resourceConfig.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
		resourceConfig.register(MoxyJsonFeature.class);
		resourceConfig.register(jsonConfigResolver);
		

		//enable full trace in console logs
		Logger l = Logger.getLogger("org.glassfish.grizzly.http.server.HttpHandler");
		l.setLevel(Level.FINE);
		l.setUseParentHandlers(false);
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		l.addHandler(ch);
		
		
		// create and start a new instance of grizzly http server
		// exposing the Jersey application at BASE_URI
		return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, resourceConfig);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final HttpServer server = startServer();

		ClassLoader loader = Main.class.getClassLoader();
		CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
		docsHandler.setFileCacheEnabled(false);

		ServerConfiguration cfg = server.getServerConfiguration();
		cfg.addHttpHandler(docsHandler, "/swagger/");

		System.out.println(String.format(
				"Jersey app started with WADL available at\n" + "%sapplication.wadl\nHit Q to stop it...",
				BASE_URI));
		System.out.println(String.format("swagger io available at\n" + "0.0.0.0:8002/swagger/"));

		int c = 0;
		while(c != 255) {
			c = (char) System.in.read();
		}
		// close persistence ( hibernate sessionFactory )
		SessionFactoryHolder.closeSessionFactory();
		// and close server
		server.shutdown();
		System.exit(0);
	}

}
