package com.jpm.gcc.e2e.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StreamUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ResourceUtil {

    private static PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper("${", "}", ":", true);

    private ResourceUtil() {
    }

    private static Resource[] seekAllResource(String path) {
        return new Resource[]{new FileSystemResource(Paths.get(path).toFile()), new ClassPathResource(path)};
    }

    public static Resource findResource(String path) throws IOException {
        for (Resource resource : seekAllResource(path)) {
            if (resource.exists()) {
                return resource;
            }
        }
        throw new IOException(path + "is not found");
    }

    public static String loadTextResource(String path) throws IOException {
        Resource resource = findResource(path);
        try(InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        }
    }

    public static byte[] loadByteResource(String path) throws IOException {
        Resource resource = findResource(path);
        try(InputStream inputStream = resource.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream);
        }
    }

    public static String loadTextResourceWithPlaceHolders(String path, Properties values) throws IOException {
       String content = loadTextResource(path);
       return replacePlaceHolders(content,values);
    }

    public static String replacePlaceHolders(String content, Properties values){
        return placeholderHelper.replacePlaceholders(content, values);
    }

    public static void writeToFile(Path filePath, String messageText) throws IOException {
        Files.createDirectories(filePath.getParent());
        try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            writer.write(messageText);
            writer.flush();
        }
    }

    public static Properties loadProperties(String path){
        Properties properties = new Properties();
        for(Resource resource : seekAllResource(path)){
            try{
                PropertiesLoaderUtils.fillProperties(properties, resource);
            }catch(IOException e){
                properties.isEmpty();
            }
        }
        return properties;
    }

    public static void loadPropertiesTo(String path, Properties properties) throws IOException{
        Properties loadedProperties = ResourceUtil.loadProperties(path);
        loadedProperties.forEach((key, value)->{
            String val = ResourceUtil.replacePlaceHolders(String.valueOf(value),properties);
            properties.setProperty(String.valueOf(key),val);
        });
    }

}
