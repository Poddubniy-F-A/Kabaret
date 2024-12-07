import Extensions.Node;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

import static Extensions.GridsFunctions.getStringGridFrom;

public class OutputHandler implements AutoCloseable {
    private final CSVWriter hWriter, uWriter, vWriter;

    public OutputHandler(String pathToConfig,
                         double[] gridT, double[] gridX, double[] gridY,
                         String pathToH, String pathToU, String pathToV) throws IOException {
        CSVWriter configWriter = configuredWriter(pathToConfig);
        configWriter.writeNext(getStringGridFrom(gridT));
        configWriter.writeNext(getStringGridFrom(gridX));
        configWriter.writeNext(getStringGridFrom(gridY));
        configWriter.close();

        hWriter = configuredWriter(pathToH);
        uWriter = configuredWriter(pathToU);
        vWriter = configuredWriter(pathToV);
    }

    private CSVWriter configuredWriter(String path) throws IOException {
        return new CSVWriter(new FileWriter(path), ',', '\0');
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

    public void addRecord(Node[][] slice) {
        int rowLength = slice[0].length;

        for (Node[] nodes : slice) {
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
}
