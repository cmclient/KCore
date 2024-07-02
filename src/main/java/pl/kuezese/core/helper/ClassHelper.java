package pl.kuezese.core.helper;

import pl.kuezese.core.CorePlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class ClassHelper {

    public static List<Class<?>> find(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return Collections.emptyList();
        }

        Map<File, String> directoryToPackageName = new HashMap<>();
        ClassLoader cld = Thread.currentThread().getContextClassLoader();

        for (Class<?> clazz : classes) {
            String bundlePath = getBundlePath(clazz);
            if (bundlePath == null) continue;

            if (bundlePath.endsWith(".jar")) {
                return getClassesFromJar(bundlePath, classes);
            }

            processDirectories(clazz, cld, directoryToPackageName);
        }

        return directoryToPackageName.entrySet().stream()
                .flatMap(entry -> getClassesFromDirectory(entry.getKey(), entry.getValue()).stream())
                .collect(Collectors.toList());
    }

    private static void processDirectories(Class<?> clazz, ClassLoader cld, Map<File, String> directoryToPackageName) {
        try {
            String path = clazz.getPackage().getName().replace('.', File.separatorChar);
            Enumeration<URL> resources = cld.getResources(path);

            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(URLDecoder.decode(resource.getPath(), "UTF-8"));
                directoryToPackageName.put(directory, clazz.getPackage().getName());
            }
        } catch (IOException ex) {
            CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to process directories!", ex);
        }
    }

    private static List<Class<?>> getClassesFromDirectory(File directory, String packageName) {
        if (!directory.exists()) {
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(directory.list()))
                .filter(file -> file.endsWith(".class"))
                .map(file -> toClass(packageName, file))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Class<?> toClass(String packageName, String fileName) {
        try {
            return Class.forName(packageName + '.' + fileName.substring(0, fileName.length() - 6));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static List<Class<?>> getClassesFromJar(String jarPath, Class<?>[] classes) {
        List<Class<?>> result = new ArrayList<>();
        try (JarFile jarFile = new JarFile(jarPath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    for (Class<?> clazz : classes) {
                        String packageName = clazz.getPackage().getName().replace('.', '/');
                        if (entryName.startsWith(packageName)) {
                            String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
                            try {
                                result.add(Class.forName(className));
                            } catch (ClassNotFoundException ignored) {
                                // Log exception or handle it as necessary
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            CorePlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to load classes from jar!", ex);
        }
        return result;
    }

    public static String getBundlePath(Class<?> clazz) {
        ProtectionDomain pd = clazz.getProtectionDomain();
        CodeSource cs = pd != null ? pd.getCodeSource() : null;
        URL url = cs != null ? cs.getLocation() : null;
        return url != null ? url.getFile() : null;
    }
}