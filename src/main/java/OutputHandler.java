import Extensions.Node;
import Extensions.Nodes;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static Extensions.GridsFunctions.getStringGridFrom;

public class OutputHandler implements AutoCloseable {
    private final CSVWriter hWriter, uWriter, vWriter;
    private final Nodes nodes;

    public OutputHandler(String pathToH, String pathToU, String pathToV, Nodes nodes) throws IOException {
        hWriter = configuredWriter(pathToH);
        uWriter = configuredWriter(pathToU);
        vWriter = configuredWriter(pathToV);

        this.nodes = nodes;
    }

    @Override
    public void close() {
        try {
            hWriter.close();
            uWriter.close();
            vWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRecord() {
        final int rowLength = nodes.getSizeX();

        for (Node[] nodes : nodes.getNodesArray()) {
            String[]
                    rowH = new String[rowLength],
                    rowU = new String[rowLength],
                    rowV = new String[rowLength];
            for (int i = 0; i < rowLength; i++) {
                rowH[i] = String.valueOf(nodes[i].h());
                rowU[i] = String.valueOf(nodes[i].u());
                rowV[i] = String.valueOf(nodes[i].v());
            }
            hWriter.writeNext(rowH);
            uWriter.writeNext(rowU);
            vWriter.writeNext(rowV);
        }
    }

    public void fillConfig(String pathToConfig, ArrayList<Double> times) throws IOException {
        String[] stringTimes = new String[times.size()];
        for (int i = 0; i < stringTimes.length; i++) {
            stringTimes[i] = times.get(i).toString();
        }

        CSVWriter configWriter = configuredWriter(pathToConfig);
        configWriter.writeNext(stringTimes);
        configWriter.writeNext(getStringGridFrom(nodes.getGridX()));
        configWriter.writeNext(getStringGridFrom(nodes.getGridY()));
        configWriter.close();
    }

    private CSVWriter configuredWriter(String path) throws IOException {
        return new CSVWriter(new FileWriter(path), ',', '\0');
    }
}
