package uk.ac.soton.ecs.fa2u17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

import java.io.File;
import java.io.IOException;

public class MyHybridImages {

    static MyConvolution myConvolution;

    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage
     *            the image to which apply the low pass filter
     * @param lowSigma
     *            the standard deviation of the low-pass filter
     * @param highImage
     *            the image to which apply the high pass filter
     * @param highSigma
     *            the standard deviation of the low-pass component of computing the
     *            high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {

        /**
         * @param kernel_size_LOW_IMAGE
         *              kernel size of the Low Pass image
         * @param kernel_size_HIGH_IMAGE
         *              kernel size of the High Pass image
         **/
        int kernel_size_LOW_IMAGE;
        int kernel_size_HIGH_IMAGE;

        /**
         * @param image_HIGH_PASS_CLONE
         *              copy of the High Pass image
         * @param image_FINAL
         *              the final image to be returned by the function
         * @param image_HIGH_PASS
         *              the High Pass image
         **/
        MBFImage image_HIGH_PASS_CLONE = highImage.clone();
        MBFImage image_FINAL;
        MBFImage image_HIGH_PASS;

        //calculating the size of the kernel for the Low Pass image
        kernel_size_LOW_IMAGE = (int) (8.0f * lowSigma + 1.0f);
        // +1 if the size of the kernel is even
        if(kernel_size_LOW_IMAGE % 2 == 0) {

            kernel_size_LOW_IMAGE++;

        }

        //calculating the size of the kernel for the High Pass image
        kernel_size_HIGH_IMAGE = (int) (8.0f * highSigma + 1.0f);
        // +1 if the size of the kernel is even
        if(kernel_size_HIGH_IMAGE % 2 == 0) {

            kernel_size_HIGH_IMAGE++;

        }

        //create the kernel for the Low Pass and High Pass images
        FImage kernel_LOW = Gaussian2D.createKernelImage(kernel_size_LOW_IMAGE, lowSigma);
        FImage kernel_HIGH = Gaussian2D.createKernelImage(kernel_size_HIGH_IMAGE, highSigma);

        //apply the convolution for the Low Pass Image
        lowImage.processInplace(new MyConvolution(kernel_LOW.pixels));

        //apply the convolution for the High Pass Image
        image_HIGH_PASS_CLONE.processInplace(new MyConvolution(kernel_HIGH.pixels));
        image_HIGH_PASS = highImage.subtract(image_HIGH_PASS_CLONE);

        //add the 2 images together
        image_FINAL = image_HIGH_PASS.add(lowImage);

        //return the image
        return image_FINAL;

    }

//    public static void main(String[] args) {
//
//        MBFImage image_FRAME1;
//        MBFImage image_FRAME2;
//        MBFImage image_FRAME3;
//        MBFImage image_FRAME4;
//        MBFImage image_FRAME5;
//        MBFImage image_FRAME6;
//
//
//        try {
//
//            MBFImage hybridImage;
//
//            MBFImage dog = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/dog.bmp"));
//            MBFImage cat = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/cat.bmp"));
//            hybridImage = makeHybrid(dog,10,cat,10);
//            image_FRAME1 = new MBFImage(dog.getWidth()*2, dog.getHeight());
//            image_FRAME1.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME1.drawImage(hybridImage,0,0);
//            image_FRAME1.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME1.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME1.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME1.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME1);
//
//            MBFImage bicycle = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/bicycle.bmp"));
//            MBFImage motorcycle = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/motorcycle.bmp"));
//            hybridImage = makeHybrid(motorcycle,4,bicycle,4);
//            image_FRAME2 = new MBFImage(bicycle.getWidth()*2, motorcycle.getHeight());
//            image_FRAME2.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME2.drawImage(hybridImage,0,0);
//            image_FRAME2.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME2.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME2.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME2.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME2);
//
//            MBFImage marilyn = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/marilyn.bmp"));
//            MBFImage einstein = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/einstein.bmp"));
//            hybridImage = makeHybrid(marilyn,4,einstein,4);
//            image_FRAME3 = new MBFImage(marilyn.getWidth()*2, marilyn.getHeight());
//            image_FRAME3.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME3.drawImage(hybridImage,0,0);
//            image_FRAME3.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME3.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME3.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME3.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME3);
//
//            MBFImage bird = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/bird.bmp"));
//            MBFImage plane = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/plane.bmp"));
//            hybridImage = makeHybrid(plane,4,bird,4);
//            image_FRAME4 = new MBFImage(bird.getWidth()*2, bird.getHeight());
//            image_FRAME4.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME4.drawImage(hybridImage,0,0);
//            image_FRAME4.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME4.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME4.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME4.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME4);
//
//            MBFImage fish = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/fish.bmp"));
//            MBFImage submarine = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/submarine.bmp"));
//            hybridImage = makeHybrid(submarine,4,fish,4);
//            image_FRAME5 = new MBFImage(fish.getWidth()*2, fish.getHeight());
//            image_FRAME5.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME5.drawImage(hybridImage,0,0);
//            image_FRAME5.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME5.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME5.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME5.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME5);
//
//            MBFImage trump = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/trump.jpg"));
//            MBFImage boris = ImageUtilities.readMBF(new File("/Users/filipandonie/desktop/projects/ComputerVision/Coursework2/data/boris.jpg"));
//            hybridImage = makeHybrid(boris,4,trump,5);
//            image_FRAME6 = new MBFImage(trump.getWidth()*2, boris.getHeight());
//            image_FRAME6.fill(RGBColour.LIGHT_GRAY);
//            image_FRAME6.drawImage(hybridImage,0,0);
//            image_FRAME6.drawImage(ResizeProcessor.halfSize(hybridImage),hybridImage.getWidth() + 1, hybridImage.getHeight()/2);
//            image_FRAME6.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)), (int) (hybridImage.getWidth()* 1.5 + 2), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2);
//            image_FRAME6.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))), (int) (hybridImage.getWidth()* 1.75 + 3), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2);
//            image_FRAME6.drawImage(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)))), (int) (hybridImage.getWidth()* 1.875 + 4), hybridImage.getHeight()/2 + ResizeProcessor.halfSize(hybridImage).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage)).getHeight()/2 + ResizeProcessor.halfSize(ResizeProcessor.halfSize(ResizeProcessor.halfSize(hybridImage))).getHeight()/2);
//            DisplayUtilities.display(image_FRAME6);
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
