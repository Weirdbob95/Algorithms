
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.function.BiConsumer;

public class SeamCarver {

    private Picture picture;
    private int[][] red, green, blue;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        red = new int[width()][height()];
        green = new int[width()][height()];
        blue = new int[width()][height()];
        forEach(0, width(), 0, height(), (x, y) -> {
            Color c = picture.get(x, y);
            red[x][y] = c.getRed();
            green[x][y] = c.getGreen();
            blue[x][y] = c.getBlue();
        });
        energy = new double[width()][height()];
        forEach(0, width(), 0, height(), (x, y) -> energy[x][y] = energy(x, y));
    }

    private Color fastGet(int x, int y) {
        return new Color(red[x][y], green[x][y], blue[x][y]);
    }

    private int diffSquare(int i1, int i2) {
        return (i1 - i2) * (i1 - i2);
    }

    private int gradient(Color c1, Color c2) {
        return diffSquare(c1.getRed(), c2.getRed()) + diffSquare(c1.getGreen(), c2.getGreen()) + diffSquare(c1.getBlue(), c2.getBlue());
    }

    private void forEach(int startx, int endx, int starty, int endy, BiConsumer<Integer, Integer> func) {
        for (int x = startx; x < endx; x++) {
            for (int y = starty; y < endy; y++) {
                func.accept(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        } else {
            return Math.sqrt(gradient(fastGet(x, y - 1), fastGet(x, y + 1)) + gradient(fastGet(x - 1, y), fastGet(x + 1, y)));
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] costs = new double[width()][height()];
        int[][] parents = new int[width()][height()];
        for (int y = 0; y < height(); y++) {
            costs[0][y] = 1000;
        }

        forEach(1, width(), 0, height(), (x, y) -> {
            for (int py = y - 1; py <= y + 1; py++) {
                if (py >= 0 && py < height()) {
                    double newCost = energy[x][y] + costs[x - 1][py];
                    if (costs[x][y] == 0 || newCost < costs[x][y]) {
                        costs[x][y] = newCost;
                        parents[x][y] = py;
                    }
                }
            }
        });
        int min = 0;
        for (int y = 1; y < height(); y++) {
            if (costs[width() - 1][y] < costs[width() - 1][min]) {
                min = y;
            }
        }
        int[] r = new int[width()];
        r[width() - 1] = min;
        for (int x = width() - 1; x > 0; x--) {
            r[x - 1] = min = parents[x - 1][min];
        }
        return r;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] costs = new double[height()][width()];
        int[][] parents = new int[height()][width()];
        for (int x = 0; x < width(); x++) {
            costs[0][x] = 1000;
        }
        forEach(1, height(), 0, width(), (y, x) -> {
            for (int px = x - 1; px <= x + 1; px++) {
                if (px >= 0 && px < width()) {
                    double newCost = energy[x][y] + costs[y - 1][px];
                    if (costs[y][x] == 0 || newCost < costs[y][x]) {
                        costs[y][x] = newCost;
                        parents[y][x] = px;
                    }
                }
            }
        });
        int min = 0;
        for (int x = 1; x < width(); x++) {
            if (costs[height() - 1][x] < costs[height() - 1][min]) {
                min = x;
            }
        }
        int[] r = new int[height()];
        r[height() - 1] = min;
        for (int y = height() - 1; y > 0; y--) {
            r[y - 1] = min = parents[y - 1][min];
        }
        return r;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture p = new Picture(width(), height() - 1);
        forEach(0, width(), 0, height() - 1, (x, y) -> {
            Color c = fastGet(x, y >= seam[x] ? y + 1 : y);
            p.set(x, y, c);
            red[x][y] = c.getRed();
            green[x][y] = c.getGreen();
            blue[x][y] = c.getBlue();
        });
        forEach(0, width(), 0, height() - 1, (x, y) -> {
            if (Math.abs(y - seam[x]) < 2) {
                energy[x][y] = energy(x, y >= seam[x] ? y + 1 : y);
            } else {
                energy[x][y] = energy[x][y >= seam[x] ? y + 1 : y];
            }
        });
        picture = p;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture p = new Picture(width() - 1, height());
        forEach(0, width() - 1, 0, height(), (x, y) -> {
            Color c = fastGet(x >= seam[y] ? x + 1 : x, y);
            p.set(x, y, c);
            red[x][y] = c.getRed();
            green[x][y] = c.getGreen();
            blue[x][y] = c.getBlue();
        });
        forEach(0, width() - 1, 0, height(), (x, y) -> {
            if (Math.abs(x - seam[y]) < 2) {
                energy[x][y] = energy(x >= seam[y] ? x + 1 : x, y);
            } else {
                energy[x][y] = energy[x >= seam[y] ? x + 1 : x][y];
            }
        });
        picture = p;
    }

    public static void main(String[] args) {
        Picture p = new Picture("seamcarving/HJocean.png");
        p.show();
        SeamCarver seamcarver = new SeamCarver(p);
        for (int i = 1; i <= p.height() / 2; i++) {
            if (i % 10 == 0) {
                System.out.println("H SEAM " + i);
            }
            seamcarver.removeHorizontalSeam(seamcarver.findHorizontalSeam());
        }
        for (int i = 1; i <= p.width() / 2; i++) {
            if (i % 10 == 0) {
                System.out.println("V SEAM " + i);
            }
            seamcarver.removeVerticalSeam(seamcarver.findVerticalSeam());
        }
        seamcarver.picture.show();
    }
}
