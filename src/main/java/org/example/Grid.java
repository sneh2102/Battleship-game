package org.example;

class Grid {
    private final int size;
    private final GridElement[][] elements;

    public Grid(int size) {
        this.size = size;
        elements = new GridElement[size][size];
    }

    public boolean isValidCoordinate(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public GridElement getElement(int row, int col) {
        return elements[row][col];
    }

    public void setElement(int row, int col, GridElement element) {
        elements[row][col] = element;
    }
}