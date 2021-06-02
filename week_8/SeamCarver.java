import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int [][] img;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException();

        this.width = picture.width();
        this.height = picture.height();

        img = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                img[x][y] = picture.getRGB(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                picture.setRGB(x, y, img[x][y]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException();

        final double THRESHOLD = 1000.0;
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1)
            return THRESHOLD;

        int top    = img[x][y - 1];
        int bottom = img[x][y + 1];
        int left   = img[x - 1][y];
        int right  = img[x + 1][y];

        double red   = Math.pow((0xFF & (left >> 16)) - (0xFF & (right >> 16)), 2);
        double green = Math.pow((0xFF & (left >>  8)) - (0xFF & (right >>  8)), 2);
        double blue  = Math.pow((0xFF & (left >>  0)) - (0xFF & (right >>  0)), 2);
        double diffLR = red + green + blue;

        red   = Math.pow((0xFF & (top >> 16)) - (0xFF & (bottom >> 16)), 2);
        green = Math.pow((0xFF & (top >>  8)) - (0xFF & (bottom >>  8)), 2);
        blue  = Math.pow((0xFF & (top >>  0)) - (0xFF & (bottom >>  0)), 2);
        double diffTB = red + green + blue;

        return Math.sqrt(diffLR + diffTB);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose(true);
        int[] array = findVerticalSeam();
        transpose(false);

        for (int i = 0; i < array.length; i++)
            array[i] = height() - array[i] - 1;

        return array;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int h = height();
        int w = width();

        double[][] dist = new double[h][w];
        int[][] point = new int[h][w];

        for (int x = 0; x < height(); x++) {
            for (int y = 0; y < width(); y++) {
                double energy = energy(y, x);
                for (int i = -1; i <= 1; i++) {
                    if (x < 1 || y + i < 0 || y + i >= width())
                        continue;

                    if (energy < dist[x][y] - dist[x - 1][y + i]) {
                        dist[x][y] = dist[x - 1][y + i] + energy;
                        point[x][y] = y + i;
                    }

                    if (dist[x][y] == 0.0) {
                        dist[x][y] = dist[x - 1][y + i] + energy;
                        point[x][y] = y + i;
                        continue;
                    }
                }
            }
        }

        int minY = 0;
        double distMin = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width(); col++) {
            if (dist[height() - 1][col] < distMin) {
                minY = col;
                distMin = dist[height() - 1][col];
            }
        }

        int heightArr = height();
        int[] array = new int[heightArr];
        for (int x = height() - 1; x >= 0; x--) {
            array[x] = minY;
            minY = point[x][minY];
        }

        return array;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();

        transpose(true);
        int[] seamThat = new int[seam.length];
        for (int i = 0; i < seam.length; i++)
            seamThat[i] = width() - seam[i] - 1;

        removeVerticalSeam(seamThat);
        transpose(false);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException();

        for (int y = 0; y < height; y++) {
            int x = seam[y];
            while (x < width() - 1) {
                img[x][y] = img[x + 1][y];
                x++;
            }
        }
        width--;
    }

    private void transpose(boolean transposed) {
        int[][] imgNew = new int[height][width];

        if (!transposed) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    imgNew[y][width - x - 1] = img[x][y];
                }
            }
        } else {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    imgNew[height - y - 1][x] = img[x][y];
                }
            }
        }

        this.img = imgNew;
        int temp = width;
        width = height;
        height = temp;
    }
}