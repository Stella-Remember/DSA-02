# SparseMatrix Java Program

## Overview
This Java program performs operations on sparse matrices, including addition, subtraction, and multiplication. It reads sparse matrices from files, executes the chosen operation, and writes the result to a specified directory.

## Features
- Load sparse matrices from files.
- Perform addition, subtraction, and multiplication on sparse matrices.
- Save the results of operations to files.

## Prerequisites
- Java Development Kit (JDK) installed on your machine.

## Compilation
1. Save the provided Java code into a file named `SparseMatrix.java`.
2. Open a terminal or command prompt.
3. Navigate to the directory containing `SparseMatrix.java`.
4. Compile the Java file using the following command:
   ```sh
   javac SparseMatrix.java
   ```

## Usage
To run the program, use the following command:
```sh
java SparseMatrix <matrix1_file_path> <matrix2_file_path> <result_directory>
```
- `<matrix1_file_path>`: Path to the first matrix file.
- `<matrix2_file_path>`: Path to the second matrix file.
- `<result_directory>`: Directory where the result files will be saved.

### Matrix File Format
Matrix files should be formatted as follows:
```
rows=<number_of_rows>
cols=<number_of_cols>
(row1, col1, value1)
(row2, col2, value2)
...
```
- `number_of_rows`: Total number of rows in the matrix.
- `number_of_cols`: Total number of columns in the matrix.
- `(row, col, value)`: Non-zero elements of the matrix.

### Example
Assume we have two matrix files `matrix1.txt` and `matrix2.txt` in the current directory, and we want to save the results in the `results` directory.

#### `matrix1.txt`:
```
rows=3
cols=3
(0, 0, 1)
(1, 1, 2)
(2, 2, 3)
```

#### `matrix2.txt`:
```
rows=3
cols=3
(0, 0, 4)
(1, 1, 5)
(2, 2, 6)
```

#### Run the Program
```sh
java SparseMatrix matrix1.txt matrix2.txt results
```

#### Follow the On-screen Instructions
The program will prompt you to choose an operation (Add, Subtract, Multiply, Exit). Based on your choice, it will perform the operation and save the result in the `results` directory.

## Author
This program in Java by `Stella Habiyambere`, demonstrating matrix operations and file handling
