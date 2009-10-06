package chameleon.oo.language;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * This class represents the classpath as set by the user during VM startup.
 *
 * @author Yves Vandewoude
 * @author Marko van Dooren
 */

public class ClassPath {

    // Hashtable containing the names of all the packages in the classpath.
    // All items in this hashtable have the same key as value and are of the form (packagename, packagename)
    private Hashtable $packages = new Hashtable();
    private static ClassPath $instance = null;

    public static ClassPath getInstance() {
        if ($instance == null)
            $instance = new ClassPath();
        return $instance;
    }

    private ClassPath() {
        StringTokenizer st = new StringTokenizer(System.getProperty("java.class.path"),
                System.getProperty("path.separator"));
        while (st.hasMoreTokens()) {
            String currentItem = st.nextToken();
            if (currentItem.lastIndexOf(".jar") == (currentItem.length() - 4))
                addPackagesFromJar(currentItem);
            else
                addPackagesFromDir(currentItem);
        }
    }

    /*
     * Add all packages inside a jar to the hashtable.
     * If $includeEmptyPackages, all packages are included who have at least one subpackage with a class.
     * If !$includeEmptyPackages, only packages are included that have at least one class themselves.
     * e.g. "java" is only included if $includeEmptyPackages == true, since it does not contain classes but
     * "java.lang" does.
     *
     * param jarName The name of the jar-file to add
     */
    private void addPackagesFromJar(String jarName) {
        try {
            JarFile jf = new JarFile(jarName);
            Enumeration entries = jf.entries();
            while (entries.hasMoreElements()) {
                String entry = ((ZipEntry) entries.nextElement()).toString();
                if (entry.lastIndexOf(".class") == (entry.length() - 6)) {
                    if (entry.lastIndexOf('/') != -1) {
                        String dir = entry.substring(0, entry.lastIndexOf('/')).replace('/', '.');
                            StringTokenizer st = new StringTokenizer(dir, ".");
                            String currentPackageName = null;
                            while (st.hasMoreTokens()) {
                                String current = st.nextToken();
                                if (currentPackageName == null)
                                    currentPackageName = current;
                                else
                                    currentPackageName = currentPackageName + "." + current;
                                $packages.put(currentPackageName, currentPackageName);
                            }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Add all packages that exist below a given directory to the hashtable.
     * If $includeEmptyPackages, all packages are included who have at least one subpackage with a class.
     * If !$includeEmptyPackages, only packages are included that have at least one class themselves.
     * e.g. "java" is only included if $includeEmptyPackages == true, since it does not contain classes but
     * "java.lang" does.
     *
     * param dirName The name of the directory to add
     */
    private void addPackagesFromDir(String dirName) {
        // System.out.println("Processing: " + dirName);
        File f = new File(dirName);
        Set s = new HashSet();
        addAllClassFiles(s, f);
        Iterator i = s.iterator();
        while (i.hasNext()) {
            String item = ((String) i.next()).substring(dirName.length() + 1);
            int dirIndex = item.lastIndexOf(File.separator);
            if (dirIndex != -1) {
                String dir = item.substring(0, dirIndex).replace(File.separator.charAt(0), '.');
                    StringTokenizer st = new StringTokenizer(dir, ".");
                    String currentPackageName = null;
                    while (st.hasMoreTokens()) {
                        String current = st.nextToken();
                        if (currentPackageName == null)
                            currentPackageName = current;
                        else
                            currentPackageName = currentPackageName + "." + current;
                        $packages.put(currentPackageName, currentPackageName);
                    }
            }

        }
    }

    /*
     * Recursively adds the filenames of all classfiles below a given directory (given as a file) to a given set.
     */
    private void addAllClassFiles(Set s, File f) {
        if (f.isFile()) {
            if (f.getName().indexOf(".class") != -1)
                s.add(f.getAbsolutePath());
        } else {
            File[] filesInDir = f.listFiles();
            for (int index = 0; index < filesInDir.length; index++) {
                addAllClassFiles(s, filesInDir[index]);
            }
        }
    }


    /**
     * Checks whether a package exists in the classpath.
     * <p/>
     * If includeEmptyPackages was set to true during construction, all packages who have at least one
     * subpackage that contains a class are considered to exist, otherwise only packages are included
     * that have at least one class themselves.
     * e.g. "java" is only included if includeEmptyPackages == true, since it does not contain classes but
     * "java.lang" does.
     *
     * @param packageName Name of the package to check for
     * @return Whether the package exists in the classpath.
     */
    public boolean packageExists(String packageName) {
        return ($packages.get(packageName) != null);
    }

    /**
     * Returns the names of all known packages in an Enumeration
     *
     * @return Enumeration containing the names of the known packages.
     */
    public Enumeration getAllPackages() {
        return $packages.elements();
    }
}
