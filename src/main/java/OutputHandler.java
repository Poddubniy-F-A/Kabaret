import Extensions.Nodes.*;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

public class OutputHandler implements AutoCloseable {
    private static final String
            pathToOutputDir = "../output/",
            outputH = "outputH.csv", outputU = "outputU.csv", outputV = "outputV.csv",
            outputHLimits = "h.csv",
            config = "config.csv";

    private static List<String[]> getStringsListOf(double[][] array) {
        return Arrays.stream(array).map(OutputHandler::getStringArrayFrom).collect(Collectors.toList());
    }

    private static String[] getStringArrayFrom(double[] array) {
        return Arrays.stream(array).mapToObj(String::valueOf).toArray(String[]::new);
    }

    private final CSVWriter hWriter, uWriter, vWriter, hLimitsWriter;
    private final Nodes nodes;
    private final ArrayList<Double> times;

    public OutputHandler(Nodes nodes, ArrayList<Double> times) throws IOException {
        hWriter = configuredWriter(outputH);
        uWriter = configuredWriter(outputU);
        vWriter = configuredWriter(outputV);
        hLimitsWriter = configuredWriter(outputHLimits);

        this.nodes = nodes;
        this.times = times;
    }

    @Override
    public void close() {
        try {
            CSVWriter configWriter = configuredWriter(config);
            configWriter.writeNext(times.stream().map(String::valueOf).toArray(String[]::new));
            configWriter.writeNext(getStringArrayFrom(nodes.getGridX()));
            configWriter.writeNext(getStringArrayFrom(nodes.getGridY()));
            configWriter.close();

            hWriter.close();
            uWriter.close();
            vWriter.close();

            hLimitsWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CSVWriter configuredWriter(String fileName) throws IOException {
        return new CSVWriter(new FileWriter(pathToOutputDir + fileName), ',', '\0');
    }

    public void addRecord() {
        writeValuesBy(hWriter, Node::h);
        writeValuesBy(uWriter, Node::u);
        writeValuesBy(vWriter, Node::v);

        hLimitsWriter.writeNext(new String[]{String.valueOf(nodes.getHMin()), String.valueOf(nodes.getHMax())});
    }

    private void writeValuesBy(CSVWriter writer, ToDoubleFunction<Node> function) {
        writer.writeAll(getStringsListOf(nodes.getValuesArrayBy(function)));
    }
}
