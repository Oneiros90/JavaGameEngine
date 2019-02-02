import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
 
/**
 * Resources è un gestore delle risorse interne di un'applicazione. La classe
 * fornisce diversi metodi per leggere immagini, suoni o testo
 *
 * @author Oneiros
 */
public class Resources {

    public static PrintStream Logger = new PrintStream(new OutputStream() {
        public void write(int b) { /*DO NOTHING*/ }
    });

    public static final String RUNNING_PATH;
    public static final boolean IS_RUNNING_FROM_JAR;
    public static final String CURRENT_URL_FORMAT;

    static {
        String path = Resources.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        RUNNING_PATH = path.endsWith("/") ? path.substring(0, path.length()-1) : path;
        IS_RUNNING_FROM_JAR = RUNNING_PATH.endsWith(".jar");
        CURRENT_URL_FORMAT = IS_RUNNING_FROM_JAR ? "jar:file:" + RUNNING_PATH + "!/%s" : "file:" + RUNNING_PATH + "/%s";
    }

    public static final ResourceManager<BufferedImage> Images = new ResourceManager<BufferedImage>(){

        @Override
        BufferedImage load(URL url) throws IOException {
            BufferedImage image = ImageIO.read(url);
            if (image == null){
                throw new IOException("Can't read image " + url);
            }
            return image;
        }
    };
    public static final ResourceManager<String> Text = new ResourceManager<String>(){

        @Override
        String load(URL url) throws IOException {

            InputStream byteStream = url.openStream();
            InputStreamReader txtStream = new InputStreamReader(byteStream, "UTF-8");
            BufferedReader reader = new BufferedReader(txtStream);

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            return builder.toString();
        }
    };

    /**
     * Costruttore privato (Resources è una classe statica)
     */
    private Resources() {
    }
 
    /**
     * Estrae un file interno al jar nella stessa cartella del jar e ne
     * restituisce il path. Nel caso in cui il programma risulti non essere
     * eseguito da un jar ma da un file .class, il metodo ritorna direttamente
     * il path del file presente nelle risorse del file .class
     *
     * @param path Il path del file interno al jar
     * @return Il path del file estratto
     */
    public static String extract(String path) throws IOException {
        return extract(path, null);
    }
 
    /**
     * Estrae un file interno al jar nella cartella specificata e ne restituisce
     * il path. Nel caso in cui il programma risulti non essere eseguito da un
     * jar ma da un file .class, il metodo ritorna direttamente il path del file
     * presente nelle risorse del file .class
     *
     * @param path Il path del file interno al jar
     * @param destinationPath Il path della cartella di destinazione
     * @return Il path del file estratto
     */
    public static String extract(String path, String destinationPath) throws IOException {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (IS_RUNNING_FROM_JAR) {
            if (destinationPath == null || destinationPath.isEmpty()) {
                destinationPath = RUNNING_PATH.substring(0, RUNNING_PATH.lastIndexOf("/"));
            }
            JarFile jar = new JarFile(RUNNING_PATH);
            JarEntry file = jar.getJarEntry(path);
            String fileName = file.getName();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
            File destination = new File(destinationPath + File.separator + fileName);
            if (!destination.exists()) {
                if (file.isDirectory()) {
                    destination.mkdir();
                }
                try (InputStream input = jar.getInputStream(file);
                    FileOutputStream output = new FileOutputStream(destination)) {
                    while (input.available() > 0) {
                        output.write(input.read());
                    }
                }
            }
            return destination.getAbsolutePath();
        } else {
            return RUNNING_PATH + path;
        }
    }
 
    /**
     * Restituisce l'URL ad una risorsa localizzata tramite il suo path relativo alla cartella src
     *
     * @return L'url di una risorsa nel progetto
     */
    public static URL getResourceURL(String resource) {
        if (resource.startsWith("/")) resource = resource.substring(1);
        try {
            return new URL(String.format(CURRENT_URL_FORMAT, resource));
        } catch (MalformedURLException e) {
            return null;
        }
    }
 
    /**
     * Apre un InputStream su una risorsa localizzata tramite il suo path relativo alla cartella src
     *
     * @return Un InputStream su una risorsa del progetto
     */
    public static InputStream getResourceAsStream(String resource) {
        try {
            URL url = getResourceURL(resource);
            if (url == null) return null;
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }

    private static void log(String log){
        Logger.println("[Resources] " + log);
    }

    public static abstract class ResourceManager<T> {

        protected HashMap<String, ResourceEntry> map = new HashMap<>();

        public void prepare(String path){
            this.prepare(path, path);
        }

        public void prepare(String path, String alias){
            ResourceEntry entry = new ResourceEntry();
            entry.path = path;
            this.map.put(alias, entry);
            log("REGISTERED: " + alias + " (PATH: " + path + ")");
        }

        public void prepareAll(String folder){
            if (!folder.endsWith("/")) folder += "/";
            URL url = getResourceURL(folder);
            if (url == null) return;
            try {
                if (IS_RUNNING_FROM_JAR){
                    Enumeration jarFiles = new JarFile(RUNNING_PATH).entries();
                    String regex = "^" + url.toString().split("!/")[1] + "(.+)";
                    JarEntry entry;
                    while (jarFiles.hasMoreElements()) {
                        entry = (JarEntry) jarFiles.nextElement();
                        if (entry != null) {
                            String fileName = entry.getName();
                            if (fileName.matches(regex) && !entry.isDirectory()){
                                prepare(fileName);
                            }
                        }
                    }
                } else {
                    File dir = new File(URLDecoder.decode(url.getFile(), "UTF-8"));
                    if (dir.isDirectory()){
                        File[] files = dir.listFiles();
                        for (File file : files) {
                            String relativePath = file.getPath().replace(RUNNING_PATH + "/", "");
                            if (file.isFile()) {
                                prepare(relativePath);
                            } else if (file.isDirectory()) {
                                prepareAll(relativePath);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public T get(String alias){
            ResourceEntry entry = this.map.get(alias);
            if (entry == null) {
                this.prepare(alias);
                entry = this.map.get(alias);
            }
            if (entry.resource == null){
                URL url = getResourceURL(entry.path);
                try {
                    log("LOADING: " + entry.path);
                    entry.resource = load(url);
                    log("DONE");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return entry.resource;
        }

        public void release(String alias){
            this.map.remove(alias);
        }

        public void loadEverything(){
            for (String key : this.map.keySet()){
                get(key);
            }
        }

        abstract T load(URL url) throws IOException;

        private class ResourceEntry {
            String path;
            T resource;
        }
    }
}