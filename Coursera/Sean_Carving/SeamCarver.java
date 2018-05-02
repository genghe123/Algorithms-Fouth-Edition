import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int[][] color;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        color = new int[picture.width()][picture.height()];

        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                color[i][j] = picture.getRGB(i, j);
            }
        }
    }

    public static void main(String args[]) {

    }

    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                picture.setRGB(i, j, color[i][j]);
            }
        }
        return picture;
    }

    public int width() {
        return color.length;
    }

    public int height() {
        return color[0].length;
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) throw new IllegalArgumentException();
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000;

        int rgbLeft = color[x - 1][y];
        int rgbRight = color[x + 1][y];
        int rgbTop = color[x][y - 1];
        int rgbBottom = color[x][y + 1];

        int energy = 0;
        for (int i = 0; i <= 16; i += 8) {
            energy += Math.pow(((rgbRight >> i) & 0xFF) - ((rgbLeft >> i) & 0xFF), 2);
            energy += Math.pow(((rgbBottom >> i) & 0xFF) - ((rgbTop >> i) & 0xFF), 2);
        }
        return Math.sqrt(energy);
    }

    private int index(int x, int y) {
        return width() * y + x;
    }

    private int[][] transpose(int[][] origin) {
        if (origin == null) throw new NullPointerException();
        if (origin.length < 1) throw new IllegalArgumentException();
        int[][] result = new int[origin[0].length][origin.length];
        for (int i = 0; i < origin[0].length; i++) {
            for (int j = 0; j < origin.length; j++) {
                result[i][j] = origin[j][i];
            }
        }
        return result;
    }

    public int[] findHorizontalSeam() {
        this.color = transpose(this.color);
        int[] seam = findVerticalSeam();
        this.color = transpose(this.color);
        return seam;
    }

    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        int[] nodeTo = new int[height() * width()];
        double[] distTo = new double[height() * width()];
        for (int i = 0; i < height() * width(); i++) {
            if (i < width())
                distTo[i] = 0;
            else
                distTo[i] = Double.POSITIVE_INFINITY;
        }

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                for (int k = -1; k <= 1; k++) {
                    if (j + k < 0 || j + k > this.width() - 1 || i + 1 < 0 || i + 1 > this.height() - 1) {
                        continue;
                    } else {
                        if (distTo[index(j + k, i + 1)] > distTo[index(j, i)] + energy(j, i)) {
                            distTo[index(j + k, i + 1)] = distTo[index(j, i)] + energy(j, i);
                            nodeTo[index(j + k, i + 1)] = index(j, i);
                        }
                    }
                }
            }
        }

        double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int j = 0; j < width(); j++) {
            if (distTo[j + width() * (height() - 1)] < min) {
                index = j + width() * (height() - 1);
                min = distTo[j + width() * (height() - 1)];
            }
        }

        for (int j = 0; j < height(); j++) {
            int y = height() - j - 1;
            int x = index - y * width();
            seam[height() - 1 - j] = x;
            index = nodeTo[index];
        }

        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) throw new IllegalArgumentException();
        if (seam == null) throw new NullPointerException();
        if (seam.length != width()) throw new IllegalArgumentException();

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1)
                throw new IllegalArgumentException();
            if (i < width() - 1 && Math.pow(seam[i] - seam[i + 1], 2) > 1)
                throw new IllegalArgumentException();
        }

        int[][] updatedColor = new int[width()][height() - 1];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] == 0) {
                System.arraycopy(this.color[i], seam[i] + 1, updatedColor[i], 0, height() - 1);
            } else if (seam[i] == height() - 1) {
                System.arraycopy(this.color[i], 0, updatedColor[i], 0, height() - 1);
            } else {
                System.arraycopy(this.color[i], 0, updatedColor[i], 0, seam[i]);
                System.arraycopy(this.color[i], seam[i] + 1, updatedColor[i], seam[i], height() - seam[i] - 1);
            }
        }
        this.color = updatedColor;
    }

    public void removeVerticalSeam(int[] seam) {
        this.color = transpose(this.color);
        removeHorizontalSeam(seam);
        this.color = transpose(this.color);
    }
}