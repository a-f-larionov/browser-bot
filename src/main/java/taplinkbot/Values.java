package taplinkbot;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Component
public class Values {

    final private Writer writer = new FileWriter("values.bin");

    public Values() throws IOException {
    }

    public void save(String name) {

    }

    public void load(String name) {

    }
}
