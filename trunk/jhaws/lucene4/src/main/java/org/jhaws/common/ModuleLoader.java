package org.jhaws.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * java (Mustang) compatible service loader.<br>
 * <br>
 * <u><b>routine:</b></u><br>
 * <br>
 * create an service Interface and implement classes for it<br>
 * put the fully qualified names in a line terminated file named as the fully qualified name of the service (no extension, but does have . characters)
 * in the directory /META-INF/services/ in the jar<br>
 * you can have implementations for this interface in any jar file (all these jars have such a file in the /META-INF/services/ directory)<br>
 * you can have different types of implementations for an interface with key determined by the {@link Object#toString()} function or multiple keys by
 * splitting the toString by the ; sign<br>
 * these implementations must have a default constructor and the functions are determined by the interface including getters and setters for
 * overriding presets<br>
 * use this class to load everything on first use (caching), reload etc.<br>
 * <br>
 * {@link #getImplementations()} for any and every implementation<br>
 * {@link #getImplementation(String)} and {@link #getImplementationsIterator(String)} for a specific implementation determined by the toString of the
 * class(the objects created are cached); if you want a new object any ways, use {@link #getNewImplementation(String)}<br>
 * use {@link #reload()} if you load a har after startup if that jar (can) contain service implementations<br>
 * {@link #getKeys()} gives all found keys (toString)<br>
 * {@link #getName()} gives the fully qualified name of the Interface<br>
 * {@link #supports(String)} if a key is supported at all<br>
 * {@link #getModule()} returns the Interface<br>
 * <br>
 * <u><b>example:</b></u><br>
 * interface.ImageLoader<br>
 * classes.PngLoader implements interface.ImageLoader and has toString returns "png"<br>
 * classes.JpegLoader implements interface.ImageLoader and has toString returns "jpg;jpeg"<br>
 * file /META-INF/services/interface.ImageLoader contains lines classes.PngLoader and classes.JpegLoader<br>
 * ModuleLoader&lt;interface.ImageLoader&gt; imageLoaders = new ModuleLoader(interface.ImageLoader);<br>
 * imageLoaders.getImplementation("png") returns a cached instance of class classes.PngLoader<br>
 * imageLoaders.getImplementation("jpg") and imageLoaders.getImplementation("jpeg") return each another cached instance of class classes.JpegLoader<br>
 * imageLoaders.getNewImplementation("png") returns always an new object of class classes.PngLoader<br>
 * 
 * @author Jurgen De Landsheer
 * 
 * @param <T> class type
 */
public class ModuleLoader<T> {
    /**
     * module factory
     * 
     * @author Jurgen De Landsheer
     */
    static class ModuleFactory {
        /** singleton */
        private static ModuleFactory instance = new ModuleFactory();

        /**
         * gets instance
         * 
         * @return Returns the instance.
         */
        public static ModuleFactory getInstance() {
            return ModuleFactory.instance;
        }

        /** Map Class on ModuleLoader */
        private final Map<Class<?>, ModuleLoader<?>> moduleLoaders = new HashMap<Class<?>, ModuleLoader<?>>();

        /**
         * Creates a new ModuleFactory object.
         */
        private ModuleFactory() {
            super();
        }

        /**
         * adds a module loader
         * 
         * @param <P>
         * @param serviceInterface serviceInterface
         * @param moduleLoader ModuleLoader of type P
         */
        public <P> void addModuleLoader(Class<P> serviceInterface, ModuleLoader<P> moduleLoader) {
            this.moduleLoaders.put(serviceInterface, moduleLoader);
        }

        /**
         * gets moduleLoaders
         * 
         * @param <P>
         * @param serviceInterface serviceInterface
         * 
         * @return Returns the moduleLoaders.
         */
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public <P> ModuleLoader<P> getModuleLoader(Class<P> serviceInterface) {
            return (ModuleLoader<P>) this.moduleLoaders.get(serviceInterface);
        }
    }

    /** "/META-INF/services/" */
    private static final String SERVICES_CONFIG_LOCATION = "META-INF/services/";

    /**
     * gets module loader for given interface
     * 
     * @param <T> type Class T
     * @param moduleInterface interface of type T
     * 
     * @return ModuleLoader of type T
     */
    public static <T> ModuleLoader<T> getModuleLoader(Class<T> moduleInterface) {
        ModuleLoader<T> returning = ModuleFactory.getInstance().getModuleLoader(moduleInterface);

        if (returning == null) {
            returning = new ModuleLoader<T>(moduleInterface);
            ModuleFactory.getInstance().addModuleLoader(moduleInterface, returning);
        }

        return returning;
    }

    /**
     * get loader for interface (name from interface)
     * 
     * @param moduleInterface name from interface
     * 
     * @return ModuleLoader
     */
    public static ModuleLoader<?> getModuleLoader(String moduleInterface) {
        try {
            return ModuleLoader.getModuleLoader(Class.forName(moduleInterface));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();

            return null;
        }
    }

    /** serviceInterface */
    private final Class<T> serviceInterface;

    /** internal use */
    private final Map<String, Map<String, T>> implementations = new HashMap<String, Map<String, T>>();

    /** Class name of serviceInterface */
    private final String serviceInterfaceClassName;

    /**
     * Creates a new ModuleLoader object.
     * 
     * @param serviceInterface serviceInterface
     */
    private ModuleLoader(Class<T> serviceInterface) {
        this.serviceInterface = serviceInterface;
        this.serviceInterfaceClassName = serviceInterface.getName();

        ModuleFactory.getInstance().addModuleLoader(serviceInterface, this);
        this.reload();
    }

    /**
     * TODO DOCUMENT ME!
     * 
     * @return TODO DOCUMENT ME!
     */
    public Set<T> getAllImplementations() {
        Set<T> tmp = new HashSet<T>();

        for (String key : this.implementations.keySet()) {
            tmp.addAll(this.getImplementations(key));
        }

        return tmp;
    }

    /**
     * gets any ONE found implementations for key determined by the {@link Object#toString()} of the class (split by the ; sign); cached
     * 
     * @param key String
     * 
     * @return T
     */
    public T getImplementation(String key) {
        Collection<T> list = this.getImplementations(key);

        if ((list == null) || (list.size() == 0)) {
            return null;
        }

        return list.iterator().next();
    }

    /**
     * gets all and any implementations of a specific service
     * 
     * @return Returns the implementations.
     */
    public Map<String, Collection<T>> getImplementations() {
        Map<String, Collection<T>> tmp = new HashMap<String, Collection<T>>();

        for (String key : this.implementations.keySet()) {
            tmp.put(key, this.getImplementations(key));
        }

        return tmp;
    }

    /**
     * gets all found implementations for key determined by the {@link Object#toString()} of the class (split by the ; sign); cached
     * 
     * @param key String
     * 
     * @return Collection
     */
    public Collection<T> getImplementations(String key) {
        if (this.implementations.get(key) == null) {
            return new HashSet<T>();
        }

        return this.implementations.get(key).values();
    }

    /**
     * gets all found implementations for key determined by the {@link Object#toString()} of the class (split by the ; sign); cached
     * 
     * @param key String
     * 
     * @return Iterator
     */
    public Iterator<T> getImplementationsIterator(String key) {
        Collection<T> tmp = this.getImplementations(key);

        if (tmp == null) {
            return new Iterator<T>() {
                /**
                 * 
                 * @see java.util.Iterator#hasNext()
                 */
                @Override
                public boolean hasNext() {
                    return false;
                }

                /**
                 * 
                 * @see java.util.Iterator#next()
                 */
                @Override
                public T next() {
                    return null;
                }

                /**
                 * 
                 * @see java.util.Iterator#remove()
                 */
                @Override
                public void remove() {
                    return;
                }
            };
        }

        return this.getImplementations(key).iterator();
    }

    /**
     * gives all found keys (toString)
     * 
     * @return String[]
     */
    public String[] getKeys() {
        return this.implementations.keySet().toArray(new String[0]);
    }

    /**
     * gets serviceInterface
     * 
     * @return Returns the serviceInterface.
     */
    public Class<T> getModule() {
        return this.serviceInterface;
    }

    /**
     * gives the fully qualified name of the Interface
     * 
     * @return Returns the serviceInterfaceClassName.
     */
    public String getName() {
        return this.serviceInterfaceClassName;
    }

    /**
     * gets new implementation for key (even when cached)
     * 
     * @param key String
     * 
     * @return T
     */
    public T getNewImplementation(String key) {
        try {
            T old = this.getImplementation(key);
            T newOne = this.serviceInterface.cast(Class.forName(old.getClass().getName()).newInstance());

            return newOne;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * gets a new instance of each implementation for this loader
     * 
     * @throws IOException IOException
     */
    private void loadImplementations() throws IOException {
        ClassLoader cl = this.serviceInterface.getClassLoader();

        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }

        Enumeration<URL> enumeration = null;

        try {
            String search = (ModuleLoader.SERVICES_CONFIG_LOCATION + this.serviceInterfaceClassName);
            enumeration = cl.getResources(search);
        } catch (NullPointerException ex) {
            // no implementations found
            return;
        }

        while (enumeration.hasMoreElements()) {
            try {
                URL url = enumeration.nextElement();
                InputStream in = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line = reader.readLine();

                while (line != null) {
                    line = line.trim();

                    if (!line.startsWith("#") && (line.trim().length() > 1)) {

                        Object o = null;

                        try {
                            Class<?> c = Class.forName(line);
                            o = c.newInstance();

                            if (!this.serviceInterface.isInstance(o)) {
                                line = reader.readLine();

                                continue;
                            }
                        } catch (ClassNotFoundException cnfe) {
                            cnfe.printStackTrace();
                            line = reader.readLine();

                            continue;
                        } catch (InstantiationException ie) {
                            ie.printStackTrace();
                            line = reader.readLine();

                            continue;
                        } catch (IllegalAccessException iae) {
                            iae.printStackTrace();
                            line = reader.readLine();

                            continue;
                        }

                        T implementation = this.serviceInterface.cast(o);

                        for (String key : implementation.toString().split(";")) {

                            Map<String, T> implementationList = this.implementations.get(key);

                            if (implementationList == null) {
                                implementationList = new HashMap<String, T>();
                                this.implementations.put(key, implementationList);
                            }

                            implementationList.put(implementation.getClass().getName(), implementation);
                        }
                    }

                    line = reader.readLine();
                }

                in.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    /**
     * reloads all implementations
     */
    public void reload() {
        try {
            this.loadImplementations();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * is implementation for key found?
     * 
     * @param key String
     * 
     * @return boolean
     */
    public boolean supports(String key) {
        return this.getImplementation(key) != null;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getClass().getName() + "[" + this.serviceInterfaceClassName + "]"; //$NON-NLS-2$
    }
}
