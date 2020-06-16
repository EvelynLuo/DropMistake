package bupt.dropmistake;

import java.util.Scanner;

public class Matrix {
    private double[] matrix;
    private int row;

    public Matrix() {
        // TODO Auto-generated constructor stub
    }

    public Matrix(int row) {
        // TODO Auto-generated constructor stub
        this.row = row;
        matrix = new double[row];
        for (int i = 0; i < row; i++) {
            matrix[i] = 1.0 / row;
        }
    }

    public double[] createMatrix(int row) {
        double[] matrix = new double[row];
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < row; i++) {
            System.out.print("请输入第" + (i + 1) + "个数：");
            matrix[i] = input.nextDouble();
        }
        return matrix;
    }

    public int getRow() {
        return row;
    }

    public double[] getMatrix() {
        return matrix;
    }

    public Matrix mutipleMatrix(double[][] matrix) {
        Matrix output = new Matrix(row);
        for (int i = 0; i < row; i++) {
            output.getMatrix()[i] = 0;
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                output.getMatrix()[i] += matrix[i][j] * this.matrix[j];
            }
        }
        return output;
    }

    public Matrix mutipleNumber(double a) {
        Matrix output = new Matrix(row);
        for (int i = 0; i < row; i++) {
            output.getMatrix()[i] = matrix[i] * a;
        }
        return output;
    }

    public Matrix add(double[] matrix) {
        Matrix output = new Matrix(row);
        for (int i = 0; i < row; i++) {
            output.getMatrix()[i] = matrix[i] + this.matrix[i];
        }
        return output;

    }

    public Matrix subtraction(Matrix matrix) {
        Matrix output = new Matrix(row);
        for (int i = 0; i < row; i++) {
            if (this.matrix[i] - matrix.getMatrix()[i] >= 0)
                output.getMatrix()[i] = this.matrix[i] - matrix.getMatrix()[i];
            else
                output.getMatrix()[i] = matrix.getMatrix()[i] - this.matrix[i];
        }
        return output;
    }

    public boolean less(double[] matrix) {
        int i = 0;
        for (; i < row; i++) {
            if (this.matrix[i] > matrix[i])
                break;
        }
        if (i < row)
            return false;
        else
            return true;
    }

    public double[][] transition(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            int count = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0)
                    count++;
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] != 0)
                    matrix[i][j] = 1 / count;
            }
        }
        return matrix;
    }
}