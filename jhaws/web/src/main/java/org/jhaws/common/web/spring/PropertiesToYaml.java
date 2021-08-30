package org.jhaws.common.web.spring;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

// https://stackabuse.com/reading-and-writing-yaml-files-in-java-with-snakeyaml/
public class PropertiesToYaml {
    public static void main(String[] args) {
        try {
            System.out.println("properties: " + args[0]);
            System.out.println("    > yaml: " + args[1]);
            File from = new File(args[0]);
            File to = new File(args[1]);
            if (!from.exists()) {
                System.err.println("properties does not exist: " + args[0]);
            }
            if (to.exists()) {
                System.err.println("yaml does exist: " + args[1]);
            }
            Properties props = new Properties();
            props.load(new FileInputStream(from));
            PrintWriter writer = new PrintWriter(to);
            DumperOptions options = new DumperOptions();
            options.setIndent(2);
            options.setPrettyFlow(true);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            Map<String, Object> data = clean(props);
            String dumpAs = yaml.dumpAs(data, org.yaml.snakeyaml.nodes.Tag.MAP, DumperOptions.FlowStyle.AUTO);
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dumpAs.getBytes())));
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().equals("{")) {
                    continue;
                }
                if (line.trim().equals("}")) {
                    continue;
                }
                line = line.replace("'", "");
                if (line.trim().endsWith("{")) {
                    line = line.substring(0, line.lastIndexOf("{"));
                }
                if (line.trim().endsWith("}")) {
                    line = line.substring(0, line.lastIndexOf("}"));
                }
                if (line.trim().endsWith(",")) {
                    line = line.substring(0, line.lastIndexOf(","));
                }
                writer.write(line + "\r");
            }
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    private static Map<String, Object> clean(Properties props) {
        Map<String, Object> data = new LinkedHashMap<>();
        props.entrySet().stream().sorted((a, b) -> new CompareToBuilder().append(a.getKey(), b.getKey()).toComparison()).forEach(entry -> {
            String[] keyParts = entry.getKey().toString().split("\\.");
            Map<String, Object> node = data;
            for (int i = 0; i < keyParts.length - 1; i++) {
                @SuppressWarnings("unchecked")
                Map<String, Object> childNode = (Map<String, Object>) node.get(keyParts[i]);
                if (childNode == null) {
                    childNode = new LinkedHashMap<>();
                    node.put(keyParts[i], childNode);
                }
                node = childNode;
            }
            node.put(keyParts[keyParts.length - 1], entry.getValue());
        });
        return data;
    }
}
