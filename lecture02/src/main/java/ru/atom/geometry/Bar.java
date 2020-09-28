package ru.atom.geometry;

public class Bar implements Collider{
    private int firstCornerX;
    private int firstCornerY;
    private int secondCornerX;
    private int secondCornerY;

    private Point firstPoint;
    private Point secondPoint;

    public Bar(Point firstPoint, Point secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
//        this.firstCornerX = firstCornerX;
//        this.firstCornerY = firstCornerY;
//        this.secondCornerX = secondCornerX;
//        this.secondCornerY = secondCornerY;
    }


    private boolean intersect(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        // cast from Object to Point
//        Bar point = (Bar) o;
        return false;
    }
    @Override
    public boolean isColliding(Collider other) {
        return false;
    }
}
