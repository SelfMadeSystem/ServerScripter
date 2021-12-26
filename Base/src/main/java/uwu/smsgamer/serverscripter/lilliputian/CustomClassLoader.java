package uwu.smsgamer.serverscripter.lilliputian;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * A custom class loader that can load classes from given jars.
 */
public class CustomClassLoader extends URLClassLoader {
    public CustomClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public CustomClassLoader(URL[] urls) {
        super(urls);
    }

    public CustomClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public void loadJar(URL url) {
        try {
            this.addURL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadJar(String path) {
        try {
            this.loadJar(new File(path).toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadJar(File file) {
        try {
            this.loadJar(file.toURI().toURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
