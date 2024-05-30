import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SparseMatrix {
    private int numRows;
    private int numCols;
    private Map<Pair, Integer> elements;

    public SparseMatrix() {
        elements = new HashMap<>();
    }

    public SparseMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        elements = new HashMap<>();
    }

    public SparseMatrix(String matrixFilePath) throws IOException {
        elements = new HashMap<>();
        loadFromFile(matrixFilePath);
    }

    private void loadFromFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            numRows = Integer.parseInt(reader.readLine().split("=")[1]);
            numCols = Integer.parseInt(reader.readLine().split("=")[1]);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.charAt(0) != '(' || line.charAt(line.length() - 1) != ')') {
                        throw new IllegalArgumentException("Input file has wrong format");
                    }

                    String[] parts = line.substring(1, line.length() - 1).split(",");
                    if (parts.length != 3) {
                        throw new IllegalArgumentException("Input file has wrong format");
                    }

                    int row = Integer.parseInt(parts[0].trim());
                    int col = Integer.parseInt(parts[1].trim());
                    int value = Integer.parseInt(parts[2].trim());

                    setElement(row, col, value);
                }
            }
        }
    }

    public int getElement(int row, int col) {
        return elements.getOrDefault(new Pair(row, col), 0);
    }

    public void setElement(int row, int col, int value) {
        Pair key = new Pair(row, col);
        if (value != 0) {
            elements.put(key, value);
        } else {
            elements.remove(key);
        }
    }

    public SparseMatrix add(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrix dimensions must be the same for addition");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        for (Map.Entry<Pair, Integer> entry : this.elements.entrySet()) {
            Pair key = entry.getKey();
            int value = entry.getValue() + other.getElement(key.row, key.col);
            result.setElement(key.row, key.col, value);
        }

        for (Map.Entry<Pair, Integer> entry : other.elements.entrySet()) {
            Pair key = entry.getKey();
            if (!this.elements.containsKey(key)) {
                result.setElement(key.row, key.col, entry.getValue());
            }
        }

        return result;
    }

    public SparseMatrix subtract(SparseMatrix other) {
        if (this.numRows != other.numRows || this.numCols != other.numCols) {
            throw new IllegalArgumentException("Matrix dimensions must be the same for subtraction");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, this.numCols);
        for (Map.Entry<Pair, Integer> entry : this.elements.entrySet()) {
            Pair key = entry.getKey();
            int value = entry.getValue() - other.getElement(key.row, key.col);
            result.setElement(key.row, key.col, value);
        }

        for (Map.Entry<Pair, Integer> entry : other.elements.entrySet()) {
            Pair key = entry.getKey();
            if (!this.elements.containsKey(key)) {
                result.setElement(key.row, key.col, -entry.getValue());
            }
        }

        return result;
    }

    public SparseMatrix multiply(SparseMatrix other) {
        if (this.numCols != other.numRows) {
            throw new IllegalArgumentException("Matrix dimensions are not suitable for multiplication");
        }

        SparseMatrix result = new SparseMatrix(this.numRows, other.numCols);
        for (Map.Entry<Pair, Integer> entry : this.elements.entrySet()) {
            Pair key = entry.getKey();
            int row1 = key.row;
            int col1 = key.col;
            int value1 = entry.getValue();

            for (int col2 = 0; col2 < other.numCols; col2++) {
                int value2 = other.getElement(col1, col2);
                if (value2 != 0) {
                    result.setElement(row1, col2, result.getElement(row1, col2) + value1 * value2);
                }
            }
        }

        return result;
    }

    public void toFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("rows=" + numRows + "\n");
            writer.write("cols=" + numCols + "\n");
            for (Map.Entry<Pair, Integer> entry : elements.entrySet()) {
                Pair key = entry.getKey();
                int value = entry.getValue();
                writer.write("(" + key.row + ", " + key.col + ", " + value + ")\n");
            }
        }
    }

    private static class Pair {
        int row, col;

        Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return row == pair.row && col == pair.col;
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java SparseMatrix <matrix1_file_path> <matrix2_file_path> <result_directory>");
            return;
        }

        String matrix1Path = args[0];
        String matrix2Path = args[1];
        String resultDir = args[2];

        File resultDirectory = new File(resultDir);
        if (!resultDirectory.exists() || !resultDirectory.isDirectory()) {
            System.out.println("Error: " + resultDir + " is not a directory or does not exist.");
            return;
        }

        try {
            SparseMatrix matrix1 = new SparseMatrix(matrix1Path);
            SparseMatrix matrix2 = new SparseMatrix(matrix2Path);

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nChoose an operation:");
                System.out.println("1. Add");
                System.out.println("2. Subtract");
                System.out.println("3. Multiply");
                System.out.println("4. Exit");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        SparseMatrix sum = matrix1.add(matrix2);
                        String sumPath = resultDir + File.separator + "sum.txt";
                        sum.toFile(sumPath);
                        System.out.println("Addition result saved to " + sumPath);
                        break;
                    case "2":
                        SparseMatrix difference = matrix1.subtract(matrix2);
                        String differencePath = resultDir + File.separator + "difference.txt";
                        difference.toFile(differencePath);
                        System.out.println("Subtraction result saved to " + differencePath);
                        break;
                    case "3":
                        SparseMatrix product = matrix1.multiply(matrix2);
                        String productPath = resultDir + File.separator + "product.txt";
                        product.toFile(productPath);
                        System.out.println("Multiplication result saved to " + productPath);
                        break;
                    case "4":
                        System.out.println("Exiting.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading matrices: " + e.getMessage());
        }
    }
}
