package uk.ac.soton.ecs.fa2u17.hybridimages;

import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {

    private float[][] kernel;

    public MyConvolution(float[][] kernel){

        this.kernel = kernel;

    }

    @Override
    public void processImage(FImage fImage) {

        /**
         *
         * @param index_ROWS - index to go through all the rows of the image
         * @param index_COLUMNS - index to go through all the columns of the image
         * @param template_ROWS - number of rows in the image
         * @param template_COLUMNS - number of columns in the image
         * @param template_ROWS_HALF - half of template rows
         * @param template_COLUMNS_HALF - half of template columns
         * @param sum_CENTRE_PIXEL - the sum produced for the centre pixel, by convolution
         *
         **/
        int index_ROWS;
        int index_COLUMNS;
        int template_ROWS;
        int template_COLUMNS;
        int template_ROWS_HALF;
        int template_COLUMNS_HALF;
        float sum_CENTRE_PIXEL;

        //get image dimensions
        index_ROWS = fImage.getRows();
        index_COLUMNS = fImage.getCols();

        //get template dimensions
        template_ROWS = kernel.length;
        template_COLUMNS = kernel.length;

        /**
         * @param convolved - black output image
         *                  - stores the convoluted pixels
         **/
        float convolved[][] = new float[fImage.height][fImage.width];

        //half of template columns is
        template_COLUMNS_HALF = (int) Math.floor(template_COLUMNS/2);
        //half of template rows is
        template_ROWS_HALF = (int) Math.floor(template_ROWS/2);

        //convolve the template
        for(int i = 0;i<index_COLUMNS;i++){

            for(int j = 0;j<index_ROWS;j++){

                //reinitialise the sum
                sum_CENTRE_PIXEL = 0;

                //address all points in the template
                for(int index_ROWS_WINDOW = 1; index_ROWS_WINDOW < template_ROWS; index_ROWS_WINDOW++){

                    for(int index_COLUMNS_WINDOW = 1; index_COLUMNS_WINDOW < template_COLUMNS; index_COLUMNS_WINDOW++){

                        try {
                            sum_CENTRE_PIXEL = sum_CENTRE_PIXEL + fImage.pixels[j + index_COLUMNS_WINDOW - template_COLUMNS_HALF - 1][i + index_ROWS_WINDOW - template_ROWS_HALF - 1] * kernel[index_COLUMNS_WINDOW][index_ROWS_WINDOW];
                        } catch (Exception e) {}
                    }

                }

                //store as new point
                convolved[j][i] = sum_CENTRE_PIXEL;

            }

        }

        //returning the image
        FImage final_image = new FImage(convolved);
        fImage.internalAssign(final_image);

    }

}