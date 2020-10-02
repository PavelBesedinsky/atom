package ru.atom.geometry;

public class Bar implements Collider {
    private final Point firstPoint;
    private final Point secondPoint;


    public Bar(Point firstPoint, Point secondPoint) {
        final int xMax = Math.max(firstPoint.getX(), secondPoint.getX());
        final int yMax = Math.max(firstPoint.getY(), secondPoint.getY());
        final int xMin = Math.min(firstPoint.getX(), secondPoint.getX());
        final int yMin = Math.min(firstPoint.getY(), secondPoint.getY());

        this.firstPoint = new Point(xMin, yMin);
        this.secondPoint = new Point(xMax, yMax);
    }


    private boolean isIncomePointColliding(Point point) {
        return point.getX() >= this.firstPoint.getX()
                && point.getY() >= this.firstPoint.getY()
                && point.getX() <= this.secondPoint.getX()
                && point.getY() <= this.secondPoint.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bar) {
            final Bar bar = (Bar) obj;
            if (!(isIncomePointColliding(bar.firstPoint) || isIncomePointColliding(bar.secondPoint))) {
                return bar.firstPoint.getX() <= this.secondPoint.getX()
                        && bar.firstPoint.getY() <= this.secondPoint.getY()
                        && bar.secondPoint.getX() >= this.firstPoint.getX()
                        && bar.secondPoint.getY() >= this.firstPoint.getY();
            }
            return true;
        } else if (obj instanceof Point) {
            return isIncomePointColliding((Point) obj);
        }
        return false;
    }

    @Override
    public boolean isColliding(Collider other) {
        return equals(other);
    }
}
